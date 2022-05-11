package com.littlejerk.xpopup;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;

import com.littlejerk.xpopup.view.XPopupViewHolder;

/**
 * @author HHotHeart
 * @date 2021/12/30 11:41
 * @desc XPopup事件监听
 */
public interface XPopupInterface {

    /**
     * XPopup显示
     */
    void show();

    /**
     * XPopup消失
     */
    void dismiss();

    /**
     * 初始化ViewHolder
     *
     * @param viewHolder ViewHolder
     */
    void initViewHolder(XPopupViewHolder viewHolder);

    interface OnCancelListener {
        /**
         * XPopup取消监听
         *
         * @param popupInterface XPopup实例
         */
        void onCancel(XPopupInterface popupInterface);
    }

    interface OnDismissListener {
        /**
         * XPopup消失监听
         *
         * @param popupInterface XPopup实例
         */
        void onDismiss(XPopupInterface popupInterface);
    }

    interface OnShowListener {
        /**
         * XPopup显示监听
         *
         * @param popupInterface XPopup实例
         */
        void onShow(XPopupInterface popupInterface);
    }

    interface OnClickListener {
        /**
         * XPopup点击事件监听
         *
         * @param popupInterface XPopup实例
         * @param view           View
         * @param holder         操作XPopup View的类
         */
        void onClick(XPopupInterface popupInterface, View view, XPopupViewHolder holder);
    }

    interface OnLongClickListener {
        /**
         * XPopup长按事件监听
         *
         * @param popupInterface XPopup实例
         * @param view           View
         * @param holder         操作XPopup View的类
         */
        boolean onLongClick(XPopupInterface popupInterface, View view, XPopupViewHolder holder);
    }

    interface OnKeyListener {
        /**
         * 拦截物理按键监听
         *
         * @param popupInterface XPopup实例
         * @param keyCode        物理按键
         * @param event          事件
         * @return 是否拦截处理
         */
        boolean onKey(XPopupInterface popupInterface, int keyCode, KeyEvent event);
    }

    interface OnBindViewListener {
        /**
         * RootView绑定XPopup之前
         *
         * @param holder XPopupView
         */
        void bindView(XPopupViewHolder holder);
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
