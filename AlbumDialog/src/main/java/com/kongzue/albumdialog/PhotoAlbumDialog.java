package com.kongzue.albumdialog;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING;
import static com.kongzue.albumdialog.util.AlbumUtil.getAllAlbums;
import static com.kongzue.albumdialog.util.AlbumUtil.getPhotosByAlbum;
import static com.kongzue.dialogx.dialogs.PopTip.tip;
import static com.kongzue.dialogx.interfaces.BaseDialog.isNull;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kongzue.albumdialog.util.AlbumAdapter;
import com.kongzue.albumdialog.util.DialogImplCallback;
import com.kongzue.albumdialog.util.PhotoAdapter;
import com.kongzue.albumdialog.util.GridSpacingItemDecoration;
import com.kongzue.albumdialog.views.PhotoSelectImageView;
import com.kongzue.albumdialog.views.ScrollableRecycleView;
import com.kongzue.albumdialog.util.SelectPhotoCallback;
import com.kongzue.albumdialog.views.crop.ClipView;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnSafeInsetsChangeListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/5/13 22:53
 */
public class PhotoAlbumDialog {

    public static String fileProvider;      //一般是 context.getPackageName() + ".fileprovider"，请自行配置，如果不配置使用压缩、裁剪图像后默认返回 file 路径。
    public static final int PERMISSION_REQUEST_CODE = 10493;
    public static String PhotoAlbumDialogPermissionRequestKey = "PhotoAlbumDialogPermissionRequest";

    public enum SORT_MODE {
        ASC,
        DESC
    }

    enum STEP {
        SELECT_PHOTO,
        CLIP_PHOTO
    }

    STEP step = STEP.SELECT_PHOTO;
    SORT_MODE sortMode = SORT_MODE.DESC;
    String sortBy = MediaStore.Images.Media.DATE_ADDED;
    String defaultSelectAlbumName;
    int previewPhotoMaxColum = 4;
    Integer previewPhotoMargin;
    Integer previewPhotoHeight;
    int errorPhotoDrawableRes = R.drawable.album_dialog_error_photo;
    String dialogTipText;
    String clipTipText;
    String errorMaxSelectTipText;
    List<String> selectedPhotos = new ArrayList<>();
    DialogImplCallback<FullScreenDialog> dialogDialogImplCallback;  //对话框构建回调
    int maxSelectPhotoCount;        //最多选几张
    SelectPhotoCallback callback;   //回调
    boolean clip;                   //是否开启裁切
    boolean compressPhoto;          //是否压缩
    int compressQuality = 90;       //压缩质量，compressPhoto=true 时生效
    int maxWidth, maxHeight;        //最大宽高

    public static PhotoAlbumDialog build() {
        return new PhotoAlbumDialog();
    }

    List<String> allAlbums;
    List<String> defaultAlbumPhotos;
    GridSpacingItemDecoration photoGridSpacingItemDecoration;

    LinearLayout boxLayout;
    TextView tipView;
    FrameLayout contentLayout;
    ScrollableRecycleView recyclerView;
    TextView albumSelectView;
    TextView otherAlbumSelectView;
    LinearLayout clipBox;
    ImageView closeButton;
    ImageView okButton;
    FullScreenDialog fullScreenDialog;
    Context activityContext;

    public PhotoAlbumDialog() {
        fullScreenDialog = FullScreenDialog.build();
    }

