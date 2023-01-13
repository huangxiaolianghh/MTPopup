package com.huangxiaoliang.popup;

import android.content.Context;

/**
 * @author huangxiaolianghh
 * @date 2022/3/30 20:52
 * @desc 普通Dialog 弹框配置
 */
public final class DialogConfig extends BaseConfig<DialogConfig, DialogDelegate> {

    protected static DialogConfig get(Context context) {
        return new DialogConfig(context);
    }

    protected DialogConfig(Context context) {
        super(context);
    }

    @Override
    public Popup<DialogDelegate> create() {
        return new Popup<>(this, DialogDelegate.class, PopupCompat.Dialog);
    }
}