package com.huangxiaoliang.xpopup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.lifecycle.Lifecycle;

import static com.huangxiaoliang.xpopup.util.Utils.NO_RES_ID;

/**
 * @author HHotHeart
 * @date 2021/12/30 11:14
 * @desc 描述
 */
@SuppressWarnings("unchecked")
public abstract class BaseConfig<T> {

    /**
     * Context上下文
     */
    private Context mContext;

    /**
     * 被观察者，用于XPopup生命周期的监听
     */
    private Lifecycle mLifecycle;

    /**
     * XPopup生命周期观察者
     */
    private XPopupLifecycleObserver mXPopupLifecycleObserver;

    /**
     * 被观察者生命周期onDestroy()时关闭XPopup，如果实现了XPopupLifecycleObserver会被覆盖
     */
    private boolean mDismissObserverOnDestroy;

    /**
     * XPopup的Style样式
     */
    @StyleRes
    private int mThemeStyle = NO_RES_ID;

    /**
     * XPopup的进场和出场动画
     */
    @StyleRes
    private int mAnimStyle = NO_RES_ID;

    /**
     * XPopup的RootView
     */
    private View mXPopupRootView;

    /**
     * XPopup内容ContentView
     */
    private View mContentView;

    /**
     * View绑定到XPopup前的监听器
     */
    private XPopupInterface.OnBindViewListener mOnBindViewListener;

    /**
     * XPopup显示隐藏监听器
     */
    private XPopupInterface.OnShowListener mOnShowListener;
    private XPopupInterface.OnDismissListener mOnDismissListener;

    /**
     * 点击View的id集合
     */
    private int[] mClickIds;
    /**
     * View点击监听器
     */
    private XPopupInterface.OnClickListener mOnClickListener;
    /**
     * 存储点击事件键值对，key：view id，value：点击事件
     */
    private SparseArray<XPopupInterface.OnClickListener> mClickSparseArray;

    /**
     * 长按View的id集合
     */
    private int[] mLongClickIds;
    /**
     * View长按事件监听器
     */
    private XPopupInterface.OnLongClickListener mOnLongClickListener;
    /**
     * 存储长按事件键值对，key：view id，value：长按事件
     */
    private SparseArray<XPopupInterface.OnLongClickListener> mLongClickSparseArray;

    /**
     * XPopup的宽高，单位：px
     */
    private int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
    private int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

    /**
     * XPopup的最大宽高，单位：px
     */
    private int mMaxWidth;
    private int mMaxHeight;

    /**
     * XPopup Window占屏幕的宽高比，范围：0~1
     */
    private float mWidthInPercent;
    private float mHeightInPercent;

    /**
     * XPopup显示位置，默认居中显示，{@link Gravity}
     */
    private int mGravity = Gravity.CENTER;

    /**
     * XPopup Window 内容的背景
     */
    private Drawable mBackgroundDrawable;

    /**
     * Window dimAmount
     */
    private float mDimAmount = 0.5f;

    /**
     * XPopup自动消失的时间，单位：毫秒
     */
    private long mAutoDismissTime;

    /**
     * 返回键或点击外部区域是否将XPopup关掉
     */
    private boolean mCancelable = true;
    private boolean mCancelableOutside = true;

    /**
     * XPopup的圆角（TL:左上、TR:右上、BL:左下、BR:右下），单位：dp
     */
    private int mRadiusSideLeft;
    private int mRadiusSideTop;
    private int mRadiusSideRight;
    private int mRadiusSideBottom;

    /**
     * XPopup的圆角，单位：dp
     */
    private int mRadius;

