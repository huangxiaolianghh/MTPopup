package com.huangxiaoliang.popup;

import android.content.Context;
import android.view.Gravity;

/**
 * @author huangxiaolianghh
 * @date 2022/5/4 22:21
 * @desc 底部可拖拽的Dialog
 */
public final class BottomSheetDialogConfig extends BottomSheetBehaviorConfig<BottomSheetDialogConfig,
        BottomSheetDialogDelegate> {

    protected static BottomSheetDialogConfig get(Context context) {
        return new BottomSheetDialogConfig(context);
    }

    private BottomSheetDialogConfig(Context context) {
        super(context);
        gravity(Gravity.BOTTOM);
    }

    @Override
    public BottomSheetDialogConfig gravity(int gravity) {
        return super.gravity(Gravity.BOTTOM);
    }

    @Override
    public Popup<BottomSheetDialogDelegate> create() {
        return new Popup<>(this, BottomSheetDialogDelegate.class, PopupCompat.BottomSheetDialog);
    }
}