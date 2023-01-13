package com.huangxiaoliang.popup;

import android.content.Context;

import com.huangxiaoliang.popup.view.PopupViewHolder;

/**
 * @author huangxiaolianghh
 * @date 2021/12/31 15:51
 * @desc 描述
 */
public interface IPopup<Delegate extends BaseDelegate<?, ?>> {

    /**
     * 显示
     */
    void show();

    /**
     * 关闭
     */
    void dismiss();

    /**
     * 获取上下文
     *
     * @return Context
     */
    Context getContext();

    /**
     * Popup 代理实例
     *
     * @return Delegate
     */
    Delegate getDelegate();

    /**
     * 获取PopupView的holder的对象
     *
     * @return ViewHolder
     */
    PopupViewHolder getPopupViewHolder();

    /**
     * Popup类型
     *
     * @return {@link PopupCompat#Dialog}、{@link PopupCompat#DialogFragment}、{@link PopupCompat#DialogActivity}、{@link PopupCompat#PopupWindow}
     */
    @PopupCompat.PopupType
    int getPopupType();
}