    protected BaseConfig() {
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    protected BaseConfig(Context context) {
        mContext = context;
    }

    /**
     * 获取XPopup上下文
     *
     * @return 上下文
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 添加被观察者
     *
     * @param lifecycle              被观察者
     * @param popupLifecycleObserver 生命周期回调监听
     * @return 实例链
     */
    public T observeOn(Lifecycle lifecycle, XPopupLifecycleObserver popupLifecycleObserver) {
        mLifecycle = lifecycle;
        mXPopupLifecycleObserver = popupLifecycleObserver;
        return (T) this;
    }

    /**
     * 添加被观察者，onDestroy()方法执行关闭XPopup开关
     *
     * @param lifecycle                被观察者
     * @param dismissObserverOnDestroy onDestroy()方法执行关闭XPopup开关
     * @return 实例链
     */
    public T observeOn(Lifecycle lifecycle, boolean dismissObserverOnDestroy) {
        mLifecycle = lifecycle;
        mDismissObserverOnDestroy = dismissObserverOnDestroy;
        return (T) this;
    }

    /**
     * 获取被观察者
     *
     * @return Lifecycle
     */
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

    /**
     * 获取被观察者生命周期回调监听
     *
     * @return 被观察者生命周期回调监听
     */
    public XPopupLifecycleObserver getXPopupLifecycleObserver() {
        return mXPopupLifecycleObserver;
    }

    /**
     * 是否开启OnDestroy()时关闭XPopup
     *
     * @return boolean
     */
    public boolean isDismissObserverOnDestroy() {
        return mDismissObserverOnDestroy;
    }

    /**
     * 设置XPopup根布局
     *
     * @param xPopupRootView XPopupRootView
     * @return 实例链
     */
    protected void xPopupRootView(View xPopupRootView) {
        mXPopupRootView = xPopupRootView;
    }

    /**
     * 获取XPopup根布局
     *
     * @return XPopup根布局
     */
    protected View getXPopupRootView() {
        return mXPopupRootView;
    }


    public T view(View contentView) {
        mContentView = contentView;
        return (T) this;
    }

    public T view(@LayoutRes int contentViewResId) {
        return view(LayoutInflater.from(mContext).inflate(contentViewResId, null));
    }

    /**
     * 获取XPopup ContentView
     *
     * @return 内容View
     */
    public View getContentView() {
        return mContentView;
    }

    /**
     * 获取XPopup的主题样式
     *
     * @return XPopup的主题样式
     */
    public int getThemeStyle() {
        return mThemeStyle;
    }

    /**
     * 设置XPopup主题样式
     *
     * @param themeStyle XPopup主题样式
     * @return 实例链
     */
    public T themeStyle(@StyleRes int themeStyle) {
        mThemeStyle = themeStyle;
        return (T) this;
    }

    /**
     * 获取XPopup显示和消失动画资源id
     *
     * @return XPopup显示和消失动画资源id
     */
    public int getAnimStyle() {
        return mAnimStyle;
    }

    /**
     * 设置XPopup显示和消失动画资源id
     *
     * @param animStyle XPopup显示和消失动画资源id
     * @return 实例链
     */
    public T animStyle(@StyleRes int animStyle) {
        mAnimStyle = animStyle;
        return (T) this;
    }

    /**
     * 获取View绑定到XPopup前的监听器
     *
     * @return View绑定到XPopup前的监听器
     */
    public XPopupInterface.OnBindViewListener getBindViewListener() {
        return mOnBindViewListener;
    }

    /**
     * 设置View绑定到XPopup前的监听器
     *
     * @param listener View绑定到XPopup前的监听器
     * @return 实例链
     */
    public T bindViewListener(XPopupInterface.OnBindViewListener listener) {
        mOnBindViewListener = listener;
        return (T) this;
    }

    /**
     * 获取XPopup显示监听器
     *
     * @return XPopup显示监听器
     */
    public XPopupInterface.OnShowListener getOnShowListener() {
        return mOnShowListener;
    }

    /**
     * 设置XPopup显示监听器
     *
     * @param onShowListener XPopup显示监听器
     * @return 实例链
     */
    public T showListener(XPopupInterface.OnShowListener onShowListener) {
        mOnShowListener = onShowListener;
        return (T) this;
    }

    /**
     * 获取XPopup消失监听器
     *
     * @return XPopup消失监听器
     */
    public XPopupInterface.OnDismissListener getOnDismissListener() {
        return mOnDismissListener;
    }

    /**
     * 设置XPopup消失监听器
     *
     * @param onDismissListener XPopup消失监听器
     * @return 实例链
     */
    public T dismissListener(XPopupInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
        return (T) this;
    }

    /**
     * 获取XPopup宽度，单位：px
     *
     * @return XPopup宽度
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * 设置XPopup宽度，单位：px
     *
     * @param width XPopup宽度
     * @return 实例链
     */
    public T width(int width) {
        mWidth = width;
        return (T) this;
    }

    /**
     * 获取XPopup高度，单位：px
     *
     * @return XPopup高度
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * 设置XPopup高度，单位：px
     *
     * @param height XPopup高度
     * @return 实例链
     */
    public T height(int height) {
        mHeight = height;
        return (T) this;
    }

    /**
     * 设置XPopup宽度为MATCH_PARENT
     *
     * @return 实例链
     */
    public T matchWidth() {
        mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        return (T) this;
    }

    /**
     * 设置XPopup高度为MATCH_PARENT
     *
     * @return 实例链
     */
    public T matchHeight() {
        mHeight = ViewGroup.LayoutParams.MATCH_PARENT;
        return (T) this;
    }

    /**
     * 设置XPopup宽度为WRAP_CONTENT
     *
     * @return 实例链
     */
    public T wrapWidth() {
        mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        return (T) this;
    }

    /**
     * 设置XPopup高度为WRAP_CONTENT
     *
     * @return 实例链
     */
    public T wrapHeight() {
        mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        return (T) this;
    }

    /**
     * 获取XPopup最大宽度，单位：px
     *
     * @return XPopup最大宽度
     */
    public int getMaxWidth() {
        return mMaxWidth;
    }

    /**
     * 设置XPopup最大宽度，单位：px
     *
     * @param maxWidth XPopup最大宽度
     * @return 实例链
     */
    public T maxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
        return (T) this;
    }

