package com.huangxiaoliang.popup;

import static com.huangxiaoliang.popup.util.Utils.ALPHA_0;
import static com.huangxiaoliang.popup.util.Utils.NO_RES_ID;
import static com.huangxiaoliang.popup.util.Utils.isOutOfBounds;
import static com.huangxiaoliang.popup.view.IDecorLayout.HIDE_RADIUS_SIDE_BOTTOM;
import static com.huangxiaoliang.popup.view.IDecorLayout.HIDE_RADIUS_SIDE_LEFT;
import static com.huangxiaoliang.popup.view.IDecorLayout.HIDE_RADIUS_SIDE_RIGHT;
import static com.huangxiaoliang.popup.view.IDecorLayout.HIDE_RADIUS_SIDE_TOP;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.huangxiaoliang.popup.util.Preconditions;
import com.huangxiaoliang.popup.util.Utils;
import com.huangxiaoliang.popup.view.PopupRootView;
import com.huangxiaoliang.popup.view.PopupViewHolder;

/**
 * @author huangxiaolianghh
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
    public static <T extends BaseConfig<T, ?>> T decorateView(T config) {
        PopupRootView popupRootView = new PopupRootView(config.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(config.getWidth(), config.getHeight());
        popupRootView.setLayoutParams(layoutParams);
        if (config.getRadius() > 0) {
            popupRootView.setRadius(config.getRadius());
        } else if (config.getRadiusSideLeft() > 0) {
            popupRootView.setRadius(config.getRadiusSideLeft(), HIDE_RADIUS_SIDE_RIGHT);
        } else if (config.getRadiusSideTop() > 0) {
            popupRootView.setRadius(config.getRadiusSideTop(), HIDE_RADIUS_SIDE_BOTTOM);
        } else if (config.getRadiusSideRight() > 0) {
            popupRootView.setRadius(config.getRadiusSideRight(), HIDE_RADIUS_SIDE_LEFT);
        } else if (config.getRadiusSideBottom() > 0) {
            popupRootView.setRadius(config.getRadiusSideBottom(), HIDE_RADIUS_SIDE_TOP);
        }
        Preconditions.checkNotNull(config.getContentView(), "please call config view()");
        popupRootView.addView(config.getContentView());
        //popupRootView最大宽高设置
        if (config.getMaxWidth() > 0) {
            popupRootView.setWidthLimit(config.getMaxWidth());
        }
        if (config.getMaxHeight() > 0) {
            popupRootView.setHeightLimit(config.getMaxHeight());
        }
        config.popupRootView(popupRootView);
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
    protected static <T extends BaseConfig<T, ?>> T bindListener(PopupInterface popupInterface, T config) {
        //View绑定Dialog之前回调
        PopupViewHolder holder = new PopupViewHolder(config.getContext(), config.getPopupRootView());
        popupInterface.bindToViewHolder(holder);
        if (config.getBindViewListener() != null) {
            config.getBindViewListener().bindView(holder);
        }
        //处理点击事件
        SparseArray<PopupInterface.OnClickListener> clickElement = config.getClickElement();
        if (clickElement != null) {
            for (int i = 0, size = clickElement.size(); i < size; i++) {
                int viewId = clickElement.keyAt(i);
                PopupInterface.OnClickListener popupClickListener = clickElement.valueAt(i);
                holder.setOnClickListener(viewId, v -> popupClickListener.onClick(popupInterface, v, holder));
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
        SparseArray<PopupInterface.OnLongClickListener> longClickElement = config.getLongClickElement();
        if (longClickElement != null) {
            for (int i = 0, size = longClickElement.size(); i < size; i++) {
                int viewId = longClickElement.keyAt(i);
                PopupInterface.OnLongClickListener popupLongClickListener = longClickElement.valueAt(i);
                holder.setOnLongClickListener(viewId, v -> popupLongClickListener.onLongClick(popupInterface, v, holder));
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
    protected static <T extends BaseConfig<T, ?>> void applyParamsToPopup(PopupInterface popupInterface,
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
        if (config.getDimAmount() >= ALPHA_0) {
            window.setDimAmount(config.getDimAmount());
        }
        if (popup instanceof BottomSheetDialog) {
            //BottomSheetDialog实例，设置消失时动画开关（BottomSheetFragmentDialog）
            ((BottomSheetDialog) popup).setDismissWithAnimation(true);
            ((BottomSheetDialog) popup).setCancelable(config.isCancelable());
            ((BottomSheetDialog) popup).setCanceledOnTouchOutside(config.isCancelableOutside());
        } else {
            //非BottomSheetDialog可更新Window的宽高
            //如果是BottomSheetDialog执行此操作，对于有导航栏的手机会抖动
            ApplyParamsManager.updateWindowLayout(window, config);
            popup.setCancelable(config.isCancelable());
            popup.setCanceledOnTouchOutside(config.isCancelableOutside());
        }
        popup.setOnKeyListener((dialog, keyCode, event) -> {
            if (config.isCancelable()) {
                popupInterface.dismiss();
            }
            return true;
        });
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
    protected static <T extends BaseConfig<T, ?>> void updateWindowLayout(@NonNull Window window, T config) {
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
    protected static <T extends BottomSheetBehaviorConfig<T, ?>> void updateBottomSheetDialogLayout(@NonNull FrameLayout bottomSheetView, T config) {
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