    public void show(Activity activityContext) {
        this.activityContext = activityContext;
        if (checkPermission(activityContext)) {
            previewPhotoHeight = getScreenWidth(activityContext) / previewPhotoMaxColum;

            allAlbums = getAllAlbums(activityContext);
            defaultAlbumPhotos = getPhotosByAlbum(activityContext, defaultSelectAlbumName, sortBy, sortMode);

            Log.i(">>>", "allAlbums: " + allAlbums.size());
            Log.i(">>>", "defaultAlbumPhotos: " + defaultAlbumPhotos.size());

            allAlbums.add(0, activityContext.getString(R.string.album_dialog_default_album_name));

            boxLayout = new LinearLayout(activityContext); //照片选择总布局
            boxLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            boxLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView scrollBar = new ImageView(activityContext);       //滑动提示条
            scrollBar.setBackgroundResource(R.drawable.album_dialog_bkg_album_gray);
            LinearLayout.LayoutParams scrollBarLp = new LinearLayout.LayoutParams(dip2px(30), dip2px(5));
            scrollBarLp.topMargin = dip2px(10);
            boxLayout.addView(scrollBar, scrollBarLp);

            tipView = new TextView(activityContext);       //提示文本：选择需要使用的照片
            tipView.setText(getDialogTipText());
            tipView.setGravity(Gravity.CENTER);
            tipView.setTextColor(activityContext.getResources().getColor(R.color.albumDefaultLabelColor));
            tipView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tipView.setPadding(dip2px(15), dip2px(10), dip2px(15), dip2px(10));
            boxLayout.addView(tipView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (!isNull(dialogTipText)) {
                tipView.setText(dialogTipText);
            }

            boxLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            boxLayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout albumSelectLayout = new LinearLayout(activityContext); //相册选择条（关闭按钮、已选择的相册、其他相册、确定按钮）
            albumSelectLayout.setGravity(Gravity.CENTER);
            albumSelectLayout.setPadding(dip2px(10), 0, dip2px(10), 0);
            {
                albumSelectView = new TextView(activityContext);
                otherAlbumSelectView = new TextView(activityContext);

                albumSelectView.setText(isNull(defaultSelectAlbumName) ? activityContext.getString(R.string.album_dialog_default_album_name) : defaultSelectAlbumName);
                albumSelectView.setTextColor(Color.BLACK);
                albumSelectView.setCompoundDrawables(null, null, null, null);
                albumSelectView.setBackgroundResource(R.drawable.album_dialog_bkg_album_selector);
                albumSelectView.setBackground(getRippleBackgroundDrawable(activityContext, R.drawable.album_dialog_bkg_album_selector));
                albumSelectView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                albumSelectView.setClickable(true);
                albumSelectView.setFocusable(true);
                albumSelectView.setPadding(dip2px(20), dip2px(10), dip2px(20), dip2px(10));
                albumSelectView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerView.animate().alpha(0f).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                loadPhotoList(activityContext);
                                albumSelectView.setBackground(getRippleBackgroundDrawable(activityContext, R.drawable.album_dialog_bkg_album_selector));
                                otherAlbumSelectView.setBackground(getRippleBackgroundDrawable(activityContext, R.drawable.album_dialog_bkg_album_gray));
                            }
                        }).setDuration(100);
                    }
                });
                albumSelectLayout.addView(albumSelectView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                otherAlbumSelectView.setText(R.string.album_dialog_other_album);
                otherAlbumSelectView.setTextColor(Color.BLACK);
                otherAlbumSelectView.setCompoundDrawables(null, null, null, null);
                otherAlbumSelectView.setBackground(getRippleBackgroundDrawable(activityContext, R.drawable.album_dialog_bkg_album_gray));
                otherAlbumSelectView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                otherAlbumSelectView.setClickable(true);
                otherAlbumSelectView.setFocusable(true);
                otherAlbumSelectView.setPadding(dip2px(20), dip2px(10), dip2px(20), dip2px(10));
                LinearLayout.LayoutParams albumSelectViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                albumSelectViewLp.leftMargin = dip2px(5);
                albumSelectLayout.addView(otherAlbumSelectView, albumSelectViewLp);
                otherAlbumSelectView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadAlbumList(activityContext);
                        albumSelectView.setBackground(getRippleBackgroundDrawable(activityContext, R.drawable.album_dialog_bkg_album_gray));
                        otherAlbumSelectView.setBackground(getRippleBackgroundDrawable(activityContext, R.drawable.album_dialog_bkg_album_selector));
                    }
                });
            }
            {
                LinearLayout.LayoutParams spaceLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                spaceLp.weight = 1;
                albumSelectLayout.addView(new Space(activityContext), 0, spaceLp);

                int dp2 = dip2px(2);
                closeButton = new ImageView(activityContext);
                closeButton.setPadding(dp2, dp2, dp2, dp2);
                closeButton.setImageTintList(ColorStateList.valueOf(activityContext.getResources().getColor(R.color.albumDefaultThemeDeep)));
                closeButton.setImageResource(R.mipmap.img_album_dialog_button_close);
                closeButton.setBackground(ContextCompat.getDrawable(activityContext, R.drawable.album_dialog_ripple_effect_oval));
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (step == STEP.SELECT_PHOTO) {
                            if (getFullScreenDialog() != null) {
                                getFullScreenDialog().dismiss();
                            }
                        } else {
                            step = STEP.SELECT_PHOTO;
                            tipView.setText(getDialogTipText());
                            albumSelectView.setVisibility(View.VISIBLE);
                            otherAlbumSelectView.setVisibility(View.VISIBLE);
                            fullScreenDialog.setAllowInterceptTouch(true);
                            recyclerView.animate().x(0).setDuration(300).setInterpolator(new DecelerateInterpolator(2f));
                            clipBox.animate().x(recyclerView.getWidth()).setDuration(300).setInterpolator(new DecelerateInterpolator(2f));
                            okButton.setVisibility(maxSelectPhotoCount > 1 ? View.VISIBLE : View.INVISIBLE);
                            closeButton.setImageResource(R.mipmap.img_album_dialog_button_close);

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    clipBox.setVisibility(View.GONE);
                                }
                            }, 300);
                        }
                    }
                });
                albumSelectLayout.addView(closeButton, 0, new LinearLayout.LayoutParams(dip2px(35), dip2px(35)));

                albumSelectLayout.addView(new Space(activityContext), spaceLp);

                okButton = new ImageView(activityContext);
                okButton.setPadding(dp2, dp2, dp2, dp2);
                okButton.setImageTintList(ColorStateList.valueOf(activityContext.getResources().getColor(R.color.albumDefaultThemeDeep)));
                okButton.setImageResource(R.mipmap.img_album_dialog_button_ok);
                okButton.setBackground(ContextCompat.getDrawable(activityContext, R.drawable.album_dialog_ripple_effect_oval));
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFinishSelectPhoto(selectedPhotos);
                    }
                });
                albumSelectLayout.addView(okButton, new LinearLayout.LayoutParams(dip2px(35), dip2px(35)));
                okButton.setVisibility(maxSelectPhotoCount > 1 ? View.VISIBLE : View.INVISIBLE);
            }
            LinearLayout.LayoutParams albumBoxSelectViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(50));
            albumBoxSelectViewLp.bottomMargin = dip2px(10);
            boxLayout.addView(albumSelectLayout, albumBoxSelectViewLp);

            contentLayout = new FrameLayout(activityContext);       //内容布局

            recyclerView = new ScrollableRecycleView(activityContext);              //照片选择列表
            recyclerView.setLayoutManager(new GridLayoutManager(activityContext, previewPhotoMaxColum));
            photoGridSpacingItemDecoration = new GridSpacingItemDecoration(previewPhotoMaxColum, previewPhotoMargin == null ? dip2px(1) : previewPhotoMargin, true);
            recyclerView.addItemDecoration(photoGridSpacingItemDecoration);
            recyclerView.setClipToPadding(false);
            recyclerView.setTag("ScrollController");
            recyclerView.setAlpha(0f);

