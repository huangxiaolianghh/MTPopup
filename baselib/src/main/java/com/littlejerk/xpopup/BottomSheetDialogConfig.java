package com.littlejerk.xpopup;

import android.content.Context;
import android.view.Gravity;

/**
 * @author HHotHeart
 * @date 2022/5/4 22:21
 * @desc 底部可拖拽的Dialog
 */
public final class BottomSheetDialogConfig extends BottomSheetBehaviorConfig<BottomSheetDialogConfig> {

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

    @SuppressWarnings("rawtypes")
    @Override
    public XPopup<BottomSheetDialogConfig, BottomSheetDialogDelegate> create() {
        return new XPopup<BottomSheetDialogConfig, BottomSheetDialogDelegate>(
                this,
                BottomSheetDialogDelegate.class,
                XPopupCompat.BottomSheetDialog);
    }
}