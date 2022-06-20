package com.huangxiaoliang.xpopup;

import android.content.Context;
import android.util.Log;

import com.huangxiaoliang.xpopup.util.Preconditions;
import com.huangxiaoliang.xpopup.view.XPopupViewHolder;

import androidx.annotation.NonNull;

/**
 * @author HHotHeart
 * @date 2021/12/31 15:51
 * @desc XPopup
 */
@SuppressWarnings("rawtypes")
public class XPopup<Config extends BaseConfig, Delegate extends BaseDelegate> implements IPopup {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * XPopup类型
     */
    @XPopupCompat.PopupType
    private int mPopupType;

    /**
     * XPopup 代理实例
     */
    private Delegate mDelegate;

    protected XPopup(@NonNull Config config, Class<Delegate> delegateClass, @XPopupCompat.PopupType int popupType) {
        mContext = config.getContext();
        mPopupType = popupType;
        mDelegate = getT(delegateClass, config);
        Preconditions.checkNotNull(mDelegate, "delegate is null,please check popup class type");
    }

    @Override
    public void dismiss() {
        getDelegate().dismiss();
    }

    @Override
    public void show() {
        getDelegate().show();
    }

    @Override
    public Delegate getDelegate() {
        return mDelegate;
    }

    @Override
    public XPopupViewHolder getPopupViewHolder() {
        Preconditions.checkNotNull(getDelegate(), "Delegate is null,please check the Popup create() method");
        return getDelegate().getPopupViewHolder();
    }

    @Override
    public int getPopupType() {
        return mPopupType;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * 泛型实例化带参数的构造函数
     *
     * @param cls    popup代理类
     * @param config config配置类
     * @return popup代理实例对象
     */
    public Delegate getT(Class<Delegate> cls, Config config) {
        Delegate delegate = null;
        try {
            delegate = cls.getConstructor(config.getClass()).newInstance(config);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("XPopup", e.getMessage());
        }
        return delegate;
    }
}