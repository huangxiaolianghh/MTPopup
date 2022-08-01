package com.huangxiaoliang.xpopup;

import android.content.Context;

/**
 * @author HHotHeart
 * @date 2022/3/30 20:52
 * @desc 普通Dialog 弹框配置
 */
public final class DialogConfig extends BaseConfig<DialogConfig> {

    protected static DialogConfig get(Context context) {
        return new DialogConfig(context);
    }

    protected DialogConfig(Context context) {
        super(context);
    }

    @Override
    public XPopup<DialogConfig, DialogDelegate> create() {
        return new XPopup<>(this, DialogDelegate.class, XPopupCompat.Dialog);
    }
}