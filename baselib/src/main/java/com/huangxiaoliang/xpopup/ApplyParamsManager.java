package com.huangxiaoliang.xpopup;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.huangxiaoliang.xpopup.util.Preconditions;
import com.huangxiaoliang.xpopup.util.Utils;
import com.huangxiaoliang.xpopup.view.XPopupRootView;
import com.huangxiaoliang.xpopup.view.XPopupViewHolder;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.huangxiaoliang.xpopup.util.Utils.ALPHA_0;
import static com.huangxiaoliang.xpopup.util.Utils.NO_RES_ID;
import static com.huangxiaoliang.xpopup.util.Utils.isOutOfBounds;
import static com.huangxiaoliang.xpopup.view.IDecorLayout.HIDE_RADIUS_SIDE_BOTTOM;
import static com.huangxiaoliang.xpopup.view.IDecorLayout.HIDE_RADIUS_SIDE_LEFT;
import static com.huangxiaoliang.xpopup.view.IDecorLayout.HIDE_RADIUS_SIDE_RIGHT;
import static com.huangxiaoliang.xpopup.view.IDecorLayout.HIDE_RADIUS_SIDE_TOP;

/**
 * @author HHotHeart
 * @date 2022/4/11 18:19
 * @desc 抽取公共逻辑
 */
class ApplyParamsManager {