//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    switch (newState){
//                        case SCROLL_STATE_SETTLING:
//                            Glide.with(recyclerView.getContext()).pauseRequests();
//                            break;
//                        case SCROLL_STATE_IDLE:
//                            Glide.with(recyclerView.getContext()).resumeRequests();
//                            break;
//                    }
//                }
//            });

            contentLayout.addView(recyclerView);
            boxLayout.addView(contentLayout);

            fullScreenDialog.setCustomView(new OnBindView<FullScreenDialog>(boxLayout) {
                        @Override
                        public void onBind(FullScreenDialog dialog, View v) {
                            dialog.getDialogImpl().setScrollView(recyclerView);
                            dialog.getDialogImpl().boxRoot.setOnSafeInsetsChangeListener(new OnSafeInsetsChangeListener() {
                                @Override
                                public void onChange(Rect unsafeRect) {
                                    recyclerView.setPaddingRelative(0, 0, 0, unsafeRect.bottom);
                                }
                            });

                            loadPhotoList(activityContext);
                        }
                    })
                    .setDialogLifecycleCallback(new DialogLifecycleCallback<FullScreenDialog>() {
                        @Override
                        public void onDismiss(FullScreenDialog dialog) {
                            super.onDismiss(dialog);
                            boxLayout = null;
                            tipView = null;
                            contentLayout = null;
                            recyclerView = null;
                            albumSelectView = null;
                            otherAlbumSelectView = null;
                            clipBox = null;
                            closeButton = null;
                            okButton = null;
                            fullScreenDialog = null;
                            PhotoAlbumDialog.this.activityContext = null;
                        }
                    })
                    .setRadius(dip2px(15))
                    .hideActivityContentView(true)
                    .setBottomNonSafetyAreaBySelf(true);

            getDialogDialogImplCallback().onDialogCreated(getFullScreenDialog());
            fullScreenDialog.setCancelable(true);
            fullScreenDialog.show(activityContext);
        }
    }


    private Drawable getRippleBackgroundDrawable(Context context, int albumDialogBkgAlbumSelector) {
        return new LayerDrawable(new Drawable[]{context.getResources().getDrawable(albumDialogBkgAlbumSelector), ContextCompat.getDrawable(context, R.drawable.album_dialog_ripple_effect)});
    }

    private void loadAlbumList(Activity activityContext) {
        recyclerView.animate().alpha(0f).withEndAction(new Runnable() {
            @Override
            public void run() {
                photoGridSpacingItemDecoration.setSpanCount(2).setSpacing(-dip2px(10));
                recyclerView.setLayoutManager(new GridLayoutManager(activityContext, 2));
                AlbumAdapter albumAdapter = new AlbumAdapter(activityContext, allAlbums, dip2px(230), errorPhotoDrawableRes, defaultSelectAlbumName) {
                    @Override
                    public void onChangeAlbum(String albumName) {
                        defaultSelectAlbumName = albumName;
                        albumSelectView.setText(isNull(albumName) ? activityContext.getString(R.string.album_dialog_default_album_name) : albumName);
                        albumSelectView.callOnClick();
                    }
                };
                recyclerView.setAdapter(albumAdapter);

                recyclerView.animate().alpha(1f).setDuration(100);
            }
        }).setDuration(100);
    }

    private void loadPhotoList(Activity activityContext) {
        if (!isNull(defaultSelectAlbumName)) {
            defaultAlbumPhotos = getPhotosByAlbum(activityContext, defaultSelectAlbumName, sortBy, sortMode);
        }
        photoGridSpacingItemDecoration.setSpanCount(4).setSpacing(previewPhotoMargin == null ? dip2px(1) : previewPhotoMargin);
        recyclerView.setLayoutManager(new GridLayoutManager(activityContext, previewPhotoMaxColum));
        PhotoAdapter adapter = new PhotoAdapter(activityContext, defaultAlbumPhotos, previewPhotoHeight, errorPhotoDrawableRes, selectedPhotos) {
            @Override
            public boolean onSelectPhoto(String uri, List<String> selectedPhotos, boolean add) {
                if (add && selectedPhotos.size() == getMaxSelectPhotoCount() && (maxSelectPhotoCount <= 1)) {
                    onFinishSelectPhoto(selectedPhotos);
                    return false;
                }
                boolean flag = !(add && selectedPhotos.size() > getMaxSelectPhotoCount());
                if (!flag) {
                    tip(getErrorMaxSelectTipTextReal());
                }
                return flag;
            }
        };
        recyclerView.setAdapter(adapter);

        recyclerView.animate().alpha(1f).setDuration(100);
    }

    List<ClipView> clipViewList;
    int clipIndex;
    int selectedPhotosSize;


    private void onFinishSelectPhoto(List<String> selectedPhotos) {
        if (!clip) {
            if (needResizePhotos()) {
                WaitDialog.show(R.string.please_wait);
                List<String> resultPhotos = new ArrayList<>();

                selectedPhotosSize = selectedPhotos.size();
                for (String uri : selectedPhotos) {
                    Glide.with(getContext())
                            .asBitmap()
                            .load(uri)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                    Bitmap result = resizeBitmap(bitmap);

                                    String filename = getRandomImageFileName();
                                    try (FileOutputStream out = new FileOutputStream(filename)) {
                                        result.compress(compressPhoto ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, compressQuality, out);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (!isNull(fileProvider)) {
                                        resultPhotos.add(FileProvider.getUriForFile(getContext(), fileProvider, new File(filename)).toString());
                                    } else {
                                        resultPhotos.add(new File(filename).getAbsolutePath());
                                    }

                                    if (resultPhotos.size() == selectedPhotosSize) {
                                        WaitDialog.dismiss();
                                        callback(resultPhotos);
                                    }
                                }
                            });
                }
            } else {
                callback(selectedPhotos);
            }
        } else {
            if (getContext() == null) {
                return;
            }
            if (step == STEP.SELECT_PHOTO) {
                step = STEP.CLIP_PHOTO;

                clipBox = new LinearLayout(getContext());       //裁剪功能总布局
                clipBox.setOrientation(LinearLayout.VERTICAL);
                clipBox.setVisibility(View.GONE);

                clipViewList = new ArrayList<>();
                LinearLayout preCutPhotoBox = new LinearLayout(getContext());   //待裁剪图片选择器
                preCutPhotoBox.setOrientation(LinearLayout.HORIZONTAL);
                preCutPhotoBox.setGravity(Gravity.CENTER_VERTICAL);
                FrameLayout clipPhotoBox = new FrameLayout(getContext());

                LinearLayout.LayoutParams cLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                cLp.weight = 1;
                clipBox.addView(clipPhotoBox, cLp);

                for (int index = 0; index < selectedPhotos.size(); index++) {
                    String uri = selectedPhotos.get(index);
                    ClipView clipView = new ClipView(getContext());
                    clipView.setVisibility(index == clipIndex ? View.VISIBLE : View.GONE);
                    if (index == clipIndex) {
                        clipView.loadImage(uri);
                    }

                    clipPhotoBox.addView(clipView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    clipViewList.add(clipView);

                    PhotoSelectImageView photoView = new PhotoSelectImageView(getContext());        //照片选择缩略图按钮
                    int thumbnailSize = dip2px(60);
                    photoView.setTag(uri);
                    Glide.with(getContext())
                            .load(uri)
                            .override(thumbnailSize)
                            .error(errorPhotoDrawableRes)
                            .into(photoView);
                    photoView.setRadius(dip2px(2));
                    photoView.setBorderWidth((float) dip2px(2));
                    photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    photoView.setSelectState(index == clipIndex);
                    photoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((PhotoSelectImageView) preCutPhotoBox.getChildAt(clipIndex)).setSelectState(false);
                            if (clipIndex == preCutPhotoBox.indexOfChild(view)) {
                                return;
                            }
                            clipViewList.get(clipIndex).setVisibility(View.GONE);
                            clipIndex = preCutPhotoBox.indexOfChild(view);
                            clipViewList.get(clipIndex).setVisibility(View.VISIBLE);
                            clipViewList.get(clipIndex).loadImage(uri);
                            ((PhotoSelectImageView) preCutPhotoBox.getChildAt(clipIndex)).setSelectState(true);
                        }
                    });
                    LinearLayout.LayoutParams pLp = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
                    pLp.rightMargin = dip2px(5);
                    preCutPhotoBox.addView(photoView, pLp);
                }

                if (selectedPhotos.size() > 1) {
                    HorizontalScrollView selectorScrollView = new HorizontalScrollView(getContext());   //待裁剪图片选择器滚动布局
                    selectorScrollView.setOverScrollMode(HorizontalScrollView.OVER_SCROLL_NEVER);
                    selectorScrollView.addView(preCutPhotoBox, new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dip2px(70)));
                    selectorScrollView.setHorizontalFadingEdgeEnabled(true);
                    selectorScrollView.setHorizontalScrollBarEnabled(false);
                    LinearLayout.LayoutParams sLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(70));
                    sLp.leftMargin = dip2px(10);
                    sLp.rightMargin = dip2px(10);
                    sLp.bottomMargin = getFullScreenDialog().getDialogImpl().boxRoot.getUnsafePlace().bottom + dip2px(5);
                    clipBox.addView(selectorScrollView, sLp);
                }
                contentLayout.addView(clipBox, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                clipBox.setX(recyclerView.getWidth());
                clipBox.setVisibility(View.VISIBLE);
                recyclerView.animate().x(-recyclerView.getWidth()).setDuration(300).setInterpolator(new DecelerateInterpolator(2f));
                clipBox.animate().x(0).setDuration(300).setInterpolator(new DecelerateInterpolator(2f));
                okButton.setVisibility(View.VISIBLE);

                tipView.setText(getClipTipText());
                albumSelectView.setVisibility(View.GONE);
                otherAlbumSelectView.setVisibility(View.GONE);
                fullScreenDialog.setAllowInterceptTouch(false);
                closeButton.setImageResource(R.mipmap.img_album_dialog_button_back);
            } else {
                if (selectedPhotos.size() > 1) {
                    WaitDialog.show(R.string.please_wait);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            List<String> resultPhotos = new ArrayList<>();
                            for (int i = 0; i < clipViewList.size(); i++) {
                                ClipView clipView = clipViewList.get(i);
                                if (clipView.getBitmap() == null) {
                                    WaitDialog.dismiss();
                                    tip(String.format(getString(R.string.album_dialog_error_not_clip_photo), String.valueOf(i + 1)));
                                    return;
                                }
                                Bitmap result = clipView.clip();
                                result = resizeBitmap(result);
                                String filename = getRandomImageFileName();
                                try (FileOutputStream out = new FileOutputStream(filename)) {
                                    result.compress(compressPhoto ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, compressQuality, out);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (!isNull(fileProvider)) {
                                    resultPhotos.add(FileProvider.getUriForFile(getContext(), fileProvider, new File(filename)).toString());
                                } else {
                                    resultPhotos.add(new File(filename).getAbsolutePath());
                                }
                            }
                            WaitDialog.dismiss();
                            callback(resultPhotos);
                        }
                    }.start();
                } else {
                    List<String> resultPhotos = new ArrayList<>();
                    ClipView clipView = clipViewList.get(0);
                    Bitmap result = clipView.clip();
                    result = resizeBitmap(result);
                    String filename = getRandomImageFileName();
                    try (FileOutputStream out = new FileOutputStream(filename)) {
                        result.compress(compressPhoto ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, compressQuality, out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!isNull(fileProvider)) {
                        resultPhotos.add(FileProvider.getUriForFile(getContext(), fileProvider, new File(filename)).toString());
                    } else {
                        resultPhotos.add(new File(filename).getAbsolutePath());
                    }
                    callback(resultPhotos);
                }
            }
        }
    }

    private boolean needResizePhotos() {
        return getMaxHeight() != 0 || getMaxWidth() != 0;
    }

    private Bitmap resizeBitmap(Bitmap originBitmap) {
        if (!needResizePhotos()){
            return originBitmap;
        }
        int width = originBitmap.getWidth();
        int height = originBitmap.getHeight();

        float scaleWidth = ((float) maxWidth) / width;
        float scaleHeight = ((float) maxHeight) / height;
        float scaleFactor = min(scaleWidth, scaleHeight);

        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor, scaleFactor);
        Bitmap resizedBitmap = Bitmap.createBitmap(originBitmap, 0, 0, width, height, matrix, true);
