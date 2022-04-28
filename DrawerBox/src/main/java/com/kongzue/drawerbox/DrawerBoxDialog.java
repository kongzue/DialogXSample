package com.kongzue.drawerbox;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.ScrollController;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.BlurView;
import com.kongzue.dialogx.util.views.BottomDialogScrollView;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/04/28 15:47
 */
public class DrawerBoxDialog extends BaseDialog {
    
    int minHeight;
    
    public static int overrideEnterDuration = -1;
    public static int overrideExitDuration = -1;
    public static BOOLEAN overrideCancelable;
    protected OnBindView<DrawerBoxDialog> onBindView;
    protected CharSequence title;
    protected CharSequence message;
    protected CharSequence cancelText;
    protected CharSequence okText;
    protected CharSequence otherText;
    protected boolean allowInterceptTouch = true;
    protected int maskColor = -1;
    protected OnDialogButtonClickListener<DrawerBoxDialog> cancelButtonClickListener;
    protected OnDialogButtonClickListener<DrawerBoxDialog> okButtonClickListener;
    protected OnDialogButtonClickListener<DrawerBoxDialog> otherButtonClickListener;
    protected BOOLEAN privateCancelable;
    
    protected TextInfo titleTextInfo;
    protected TextInfo messageTextInfo;
    protected TextInfo menuTextInfo;
    protected TextInfo cancelTextInfo = new TextInfo().setBold(true);
    protected TextInfo okTextInfo = new TextInfo().setBold(true);
    protected TextInfo otherTextInfo = new TextInfo().setBold(true);
    
    /**
     * 此值用于，当禁用滑动时（style.overrideBottomDialogRes.touchSlide = false时）的最大显示高度。
     * 0：不限制，最大显示到屏幕可用高度。
     */
    protected float bottomDialogMaxHeight = 0f;
    
    protected DialogLifecycleCallback<DrawerBoxDialog> dialogLifecycleCallback;
    
    protected DrawerBoxDialog me = this;
    