    /**
     * 获取XPopup最大高度，单位：px
     *
     * @return XPopup最大高度
     */
    public int getMaxHeight() {
        return mMaxHeight;
    }

    /**
     * 设置XPopup最大高度，单位：px
     *
     * @param maxHeight XPopup最大高度
     * @return 实例链
     */
    public T maxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
        return (T) this;
    }

    /**
     * 获取XPopup百分比屏幕宽
     *
     * @return XPopup百分比屏幕宽
     */
    public float getWidthInPercent() {
        return mWidthInPercent;
    }

    /**
     * 设置XPopup百分比屏幕宽
     *
     * @param widthInPercent XPopup百分比屏幕宽
     * @return 实例链
     */
    public T widthInPercent(float widthInPercent) {
        mWidthInPercent = widthInPercent;
        return (T) this;
    }

    /**
     * 获取XPopup百分比屏幕高
     *
     * @return XPopup百分比屏幕高
     */
    public float getHeightInPercent() {
        return mHeightInPercent;
    }

    /**
     * 设置XPopup百分比屏幕高
     *
     * @param heightInPercent XPopup百分比屏幕高
     * @return 实例链
     */
    public T heightInPercent(float heightInPercent) {
        mHeightInPercent = heightInPercent;
        return (T) this;
    }

    /**
     * 设置XPopup显示位置
     *
     * @return XPopup显示位置
     */
    public int getGravity() {
        return mGravity;
    }

    /**
     * 获取XPopup显示位置
     *
     * @param gravity XPopup显示位置
     * @return 实例链
     */
    public T gravity(int gravity) {
        mGravity = gravity;
        return (T) this;
    }

    /**
     * XPopupWindow设置背景
     *
     * @param backgroundDrawable 背景Drawable
     * @return 实例链
     */
    public T backgroundDrawable(Drawable backgroundDrawable) {
        mBackgroundDrawable = backgroundDrawable;
        return (T) this;
    }

    /**
     * 获取背景
     *
     * @return 背景Drawable
     */
    public Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    /**
     * 设置Window dimAmount
     *
     * @param dimAmount 背景透明值
     * @return 实例链
     */
    public T dimAmount(float dimAmount) {
        mDimAmount = dimAmount;
        return (T) this;
    }

    /**
     * 获取Window背景透明值
     *
     * @return 背景透明值
     */
    public float getDimAmount() {
        return mDimAmount;
    }

    /**
     * 获取XPopup倒计时消失时间，单位：毫秒
     *
     * @return XPopup倒计时消失时间
     */
    public long getAutoDismissTime() {
        return mAutoDismissTime;
    }

    /**
     * 设置XPopup倒计时消失时间，单位：毫秒
     *
     * @param autoDismissTime XPopup倒计时消失时间
     * @return 实例链
     */
    public T autoDismissTime(long autoDismissTime) {
        mAutoDismissTime = autoDismissTime;
        return (T) this;
    }

    /**
     * 获取XPopup是否允许回退按钮将XPopup关闭标识
     *
     * @return XPopup是否允许回退按钮将XPopup关闭标识
     */
    public boolean isCancelable() {
        return mCancelable;
    }

    /**
     * 设置XPopup是否允许回退按钮将XPopup关闭标识
     *
     * @param cancelable XPopup是否允许回退按钮将XPopup关闭标识
     * @return 实例链
     */
    public T cancelable(boolean cancelable) {
        mCancelable = cancelable;
        return (T) this;
    }

    /**
     * 获取XPopup是否允许点击外部区域将XPopup关闭标识
     *
     * @return XPopup是否允许点击外部区域将XPopup关闭标识
     */
    public boolean isCancelableOutside() {
        return mCancelableOutside;
    }

    /**
     * 设置XPopup是否允许点击外部区域将XPopup关闭标识
     *
     * @param cancelableOutside XPopup是否允许点击外部区域将XPopup关闭标识
     * @return 实例链
     */
    public T cancelableOutside(boolean cancelableOutside) {
        mCancelableOutside = cancelableOutside;
        return (T) this;
    }

    /**
     * 设置XPopup的左边圆角，单位：px
     *
     * @param radiusSideLeft 左边圆角
     * @return 实例链
     */
    public T radiusSideLeft(int radiusSideLeft) {
        mRadiusSideLeft = radiusSideLeft;
        return (T) this;
    }

    /**
     * 获取XPopup的左边圆角，单位：px
     *
     * @return XPopup的左边圆角
     */
    public int getRadiusSideLeft() {
        return mRadiusSideLeft;
    }

    /**
     * 设置XPopup的上边圆角，单位：px
     *
     * @param radiusSideTop 上边圆角
     * @return 实例链
     */
    public T radiusSideTop(int radiusSideTop) {
        mRadiusSideTop = radiusSideTop;
        return (T) this;
    }

    /**
     * 获取XPopup的上边圆角，单位：px
     *
     * @return XPopup的上边圆角
     */
    public int getRadiusSideTop() {
        return mRadiusSideTop;
    }

    /**
     * 设置XPopup的右边圆角，单位：px
     *
     * @param radiusSideRight 右边圆角
     * @return 实例链
     */
    public T radiusSideRight(int radiusSideRight) {
        mRadiusSideRight = radiusSideRight;
        return (T) this;
    }

    /**
     * 获取XPopup的右边圆角，单位：px
     *
     * @return XPopup的右边圆角
     */
    public int getRadiusSideRight() {
        return mRadiusSideRight;
    }

    /**
     * 设置XPopup的下边圆角，单位：px
     *
     * @param radiusSideBottom 下边圆角
     * @return 实例链
     */
    public T radiusSideBottom(int radiusSideBottom) {
        mRadiusSideBottom = radiusSideBottom;
        return (T) this;
    }

    /**
     * 获取XPopup的下边圆角，单位：px
     *
     * @return XPopup的下边圆角
     */
    public int getRadiusSideBottom() {
        return mRadiusSideBottom;
    }

    /**
     * 设置XPopup的圆角，单位：px
     *
     * @param radius 圆角
     * @return 实例链
     */
    public T radius(int radius) {
        mRadius = radius;
        return (T) this;
    }

    /**
     * 获取XPopup的圆角，单位：px
     *
     * @return XPopup的圆角
     */
    public int getRadius() {
        return mRadius;
    }

    /**
     * View添加点击事件
     *
     * @param viewId          view id
     * @param onClickListener 点击事件
     * @return 实例链
     */
    public T clickListener(@NonNull @IdRes int viewId, @NonNull XPopupInterface.OnClickListener onClickListener) {
        if (mClickSparseArray == null) {
            mClickSparseArray = new SparseArray<>();
        }
        mClickSparseArray.put(viewId, onClickListener);
        return (T) this;
    }

    /**
     * 添加点击事件
     *
     * @param clickIds view对应的id集合
     * @return 实例链
     */
    public T clickIds(@IdRes int... clickIds) {
        mClickIds = clickIds;
        return (T) this;
    }

    /**
     * 获取设置的点击View id集合
     *
     * @return View id集合
     */
    public int[] getClickIds() {
        return mClickIds;
    }

    /**
     * 设置View点击监听器
     *
     * @param onClickListener 监听器
     * @return 实例链
     */
    public T clickIdsListener(@NonNull XPopupInterface.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        return (T) this;
    }

    /**
     * 获取点击事件监听器
     *
     * @return 监听器
     */
    public XPopupInterface.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    /**
     * 获取存储点击事件对象
     *
     * @return
     */
    public SparseArray<XPopupInterface.OnClickListener> getClickElement() {
        return mClickSparseArray;
    }

    /**
     * View添加长按事件
     *
     * @param viewId              view id
     * @param onLongClickListener 长按事件
     * @return 实例链
     */
    public T longClickListener(@NonNull @IdRes int viewId, @NonNull XPopupInterface.OnLongClickListener onLongClickListener) {
        if (mLongClickSparseArray == null) {
            mLongClickSparseArray = new SparseArray<>();
        }
        mLongClickSparseArray.put(viewId, onLongClickListener);
        return (T) this;
    }

    /**
     * 添加长按事件
     *
     * @param longClickIds view对应的id集合
     * @return 实例链
     */
    public T longClickIds(@IdRes int... longClickIds) {
        mLongClickIds = longClickIds;
        return (T) this;
    }

    /**
     * 获取设置的长按View id集合
     *
     * @return View id集合
     */
    public int[] getLongClickIds() {
        return mLongClickIds;
    }

    /**
     * 设置View长按事件监听器
     *
     * @param onLongClickListener 监听器
     * @return 实例链
     */
    public T longClickIdsListener(@NonNull XPopupInterface.OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
        return (T) this;
    }

    /**
     * 获取长按事件监听器
     *
     * @return 监听器
     */
    public XPopupInterface.OnLongClickListener getOnLongClickListener() {
        return mOnLongClickListener;
    }

    /**
     * 获取存储长按事件对象
     *
     * @return
     */
    public SparseArray<XPopupInterface.OnLongClickListener> getLongClickElement() {
        return mLongClickSparseArray;
    }

    /**
     * 创建XPopup实例
     *
     * @return XPopup实例
     */
    public abstract XPopup create();

}