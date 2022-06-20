package com.huangxiaoliang.xpopup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.huangxiaoliang.xpopup.util.Preconditions;
import com.huangxiaoliang.xpopup.util.Utils;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import static com.huangxiaoliang.xpopup.util.Utils.ALPHA_0;
import static com.huangxiaoliang.xpopup.util.Utils.ALPHA_1;
import static com.huangxiaoliang.xpopup.util.Utils.NO_RES_ID;
import static com.huangxiaoliang.xpopup.util.Utils.isOutOfBounds;

/**
 * @author HHotHeart
 * @date 2022/4/6 11:45
 * @desc PopupWindow代理类
 */
public final class PopupWindowDelegate extends BaseDelegate<PopupWindowConfig, PopupWindow> {

    /**
     * PopupWindow实例
     */
    protected PopupWindow mPopupWindow;

    /**
     * Window管理类
     */
    private WindowManager mWindowManager;

    /**
     * ContentView背景容器
     */
    private FrameLayout mBackgroundView;

    /**
     * 构造方法，不允许外部实例化
     *
     * @param config PopupWindowConfig
     */
    public PopupWindowDelegate(@NonNull PopupWindowConfig config) {
        super(config);
    }

    @Override
    protected void buildPopupBeforeDecorateView() {
        mPopupWindow = new PopupWindow(config().getContext());
        mWindowManager = (WindowManager) config().getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    protected void decorateView() {
        ApplyParamsManager.decorateView(config());
    }

    @Override
    protected void bindListener() {
        Preconditions.checkNotNull(config().getXPopupRootView(), "please init PopupWindow root view");
        ApplyParamsManager.bindListener(this, config());
    }

    @Override
    public PopupWindow getPopup() {
        return mPopupWindow;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void applyParamsToPopup() {
        Preconditions.checkNotNull(getPopup(), "please check PopupWindow:getPopup()==null");
        getPopup().setFocusable(config().isFocusable());
        getPopup().setTouchable(config().isTouchable());
        getPopup().setBackgroundDrawable(config().getBackgroundDrawable() == null
                ? new ColorDrawable(Color.TRANSPARENT) : config().getBackgroundDrawable());
        //设置Popup进出场动画
        if (config().getAnimStyle() != NO_RES_ID) {
            getPopup().setAnimationStyle(config().getAnimStyle());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getPopup().setIsLaidOutInScreen(config().getLayoutInScreen());
            getPopup().setIsClippedToScreen(config().getClippedToScreen());
        }
        getPopup().setContentView(config().getXPopupRootView());
        int width = config().getWidth();
        int height = config().getHeight();
        if (config().getWidthInPercent() > 0 && config().getWidthInPercent() <= 1) {
            width = (int) (config().getWidthInPercent() * Utils.getScreenWidth(config().getContext()));
        }
        if (config().getHeightInPercent() > 0 && config().getHeightInPercent() <= 1) {
            height = (int) (config().getHeightInPercent() * Utils.getScreenHeight(config().getContext()));
        }
        if (config().getMaxWidth() > 0 && config().getMaxWidth() <= Utils.getScreenWidth(config().getContext())) {
            width = width > 0 ? Math.min(width, config().getMaxWidth()) : config().getMaxWidth();
        }
        if (config().getMaxHeight() > 0 && config().getMaxHeight() <= Utils.getScreenHeight(config().getContext())) {
            height = height > 0 ? Math.min(width, config().getMaxHeight()) : config().getMaxHeight();
        }
        getPopup().setWidth(width);
        getPopup().setHeight(height);
        //设置Popup消失事件监听
        if (config().getOnDismissListener() != null) {
            getPopup().setOnDismissListener(() -> {
                if (!config().isDecorateStatusBar()) {
                    updateContentViewDimAmount(ALPHA_0);
                }
                config().getOnDismissListener().onDismiss(PopupWindowDelegate.this);
            });
        }
        // 拦截处理外部点击事件，如果自己实现拦截监听，则cancelableOutside = false会不生效
        if (config().getTouchInterceptor() != null) {
            getPopup().setTouchInterceptor(config().getTouchInterceptor());
            return;
        }
        getPopup().setTouchInterceptor((v, event) -> {
            if (isOutOfBounds(v, event)) {
                if (config().isCancelableOutside()) {
                    dismiss();
                }
                //消费掉事件，不往下传递
                return true;
            }
            return false;
        });
    }

    @Override
    void showPopup() {
        Preconditions.checkNotNull(config().getTargetView(), "please set anchor view");
        //灰色背景不允许遮住状态栏
        if (!config().isDecorateStatusBar()) {
            updateContentViewDimAmount(config().getDimAmount());
        }
        if (config().isShowAsDropDown()) {
            getPopup().showAsDropDown(config().getTargetView(), config().getX(), config().getY(), config().getGravity());
        } else {
            getPopup().showAtLocation(config().getTargetView(), config().getGravity(), config().getX(), config().getY());
        }
        //灰色背景允许遮住状态栏
        if (config().isDecorateStatusBar()) {
            updateWindowDimAmount(config().getDimAmount());
        }
        //Popup显示时监听
        if (getPopup().isShowing()) {
            if (config().getOnShowListener() != null) {
                config().getOnShowListener().onShow(this);
            }
        }
    }

    @Override
    void dismissPopup() {
        getPopup().dismiss();
    }

    /**
     * 获取PopupWindow的DecorView
     *
     * @return View
     */
    @Override
    public View getDecorView() {
        View decorView = null;
        try {
            if (getPopup().getBackground() == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView = (View) getPopup().getContentView().getParent();
                } else {
                    decorView = getPopup().getContentView();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView = (View) getPopup().getContentView().getParent().getParent();
                } else {
                    decorView = (View) getPopup().getContentView().getParent();
                }
            }
        } catch (Exception e) {
        }
        return decorView;
    }

    /**
     * 设置ContentView灰色背景
     *
     * @param dimAmount 透明度，0-1
     */
    protected void updateContentViewDimAmount(@FloatRange(from = ALPHA_0, to = ALPHA_1) float dimAmount) {
        if (config().getDimAmount() == ALPHA_0) {
            return;
        }
        Activity activity = (Activity) config().getContext();
        FrameLayout root = activity.findViewById(android.R.id.content);
        if (mBackgroundView == null) {
            mBackgroundView = new FrameLayout(config().getContext());
            root.addView(mBackgroundView, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (dimAmount == ALPHA_0) {
            //去掉遮罩，恢复原来布局
            root.removeView(mBackgroundView);
        } else {
            //获取背景透明度颜色值
            int backgroundColor = Utils.calculateStatusColor(Color.WHITE, (int) (dimAmount * 255));
            mBackgroundView.setBackgroundColor(backgroundColor);
            mBackgroundView.setAlpha(dimAmount);
        }
    }

    /**
     * 设置Window灰色背景
     *
     * @param dimAmount 0.0f（透明）~1.0f（全黑）
     */
    private void updateWindowDimAmount(float dimAmount) {
        View decorView = getDecorView();
        if (decorView != null) {
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) decorView.getLayoutParams();
            p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = dimAmount;
            modifyWindowLayoutParams(p);
            mWindowManager.updateViewLayout(decorView, p);
        }
    }

    /**
     * 实现不同WindowManager.LayoutParams时，需要重写
     *
     * @param lp WindowManager.LayoutParams
     */
    protected void modifyWindowLayoutParams(WindowManager.LayoutParams lp) {

    }

}