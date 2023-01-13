package com.huangxiaoliang.popup;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;

import com.huangxiaoliang.popup.view.PopupViewHolder;

/**
 * @author huangxiaolianghh
 * @date 2021/12/30 11:41
 * @desc Popup事件监听
 */
public interface PopupInterface {

    /**
     * Popup显示
     */
    void show();

    /**
     * Popup消失
     */
    void dismiss();

    /**
     * 初始化ViewHolder
     *
     * @param viewHolder ViewHolder
     */
    void bindToViewHolder(PopupViewHolder viewHolder);

    /**
     * 绑定Popup实例
     *
     * @param popup Popup实例
     */
    void bindToPopup(Popup<?> popup);

    interface OnCancelListener {
        /**
         * Popup取消监听
         *
         * @param popupInterface Popup实例
         */
        void onCancel(PopupInterface popupInterface);
    }

    interface OnDismissListener {
        /**
         * Popup消失监听
         *
         * @param popupInterface Popup实例
         */
        void onDismiss(PopupInterface popupInterface);
    }

    interface OnShowListener {
        /**
         * Popup显示监听
         *
         * @param popupInterface Popup实例
         */
        void onShow(PopupInterface popupInterface);
    }

    interface OnClickListener {
        /**
         * Popup点击事件监听
         *
         * @param popupInterface Popup实例
         * @param view           View
         * @param holder         操作Popup View的类
         */
        void onClick(PopupInterface popupInterface, View view, PopupViewHolder holder);
    }

    interface OnLongClickListener {
        /**
         * Popup长按事件监听
         *
         * @param popupInterface Popup实例
         * @param view           View
         * @param holder         操作Popup View的类
         * @return 是否拦截事件
         */
        boolean onLongClick(PopupInterface popupInterface, View view, PopupViewHolder holder);
    }

    interface OnKeyListener {
        /**
         * 拦截物理按键监听
         *
         * @param popupInterface Popup实例
         * @param keyCode        物理按键
         * @param event          事件
         * @return 是否拦截处理
         */
        boolean onKey(PopupInterface popupInterface, int keyCode, KeyEvent event);
    }

    interface OnBindViewListener {
        /**
         * RootView绑定Popup之前
         *
         * @param holder PopupView
         */
        void bindView(PopupViewHolder holder);
    }

    interface OnDialogActivityInit {
        /**
         * DialogActivity实例回调
         *
         * @param activity Activity实例
         */
        void onInit(Activity activity);
    }
}