    protected DrawerBoxDialog() {
        super();
    }
    
    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }
    
    private View dialogView;
    
    public static DrawerBoxDialog build() {
        return new DrawerBoxDialog();
    }
    
    public static DrawerBoxDialog build(OnBindView<DrawerBoxDialog> onBindView) {
        return new DrawerBoxDialog().setCustomView(onBindView);
    }
    
    public DrawerBoxDialog(CharSequence title, CharSequence message) {
        this.title = title;
        this.message = message;
    }
    
    public DrawerBoxDialog(int titleResId, int messageResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
    }
    
    public static DrawerBoxDialog show(CharSequence title, CharSequence message) {
        DrawerBoxDialog drawerBoxDialog = new DrawerBoxDialog(title, message);
        drawerBoxDialog.show();
        return drawerBoxDialog;
    }
    
    public static DrawerBoxDialog show(int titleResId, int messageResId) {
        DrawerBoxDialog drawerBoxDialog = new DrawerBoxDialog(titleResId, messageResId);
        drawerBoxDialog.show();
        return drawerBoxDialog;
    }
    
    public DrawerBoxDialog(CharSequence title, CharSequence message, OnBindView<DrawerBoxDialog> onBindView) {
        this.title = title;
        this.message = message;
        this.onBindView = onBindView;
    }
    
    public DrawerBoxDialog(int titleResId, int messageResId, OnBindView<DrawerBoxDialog> onBindView) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.onBindView = onBindView;
    }
    
    public static DrawerBoxDialog show(CharSequence title, CharSequence message, OnBindView<DrawerBoxDialog> onBindView) {
        DrawerBoxDialog drawerBoxDialog = new DrawerBoxDialog(title, message, onBindView);
        drawerBoxDialog.show();
        return drawerBoxDialog;
    }
    
    public static DrawerBoxDialog show(int titleResId, int messageResId, OnBindView<DrawerBoxDialog> onBindView) {
        DrawerBoxDialog drawerBoxDialog = new DrawerBoxDialog(titleResId, messageResId, onBindView);
        drawerBoxDialog.show();
        return drawerBoxDialog;
    }
    
    public DrawerBoxDialog(CharSequence title, OnBindView<DrawerBoxDialog> onBindView) {
        this.title = title;
        this.onBindView = onBindView;
    }
    
    public DrawerBoxDialog(int titleResId, OnBindView<DrawerBoxDialog> onBindView) {
        this.title = getString(titleResId);
        this.onBindView = onBindView;
    }
    
    public static DrawerBoxDialog show(CharSequence title, OnBindView<DrawerBoxDialog> onBindView) {
        DrawerBoxDialog drawerBoxDialog = new DrawerBoxDialog(title, onBindView);
        drawerBoxDialog.show();
        return drawerBoxDialog;
    }
    
    public static DrawerBoxDialog show(int titleResId, OnBindView<DrawerBoxDialog> onBindView) {
        DrawerBoxDialog drawerBoxDialog = new DrawerBoxDialog(titleResId, onBindView);
        drawerBoxDialog.show();
        return drawerBoxDialog;
    }
    
    public DrawerBoxDialog(OnBindView<DrawerBoxDialog> onBindView) {
        this.onBindView = onBindView;
    }
    
    public static DrawerBoxDialog show(OnBindView<DrawerBoxDialog> onBindView) {
        DrawerBoxDialog drawerBoxDialog = new DrawerBoxDialog(onBindView);
        drawerBoxDialog.show();
        return drawerBoxDialog;
    }
    
    public void show() {
        super.beforeShow();
        if (getDialogView() == null) {
            int layoutId = isLightTheme() ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
            if (style.overrideBottomDialogRes() != null) {
                layoutId = style.overrideBottomDialogRes().overrideDialogLayout(isLightTheme());
            }
            
            dialogView = createView(layoutId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
        }
        BaseDialog.show(dialogView);
    }
    
    public void show(Activity activity) {
        super.beforeShow();
        if (getDialogView() == null) {
            int layoutId = isLightTheme() ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
            if (style.overrideBottomDialogRes() != null) {
                layoutId = style.overrideBottomDialogRes().overrideDialogLayout(isLightTheme());
            }
            
            dialogView = createView(layoutId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
        }
        BaseDialog.show(activity, dialogView);
    }
    
    protected DialogImpl dialogImpl;
    
    public class DialogImpl implements DialogConvertViewInterface {
        
        private BottomDialogTouchEventInterceptor bottomDialogTouchEventInterceptor;
        
        public DialogXBaseRelativeLayout boxRoot;
        public RelativeLayout boxBkg;
        public MaxRelativeLayout bkg;
        public ViewGroup boxBody;
        public ImageView imgTab;
        public TextView txtDialogTitle;
        public ScrollController scrollView;
        public LinearLayout boxContent;
        public TextView txtDialogTip;
        public View imgSplit;
        public RelativeLayout boxList;
        public RelativeLayout boxCustom;
        public BlurView blurView;
        public ViewGroup boxCancel;
        public TextView btnCancel;
        public BlurView cancelBlurView;
        
        public TextView btnSelectOther;
        public TextView btnSelectPositive;
        
        public DialogImpl(View convertView) {
            if (convertView == null) return;
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBkg = convertView.findViewById(R.id.box_bkg);
            bkg = convertView.findViewById(R.id.bkg);
            boxBody = convertView.findViewWithTag("body");
            imgTab = convertView.findViewById(R.id.img_tab);
            txtDialogTitle = convertView.findViewById(R.id.txt_dialog_title);
            scrollView = convertView.findViewById(R.id.scrollView);
            boxContent = convertView.findViewById(R.id.box_content);
            txtDialogTip = convertView.findViewById(R.id.txt_dialog_tip);
            imgSplit = convertView.findViewWithTag("split");
            boxList = convertView.findViewById(R.id.box_list);
            boxCustom = convertView.findViewById(R.id.box_custom);
            blurView = convertView.findViewById(R.id.blurView);
            boxCancel = convertView.findViewWithTag("cancelBox");
            btnCancel = convertView.findViewWithTag("cancel");
    
            boxRoot.setBackgroundResource(R.color.black30);
            boxRoot.setBkgAlpha(0f);
            
            btnSelectOther = convertView.findViewById(R.id.btn_selectOther);
            btnSelectPositive = convertView.findViewById(R.id.btn_selectPositive);
            
            init();
            dialogImpl = this;
            refreshView();
        }
        
        public void reBuild() {
            init();
            dialogImpl = this;
            refreshView();
        }
        
        /**
         * 此值记录了BottomDialog启动后的位置
         * ·当内容高度大于屏幕安全区高度时，BottomDialog会以全屏方式启动，但一开始只会展开到 0.8×屏幕高度，
         * 此时可以再次上划查看全部内容。
         * ·当内容高度小于屏幕安全区高度时，BottomDialog会以内容高度启动。
         * <p>
         * 记录这个值的目的是，当用户向下滑动时，判断情况该回到这个位置还是关闭对话框，
         * 并阻止当内容高度已经完全显示时的继续向上滑动操作。
         */
        public float bkgEnterAimY = -1;
        
        @Override
        public void init() {
            if (titleTextInfo == null) titleTextInfo = DialogX.menuTitleInfo;
            if (titleTextInfo == null) titleTextInfo = DialogX.titleTextInfo;
            if (messageTextInfo == null) messageTextInfo = DialogX.messageTextInfo;
            if (okTextInfo == null) okTextInfo = DialogX.okButtonTextInfo;
            if (okTextInfo == null) okTextInfo = DialogX.buttonTextInfo;
            if (cancelTextInfo == null) cancelTextInfo = DialogX.buttonTextInfo;
            if (otherTextInfo == null) otherTextInfo = DialogX.buttonTextInfo;
            if (backgroundColor == -1) backgroundColor = DialogX.backgroundColor;
            if (cancelText == null) cancelText = DialogX.cancelButtonText;
            
            txtDialogTitle.getPaint().setFakeBoldText(true);
            if (btnCancel != null) btnCancel.getPaint().setFakeBoldText(true);
            if (btnSelectPositive != null) btnSelectPositive.getPaint().setFakeBoldText(true);
            if (btnSelectOther != null) btnSelectOther.getPaint().setFakeBoldText(true);
            
            boxBkg.setY(BaseDialog.getRootFrameLayout().getMeasuredHeight());
            bkg.setMaxWidth(getMaxWidth());
            boxRoot.setParentDialog(me);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    
                    isShow = true;
                    
                    getDialogLifecycleCallback().onShow(me);
                    
                    onDialogInit(dialogImpl);
                    
                    boxRoot.post(new Runnable() {
                        @Override
                        public void run() {
                            if (style.messageDialogBlurSettings() != null && style.messageDialogBlurSettings().blurBackground() && boxBody != null && boxCancel != null) {
                                int blurFrontColor = getResources().getColor(style.messageDialogBlurSettings().blurForwardColorRes(isLightTheme()));
                                blurView = new BlurView(bkg.getContext(), null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bkg.getWidth(), bkg.getHeight());
                                blurView.setOverlayColor(backgroundColor == -1 ? blurFrontColor : backgroundColor);
                                blurView.setTag("blurView");
                                blurView.setRadiusPx(style.messageDialogBlurSettings().blurBackgroundRoundRadiusPx());
                                boxBody.addView(blurView, 0, params);
                                
                                cancelBlurView = new BlurView(boxCancel.getContext(), null);
                                RelativeLayout.LayoutParams cancelButtonLp = new RelativeLayout.LayoutParams(boxCancel.getWidth(), boxCancel.getHeight());
                                cancelBlurView.setOverlayColor(backgroundColor == -1 ? blurFrontColor : backgroundColor);
                                cancelBlurView.setTag("blurView");
                                cancelBlurView.setRadiusPx(style.messageDialogBlurSettings().blurBackgroundRoundRadiusPx());
                                boxCancel.addView(cancelBlurView, 0, cancelButtonLp);
                            }
                        }
                    });
                    
                    refreshUI();
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                    dialogImpl = null;
                    bottomDialogTouchEventInterceptor = null;
                    dialogLifecycleCallback = null;
                    System.gc();
                }
            });
            
            if (btnCancel != null) {
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cancelButtonClickListener != null) {
                            if (!cancelButtonClickListener.onClick(me, v)) {
                                dismiss();
                            }
                        } else {
                            dismiss();
                        }
                    }
                });
            }
            if (btnSelectOther != null) {
                btnSelectOther.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (otherButtonClickListener != null) {
                            if (!otherButtonClickListener.onClick(me, v)) {
                                dismiss();
                            }
                        } else {
                            dismiss();
                        }
                    }
                });
            }
            if (btnSelectPositive != null) {
                btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (okButtonClickListener != null) {
                            if (!okButtonClickListener.onClick(me, v)) {
                                dismiss();
                            }
                        } else {
                            dismiss();
                        }
                    }
                });
            }
            
            if (imgSplit != null) {
                int dividerRes = style.overrideBottomDialogRes().overrideMenuDividerDrawableRes(isLightTheme());
                int dividerHeight = style.overrideBottomDialogRes().overrideMenuDividerHeight(isLightTheme());
                if (dividerRes != 0) imgSplit.setBackgroundResource(dividerRes);
                if (dividerHeight != 0) {
                    ViewGroup.LayoutParams lp = imgSplit.getLayoutParams();
                    lp.height = dividerHeight;
                    imgSplit.setLayoutParams(lp);
                }
            }
            
            boxRoot.setOnBackPressedListener(new OnBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    if (onBackPressedListener != null) {
                        if (onBackPressedListener.onBackPressed()) {
                            preDismiss();
                        }
                        return false;
                    }
                    if (isCancelable()) {
                        preDismiss();
                    }
                    return false;
                }
            });
            boxRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preDismiss();
                }
            });
            boxRoot.setClickable(false);
            boxRoot.setInterceptBack(false);
            
            boxBkg.post(new Runnable() {
                @Override
                public void run() {
                    long enterAnimDurationTemp = 300;
                    
                    float customDialogTop = 0;
                    if (bottomDialogMaxHeight > 0 && bottomDialogMaxHeight <= 1) {
                        customDialogTop = boxBkg.getHeight() - bottomDialogMaxHeight * boxBkg.getHeight();
                    } else if (bottomDialogMaxHeight > 1) {
                        customDialogTop = boxBkg.getHeight() - bottomDialogMaxHeight;
                    }
                    
                    //上移动画
                    ObjectAnimator enterAnim = ObjectAnimator.ofFloat(boxBkg, "y", boxBkg.getY(),
                            bkgEnterAimY = (boxRoot.getUnsafePlace().top + customDialogTop
                                    + bkg.getHeight() - getRealMinHeight())
                    );
                    if (overrideEnterDuration >= 0) {
                        enterAnimDurationTemp = overrideEnterDuration;
                    }
                    if (enterAnimDuration >= 0) {
                        enterAnimDurationTemp = enterAnimDuration;
                    }
                    enterAnim.setDuration(enterAnimDurationTemp);
                    enterAnim.setAutoCancel(true);
                    enterAnim.setInterpolator(new DecelerateInterpolator(2f));
                    enterAnim.start();
                    
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomDialogTouchEventInterceptor = new BottomDialogTouchEventInterceptor(me, dialogImpl);
                            boxBkg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                @Override
                                public boolean onPreDraw() {
                                    float bkgAlphaValue = (bkgEnterAimY - boxBkg.getY()) / bkgEnterAimY;
                                    if (bkgAlphaValue < 0) bkgAlphaValue = 0;
                                    boxRoot.setBkgAlpha(bkgAlphaValue);
                                    return true;
                                }
                            });
                        }
                    }, enterAnimDurationTemp);
                }
            });
        }
        
        @Override
        public void refreshView() {
            if (boxRoot == null || BaseDialog.getContext() == null) {
                return;
            }
            if (backgroundColor != -1) {
                tintColor(bkg, backgroundColor);
                if (blurView != null && cancelBlurView != null) {
                    blurView.setOverlayColor(backgroundColor);
                    cancelBlurView.setOverlayColor(backgroundColor);
                }
                
                tintColor(btnSelectOther, backgroundColor);
                tintColor(btnCancel, backgroundColor);
                tintColor(btnSelectPositive, backgroundColor);
            }
            
            showText(txtDialogTitle, title);
            showText(txtDialogTip, message);
            
            BaseDialog.useTextInfo(txtDialogTitle, titleTextInfo);
            BaseDialog.useTextInfo(txtDialogTip, messageTextInfo);
            BaseDialog.useTextInfo(btnCancel, cancelTextInfo);
            BaseDialog.useTextInfo(btnSelectOther, otherTextInfo);
            BaseDialog.useTextInfo(btnSelectPositive, okTextInfo);
            
            if (maskColor != -1) boxRoot.setBackgroundColor(maskColor);
            
            if (onBindView != null && onBindView.getCustomView() != null) {
                onBindView.bindParent(boxCustom, me);
                if (onBindView.getCustomView() instanceof ScrollController) {
                    if (scrollView instanceof BottomDialogScrollView) {
                        ((BottomDialogScrollView) scrollView).setVerticalScrollBarEnabled(false);
                    }
                    scrollView = (ScrollController) onBindView.getCustomView();
                } else {
                    View scrollController = onBindView.getCustomView().findViewWithTag("ScrollController");
                    if (scrollController instanceof ScrollController) {
                        if (scrollView instanceof BottomDialogScrollView) {
                            ((BottomDialogScrollView) scrollView).setVerticalScrollBarEnabled(false);
                        }
                        scrollView = (ScrollController) scrollController;
                    }
                }
            }
            
            if (isAllowInterceptTouch() && isCancelable()) {
                if (imgTab != null) imgTab.setVisibility(View.VISIBLE);
            } else {
                if (imgTab != null) imgTab.setVisibility(View.GONE);
            }
            
            if (bottomDialogTouchEventInterceptor != null) {
                bottomDialogTouchEventInterceptor.refresh(me, this);
            }
            
            if (imgSplit != null) {
                if (txtDialogTitle.getVisibility() == View.VISIBLE || txtDialogTip.getVisibility() == View.VISIBLE) {
                    imgSplit.setVisibility(View.VISIBLE);
                } else {
                    imgSplit.setVisibility(View.GONE);
                }
            }
            
            if (boxCancel != null) {
                if (BaseDialog.isNull(cancelText)) {
                    boxCancel.setVisibility(View.GONE);
                } else {
                    boxCancel.setVisibility(View.VISIBLE);
                }
            }
            
            showText(btnSelectPositive, okText);
            showText(btnCancel, cancelText);
            showText(btnSelectOther, otherText);
        }
        
        @Override
        public void doDismiss(View v) {
            if (v != null) v.setEnabled(false);
            if (BaseDialog.getContext() == null) return;
            
            if (!dismissAnimFlag) {
                dismissAnimFlag = true;
                
                long exitAnimDurationTemp = 300;
                if (overrideExitDuration >= 0) {
                    exitAnimDurationTemp = overrideExitDuration;
                }
                if (exitAnimDuration >= 0) {
                    exitAnimDurationTemp = exitAnimDuration;
                }
                
                float customDialogTop = 0;
                if (bottomDialogMaxHeight > 0 && bottomDialogMaxHeight <= 1) {
                    customDialogTop = boxBkg.getHeight() - bottomDialogMaxHeight * boxBkg.getHeight();
                } else if (bottomDialogMaxHeight > 1) {
                    customDialogTop = boxBkg.getHeight() - bottomDialogMaxHeight;
                }
                
                ObjectAnimator exitAnim = ObjectAnimator.ofFloat(boxBkg, "y", boxBkg.getY(), (boxRoot.getUnsafePlace().top + customDialogTop
                        + bkg.getHeight() - getRealMinHeight()));
                exitAnim.setDuration(exitAnimDurationTemp);
                exitAnim.start();
            }
        }
        
        public void preDismiss() {
            long exitAnimDurationTemp = 300;
            if (overrideExitDuration >= 0) {
                exitAnimDurationTemp = overrideExitDuration;
            }
            if (exitAnimDuration >= 0) {
                exitAnimDurationTemp = exitAnimDuration;
            }
            
            float customDialogTop = 0;
            if (bottomDialogMaxHeight > 0 && bottomDialogMaxHeight <= 1) {
                customDialogTop = boxBkg.getHeight() - bottomDialogMaxHeight * boxBkg.getHeight();
            } else if (bottomDialogMaxHeight > 1) {
                customDialogTop = boxBkg.getHeight() - bottomDialogMaxHeight;
            }
            
            ObjectAnimator exitAnim = ObjectAnimator.ofFloat(boxBkg, "y", boxBkg.getY(), (boxRoot.getUnsafePlace().top + customDialogTop
                    + bkg.getHeight() - getRealMinHeight()));
            exitAnim.setDuration(exitAnimDurationTemp);
            exitAnim.start();
            
            boxRoot.setInterceptBack(false);
            boxRoot.setClickable(false);
        }
        
        public void fold() {
            preDismiss();
        }
        
        public void unfold() {
            ObjectAnimator enterAnim = ObjectAnimator.ofFloat(boxBkg, "y", boxBkg.getY(), boxRoot.getUnsafePlace().top);
            enterAnim.setDuration(300);
            enterAnim.start();
            boxRoot.setInterceptBack(true);
            boxRoot.setClickable(true);
        }
        
        public boolean isFold() {
            return boxBkg.getY() != boxRoot.getUnsafePlace().top;
        }
    }
    
    protected void onDialogInit(DialogImpl dialog) {
    }
    
    public void refreshUI() {
        if (getDialogImpl() == null) return;
        BaseDialog.runOnMain(new Runnable() {
            @Override
            public void run() {
                if (dialogImpl != null) dialogImpl.refreshView();
            }
        });
    }
    
    public void dismiss() {
        BaseDialog.runOnMain(new Runnable() {
            @Override
            public void run() {
                if (dialogImpl == null) return;
                BaseDialog.dismiss(dialogView);
            }
        });
    }
    
    public DialogLifecycleCallback<DrawerBoxDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<DrawerBoxDialog>() {
        } : dialogLifecycleCallback;
    }
    
    public DrawerBoxDialog setDialogLifecycleCallback(DialogLifecycleCallback<DrawerBoxDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }
    
    public OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }
    
    public DrawerBoxDialog setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }
    
    public DrawerBoxDialog setTheme(DialogX.THEME theme) {
        this.theme = theme;
        return this;
    }
    
    public boolean isCancelable() {
        if (privateCancelable != null) {
            return privateCancelable == BOOLEAN.TRUE;
        }
        if (overrideCancelable != null) {
            return overrideCancelable == BOOLEAN.TRUE;
        }
        return cancelable;
    }
    
    public DrawerBoxDialog setCancelable(boolean cancelable) {
        this.privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        refreshUI();
        return this;
    }
    
    public DialogImpl getDialogImpl() {
        return dialogImpl;
    }
    
    public CharSequence getTitle() {
        return title;
    }
    
    public DrawerBoxDialog setTitle(CharSequence title) {
        this.title = title;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setTitle(int titleResId) {
        this.title = getString(titleResId);
        refreshUI();
        return this;
    }
    
    public CharSequence getMessage() {
        return message;
    }
    
    public DrawerBoxDialog setMessage(CharSequence message) {
        this.message = message;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setMessage(int messageResId) {
        this.message = getString(messageResId);
        refreshUI();
        return this;
    }
    
    public CharSequence getCancelButton() {
        return cancelText;
    }
    
    public DrawerBoxDialog setCancelButton(CharSequence cancelText) {
        this.cancelText = cancelText;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setCancelButton(int cancelTextResId) {
        this.cancelText = getString(cancelTextResId);
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setCancelButton(OnDialogButtonClickListener<DrawerBoxDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }
    
    public DrawerBoxDialog setCancelButton(CharSequence cancelText, OnDialogButtonClickListener<DrawerBoxDialog> cancelButtonClickListener) {
        this.cancelText = cancelText;
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setCancelButton(int cancelTextResId, OnDialogButtonClickListener<DrawerBoxDialog> cancelButtonClickListener) {
        this.cancelText = getString(cancelTextResId);
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setCustomView(OnBindView<DrawerBoxDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }
    
    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }
    
    public DrawerBoxDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }
    
    public boolean isAllowInterceptTouch() {
        if (style.overrideBottomDialogRes() == null) {
            return false;
        } else {
            return allowInterceptTouch && style.overrideBottomDialogRes().touchSlide();
        }
    }
    
    public DrawerBoxDialog setAllowInterceptTouch(boolean allowInterceptTouch) {
        this.allowInterceptTouch = allowInterceptTouch;
        refreshUI();
        return this;
    }
    
    public OnDialogButtonClickListener<DrawerBoxDialog> getCancelButtonClickListener() {
        return cancelButtonClickListener;
    }
    
    public DrawerBoxDialog setCancelButtonClickListener(OnDialogButtonClickListener<DrawerBoxDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }
    
    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }
    
    public DrawerBoxDialog setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshUI();
        return this;
    }
    
    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }
    
    public DrawerBoxDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshUI();
        return this;
    }
    
    public TextInfo getCancelTextInfo() {
        return cancelTextInfo;
    }
    
    public DrawerBoxDialog setCancelTextInfo(TextInfo cancelTextInfo) {
        this.cancelTextInfo = cancelTextInfo;
        refreshUI();
        return this;
    }
    
    public int getBackgroundColor() {
        return backgroundColor;
    }
    
    public DrawerBoxDialog setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setBackgroundColorRes(@ColorRes int backgroundRes) {
        this.backgroundColor = getColor(backgroundRes);
        refreshUI();
        return this;
    }
    
    public CharSequence getOkButton() {
        return okText;
    }
    
    public DrawerBoxDialog setOkButton(CharSequence okText) {
        this.okText = okText;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setOkButton(int OkTextResId) {
        this.okText = getString(OkTextResId);
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setOkButton(OnDialogButtonClickListener<DrawerBoxDialog> OkButtonClickListener) {
        this.okButtonClickListener = OkButtonClickListener;
        return this;
    }
    
    public DrawerBoxDialog setOkButton(CharSequence OkText, OnDialogButtonClickListener<DrawerBoxDialog> OkButtonClickListener) {
        this.okText = OkText;
        this.okButtonClickListener = OkButtonClickListener;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setOkButton(int OkTextResId, OnDialogButtonClickListener<DrawerBoxDialog> OkButtonClickListener) {
        this.okText = getString(OkTextResId);
        this.okButtonClickListener = OkButtonClickListener;
        refreshUI();
        return this;
    }
    
    public CharSequence getOtherButton() {
        return otherText;
    }
    
    public DrawerBoxDialog setOtherButton(CharSequence otherText) {
        this.otherText = otherText;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setOtherButton(int OtherTextResId) {
        this.otherText = getString(OtherTextResId);
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setOtherButton(OnDialogButtonClickListener<DrawerBoxDialog> OtherButtonClickListener) {
        this.otherButtonClickListener = OtherButtonClickListener;
        return this;
    }
    
    public DrawerBoxDialog setOtherButton(CharSequence OtherText, OnDialogButtonClickListener<DrawerBoxDialog> OtherButtonClickListener) {
        this.otherText = OtherText;
        this.otherButtonClickListener = OtherButtonClickListener;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setOtherButton(int OtherTextResId, OnDialogButtonClickListener<DrawerBoxDialog> OtherButtonClickListener) {
        this.otherText = getString(OtherTextResId);
        this.otherButtonClickListener = OtherButtonClickListener;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        refreshUI();
        return this;
    }
    
    public long getEnterAnimDuration() {
        return enterAnimDuration;
    }
    
    public DrawerBoxDialog setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }
    
    public long getExitAnimDuration() {
        return exitAnimDuration;
    }
    
    public DrawerBoxDialog setExitAnimDuration(long exitAnimDuration) {
        this.exitAnimDuration = exitAnimDuration;
        return this;
    }
    
    @Override
    public void restartDialog() {
        if (dialogView != null) {
            BaseDialog.dismiss(dialogView);
            isShow = false;
        }
        if (getDialogImpl().boxCustom != null) {
            getDialogImpl().boxCustom.removeAllViews();
        }
        if (getDialogImpl().boxList != null) {
            getDialogImpl().boxList.removeAllViews();
        }
        int layoutId = isLightTheme() ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
        if (style.overrideBottomDialogRes() != null) {
            layoutId = style.overrideBottomDialogRes().overrideDialogLayout(isLightTheme());
        }
        
        enterAnimDuration = 0;
        dialogView = createView(layoutId);
        dialogImpl = new DialogImpl(dialogView);
        if (dialogView != null) dialogView.setTag(me);
        BaseDialog.show(dialogView);
    }
    
    public void hide() {
        if (getDialogView() != null) {
            getDialogView().setVisibility(View.GONE);
        }
    }
    
    @Override
    protected void shutdown() {
        dismiss();
    }
    
    public float getBottomDialogMaxHeight() {
        return bottomDialogMaxHeight;
    }
    
    public DrawerBoxDialog setBottomDialogMaxHeight(float bottomDialogMaxHeight) {
        this.bottomDialogMaxHeight = bottomDialogMaxHeight;
        return this;
    }
    
    public DrawerBoxDialog setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        refreshUI();
        return this;
    }
    
    public DrawerBoxDialog setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }
    
    public DrawerBoxDialog setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        return this;
    }
    
    private int getRealMinHeight() {
        if (getDialogImpl() == null || getDialogImpl().bkg == null) return minHeight;
        return minHeight + getDialogImpl().bkg.getHeight() - getDialogImpl().boxCustom.getHeight();
    }
    
    /**
     * 展开
     *
     * @return DrawerBoxDialog
     */
    public DrawerBoxDialog unfold() {
        if (getDialogImpl() != null) {
            getDialogImpl().unfold();
        }
        return this;
    }
    
    /**
     * 收起
     *
     * @return DrawerBoxDialog
     */
    public DrawerBoxDialog fold() {
        if (getDialogImpl() != null) {
            getDialogImpl().fold();
        }
        return this;
    }
    
    public boolean isFold() {
        if (getDialogImpl() != null) {
            return getDialogImpl().isFold();
        }
        return false;
    }
}
