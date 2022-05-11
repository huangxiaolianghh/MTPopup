package com.littlejerk.xpopup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

/**
 * @author HHotHeart
 * @date 2022/4/2 10:30
 * @desc PopupWindow 弹框配置
 */
public final class PopupWindowConfig extends BaseConfig<PopupWindowConfig> {
    /**
     * PopupWindow显示位置参考View
     */
    private View mTargetView;

    /**
     * PopupWindow显示位置的偏移量
     */
    private int mX, mY;

    /**
     * 是否使用PopupWindow的showAsDropDown显示方法
     */
    private boolean showAsDropDown;

    /**
     * PopupWindow设置是否可聚焦
     *
     * <p>false：点击返回键将直接退出Activity</p>
     * <p>true：点击返回键不会直接退出Activity而是关闭当前弹出的PopupWindow</p>
     */
    private boolean mFocusable = true;

    /**
     * PopupWindow弹出的高度
     */
    private float mElevation;

    /**
     * PopupWindow设置是否可触摸
     */
    private boolean mTouchable = true;

    /**
     * XPopupWindow窗口占满整个屏幕，忽略周围的装饰边框
     */
    private boolean mLayoutInScreen;

    /**
     * XPopupWindow设置内容是否可超出屏幕外
     */
    private boolean mClippedToScreen;

    /**
     * 绘制屏幕背景颜色，是否包括状态栏
     */
    private boolean mDecorateStatusBar;

    /**
     * PopupWindow的View拦截器
     */
    private View.OnTouchListener mTouchInterceptor;

    protected static PopupWindowConfig get(Context context) {
        return new PopupWindowConfig(context);
    }

    protected PopupWindowConfig(Context context) {
        super(context);
    }

    /**
     * 各个参数对应PopupWindow的showAsDropDown(View anchor, int xoff, int yoff, int gravity)方法
     *
     * @param anchor  展示位置参考View
     * @param xoff    目标View X 坐标偏移量
     * @param yoff    目标View Y 坐标偏移量
     * @param gravity 展示位置
     * @return 实例链
     */
    public PopupWindowConfig applyAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        mTargetView = anchor;
        mX = xoff;
        mY = yoff;
        gravity(gravity);
        showAsDropDown = true;
        return this;
    }

    /**
     * 展示位置View,其它参数都是默认设置
     *
     * @param anchor 展示位置参考View
     * @return 实例链
     */
    public PopupWindowConfig applyAsDropDown(View anchor) {
        return applyAsDropDown(anchor, 0, 0, Gravity.TOP | Gravity.START);
    }

    /**
     * 各个参数对应PopupWindow的showAtLocation(View parent, int gravity, int x, int y)方法
     *
     * @param parent  页面任意的view(可获取window的)
     * @param gravity 展示位置
     * @param x       X 坐标偏移量
     * @param y       Y 坐标偏移量
     * @return
     */
    public PopupWindowConfig applyAtLocation(View parent, int gravity, int x, int y) {
        mTargetView = parent;
        mX = x;
        mY = y;
        gravity(gravity);
        showAsDropDown = false;
        return this;
    }

    /**
     * 页面任意的view(可获取window的),其它参数都是默认设置
     *
     * @param parent 页面任意的view(可获取window的)
     * @return 实例链
     */
    public PopupWindowConfig applyAtLocation(View parent) {
        return applyAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    /**
     * 获取展示位置参考View
     *
     * @return 展示位置参考View
     */
    public View getTargetView() {
        return mTargetView;
    }

    /**
     * 获取X坐标偏移量
     *
     * @return X坐标偏移量
     */
    public int getX() {
        return mX;
    }

    /**
     * 获取Y坐标偏移量
     *
     * @return Y坐标偏移量
     */
    public int getY() {
        return mY;
    }

    /**
     * 是否调用showAsDropDown(View anchor, int xoff, int yoff, int gravity)方法
     *
     * @return 是否调用showAsDropDown()方法
     */
    public boolean isShowAsDropDown() {
        return showAsDropDown;
    }

    /**
     * XPopupWindow设置是否聚焦，为true时，cancelableOutside属性，返回键dismiss（cancelable属性无效）才会生效
     *
     * @param focusable 是否聚焦，如返回键事件
     * @return 实例链
     */
    public PopupWindowConfig focusable(boolean focusable) {
        mFocusable = focusable;
        return this;
    }

    /**
     * 获取是否聚焦
     *
     * @return 是否聚焦
     */
    public boolean isFocusable() {
        return mFocusable;
    }

    /**
     * PopupWindow View的高度elevation
     *
     * @return elevation值，单位px
     */
    public float getElevation() {
        return mElevation;
    }

    /**
     * 设置PopupWindow View的高度elevation
     *
     * @param elevation elevation值，单位px
     */
    public PopupWindowConfig elevation(float elevation) {
        mElevation = elevation;
        return this;
    }

    /**
     * XPopupWindow设置是否可触摸，为true时，内部View才可响应点击事件
     *
     * @param touchable 是否可触摸
     * @return 实例链
     */
    public PopupWindowConfig touchable(boolean touchable) {
        mTouchable = touchable;
        return this;
    }

    /**
     * 获取是否可触摸
     *
     * @return 是否可触摸
     */
    public boolean isTouchable() {
        return mTouchable;
    }

    /**
     * XPopupWindow窗口是否占满整个屏幕，忽略周围的装饰边框
     *
     * @param layoutInScreen 是否占满整个屏幕
     * @return 实例链
     */
    public PopupWindowConfig layoutInScreen(boolean layoutInScreen) {
        mLayoutInScreen = layoutInScreen;
        return this;
    }

    /**
     * 窗口是否占满整个屏幕
     *
     * @return 窗口是否占满整个屏幕
     */
    public boolean getLayoutInScreen() {
        return mLayoutInScreen;
    }

    /**
     * XPopupWindow设置内容是否可超出屏幕外
     *
     * @param clippedToScreen 内容是否可超出屏幕外
     * @return 实例链
     */
    public PopupWindowConfig clippedToScreen(boolean clippedToScreen) {
        mClippedToScreen = clippedToScreen;
        return this;
    }

    /**
     * 获取内容是否可超出屏幕外的值
     *
     * @return 内容是否可超出屏幕外
     */
    public boolean getClippedToScreen() {
        return mClippedToScreen;
    }

    /**
     * 绘制屏幕背景时，是否绘制状态
     *
     * @return 是否绘制状态
     */
    protected boolean isDecorateStatusBar() {
        return mDecorateStatusBar;
    }

    /**
     * 是否绘制状态
     *
     * @param decorateStatusBar 是否绘制状态
     * @return 实例链
     */
    protected PopupWindowConfig decorateStatusBar(boolean decorateStatusBar) {
        mDecorateStatusBar = decorateStatusBar;
        return this;
    }

    /**
     * XPopupWindow设置触摸监听器
     *
     * @param touchInterceptor 触摸监听器
     * @return 实例链
     */
    public PopupWindowConfig touchInterceptor(View.OnTouchListener touchInterceptor) {
        mTouchInterceptor = touchInterceptor;
        return this;
    }

    /**
     * 获取触摸监听器
     *
     * @return 触摸监听器
     */
    public View.OnTouchListener getTouchInterceptor() {
        return mTouchInterceptor;
    }

    @Override
    public XPopup<PopupWindowConfig, PopupWindowDelegate> create() {
        return new XPopup<PopupWindowConfig, PopupWindowDelegate>(
                this,
                PopupWindowDelegate.class,
                XPopupCompat.PopupWindow);
    }
}