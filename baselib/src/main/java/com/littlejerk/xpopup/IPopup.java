package com.littlejerk.xpopup;

import android.content.Context;

import com.littlejerk.xpopup.view.XPopupViewHolder;

/**
 * @author HHotHeart
 * @date 2021/12/31 15:51
 * @desc 描述
 */
public interface IPopup {

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
     * XPopup 代理实例
     *
     * @return Object
     */
    Object getDelegate();

    /**
     * 获取PopupView的holder的对象
     *
     * @return ViewHolder
     */
    XPopupViewHolder getPopupViewHolder();

    /**
     * XPopup类型
     *
     * @return {@link XPopupCompat#Dialog}、{@link XPopupCompat#DialogFragment}、{@link XPopupCompat#DialogActivity}、{@link XPopupCompat#PopupWindow}
     */
    @XPopupCompat.PopupType
    int getPopupType();
}