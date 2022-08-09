package com.huangxiaoliang.xpopup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.huangxiaoliang.xpopup.util.Preconditions;
import com.huangxiaoliang.xpopup.view.XPopupViewHolder;

/**
 * @author HHotHeart
 * @date 2021/12/31 15:51
 * @desc XPopup
 */
public class XPopup<Config extends BaseConfig<Config>, Delegate extends BaseDelegate<?, ?>>
        implements IPopup, LifecycleObserver {

    /**
     * Popup 代理实例
     */
    private Delegate mDelegate;

    /**
     * Popup类型
     */
    @XPopupCompat.PopupType
    private int mPopupType;

    protected XPopup(@NonNull Config config, Class<Delegate> delegateClass, @XPopupCompat.PopupType int popupType) {
        mPopupType = popupType;
        mDelegate = getT(delegateClass, config);
        Preconditions.checkNotNull(mDelegate, "delegate is null,please check popup class type");
        if (mDelegate.config().getLifecycle() != null) {
            mDelegate.config().getLifecycle().addObserver(this);
        }
        mDelegate.bindToPopup(this);
    }

    /**
     * Popup关闭时将所有资源释放，只保留Config的配置<br>
     * 下次需要显示时调用{@link BaseConfig#create()}重新初始化Popup
     */
    @Override
    public void dismiss() {
        if (mDelegate == null){
            return;
        }
        getDelegate().dismiss();
    }

    @Override
    public void show() {
        if (mDelegate == null){
            return;
        }
        getDelegate().show();
    }

    @Override
    public Delegate getDelegate() {
        return mDelegate;
    }

    @Override
    public XPopupViewHolder getPopupViewHolder() {
        Preconditions.checkNotNull(mDelegate,
                "Delegate is null,please call Config create() method again");
        return getDelegate().getPopupViewHolder();
    }

    @Override
    public int getPopupType() {
        Preconditions.checkNotNull(mDelegate,
                "Delegate is null,please call Config create() method again");
        return mPopupType;
    }

    @Override
    public Context getContext() {
        Preconditions.checkNotNull(mDelegate,
                "Delegate is null,please call Config create() method again");
        return mDelegate.config().getContext();
    }

    /**
     * 泛型实例化带参数的构造函数
     *
     * @param cls    popup代理类
     * @param config config配置类
     * @return popup代理实例对象
     */
    protected Delegate getT(Class<Delegate> cls, Config config) {
        Delegate delegate = null;
        try {
            delegate = cls.getConstructor(config.getClass()).newInstance(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return delegate;
    }

    /**
     * 释放当前资源
     */
    protected void release() {
        mDelegate = null;
        mPopupType = XPopupCompat.No_Popup;
    }

    /**
     * 被观察者onStart()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
        if (mDelegate == null) {
            return;
        }
        if (mDelegate.config().getXPopupLifecycleObserver() != null) {
            mDelegate.config().getXPopupLifecycleObserver().onStart(this);
        }
    }

    /**
     * 被观察者onResume()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        if (mDelegate == null) {
            return;
        }
        if (mDelegate.config().getXPopupLifecycleObserver() != null) {
            mDelegate.config().getXPopupLifecycleObserver().onResume(this);
        }
    }

    /**
     * 被观察者onPause()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        if (mDelegate == null) {
            return;
        }
        if (mDelegate.config().getXPopupLifecycleObserver() != null) {
            mDelegate.config().getXPopupLifecycleObserver().onPause(this);
        }
    }

    /**
     * 被观察者onStop()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        if (mDelegate == null) {
            return;
        }
        if (mDelegate.config().getXPopupLifecycleObserver() != null) {
            mDelegate.config().getXPopupLifecycleObserver().onStop(this);
        }
    }

    /**
     * 被观察者onDestroy()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
        if (mDelegate == null) {
            return;
        }
        if (mDelegate.config().getLifecycle() != null) {
            mDelegate.config().getLifecycle().removeObserver(this);
        }
        if (mDelegate.config().getXPopupLifecycleObserver() != null) {
            mDelegate.config().getXPopupLifecycleObserver().onDestroy(this);
        } else if (mDelegate.config().isDismissObserverOnDestroy()) {
            dismiss();
        }
    }
}