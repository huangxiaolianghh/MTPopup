package com.huangxiaoliang.xpopup;

import static com.huangxiaoliang.xpopup.util.Utils.NO_RES_ID;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.huangxiaoliang.xpopup.util.Preconditions;

/**
 * @author HHotHeart
 * @date 2022/4/6 15:41
 * @desc 普通Dialog代理类
 */
public final class DialogDelegate extends BaseDelegate<DialogConfig, AppCompatDialog> {

    /**
     * 普通Dialog对象
     */
    private AppCompatDialog mDialog;

    /**
     * 构造方法
     *
     * @param config DialogConfig
     */
    public DialogDelegate(@NonNull DialogConfig config) {
        super(config);
    }

    @Override
    protected void buildPopupBeforeDecorateView() {
        int themeStyle = config().getThemeStyle() == NO_RES_ID ? R.style.XPopup_Dialog : config().getThemeStyle();
        mDialog = new AppCompatDialog(config().getContext(), themeStyle);
    }

    @Override
    protected void decorateView() {
        ApplyParamsManager.decorateView(config());
    }

    @Override
    protected void bindListener() {
        Preconditions.checkNotNull(config().getXPopupRootView(), "please init Dialog root view");
        ApplyParamsManager.bindListener(this, config());
    }

    @Override
    public AppCompatDialog getPopup() {
        return mDialog;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void applyParamsToPopup() {
        Preconditions.checkNotNull(getPopup(), "please check Dialog:getPopup()==null");
        getPopup().setContentView(config().getXPopupRootView());
        ApplyParamsManager.applyParamsToPopup(this, getPopup(), getDecorView(), config());
        //设置Popup显示时监听
        if (config().getOnShowListener() != null) {
            getPopup().setOnShowListener((DialogInterface dialog) -> config().getOnShowListener().onShow(this));
        }
        //设置Popup消失时监听
        if (config().getOnDismissListener() != null) {
            getPopup().setOnDismissListener(dialog -> config().getOnDismissListener().onDismiss(this));
        }
    }

    @Override
    void showPopup() {
        getPopup().show();
    }

    @Override
    void dismissPopup() {
        getPopup().dismiss();
    }

    @Override
    public View getDecorView() {
        if (getPopup() == null || getPopup().getWindow() == null) {
            return null;
        }
        return getPopup().getWindow().getDecorView();
    }

    @Override
    public void release() {
        mDialog = null;
    }
}