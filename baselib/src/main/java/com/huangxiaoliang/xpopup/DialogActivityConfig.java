package com.huangxiaoliang.xpopup;

import android.content.Context;

/**
 * @author HHotHeart
 * @date 2022/4/2 11:44
 * @desc 描述
 */
public final class DialogActivityConfig extends BaseConfig<DialogActivityConfig> {

    protected static DialogActivityConfig get(Context context) {
        return new DialogActivityConfig(context);
    }

    private DialogActivityConfig(Context context) {
        super(context);
    }

    @Override
    public XPopup<DialogActivityConfig, DialogActivityDelegate> create() {
        return new XPopup<>(this, DialogActivityDelegate.class, XPopupCompat.DialogActivity);
    }
}