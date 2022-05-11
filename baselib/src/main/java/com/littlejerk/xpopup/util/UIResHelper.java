package com.littlejerk.xpopup.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;

import com.littlejerk.xpopup.R;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * @author HHotHeart
 * @date 2021/10/22 17:04
 * @desc 描述
 */
public final class UIResHelper {

    private static TypedValue sTmpValue;

    private UIResHelper() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static float getAttrFloatValue(Context context, int attr) {
        return getAttrFloatValue(context.getTheme(), attr);
    }

    public static float getAttrFloatValue(Resources.Theme theme, int attr) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return 0;
        }
        return sTmpValue.getFloat();
    }

    public static int getAttrColor(Context context, int attrRes) {
        return getAttrColor(context.getTheme(), attrRes);
    }

    public static int getAttrColor(Resources.Theme theme, int attr) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return 0;
        }
        if (sTmpValue.type == TypedValue.TYPE_ATTRIBUTE) {
            return getAttrColor(theme, sTmpValue.data);
        }
        return sTmpValue.data;
    }

    @Nullable
    public static ColorStateList getAttrColorStateList(Context context, int attrRes) {
        return getAttrColorStateList(context, context.getTheme(), attrRes);
    }

    @Nullable
    public static ColorStateList getAttrColorStateList(Context context, Resources.Theme theme, int attr) {
        if (attr == 0) {
            return null;
        }
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return null;
        }
        if (sTmpValue.type >= TypedValue.TYPE_FIRST_COLOR_INT
                && sTmpValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return ColorStateList.valueOf(sTmpValue.data);
        }
        if (sTmpValue.type == TypedValue.TYPE_ATTRIBUTE) {
            return getAttrColorStateList(context, theme, sTmpValue.data);
        }
        if (sTmpValue.resourceId == 0) {
            return null;
        }
        return ContextCompat.getColorStateList(context, sTmpValue.resourceId);
    }

    @Nullable
    public static Drawable getAttrDrawable(Context context, int attr) {
        return getAttrDrawable(context, context.getTheme(), attr);
    }

    @Nullable
    public static Drawable getAttrDrawable(Context context, Resources.Theme theme, int attr) {
        if (attr == 0) {
            return null;
        }
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return null;
        }
        if (sTmpValue.type >= TypedValue.TYPE_FIRST_COLOR_INT
                && sTmpValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return new ColorDrawable(sTmpValue.data);
        }
        if (sTmpValue.type == TypedValue.TYPE_ATTRIBUTE) {
            return getAttrDrawable(context, theme, sTmpValue.data);
        }

        if (sTmpValue.resourceId != 0) {
            return Utils.getVectorDrawable(context, sTmpValue.resourceId);
        }
        return null;
    }

    @Nullable
    public static Drawable getAttrDrawable(Context context, TypedArray typedArray, int index) {
        TypedValue value = typedArray.peekValue(index);
        if (value != null) {
            if (value.type != TypedValue.TYPE_ATTRIBUTE && value.resourceId != 0) {
                return Utils.getVectorDrawable(context, value.resourceId);
            }
        }
        return null;
    }

    public static int getAttrDimen(Context context, int attrRes) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!context.getTheme().resolveAttribute(attrRes, sTmpValue, true)) {
            return 0;
        }
        return TypedValue.complexToDimensionPixelSize(sTmpValue.data, Utils.getDisplayMetrics(context));
    }

    @Nullable
    public static String getAttrString(Context context, int attrRes) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!context.getTheme().resolveAttribute(attrRes, sTmpValue, true)) {
            return null;
        }
        CharSequence str = sTmpValue.string;
        return str == null ? null : str.toString();
    }

    public static int getAttrInt(Context context, int attrRes) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        context.getTheme().resolveAttribute(attrRes, sTmpValue, true);
        return sTmpValue.data;
    }


    public static int[] assignDialogActivityAnimWithAttr(Context context, int animStyle) {
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.XPopupDialogActivityAnimation, 0, animStyle);
        int count = a.getIndexCount();
        int activityOpenEnterAnimation = 0, activityOpenExitAnimation = 0, activityCloseEnterAnimation = 0, activityCloseExitAnimation = 0;
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.XPopupDialogActivityAnimation_android_activityOpenEnterAnimation) {
                activityOpenEnterAnimation = a.getResourceId(attr, 0);
                Log.e("HHHTEST", String.valueOf(activityOpenEnterAnimation));

            } else if (attr == R.styleable.XPopupDialogActivityAnimation_android_activityOpenExitAnimation) {
                activityOpenExitAnimation = a.getResourceId(attr, 0);
                Log.e("HHHTEST", String.valueOf(activityOpenExitAnimation));

            } else if (attr == R.styleable.XPopupDialogActivityAnimation_android_activityCloseEnterAnimation) {
                activityCloseEnterAnimation = a.getResourceId(attr, 0);
                Log.e("HHHTEST", String.valueOf(activityCloseEnterAnimation));

            } else if (attr == R.styleable.XPopupDialogActivityAnimation_android_activityCloseExitAnimation) {
                activityCloseExitAnimation = a.getResourceId(attr, 0);
                Log.e("HHHTEST", String.valueOf(activityCloseExitAnimation));

            }
        }
        a.recycle();
        int[] ids = {activityOpenEnterAnimation, activityOpenExitAnimation, activityCloseEnterAnimation, activityCloseExitAnimation};
        return ids;
    }
}