package com.kongzue.albumdialog;

import static com.kongzue.albumdialog.util.AlbumUtil.getAllAlbums;
import static com.kongzue.albumdialog.util.AlbumUtil.getPhotosByAlbum;
import static com.kongzue.dialogx.dialogs.PopTip.tip;
import static com.kongzue.dialogx.interfaces.BaseDialog.isNull;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.kongzue.albumdialog.util.AlbumAdapter;
import com.kongzue.albumdialog.util.DialogImplCallback;
import com.kongzue.albumdialog.util.PhotoAdapter;
import com.kongzue.albumdialog.util.GridSpacingItemDecoration;
import com.kongzue.albumdialog.util.ScrollableRecycleView;
import com.kongzue.albumdialog.util.SelectPhotoCallback;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnSafeInsetsChangeListener;

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

    public static final int PERMISSION_REQUEST_CODE = 10493;
    public static String PhotoAlbumDialogPermissionRequestKey = "PhotoAlbumDialogPermissionRequest";

    public enum SORT_MODE {
        ASC,
        DESC
    }

    SORT_MODE sortMode = SORT_MODE.DESC;
    String sortBy = MediaStore.Images.Media.DATE_ADDED;
    String defaultSelectAlbumName;
    int previewPhotoMaxColum = 4;
    Integer previewPhotoMargin;
    Integer previewPhotoHeight;
    int errorPhotoDrawableRes = R.drawable.album_dialog_error_photo;
    String dialogTipText = "请选择需要使用的照片";
    String errorMaxSelectTipText = "最多可选择 %s 张照片";
    List<String> selectedPhotos = new ArrayList<>();
    DialogImplCallback<FullScreenDialog> dialogDialogImplCallback;
    int maxSelectPhotoCount;
    SelectPhotoCallback callback;

    public static PhotoAlbumDialog build() {
        return new PhotoAlbumDialog();
    }

    ScrollableRecycleView recyclerView;
    TextView albumSelectView;
    TextView otherAlbumSelectView;
    List<String> allAlbums;
    List<String> defaultAlbumPhotos;
    GridSpacingItemDecoration photoGridSpacingItemDecoration;
    FullScreenDialog fullScreenDialog;

    public PhotoAlbumDialog() {
        fullScreenDialog = FullScreenDialog.build();
    }

    public void show(Activity activityContext) {
        if (checkPermission(activityContext)) {
            previewPhotoHeight = getScreenWidth(activityContext) / previewPhotoMaxColum;

            allAlbums = getAllAlbums(activityContext);
            defaultAlbumPhotos = getPhotosByAlbum(activityContext, defaultSelectAlbumName, sortBy, sortMode);

            Log.i(">>>", "allAlbums: " + allAlbums.size());
            Log.i(">>>", "defaultAlbumPhotos: " + defaultAlbumPhotos.size());

            allAlbums.add(0, activityContext.getString(R.string.album_dialog_default_album_name));

            LinearLayout boxLayout = new LinearLayout(activityContext);
            boxLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            boxLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView scrollBar = new ImageView(activityContext);
            scrollBar.setBackgroundResource(R.drawable.album_dialog_bkg_album_gray);
            LinearLayout.LayoutParams scrollBarLp = new LinearLayout.LayoutParams(dip2px(30), dip2px(5));
            scrollBarLp.topMargin = dip2px(10);
            boxLayout.addView(scrollBar, scrollBarLp);

            if (!isNull(dialogTipText)) {
                TextView tipView = new TextView(activityContext);
                tipView.setText(dialogTipText);
                tipView.setGravity(Gravity.CENTER);
                tipView.setTextColor(activityContext.getResources().getColor(R.color.albumDefaultLabelColor));
                tipView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                tipView.setPadding(dip2px(15), dip2px(10), dip2px(15), dip2px(10));

                boxLayout.addView(tipView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            boxLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            boxLayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout albumSelectLayout = new LinearLayout(activityContext);
            albumSelectLayout.setGravity(Gravity.CENTER_VERTICAL);
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
                ImageView closeButton = new ImageView(activityContext);
                closeButton.setPadding(dp2, dp2, dp2, dp2);
                closeButton.setImageTintList(ColorStateList.valueOf(activityContext.getResources().getColor(R.color.albumDefaultThemeDeep)));
                closeButton.setImageResource(R.mipmap.img_album_dialog_button_close);
                closeButton.setBackground(ContextCompat.getDrawable(activityContext, R.drawable.album_dialog_ripple_effect_oval));
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getFullScreenDialog() != null) {
                            getFullScreenDialog().dismiss();
                        }
                    }
                });
                albumSelectLayout.addView(closeButton, 0, new LinearLayout.LayoutParams(dip2px(35), dip2px(35)));

                albumSelectLayout.addView(new Space(activityContext), spaceLp);

                if (maxSelectPhotoCount > 1) {
                    ImageView okButton = new ImageView(activityContext);
                    okButton.setPadding(dp2, dp2, dp2, dp2);
                    okButton.setImageTintList(ColorStateList.valueOf(activityContext.getResources().getColor(R.color.albumDefaultThemeDeep)));
                    okButton.setImageResource(R.mipmap.img_album_dialog_button_ok);
                    okButton.setBackground(ContextCompat.getDrawable(activityContext, R.drawable.album_dialog_ripple_effect_oval));
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            callback(selectedPhotos);
                        }
                    });
                    albumSelectLayout.addView(okButton, new LinearLayout.LayoutParams(dip2px(35), dip2px(35)));
                }
            }
            LinearLayout.LayoutParams albumBoxSelectViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            albumBoxSelectViewLp.bottomMargin = dip2px(10);
            boxLayout.addView(albumSelectLayout, albumBoxSelectViewLp);

            recyclerView = new ScrollableRecycleView(activityContext);
            recyclerView.setLayoutManager(new GridLayoutManager(activityContext, previewPhotoMaxColum));
            photoGridSpacingItemDecoration = new GridSpacingItemDecoration(previewPhotoMaxColum, previewPhotoMargin == null ? dip2px(1) : previewPhotoMargin, true);
            recyclerView.addItemDecoration(photoGridSpacingItemDecoration);
            recyclerView.setClipToPadding(false);
            recyclerView.setTag("ScrollController");
            recyclerView.setAlpha(0f);

            boxLayout.addView(recyclerView);

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
                            recyclerView = null;
                            albumSelectView = null;
                            otherAlbumSelectView = null;
                        }
                    })
                    .setRadius(dip2px(15))
                    .hideActivityContentView(true)
                    .setBottomNonSafetyAreaBySelf(true);

            dialogDialogImplCallback.onDialogCreated(getFullScreenDialog());
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
                    callback(selectedPhotos);
                    return false;
                }
                boolean flag =  !(add && selectedPhotos.size() > getMaxSelectPhotoCount());
                if (!flag){
                    tip(getErrorMaxSelectTipTextReal());
                }
                return flag;
            }
        };
        recyclerView.setAdapter(adapter);

        recyclerView.animate().alpha(1f).setDuration(100);
    }

    private void callback(List<String> selectedPhotos) {
        if (getCallback() != null) {
            getCallback().selectedPhotos(selectedPhotos);
        }
        if (getFullScreenDialog() != null) {
            getFullScreenDialog().dismiss();
        }
    }

    private boolean checkPermission(Activity activityContext) {
        if (activityContext instanceof AppCompatActivity) {
            ((AppCompatActivity) activityContext).getActivityResultRegistry().register(PhotoAlbumDialogPermissionRequestKey, new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    show(activityContext);
                }
            });
        }
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
        return dialogTipText;
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
        if (errorMaxSelectTipText.contains("%s")){
            return String.format(errorMaxSelectTipText,String.valueOf(getMaxSelectPhotoCount()));
        }
        return errorMaxSelectTipText;
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
}