//        if (resizedBitmap != originBitmap) {
//            originBitmap.recycle();
//        }
        return resizedBitmap;
    }

    private float min(float scaleWidth, float scaleHeight) {
        return (scaleWidth == 0 || scaleHeight == 0) ? Math.max(scaleWidth, scaleHeight) : Math.min(scaleWidth, scaleHeight);
    }

    private void callback(List<String> selectedPhotos) {
        if (getCallback() != null) {
            if (selectedPhotos.size() == 1) {
                getCallback().selectedPhoto(selectedPhotos.get(0));
            }
            getCallback().selectedPhotos(selectedPhotos);
        }
        if (getFullScreenDialog() != null) {
            getFullScreenDialog().dismiss();
        }
    }

    private String getRandomImageFileName() {
        String folderName = getContext().getCacheDir().getAbsolutePath() + File.separator + "KongzuePhotoAlbumDialog" + File.separator;
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folderName + ((int) (Math.random() * 1000000)) + (compressPhoto ? ".jpg" : ".png");
    }

    private boolean checkPermission(Activity activityContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(activityContext, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activityContext, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
                return false;
            }
        } else {
            if (ContextCompat.checkSelfPermission(activityContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activityContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    public PhotoAlbumDialog setDialogDialogImplCallback(DialogImplCallback<FullScreenDialog> dialogDialogImplCallback) {
        this.dialogDialogImplCallback = dialogDialogImplCallback;
        return this;
    }

    public DialogImplCallback<FullScreenDialog> getDialogDialogImplCallback() {
        return dialogDialogImplCallback == null ? new DialogImplCallback<FullScreenDialog>() {
            @Override
            public void onDialogCreated(FullScreenDialog dialog) {

            }
        } : dialogDialogImplCallback;
    }

    public SORT_MODE getSortMode() {
        return sortMode;
    }

    public PhotoAlbumDialog setSortMode(SORT_MODE sortMode) {
        this.sortMode = sortMode;
        return this;
    }

    public String getSortBy() {
        return sortBy;
    }

    public PhotoAlbumDialog setSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public String getDefaultSelectAlbumName() {
        return defaultSelectAlbumName;
    }

    public PhotoAlbumDialog setDefaultSelectAlbumName(String defaultSelectAlbumName) {
        this.defaultSelectAlbumName = defaultSelectAlbumName;
        return this;
    }

    public int getPreviewPhotoMaxColum() {
        return previewPhotoMaxColum;
    }

    public PhotoAlbumDialog setPreviewPhotoMaxColum(int previewPhotoMaxColum) {
        this.previewPhotoMaxColum = previewPhotoMaxColum;
        return this;
    }

    public Integer getPreviewPhotoMargin() {
        return previewPhotoMargin;
    }

    public PhotoAlbumDialog setPreviewPhotoMargin(Integer previewPhotoMargin) {
        this.previewPhotoMargin = previewPhotoMargin;
        return this;
    }

    public Integer getPreviewPhotoHeight() {
        return previewPhotoHeight;
    }

    public PhotoAlbumDialog setPreviewPhotoHeight(Integer previewPhotoHeight) {
        this.previewPhotoHeight = previewPhotoHeight;
        return this;
    }

    public int getErrorPhotoDrawableRes() {
        return errorPhotoDrawableRes;
    }

    public PhotoAlbumDialog setErrorPhotoDrawableRes(int errorPhotoDrawableRes) {
        this.errorPhotoDrawableRes = errorPhotoDrawableRes;
        return this;
    }

    public String getDialogTipText() {
        String temp;
        if (dialogTipText == null) {
            temp = getString(R.string.album_dialog_default_select_tip);
        } else {
            temp = dialogTipText;
        }
        return temp;
    }

    public PhotoAlbumDialog setDialogTipText(String dialogTipText) {
        this.dialogTipText = dialogTipText;
        return this;
    }

    public List<String> getSelectedPhotos() {
        return selectedPhotos;
    }

    public PhotoAlbumDialog setSelectedPhotos(List<String> selectedPhotos) {
        this.selectedPhotos = selectedPhotos;
        return this;
    }

    public FullScreenDialog getFullScreenDialog() {
        return fullScreenDialog;
    }

    public PhotoAlbumDialog setFullScreenDialog(FullScreenDialog fullScreenDialog) {
        this.fullScreenDialog = fullScreenDialog;
        return this;
    }

    public SelectPhotoCallback getCallback() {
        return callback;
    }

    public PhotoAlbumDialog setCallback(SelectPhotoCallback callback) {
        this.callback = callback;
        return this;
    }

    private int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private int dip2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    public int getMaxSelectPhotoCount() {
        return Math.max(maxSelectPhotoCount, 1);
    }

    private String getErrorMaxSelectTipTextReal() {
        String temp;
        if (errorMaxSelectTipText == null) {
            temp = getString(R.string.album_dialog_error_select_photo_max_tip);
        } else {
            temp = errorMaxSelectTipText;
        }
        if (temp.contains("%s")) {
            return String.format(temp, String.valueOf(getMaxSelectPhotoCount()));
        }
        return temp;
    }

    private String getString(int resId) {
        if (getFullScreenDialog() == null || getFullScreenDialog().getOwnActivity() == null) {
            return "";
        }
        return getFullScreenDialog().getOwnActivity().getString(resId);
    }

    public String getErrorMaxSelectTipText() {
        return errorMaxSelectTipText;
    }

    public PhotoAlbumDialog setErrorMaxSelectTipText(String errorMaxSelectTipText) {
        this.errorMaxSelectTipText = errorMaxSelectTipText;
        return this;
    }

    public PhotoAlbumDialog setMaxSelectPhotoCount(int maxSelectPhotoCount) {
        this.maxSelectPhotoCount = maxSelectPhotoCount;
        return this;
    }

    public boolean isClip() {
        return clip;
    }

    public PhotoAlbumDialog setClip(boolean clip) {
        this.clip = clip;
        return this;
    }

    public String getClipTipText() {
        String temp;
        if (clipTipText == null) {
            temp = getString(R.string.album_dialog_default_clip_tip);
        } else {
            temp = clipTipText;
        }
        return temp;
    }

    public PhotoAlbumDialog setClipTipText(String clipTipText) {
        this.clipTipText = clipTipText;
        return this;
    }

    private Context getContext() {
        return activityContext == null ? BaseDialog.getApplicationContext() : activityContext;
    }

    public boolean isCompressPhoto() {
        return compressPhoto;
    }

    public PhotoAlbumDialog setCompressPhoto(boolean compressPhoto) {
        this.compressPhoto = compressPhoto;
        return this;
    }

    public int getCompressQuality() {
        return compressQuality;
    }

    public PhotoAlbumDialog setCompressQuality(int compressQuality) {
        setCompressPhoto(true);
        this.compressQuality = compressQuality;
        return this;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public PhotoAlbumDialog setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public PhotoAlbumDialog setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public PhotoAlbumDialog setMaxSize(int maxSize) {
        this.maxHeight = maxSize;
        this.maxWidth = maxSize;
        return this;
    }
}
