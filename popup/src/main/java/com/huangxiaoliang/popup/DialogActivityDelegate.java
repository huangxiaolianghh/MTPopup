package com.huangxiaoliang.popup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huangxiaoliang.popup.manager.DelegateManager;
import com.huangxiaoliang.popup.util.Preconditions;
import com.huangxiaoliang.popup.util.Utils;

/**
 * @author huangxiaolianghh
 * @date 2022/4/6 20:36
 * @desc DialogActivity代理类
 */
public final class DialogActivityDelegate extends BaseDelegate<DialogActivityConfig, Activity> {

    private InnerDialogActivity mActivity;

    /**
     * 构造方法
     *
     * @param config DialogActivityConfig
     */
    public DialogActivityDelegate(@NonNull DialogActivityConfig config) {
        super(config);
    }

    @Override
    protected void decorateView() {
        ApplyParamsManager.decorateView(config());
    }

    @Override
    protected void bindListener() {
        Preconditions.checkNotNull(config().getPopupRootView(), "please init ActivityDialog root view");
        ApplyParamsManager.bindListener(this, config());
    }

    @Override
    public InnerDialogActivity getPopup() {
        return mActivity;
    }

    @Override
    protected void applyParamsToPopup() {

    }

    @Override
    public boolean isShowing() {
        return getPopup() != null && getPopup().isShowing();
    }

    @Override
    void showPopup() {
        DelegateManager.getInstance().handle(config().getContext(), InnerDialogActivity.class, activity -> {
            mActivity = (InnerDialogActivity) activity;
            if (config().getThemeStyle() != Utils.NO_RES_ID) {
                mActivity.setTheme(config().getThemeStyle());
            }
            Utils.overridePendingTransition(mActivity, config().getAnimStyle(), true);
            DelegateManager.getInstance().put(activity.hashCode(), DialogActivityDelegate.this);
        });
    }

    @Override
    void dismissPopup() {
        if (mActivity != null && mActivity.isShowing) {
            mActivity.finish();
            mActivity.isShowing = false;
            if (config().getOnDismissListener() != null) {
                config().getOnDismissListener().onDismiss(this);
            }
            Utils.overridePendingTransition(mActivity, config().getAnimStyle(), false);
            //关闭弹窗同时释放相关资源
            releasePopup();
        }
    }

    @Override
    public View getDecorView() {
        if (mActivity != null && mActivity.getWindow() != null) {
            return mActivity.getWindow().getDecorView();
        }
        return null;
    }

    /**
     * InnerDialogActivity，默认主题MTPopupDialogActivityTheme，不推荐使用theme去设置转场动画效果，推荐使用animStyle()。
     */
    public static class InnerDialogActivity extends AppCompatActivity {

        private DialogActivityDelegate mDelegate;
        private DialogActivityConfig mConfig;
        protected boolean isShowing;

        private DialogActivityConfig config() {
            return mConfig;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            DelegateManager.getInstance().sendResult(this);
            mDelegate = DelegateManager.getInstance().get(this.hashCode());
            mConfig = mDelegate.config();
            super.onCreate(savedInstanceState);
            setContentView(config().getPopupRootView());
            Window window = getWindow();
            if (window == null) {
                return;
            }
            //设置Popup背景透明
            window.setBackgroundDrawable(config().getBackgroundDrawable() == null
                    ? new ColorDrawable(Color.TRANSPARENT) : config().getBackgroundDrawable());
            //设置Popup背景透明度
            if (config().getDimAmount() >= Utils.ALPHA_0) {
                window.setDimAmount(config().getDimAmount());
            }
            ApplyParamsManager.updateWindowLayout(window, config());
            //点击Popup空白区域的监听
            if (window.getDecorView() == null) {
                return;
            }
            //DialogActivity特殊
            window.getDecorView().setOnTouchListener((v, event) -> {
                if (Utils.isOutOfBounds(v, event) && isShowing) {
                    if (InnerDialogActivity.this.config().isCancelableOutside()) {
                        mDelegate.dismiss();
                    }
                    //消费掉事件，不往下传递
                    return true;
                }
                return false;
            });

        }

        /**
         * 当前Popup是否在显示
         *
         * @return Popup是否在显示
         */
        protected boolean isShowing() {
            return isShowing;
        }


        @Override
        protected void onResume() {
            super.onResume();
            if (!isShowing) {
                isShowing = true;
                if (config().getOnShowListener() != null) {
                    config().getOnShowListener().onShow(mDelegate);
                }
            }
        }

        @Override
        public void onBackPressed() {
            if (!config().isCancelable()) {
                return;
            }
            if (mDelegate != null) {
                mDelegate.dismiss();
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            isShowing = false;
            DelegateManager.getInstance().remove(this.hashCode());
        }
    }

    @Override
    protected void releaseDelegate() {
        mActivity = null;
    }
}