    /**
     * 装饰Popup的根布局
     *
     * @param config popup的配置属性
     * @param <T>    config
     * @return config
     */
    public static <T extends BaseConfig<T>> T decorateView(T config) {
        XPopupRootView xPopupRootView = new XPopupRootView(config.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(config.getWidth(), config.getHeight());
        xPopupRootView.setLayoutParams(layoutParams);
        if (config.getRadius() > 0) {
            xPopupRootView.setRadius(config.getRadius());
        } else if (config.getRadiusSideLeft() > 0) {
            xPopupRootView.setRadius(config.getRadiusSideLeft(), HIDE_RADIUS_SIDE_RIGHT);
        } else if (config.getRadiusSideTop() > 0) {
            xPopupRootView.setRadius(config.getRadiusSideTop(), HIDE_RADIUS_SIDE_BOTTOM);
        } else if (config.getRadiusSideRight() > 0) {
            xPopupRootView.setRadius(config.getRadiusSideRight(), HIDE_RADIUS_SIDE_LEFT);
        } else if (config.getRadiusSideBottom() > 0) {
            xPopupRootView.setRadius(config.getRadiusSideBottom(), HIDE_RADIUS_SIDE_TOP);
        }
        Preconditions.checkNotNull(config.getContentView(), "please call config view()");
        xPopupRootView.addView(config.getContentView());
        //xPopupRootView最大宽高设置
        if (config.getMaxWidth() > 0) {
            xPopupRootView.setWidthLimit(config.getMaxWidth());
        }
        if (config.getMaxHeight() > 0) {
            xPopupRootView.setHeightLimit(config.getMaxHeight());
        }
        config.xPopupRootView(xPopupRootView);
        return config;
    }

    /**
     * 设置相关按钮点击事件回调
     *
     * @param popupInterface popup代理实例
     * @param config         popup配置属性
     * @param <T>            config
     * @return config
     */
    @SuppressWarnings("unchecked")
    protected static <T extends BaseConfig<T>> T bindListener(XPopupInterface popupInterface, T config) {
        //View绑定Dialog之前回调
        XPopupViewHolder holder = new XPopupViewHolder(config.getContext(), config.getXPopupRootView());
        popupInterface.initViewHolder(holder);
        if (config.getBindViewListener() != null) {
            config.getBindViewListener().bindView(holder);
        }
        //处理点击事件
        SparseArray<XPopupInterface.OnClickListener> clickElement = config.getClickElement();
        if (clickElement != null) {
            for (int i = 0, size = clickElement.size(); i < size; i++) {
                int viewId = clickElement.keyAt(i);
                XPopupInterface.OnClickListener xPopupClickListener = clickElement.valueAt(i);
                holder.setOnClickListener(viewId, v -> xPopupClickListener.onClick(popupInterface, v, holder));
            }
        }
        if (config.getClickIds() != null && config.getClickIds().length > 0) {
            for (int viewId : config.getClickIds()) {
                holder.setOnClickListener(viewId, v -> {
                    if (config.getOnClickListener() != null) {
                        config.getOnClickListener().onClick(popupInterface, v, holder);
                    }
                });
            }
        }
        //处理长按事件
        SparseArray<XPopupInterface.OnLongClickListener> longClickElement = config.getLongClickElement();
        if (longClickElement != null) {
            for (int i = 0, size = longClickElement.size(); i < size; i++) {
                int viewId = longClickElement.keyAt(i);
                XPopupInterface.OnLongClickListener xPopupLongClickListener = longClickElement.valueAt(i);
                holder.setOnLongClickListener(viewId, v -> xPopupLongClickListener.onLongClick(popupInterface, v, holder));
            }
        }
        if (config.getLongClickIds() != null && config.getLongClickIds().length > 0) {
            for (int viewId : config.getLongClickIds()) {
                holder.setOnLongClickListener(viewId, v -> {
                    if (config.getOnLongClickListener() != null) {
                        return config.getOnLongClickListener().onLongClick(popupInterface, v, holder);
                    }
                    return false;
                });
            }
        }
        return config;
    }

    /**
     * 设置popup相关属性
     *
     * @param popupInterface popup代理实例
     * @param popup          弹框实例
     * @param decorView      弹框的DecorView
     * @param config         popup配置属性
     * @param <T>            config
     */
    @SuppressLint("ClickableViewAccessibility")
    protected static <T extends BaseConfig<T>> void applyParamsToPopup(XPopupInterface popupInterface,
                                                                       Dialog popup,
                                                                       View decorView,
                                                                       T config) {
        Window window = popup.getWindow();
        if (window == null) {
            return;
        }
        //设置Popup进出场动画
        if (config.getAnimStyle() != NO_RES_ID) {
            window.setWindowAnimations(config.getAnimStyle());
        }
        //设置Popup背景透明
        window.setBackgroundDrawable(config.getBackgroundDrawable() == null
                ? new ColorDrawable(Color.TRANSPARENT) : config.getBackgroundDrawable());
        //设置Popup背景透明度
        if (config.getDimAmount() > ALPHA_0) {
            window.setDimAmount(config.getDimAmount());
        }
        if (popup instanceof BottomSheetDialog) {
            //如果是BottomSheetDialog实例，则设置消失时动画开关（BottomSheetFragmentDialog）
            ((BottomSheetDialog) popup).setDismissWithAnimation(true);
            ((BottomSheetDialog) popup).setCancelable(config.isCancelable());
            ((BottomSheetDialog) popup).setCanceledOnTouchOutside(config.isCancelableOutside());
        } else {
            //非BottomSheetDialog可更新Window的宽高，如果是BottomSheetDialog执行此操作，对于有导航栏的手机会抖动
            ApplyParamsManager.updateWindowLayout(window, config);
            popup.setCancelable(config.isCancelable());
            popup.setCanceledOnTouchOutside(config.isCancelableOutside());
        }
        popup.setOnKeyListener((dialog, keyCode, event) -> !config.isCancelable());
        //点击Popup空白区域的监听
        if (config.isCancelableOutside()) {
            if (decorView == null) {
                return;
            }
            decorView.setOnTouchListener((v, event) -> {
                if (isOutOfBounds(decorView, event) && popup.isShowing()) {
                    popupInterface.dismiss();
                    return true;
                }
                return false;
            });
        }
    }


    /**
     * 重新更新Window的布局，BottomSheet系列弹框不适用<p>
     * windowDrawsSystemBarBackgrounds=true,高度需要考虑状态栏和导航栏情况
     *
     * @param window Window
     */
    protected static <T extends BaseConfig<T>> void updateWindowLayout(@NonNull Window window, T config) {
        //设置Dialog宽高为屏幕的百分比
        WindowManager.LayoutParams pl = window.getAttributes();
        int width = config.getWidth();
        int height = config.getHeight();
        if (config.getWidthInPercent() > 0 && config.getWidthInPercent() <= 1) {
            width = (int) (config.getWidthInPercent() * Utils.getScreenWidth(config.getContext()));
        }
        if (config.getHeightInPercent() > 0 && config.getHeightInPercent() <= 1) {
            height = (int) (config.getHeightInPercent() * Utils.getScreenHeight(config.getContext()));
        }
        if (config.getMaxWidth() > 0 && config.getMaxWidth() <= Utils.getScreenWidth(config.getContext())) {
            width = width > 0 ? Math.min(width, config.getMaxWidth()) : config.getMaxWidth();
        }
        if (config.getMaxHeight() > 0 && config.getMaxHeight() <= Utils.getScreenHeight(config.getContext())) {
            height = height > 0 ? Math.min(width, config.getMaxHeight()) : config.getMaxHeight();
        }
        pl.gravity = config.getGravity();
        pl.width = width;
        pl.height = height;
        window.setAttributes(pl);
    }

    /**
     * 更新BottomSheetDialog布局
     *
     * @param bottomSheetView BottomSheetDialog Root
     * @param config          config配置对象
     * @param <T>             config泛型
     */
    protected static <T extends BottomSheetBehaviorConfig<T>> void updateBottomSheetDialogLayout(@NonNull FrameLayout bottomSheetView, T config) {
        int width = config.getWidth();
        int height = config.getHeight();
        if (config.getWidthInPercent() > 0 && config.getWidthInPercent() <= 1) {
            width = (int) (config.getWidthInPercent() * Utils.getScreenWidth(config.getContext()));
        }
        if (config.getHeightInPercent() > 0 && config.getHeightInPercent() <= 1) {
            height = (int) (config.getHeightInPercent() * Utils.getScreenHeight(config.getContext()));
        }
        if (config.getMaxWidth() > 0 && config.getMaxWidth() <= Utils.getScreenWidth(config.getContext())) {
            width = width > 0 ? Math.min(width, config.getMaxWidth()) : config.getMaxWidth();
        }
        if (config.getMaxHeight() > 0 && config.getMaxHeight() <= Utils.getScreenHeight(config.getContext())) {
            height = height > 0 ? Math.min(height, config.getMaxHeight()) : config.getMaxHeight();
        }
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) bottomSheetView.getLayoutParams();
        params.width = width;
        params.height = height;

        BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheetView);
        if (config.getPeekHeight() >= 0) {
            behavior.setPeekHeight(config.getPeekHeight());
        }
        behavior.setDraggable(config.isDraggable());
        if (config.getBottomSheetCallback() != null) {
            behavior.addBottomSheetCallback(config.getBottomSheetCallback());
        }
    }
}