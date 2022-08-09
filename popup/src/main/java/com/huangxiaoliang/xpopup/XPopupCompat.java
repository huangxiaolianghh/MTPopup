package com.huangxiaoliang.xpopup;

import static androidx.fragment.app.DialogFragment.STYLE_NORMAL;
import static androidx.fragment.app.DialogFragment.STYLE_NO_FRAME;
import static androidx.fragment.app.DialogFragment.STYLE_NO_INPUT;
import static androidx.fragment.app.DialogFragment.STYLE_NO_TITLE;

import android.content.Context;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author HHotHeart
 * @date 2021/12/30 10:27
 * @desc 描述
 */
public class XPopupCompat {

    @IntDef(value = {STYLE_NORMAL, STYLE_NO_TITLE, STYLE_NO_FRAME, STYLE_NO_INPUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogFragmentStyle {
    }

    @IntDef({No_Popup, Dialog, BottomSheetDialog, DialogFragment, BottomSheetDialogFragment, DialogActivity, PopupWindow})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PopupType {
    }

    public static final int No_Popup = -1;
    public static final int Dialog = 0;
    public static final int BottomSheetDialog = 1;
    public static final int DialogFragment = 2;
    public static final int BottomSheetDialogFragment = 3;
    public static final int DialogActivity = 4;
    public static final int PopupWindow = 5;

    /**
     * 私有构造函数
     */
    private XPopupCompat() {
    }

    /**
     * 静态Holder类，静态XPopupCompat实例对象
     */
    private static class XPopupCompatHolder {
        private static final XPopupCompat INSTANCE = new XPopupCompat();
    }

    /**
     * 获取XPopupCompat单例
     *
     * @return XPopupCompat
     */
    public static XPopupCompat get() {
        return XPopupCompatHolder.INSTANCE;
    }

    /**
     * 获取Dialog的config对象
     *
     * @param context 上下文
     * @return Dialog config对象
     */
    public DialogConfig asDialog(Context context) {
        return as(context, DialogConfig.class);
    }

    /**
     * 获取BottomSheetDialog的config对象
     *
     * @param context 上下文
     * @return Dialog config对象
     */
    public BottomSheetDialogConfig asBottomSheetDialog(Context context) {
        return as(context, BottomSheetDialogConfig.class);
    }

    /**
     * 获取PopupWindow的config对象
     *
     * @param context 上下文
     * @return PopupWindow config对象
     */
    public PopupWindowConfig asPopupWindow(Context context) {
        return as(context, PopupWindowConfig.class);
    }

    /**
     * 获取DialogFragment的config对象
     *
     * @param context 上下文
     * @return DialogFragment config对象
     */
    public DialogFragmentConfig asDialogFragment(Context context) {
        return as(context, DialogFragmentConfig.class);
    }

    /**
     * 获取BottomSheetDialogFragment的config对象
     *
     * @param context 上下文
     * @return DialogFragment config对象
     */
    public BottomSheetDialogFragmentConfig asBottomSheetDialogFragment(Context context) {
        return as(context, BottomSheetDialogFragmentConfig.class);
    }

    /**
     * 获取DialogActivity的config对象
     *
     * @param context 上下文
     * @return DialogActivity config对象
     */
    public DialogActivityConfig asDialogActivity(Context context) {
        return as(context, DialogActivityConfig.class);
    }

    /**
     * 获取弹框的config对象
     *
     * @param context 上下文
     * @param tClass  弹框config的class
     * @param <T>     泛型
     * @return 弹框config对象
     */
    @SuppressWarnings("unchecked")
    protected <T> T as(Context context, Class<T> tClass) {
        if (tClass == DialogConfig.class) {
            return (T) DialogConfig.get(context);
        } else if (tClass == BottomSheetDialogConfig.class) {
            return (T) BottomSheetDialogConfig.get(context);
        } else if (tClass == DialogFragmentConfig.class) {
            return (T) DialogFragmentConfig.get(context);
        } else if (tClass == BottomSheetDialogFragmentConfig.class) {
            return (T) BottomSheetDialogFragmentConfig.get(context);
        } else if (tClass == DialogActivityConfig.class) {
            return (T) DialogActivityConfig.get(context);
        } else if (tClass == PopupWindowConfig.class) {
            return (T) PopupWindowConfig.get(context);
        } else {
            return null;
        }
    }

}