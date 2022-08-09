package com.huangxiaoliang.xpopup;

import static com.huangxiaoliang.xpopup.util.Utils.NO_RES_ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.huangxiaoliang.xpopup.manager.DelegateManager;
import com.huangxiaoliang.xpopup.util.Preconditions;

/**
 * @author HHotHeart
 * @date 2022/4/6 17:16
 * @desc BottomSheetDialog代理类
 */
public final class BottomSheetDialogDelegate
        extends BaseDelegate<BottomSheetDialogConfig, BottomSheetDialogDelegate.InnerBottomSheetDialog> {

    /**
     * BottomSheetDialog实例
     */
    private InnerBottomSheetDialog mBottomSheetDialog;

    /**
     * Dialog的Behavior对象
     */
    private BottomSheetBehavior<FrameLayout> mBottomSheetBehavior;

    /**
     * 构造方法
     *
     * @param config DialogConfig
     */
    public BottomSheetDialogDelegate(BottomSheetDialogConfig config) {
        super(config);
    }

    @Override
    protected void decorateView() {
        ApplyParamsManager.decorateView(config());
    }

    @Override
    protected void bindListener() {
        Preconditions.checkNotNull(config().getXPopupRootView(), "please init Dialog root view");
        ApplyParamsManager.bindListener(this, config());
    }

    @Override
    protected void buildPopupBeforeDecorateView() {
        int themeStyle = config().getThemeStyle() == NO_RES_ID ? R.style.XPopup_BottomSheetDialog : config().getThemeStyle();
        mBottomSheetDialog = new InnerBottomSheetDialog(config().getContext(), themeStyle);
        DelegateManager.getInstance().put(mBottomSheetDialog.hashCode(), this);
    }

    @Override
    public InnerBottomSheetDialog getPopup() {
        return mBottomSheetDialog;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void applyParamsToPopup() {
        Preconditions.checkNotNull(getPopup(), "please check Dialog:getPopup()==null");
        getPopup().setContentView(config().getXPopupRootView());
        //BottomSheetDialog对于宽高的设置暂时不生效，内容布局多高就多高
        ApplyParamsManager.applyParamsToPopup(this, getPopup(), getDecorView(), config());
        //设置Popup显示时监听
        if (config().getOnShowListener() != null) {
            getPopup().setOnShowListener((DialogInterface dialog) -> config().getOnShowListener().onShow(this));
        }

        getPopup().setOnDismissListener(dialog -> {
            //设置Popup消失时监听
            if (config().getOnDismissListener() != null) {
                config().getOnDismissListener().onDismiss(this);
            }
            //dismiss()时已调用release方法，兼顾拖拽关闭情况
            releasePopup();
        });

    }

    @Override
    public boolean isShowing() {
        return getPopup() != null && getPopup().isShowing();
    }

    @Override
    void showPopup() {
        getPopup().show();
    }

    @Override
    void dismissPopup() {
        getPopup().dismiss();
    }

    @Override
    public View getDecorView() {
        if (getPopup() == null || getPopup().getWindow() == null) {
            return null;
        }
        return getPopup().getWindow().getDecorView();
    }

    /**
     * 获取BottomSheetDialog的Behavior对象（Dialog展示后调用）
     *
     * @return Behavior对象
     */
    public BottomSheetBehavior<FrameLayout> getBottomSheetBehavior() {
        return mBottomSheetBehavior;
    }

    /**
     * 初始化BottomSheetDialog的Behavior对象
     *
     * @param bottomSheetBehavior Behavior对象
     */
    public void setBottomSheetBehavior(BottomSheetBehavior<FrameLayout> bottomSheetBehavior) {
        mBottomSheetBehavior = bottomSheetBehavior;
    }

    /**
     * 自定义BottomSheetDialog，控制LayoutParams，背景色由colorBackground属性控制
     * <p>设置布局相关高度都只会自适应内容高度，内容多高就多高，但是可以使用maxHeight()方法</p>
     */
    public static class InnerBottomSheetDialog extends BottomSheetDialog {

        private BottomSheetDialogDelegate mDelegate;
        private BottomSheetDialogConfig mConfig;

        public InnerBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mDelegate = DelegateManager.getInstance().get(this.hashCode());
            mConfig = mDelegate.config();
        }

        private BottomSheetDialogConfig config() {
            return mConfig;
        }

        @Override
        protected void onStart() {
            super.onStart();
            FrameLayout bottomSheetView = this.findViewById(R.id.design_bottom_sheet);
            if (bottomSheetView == null) {
                return;
            }
            ApplyParamsManager.updateBottomSheetDialogLayout(bottomSheetView, config());
            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheetView);
            //避免点击外部空白区域崩溃问题：java.lang.IllegalArgumentException: Illegal state argument: 5
            behavior.setHideable(true);
            mDelegate.setBottomSheetBehavior(behavior);
        }

        @Override
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            DelegateManager.getInstance().remove(this.hashCode());
        }
    }

    @Override
    protected void releaseDelegate() {
        mBottomSheetDialog = null;
        mBottomSheetBehavior = null;
    }
}