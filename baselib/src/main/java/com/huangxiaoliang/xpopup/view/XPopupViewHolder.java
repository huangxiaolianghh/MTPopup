package com.huangxiaoliang.xpopup.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

/**
 * @author HHotHeart
 * @date 2021/10/14 16:45
 * @desc 保存和处理View相关属性
 */
public final class XPopupViewHolder {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * XPopup的RootView
     */
    protected View mXPopupRootView;

    /**
     * XPopupViewHolder构造函数
     *
     * @param context        XPopu的上下文
     * @param xPopupRootView XPopup的RootView
     */
    public XPopupViewHolder(Context context, View xPopupRootView) {
        mContext = context;
        mXPopupRootView = xPopupRootView;
    }

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 获取XPopup根View
     *
     * @return View
     */
    public View getRootView() {
        return mXPopupRootView;
    }

    /**
     * 根据id获取View
     *
     * @param viewId View的id
     * @param <T>    View的泛型
     * @return View泛型实例
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        T view = mXPopupRootView.findViewById(viewId);
        return (T) view;
    }

    /**
     * 设置TextView文字
     *
     * @param viewId View的id
     * @param value  文案字符串
     * @return XPopupView实例链
     */
    public XPopupViewHolder setText(@IdRes int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    /**
     * 设置TextView文字
     *
     * @param viewId View的id
     * @param strId  文案字符串资源id
     * @return XPopupView实例链
     */
    public XPopupViewHolder setText(@IdRes int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    /**
     * 设置ImageView图片（ImageResource）
     *
     * @param viewId     View的id
     * @param imageResId 图片资源id
     * @return XPopupView实例链
     */
    public XPopupViewHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * 设置View颜色背景
     *
     * @param viewId View的id
     * @param color  颜色值, 非资源id
     * @return XPopupView实例链
     */
    public XPopupViewHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置View的背景
     *
     * @param viewId        View的id
     * @param backgroundRes 背景资源id
     * @return XPopupView实例链
     */
    public XPopupViewHolder setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * 设置TextView文字颜色
     *
     * @param viewId    View的id
     * @param textColor 文字颜色，非资源id
     * @return XPopupView实例链
     */
    public XPopupViewHolder setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * 设置ImageView图片（Drawable）
     *
     * @param viewId   View的id
     * @param drawable 图片
     * @return XPopupView实例链
     */
    public XPopupViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * 设置ImageView图片（Bitmap）
     *
     * @param viewId View的id
     * @param bitmap 图片
     * @return XPopupView实例链
     */
    public XPopupViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 设置View的透明度
     *
     * @param viewId View的id
     * @param value  透明度（0-1）
     * @return XPopupView实例链
     */
    public XPopupViewHolder setAlpha(@IdRes int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * 设置View的是否可见（true可见、false真正的不可见）
     *
     * @param viewId  View的id
     * @param visible 是否可见
     * @return XPopupView实例链
     */
    public XPopupViewHolder setGone(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置View的是否可见（true可见、false不可见但占用空间）
     *
     * @param viewId  View的id
     * @param visible 是否可见
     * @return XPopupView实例链
     */
    public XPopupViewHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    /**
     * 设置TextView字体样式
     *
     * @param viewId   View的id
     * @param typeface 字体样式
     * @return XPopupView实例链
     */
    public XPopupViewHolder setTypeface(@IdRes int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * 设置TextView字体样式
     *
     * @param typeface 字体样式
     * @param viewIds  View的id集合
     * @return XPopupView实例链
     */
    public XPopupViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * 设置ProgressBar的当前进度
     *
     * @param viewId   View的id
     * @param progress 进度数量
     * @return XPopupView实例链
     */
    public XPopupViewHolder setProgress(@IdRes int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    /**
     * 设置ProgressBar的当前进度和最大进度
     *
     * @param viewId   View的id
     * @param progress 进度数量
     * @param max      ProgressBar的最大进度
     * @return XPopupView实例链
     */
    public XPopupViewHolder setProgress(@IdRes int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * 设置ProgressBar的最大进度
     *
     * @param viewId View的id
     * @param max    ProgressBar的最大进度
     * @return XPopupView实例链
     */
    public XPopupViewHolder setMax(@IdRes int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    /**
     * 设置RatingBar当前评分
     *
     * @param viewId View的id
     * @param rating RatingBar评分（星星数量）
     * @return XPopupView实例链
     */
    public XPopupViewHolder setRating(@IdRes int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    /**
     * 设置RatingBar当前评分和允许最大评分
     *
     * @param viewId View的id
     * @param rating RatingBar评分（星星数量）
     * @param max    RatingBar最大数量
     * @return XPopupView实例链
     */
    public XPopupViewHolder setRating(@IdRes int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * 设置View是否checked
     *
     * @param viewId  View的id
     * @param checked 是否checked
     * @return XPopupView实例链
     */
    public XPopupViewHolder setChecked(@IdRes int viewId, boolean checked) {
        View view = getView(viewId);
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(checked);
        }
        return this;
    }

    /**
     * 设置View是否可点击
     *
     * @param viewId  View的id
     * @param enabled 是否可点击
     * @return XPopupView实例链
     */
    public XPopupViewHolder setEnabled(@IdRes int viewId, boolean enabled) {
        View view = getView(viewId);
        view.setEnabled(enabled);
        return this;
    }

    /**
     * 设置是否selected
     *
     * @param viewId   View的id
     * @param selected 是否selected
     * @return XPopupView实例链
     */
    public XPopupViewHolder setSelected(@IdRes int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }

    /**
     * 设置View点击监听器（OnClickListener）
     *
     * @param viewId   View的id
     * @param listener OnClickListener监听器
     * @return XPopupView实例链
     */
    public XPopupViewHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置View长按监听器（OnLongClickListener）
     *
     * @param viewId   View的id
     * @param listener OnLongClickListener监听器
     * @return XPopupView实例链
     */
    public XPopupViewHolder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * 设置View触摸监听器（OnTouchListener）
     *
     * @param viewId   View的id
     * @param listener OnTouchListener监听器
     * @return XPopupView实例链
     */
    public XPopupViewHolder setOnTouchListener(@IdRes int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * 设置CompoundButton状态监听器（OnCheckedChangeListener）
     *
     * @param viewId   View的id
     * @param listener OnCheckedChangeListener监听器
     * @return XPopupView实例链
     */
    public XPopupViewHolder setOnCheckedChangeListener(@IdRes int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    /**
     * 设置View标识
     *
     * @param viewId View的id
     * @param tag    标识
     * @return XPopupView实例链
     */
    public XPopupViewHolder setTag(@IdRes int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * 设置View标识
     *
     * @param viewId View的id
     * @param key    标识
     * @param tag    标识的key
     * @return XPopupView实例链
     */
    public XPopupViewHolder setTag(@IdRes int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

}