package com.huangxiaoliang.xpopup;

import android.view.View;

import com.huangxiaoliang.xpopup.util.Preconditions;
import com.huangxiaoliang.xpopup.util.Utils;
import com.huangxiaoliang.xpopup.view.XPopupViewHolder;

import androidx.annotation.NonNull;

/**
 * @author HHotHeart
 * @date 2022/4/6 14:12
 * @desc 代理公共类
 */
public abstract class BaseDelegate<Config extends BaseConfig<Config>, Popup> implements XPopupInterface {

    /**
     * 配置参数管理类
     */
    private final Config mConfig;

    /**
     * Popup view的Holder
     */
    private XPopupViewHolder mViewHolder;

    /**
     * 绑定Config
     *
     * @param config 配置项
     */
    protected BaseDelegate(@NonNull Config config) {
        Preconditions.checkNotNull(config.getContext(), "please init XPopup context");
        mConfig = config;
        buildPopupBeforeDecorateView();
        decorateView();
        bindListener();
        buildPopupAfterDecorateView();
        applyParamsToPopup();
    }

    @Override
    public void show() {
        if (!Utils.checkXPopupRunEnv(config().getContext())) {
            return;
        }
        Utils.runOnUiThread(this::showPopup);
        if (mConfig.getAutoDismissTime() <= 0L) {
            return;
        }
        Utils.runOnUiThreadDelayed(this::dismiss, mConfig.getAutoDismissTime());
    }

    @Override
    public void dismiss() {
        //如果添加了生命周期的监听，采用try catch方式关闭XPopup
        if (config().isDismissObserverOnDestroy() || config().getXPopupLifecycleObserver() != null) {
            try {
                Utils.runOnUiThread(this::dismissPopup);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (!Utils.checkXPopupRunEnv(config().getContext())) {
            return;
        }
        Utils.runOnUiThread(this::dismissPopup);
    }

    @Override
    public void initViewHolder(XPopupViewHolder viewHolder) {
        mViewHolder = viewHolder;
    }

    /**
     * 获取Popup View的holder对象
     *
     * @return ViewHolder
     */
    public XPopupViewHolder getPopupViewHolder() {
        return mViewHolder;
    }

    /**
     * 获取Config配置
     *
     * @return Config
     */
    public Config config() {
        return mConfig;
    }

    /**
     * 在装饰RootView前构建Popup实例
     */
    protected void buildPopupBeforeDecorateView() {
    }

    /**
     * 装饰RootView
     */
    protected abstract void decorateView();

    /**
     * 绑定监听器
     */
    protected abstract void bindListener();

    /**
     * 装饰RootView后构建Popup实例
     */
    protected void buildPopupAfterDecorateView() {
    }

    /**
     * 获取Popup实例
     *
     * @return Popup实例
     */
    public abstract Popup getPopup();

    /**
     * Popup构建相关参数
     */
    protected abstract void applyParamsToPopup();

    /**
     * 显示Popup
     */
    abstract void showPopup();

    /**
     * 关闭Popup
     */
    abstract void dismissPopup();

    /**
     * 获取DecorView
     *
     * @return DecorView
     */
    public abstract View getDecorView();

    /**
     * 释放资源
     */
    public abstract void release();

}