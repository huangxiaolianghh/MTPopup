package com.littlejerk.xpopup.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.littlejerk.xpopup.R;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

/**
 * @author HHotHeart
 * @date 2021/10/11 17:21
 * @desc 描述
 */
public class Utils {

    private static final String TAG = "Utils";

    public static final float ALPHA_0 = 0f;
    public static final float ALPHA_1 = 1f;

    public static final int NO_RES_ID = 0;

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());


    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 根据Context获取Activity
     *
     * @param context
     * @return
     */
    public static Activity getActivityByContext(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return getActivityByContext(wrapper.getBaseContext());
        } else {
            throw new IllegalStateException("The Context is not an Activity.");
        }
    }

    /**
     * 获取 DisplayMetrics
     *
     * @return DisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 返回px为单位的屏幕宽度
     *
     * @return 屏幕宽度，单位px
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * 返回px为单位的屏幕高度
     *
     * @return 屏幕高度，单位px
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    /**
     * 判读是否为主线程
     *
     * @return boolean值：是否为主线程
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 获取主线程的Handle
     *
     * @return 主线程的Handle
     */
    public static Handler getMainHandler() {
        return HANDLER;
    }

    /**
     * 运行在主线程
     *
     * @param runnable 任务Runnable
     */
    public static void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            HANDLER.post(runnable);
        }
    }

    /**
     * 延时任务运行在主线程
     *
     * @param runnable    任务Runnable
     * @param delayMillis 延时时间
     */
    public static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }


    /**
     * 检查XPopup运行环境是否正常
     *
     * @param context 上下文
     * @return boolean值：是否符合XPopup运行环境
     */
    public static boolean checkXPopupRunEnv(Context context) {
        if (context == null) {
            return false;
        }
        Activity activity = (Activity) context;
        if (activity.isDestroyed() || activity.isFinishing()) {
            return false;
        }
        return true;
    }


    /**
     * 获取VectorDrawable
     *
     * @param context   上下文
     * @param resVector 资源id
     * @return Drawable
     */
    public static
    @Nullable
    Drawable getVectorDrawable(Context context, @DrawableRes int resVector) {
        try {
            return AppCompatResources.getDrawable(context, resVector);
        } catch (Exception e) {
            Log.d(TAG, "Error in getVectorDrawable. resVector=" + resVector + ", resName=" + context.getResources().getResourceName(resVector) + e.getMessage());
            return null;
        }
    }

    /**
     * 设置Background,保持padding不变
     *
     * @param view     目标view
     * @param drawable 需要设置的drawable
     */
    public static void setBackgroundKeepingPadding(View view, Drawable drawable) {
        int[] padding = new int[]{view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()};
        view.setBackground(drawable);
        view.setPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    /**
     * 是否是XPopup外部区域
     *
     * @param decorView decorView
     * @param event     触摸事件
     * @return 是否为外部区域
     */
    public static boolean isOutOfBounds(View decorView, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        if (decorView == null) {
            Log.e("Utils", "decorView == null");
            return false;
        }
        final int slop = ViewConfiguration.get(decorView.getContext()).getScaledWindowTouchSlop();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop)) || (y > (decorView.getHeight() + slop));
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的activity
     * @param color    颜色
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void decorateStatusBar(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    public static int calculateStatusColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 设置DialogActivity的转场动画
     *
     * @param activity  上下文
     * @param animStyle 动画
     * @param enter     是否为开启动画
     */
    public static void overridePendingTransition(Activity activity, int animStyle, boolean enter) {
        if (animStyle == NO_RES_ID) {
            animStyle = R.style.ActivityEnter0Exit0Animation;
        }
        int[] animIds = UIResHelper.assignDialogActivityAnimWithAttr(activity, animStyle);
        if (enter) {
            activity.overridePendingTransition(animIds[0], animIds[1]);
        } else {
            activity.overridePendingTransition(animIds[2], animIds[3]);
        }
    }
}
