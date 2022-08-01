package com.huangxiaoliang.xpopup;

import android.content.Context;
import android.util.Log;

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
     * 上下文
     */
    private final Context mContext;

    /**
     * XPopup类型
     */
    @XPopupCompat.PopupType
    private final int mPopupType;

    /**
     * XPopup 代理实例
     */
    private Delegate mDelegate;

    protected XPopup(@NonNull Config config, Class<Delegate> delegateClass, @XPopupCompat.PopupType int popupType) {
        mPopupType = popupType;
        mDelegate = getT(delegateClass, config);
        Preconditions.checkNotNull(mDelegate, "delegate is null,please check popup class type");
        mContext = mDelegate.config().getContext();
        if (mDelegate.config().getLifecycle() != null) {
            mDelegate.config().getLifecycle().addObserver(this);
        }
    }

    @Override
    public void dismiss() {
        getDelegate().dismiss();
        getDelegate().release();
        mDelegate = null;
    }

    @Override
    public void show() {
        getDelegate().show();
    }

    @Override
    public Delegate getDelegate() {
        Preconditions.checkNotNull(mDelegate, "Delegate is null,please call Config create() method again");
        return mDelegate;
    }

    @Override
    public XPopupViewHolder getPopupViewHolder() {
        Preconditions.checkNotNull(mDelegate, "Delegate is null,please call Config create() method again");
        return getDelegate().getPopupViewHolder();
    }

    @Override
    public int getPopupType() {
        Preconditions.checkNotNull(mDelegate, "Delegate is null,please call Config create() method again");
        return mPopupType;
    }

    @Override
    public Context getContext() {
        Preconditions.checkNotNull(mDelegate, "Delegate is null,please call Config create() method again");
        return mContext;
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
            Log.e("XPopup", e.getMessage());
        }
        return delegate;
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