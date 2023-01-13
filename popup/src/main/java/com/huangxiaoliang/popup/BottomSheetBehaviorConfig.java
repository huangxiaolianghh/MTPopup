package com.huangxiaoliang.popup;

import android.content.Context;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

/**
 * @author huangxiaolianghh
 * @date 2022/5/6 21:05
 * @desc BehaviorConfig配置
 */
public abstract class BottomSheetBehaviorConfig<Config extends BottomSheetBehaviorConfig<Config, Delegate>,
        Delegate extends BaseDelegate<?, ?>> extends BaseConfig<Config, Delegate> {

    private int mPeekHeight = -1;
    private boolean mDraggable = true;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback;

    public BottomSheetBehaviorConfig() {
    }

    public BottomSheetBehaviorConfig(Context context) {
        super(context);
    }

    public boolean isDraggable() {
        return mDraggable;
    }

    public Config draggable(boolean draggable) {
        mDraggable = draggable;
        return (Config) this;
    }

    public int getPeekHeight() {
        return mPeekHeight;
    }

    public Config peekHeight(int peekHeight) {
        mPeekHeight = peekHeight;
        return (Config) this;
    }

    public BottomSheetBehavior.BottomSheetCallback getBottomSheetCallback() {
        return mBottomSheetCallback;
    }

    public Config bottomSheetCallback(BottomSheetBehavior.BottomSheetCallback bottomSheetCallback) {
        mBottomSheetCallback = bottomSheetCallback;
        return (Config) this;
    }
}