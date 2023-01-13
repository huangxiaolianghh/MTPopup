package com.huangxiaoliang.popup;

import android.content.Context;

/**
 * @author huangxiaolianghh
 * @date 2022/4/2 11:44
 * @desc 描述
 */
public final class DialogActivityConfig extends BaseConfig<DialogActivityConfig, DialogActivityDelegate> {

    protected static DialogActivityConfig get(Context context) {
        return new DialogActivityConfig(context);
    }

    private DialogActivityConfig(Context context) {
        super(context);
    }

    @Override
    public Popup<DialogActivityDelegate> create() {
        return new Popup<>(this, DialogActivityDelegate.class, PopupCompat.DialogActivity);
    }
}