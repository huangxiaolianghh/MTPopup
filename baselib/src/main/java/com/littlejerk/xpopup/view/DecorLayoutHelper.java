package com.littlejerk.xpopup.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.littlejerk.xpopup.R;

import java.lang.ref.WeakReference;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

/**
 * @author HHotHeart
 * @date 2022/4/3 16:58
 * @desc XPopup根布局装饰帮助类，摘自QMUI：https://github.com/Tencent/QMUI_Android
 */
public final class DecorLayoutHelper implements IDecorLayout {

    public static final int RADIUS_OF_HALF_VIEW_HEIGHT = -1;
    public static final int RADIUS_OF_HALF_VIEW_WIDTH = -2;
    private Context mContext;
    // size
    private int mWidthLimit = 0;
    private int mHeightLimit = 0;
    private int mWidthMini = 0;
    private int mHeightMini = 0;


    // divider
    private int mTopDividerHeight = 0;
    private int mTopDividerInsetLeft = 0;
    private int mTopDividerInsetRight = 0;
    private int mTopDividerColor;
    private int mTopDividerAlpha = 255;

    private int mBottomDividerHeight = 0;
    private int mBottomDividerInsetLeft = 0;
    private int mBottomDividerInsetRight = 0;
    private int mBottomDividerColor;
    private int mBottomDividerAlpha = 255;

    private int mLeftDividerWidth = 0;
    private int mLeftDividerInsetTop = 0;
    private int mLeftDividerInsetBottom = 0;
    private int mLeftDividerColor;
    private int mLeftDividerAlpha = 255;

    private int mRightDividerWidth = 0;
    private int mRightDividerInsetTop = 0;
    private int mRightDividerInsetBottom = 0;
    private int mRightDividerColor;
    private int mRightDividerAlpha = 255;
    private Paint mDividerPaint;

    // round
    private Paint mClipPaint;
    private PorterDuffXfermode mMode;
    private int mRadius;
    private @HideRadiusSide
    int mHideRadiusSide = HIDE_RADIUS_SIDE_NONE;
    private float[] mRadiusArray;
    private boolean mShouldUseRadiusArray;
    private RectF mBorderRect;
    private int mBorderColor = 0;
    private int mBorderWidth = 1;
    private int mOuterNormalColor = 0;
    private WeakReference<View> mOwner;
    private boolean mIsOutlineExcludePadding = false;
    private Path mPath = new Path();

    // shadow
    private boolean mIsShowBorderOnlyBeforeL = true;
    private int mShadowElevation = 0;
    private float mShadowAlpha;
    private int mShadowColor = Color.BLACK;

    // outline inset
    private int mOutlineInsetLeft = 0;
    private int mOutlineInsetRight = 0;
    private int mOutlineInsetTop = 0;
    private int mOutlineInsetBottom = 0;

    public DecorLayoutHelper(Context context, AttributeSet attrs, int defAttr, View owner) {
        this(context, attrs, defAttr, 0, owner);
    }

    public DecorLayoutHelper(Context context, AttributeSet attrs, int defAttr, int defStyleRes, View owner) {
        mContext = context;
        mOwner = new WeakReference<>(owner);
        mBottomDividerColor = mTopDividerColor =
                ContextCompat.getColor(context, R.color.xpopup_color_gray);
        mMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mClipPaint = new Paint();
        mClipPaint.setAntiAlias(true);
        mBorderRect = new RectF();

        int radius = 0, shadow = 0;
        setRadiusAndShadow(radius, mHideRadiusSide, shadow, mShadowAlpha);
    }

    @Override
    public void setOutlineExcludePadding(boolean outlineExcludePadding) {
        if (useFeature()) {
            View owner = mOwner.get();
            if (owner == null) {
                return;
            }
            mIsOutlineExcludePadding = outlineExcludePadding;
            owner.invalidateOutline();
        }

    }

    @Override
    public boolean setWidthLimit(int widthLimit) {
        if (mWidthLimit != widthLimit) {
            mWidthLimit = widthLimit;
            return true;
        }
        return false;
    }

    @Override
    public boolean setHeightLimit(int heightLimit) {
        if (mHeightLimit != heightLimit) {
            mHeightLimit = heightLimit;
            return true;
        }
        return false;
    }

    @Override
    public void updateLeftSeparatorColor(int color) {
        if (mLeftDividerColor != color) {
            mLeftDividerColor = color;
            invalidate();
        }
    }

    @Override
    public void updateBottomSeparatorColor(int color) {
        if (mBottomDividerColor != color) {
            mBottomDividerColor = color;
            invalidate();
        }
    }

    @Override
    public void updateTopSeparatorColor(int color) {
        if (mTopDividerColor != color) {
            mTopDividerColor = color;
            invalidate();
        }
    }

    @Override
    public void updateRightSeparatorColor(int color) {
        if (mRightDividerColor != color) {
            mRightDividerColor = color;
            invalidate();
        }
    }

    @Override
    public int getShadowElevation() {
        return mShadowElevation;
    }

    @Override
    public float getShadowAlpha() {
        return mShadowAlpha;
    }

    @Override
    public int getShadowColor() {
        return mShadowColor;
    }

    @Override
    public void setOutlineInset(int left, int top, int right, int bottom) {
        if (useFeature()) {
            View owner = mOwner.get();
            if (owner == null) {
                return;
            }
            mOutlineInsetLeft = left;
            mOutlineInsetRight = right;
            mOutlineInsetTop = top;
            mOutlineInsetBottom = bottom;
            owner.invalidateOutline();
        }
    }


    @Override
    public void setShowBorderOnlyBeforeL(boolean showBorderOnlyBeforeL) {
        mIsShowBorderOnlyBeforeL = showBorderOnlyBeforeL;
        invalidate();
    }

    @Override
    public void setShadowElevation(int elevation) {
        if (mShadowElevation == elevation) {
            return;
        }
        mShadowElevation = elevation;
        invalidateOutline();
    }

    @Override
    public void setShadowAlpha(float shadowAlpha) {
        if (mShadowAlpha == shadowAlpha) {
            return;
        }
        mShadowAlpha = shadowAlpha;
        invalidateOutline();
    }

    @Override
    public void setShadowColor(int shadowColor) {
        if (mShadowColor == shadowColor) {
            return;
        }
        mShadowColor = shadowColor;
        setShadowColorInner(mShadowColor);
    }

    private void setShadowColorInner(int shadowColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            View owner = mOwner.get();
            if (owner == null) {
                return;
            }
            owner.setOutlineAmbientShadowColor(shadowColor);
            owner.setOutlineSpotShadowColor(shadowColor);
        }
    }

    private void invalidateOutline() {
        if (useFeature()) {
            View owner = mOwner.get();
            if (owner == null) {
                return;
            }
            if (mShadowElevation == 0) {
                owner.setElevation(0);
            } else {
                owner.setElevation(mShadowElevation);
            }
            owner.invalidateOutline();
        }
    }

    private void invalidate() {
        View owner = mOwner.get();
        if (owner == null) {
            return;
        }
        owner.invalidate();
    }

    @Override
    public void setHideRadiusSide(@HideRadiusSide int hideRadiusSide) {
        if (mHideRadiusSide == hideRadiusSide) {
            return;
        }
        setRadiusAndShadow(mRadius, hideRadiusSide, mShadowElevation, mShadowAlpha);
    }

    @Override
    public int getHideRadiusSide() {
        return mHideRadiusSide;
    }

    @Override
    public void setRadius(int radius) {
        if (mRadius != radius) {
            setRadiusAndShadow(radius, mShadowElevation, mShadowAlpha);
        }
    }

    @Override
    public void setRadius(int radius, @HideRadiusSide int hideRadiusSide) {
        if (mRadius == radius && hideRadiusSide == mHideRadiusSide) {
            return;
        }
        setRadiusAndShadow(radius, hideRadiusSide, mShadowElevation, mShadowAlpha);
    }

    @Override
    public int getRadius() {
        return mRadius;
    }

    @Override
    public void setRadiusAndShadow(int radius, int shadowElevation, float shadowAlpha) {
        setRadiusAndShadow(radius, mHideRadiusSide, shadowElevation, shadowAlpha);
    }

    @Override
    public void setRadiusAndShadow(int radius, @HideRadiusSide int hideRadiusSide, int shadowElevation, float shadowAlpha) {
        setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, mShadowColor, shadowAlpha);
    }

    @Override
    public void setRadiusAndShadow(int radius, int hideRadiusSide, int shadowElevation, int shadowColor, float shadowAlpha) {
        final View owner = mOwner.get();
        if (owner == null) {
            return;
        }

        mRadius = radius;
        mHideRadiusSide = hideRadiusSide;

        mShouldUseRadiusArray = isRadiusWithSideHidden();
        mShadowElevation = shadowElevation;
        mShadowAlpha = shadowAlpha;
        mShadowColor = shadowColor;
        if (useFeature()) {
            if (mShadowElevation == 0 || mShouldUseRadiusArray) {
                owner.setElevation(0);
            } else {
                owner.setElevation(mShadowElevation);
            }

            setShadowColorInner(mShadowColor);

            owner.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    int w = view.getWidth(), h = view.getHeight();
                    if (w == 0 || h == 0) {
                        return;
                    }
                    float radius = getRealRadius();
                    int min = Math.min(w, h);
                    if (radius * 2 > min) {
                        // 解决 OnePlus 3T 8.0 上显示变形
                        radius = min / 2F;
                    }
                    if (mShouldUseRadiusArray) {
                        int left = 0, top = 0, right = w, bottom = h;
                        if (mHideRadiusSide == HIDE_RADIUS_SIDE_LEFT) {
                            left -= radius;
                        } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_TOP) {
                            top -= radius;
                        } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_RIGHT) {
                            right += radius;
                        } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_BOTTOM) {
                            bottom += radius;
                        }
                        outline.setRoundRect(left, top,
                                right, bottom, radius);
                        return;
                    }

                    int top = mOutlineInsetTop, bottom = Math.max(top + 1, h - mOutlineInsetBottom),
                            left = mOutlineInsetLeft, right = w - mOutlineInsetRight;
                    if (mIsOutlineExcludePadding) {
                        left += view.getPaddingLeft();
                        top += view.getPaddingTop();
                        right = Math.max(left + 1, right - view.getPaddingRight());
                        bottom = Math.max(top + 1, bottom - view.getPaddingBottom());
                    }

                    float shadowAlpha = mShadowAlpha;
                    if (mShadowElevation == 0) {
                        // outline.setAlpha will work even if shadowElevation == 0
                        shadowAlpha = 1f;
                    }

                    outline.setAlpha(shadowAlpha);

                    if (radius <= 0) {
                        outline.setRect(left, top,
                                right, bottom);
                    } else {
                        outline.setRoundRect(left, top,
                                right, bottom, radius);
                    }
                }
            });
            owner.setClipToOutline(mRadius == RADIUS_OF_HALF_VIEW_WIDTH || mRadius == RADIUS_OF_HALF_VIEW_HEIGHT || mRadius > 0);

        }
        owner.invalidate();
    }

    /**
     * 有radius, 但是有一边不显示radius。
     *
     * @return
     */
    public boolean isRadiusWithSideHidden() {
        return (mRadius == RADIUS_OF_HALF_VIEW_HEIGHT ||
                mRadius == RADIUS_OF_HALF_VIEW_WIDTH ||
                mRadius > 0) && mHideRadiusSide != HIDE_RADIUS_SIDE_NONE;
    }

    @Override
    public void updateTopDivider(int topInsetLeft, int topInsetRight, int topDividerHeight, int topDividerColor) {
        mTopDividerInsetLeft = topInsetLeft;
        mTopDividerInsetRight = topInsetRight;
        mTopDividerHeight = topDividerHeight;
        mTopDividerColor = topDividerColor;
    }

    @Override
    public void updateBottomDivider(int bottomInsetLeft, int bottomInsetRight, int bottomDividerHeight, int bottomDividerColor) {
        mBottomDividerInsetLeft = bottomInsetLeft;
        mBottomDividerInsetRight = bottomInsetRight;
        mBottomDividerColor = bottomDividerColor;
        mBottomDividerHeight = bottomDividerHeight;
    }

    @Override
    public void updateLeftDivider(int leftInsetTop, int leftInsetBottom, int leftDividerWidth, int leftDividerColor) {
        mLeftDividerInsetTop = leftInsetTop;
        mLeftDividerInsetBottom = leftInsetBottom;
        mLeftDividerWidth = leftDividerWidth;
        mLeftDividerColor = leftDividerColor;
    }

    @Override
    public void updateRightDivider(int rightInsetTop, int rightInsetBottom, int rightDividerWidth, int rightDividerColor) {
        mRightDividerInsetTop = rightInsetTop;
        mRightDividerInsetBottom = rightInsetBottom;
        mRightDividerWidth = rightDividerWidth;
        mRightDividerColor = rightDividerColor;
    }

    @Override
    public void onlyShowTopDivider(int topInsetLeft, int topInsetRight,
                                   int topDividerHeight, int topDividerColor) {
        updateTopDivider(topInsetLeft, topInsetRight, topDividerHeight, topDividerColor);
        mLeftDividerWidth = 0;
        mRightDividerWidth = 0;
        mBottomDividerHeight = 0;
    }

    @Override
    public void onlyShowBottomDivider(int bottomInsetLeft, int bottomInsetRight,
                                      int bottomDividerHeight, int bottomDividerColor) {
        updateBottomDivider(bottomInsetLeft, bottomInsetRight, bottomDividerHeight, bottomDividerColor);
        mLeftDividerWidth = 0;
        mRightDividerWidth = 0;
        mTopDividerHeight = 0;
    }

    @Override
    public void onlyShowLeftDivider(int leftInsetTop, int leftInsetBottom, int leftDividerWidth, int leftDividerColor) {
        updateLeftDivider(leftInsetTop, leftInsetBottom, leftDividerWidth, leftDividerColor);
        mRightDividerWidth = 0;
        mTopDividerHeight = 0;
        mBottomDividerHeight = 0;
    }

    @Override
    public void onlyShowRightDivider(int rightInsetTop, int rightInsetBottom, int rightDividerWidth, int rightDividerColor) {
        updateRightDivider(rightInsetTop, rightInsetBottom, rightDividerWidth, rightDividerColor);
        mLeftDividerWidth = 0;
        mTopDividerHeight = 0;
        mBottomDividerHeight = 0;
    }

    @Override
    public void setTopDividerAlpha(int dividerAlpha) {
        mTopDividerAlpha = dividerAlpha;
    }

    @Override
    public void setBottomDividerAlpha(int dividerAlpha) {
        mBottomDividerAlpha = dividerAlpha;
    }

    @Override
    public void setLeftDividerAlpha(int dividerAlpha) {
        mLeftDividerAlpha = dividerAlpha;
    }

    @Override
    public void setRightDividerAlpha(int dividerAlpha) {
        mRightDividerAlpha = dividerAlpha;
    }


    public int handleMiniWidth(int widthMeasureSpec, int measuredWidth) {
        if (View.MeasureSpec.getMode(widthMeasureSpec) != View.MeasureSpec.EXACTLY
                && measuredWidth < mWidthMini) {
            return View.MeasureSpec.makeMeasureSpec(mWidthMini, View.MeasureSpec.EXACTLY);
        }
        return widthMeasureSpec;
    }

    public int handleMiniHeight(int heightMeasureSpec, int measuredHeight) {
        if (View.MeasureSpec.getMode(heightMeasureSpec) != View.MeasureSpec.EXACTLY
                && measuredHeight < mHeightMini) {
            return View.MeasureSpec.makeMeasureSpec(mHeightMini, View.MeasureSpec.EXACTLY);
        }
        return heightMeasureSpec;
    }

    public int getMeasuredWidthSpec(int widthMeasureSpec) {
        if (mWidthLimit > 0) {
            int size = View.MeasureSpec.getSize(widthMeasureSpec);
            if (size > mWidthLimit) {
                int mode = View.MeasureSpec.getMode(widthMeasureSpec);
                if (mode == View.MeasureSpec.AT_MOST) {
                    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(mWidthLimit, View.MeasureSpec.AT_MOST);
                } else {
                    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(mWidthLimit, View.MeasureSpec.EXACTLY);
                }

            }
        }
        return widthMeasureSpec;
    }

    public int getMeasuredHeightSpec(int heightMeasureSpec) {
        if (mHeightLimit > 0) {
            int size = View.MeasureSpec.getSize(heightMeasureSpec);
            if (size > mHeightLimit) {
                int mode = View.MeasureSpec.getMode(heightMeasureSpec);
                if (mode == View.MeasureSpec.AT_MOST) {
                    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(mHeightLimit, View.MeasureSpec.AT_MOST);
                } else {
                    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(mHeightLimit, View.MeasureSpec.EXACTLY);
                }
            }
        }
        return heightMeasureSpec;
    }

    @Override
    public void setBorderColor(@ColorInt int borderColor) {
        mBorderColor = borderColor;
    }

    @Override
    public void setBorderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
    }

    @Override
    public void setOuterNormalColor(int color) {
        mOuterNormalColor = color;
        View owner = mOwner.get();
        if (owner != null) {
            owner.invalidate();
        }
    }

    @Override
    public boolean hasTopSeparator() {
        return mTopDividerHeight > 0;
    }

    @Override
    public boolean hasRightSeparator() {
        return mRightDividerWidth > 0;
    }

    @Override
    public boolean hasBottomSeparator() {
        return mBottomDividerHeight > 0;
    }

    @Override
    public boolean hasLeftSeparator() {
        return mLeftDividerWidth > 0;
    }

    @Override
    public boolean hasBorder() {
        return mBorderWidth > 0;
    }

    public void drawDividers(Canvas canvas, int w, int h) {
        View owner = mOwner.get();
        if (owner == null) {
            return;
        }
        if (mDividerPaint == null &&
                (mTopDividerHeight > 0 || mBottomDividerHeight > 0 || mLeftDividerWidth > 0 || mRightDividerWidth > 0)) {
            mDividerPaint = new Paint();
        }
        canvas.save();
        canvas.translate(owner.getScrollX(), owner.getScrollY());
        if (mTopDividerHeight > 0) {
            mDividerPaint.setStrokeWidth(mTopDividerHeight);
            mDividerPaint.setColor(mTopDividerColor);
            if (mTopDividerAlpha < 255) {
                mDividerPaint.setAlpha(mTopDividerAlpha);
            }
            float y = mTopDividerHeight / 2f;
            canvas.drawLine(mTopDividerInsetLeft, y, w - mTopDividerInsetRight, y, mDividerPaint);
        }

        if (mBottomDividerHeight > 0) {
            mDividerPaint.setStrokeWidth(mBottomDividerHeight);
            mDividerPaint.setColor(mBottomDividerColor);
            if (mBottomDividerAlpha < 255) {
                mDividerPaint.setAlpha(mBottomDividerAlpha);
            }
            float y = (float) Math.floor(h - mBottomDividerHeight / 2f);
            canvas.drawLine(mBottomDividerInsetLeft, y, w - mBottomDividerInsetRight, y, mDividerPaint);
        }

        if (mLeftDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mLeftDividerWidth);
            mDividerPaint.setColor(mLeftDividerColor);
            if (mLeftDividerAlpha < 255) {
                mDividerPaint.setAlpha(mLeftDividerAlpha);
            }
            float x = mLeftDividerWidth / 2f;
            canvas.drawLine(x, mLeftDividerInsetTop, x, h - mLeftDividerInsetBottom, mDividerPaint);
        }

        if (mRightDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mRightDividerWidth);
            mDividerPaint.setColor(mRightDividerColor);
            if (mRightDividerAlpha < 255) {
                mDividerPaint.setAlpha(mRightDividerAlpha);
            }
            float x = (float) Math.floor(w - mRightDividerWidth / 2f);
            canvas.drawLine(x, mRightDividerInsetTop, x, h - mRightDividerInsetBottom, mDividerPaint);
        }
        canvas.restore();
    }


    private int getRealRadius() {
        View owner = mOwner.get();
        if (owner == null) {
            return mRadius;
        }
        int radius;
        if (mRadius == RADIUS_OF_HALF_VIEW_HEIGHT) {
            radius = owner.getHeight() / 2;
        } else if (mRadius == RADIUS_OF_HALF_VIEW_WIDTH) {
            radius = owner.getWidth() / 2;
        } else {
            radius = mRadius;
        }
        return radius;
    }

    public void dispatchRoundBorderDraw(Canvas canvas) {
        View owner = mOwner.get();
        if (owner == null) {
            return;
        }

        int radius = getRealRadius();
        boolean needCheckFakeOuterNormalDraw = radius > 0 && !useFeature() && mOuterNormalColor != 0;
        boolean needDrawBorder = mBorderWidth > 0 && mBorderColor != 0;
        if (!needCheckFakeOuterNormalDraw && !needDrawBorder) {
            return;
        }

        if (mIsShowBorderOnlyBeforeL && useFeature() && mShadowElevation != 0) {
            return;
        }

        int width = canvas.getWidth(), height = canvas.getHeight();
        canvas.save();
        canvas.translate(owner.getScrollX(), owner.getScrollY());

        // react
        float halfBorderWith = mBorderWidth / 2f;
        if (mIsOutlineExcludePadding) {
            mBorderRect.set(
                    owner.getPaddingLeft() + halfBorderWith,
                    owner.getPaddingTop() + halfBorderWith,
                    width - owner.getPaddingRight() - halfBorderWith,
                    height - owner.getPaddingBottom() - halfBorderWith);
        } else {
            mBorderRect.set(halfBorderWith, halfBorderWith,
                    width - halfBorderWith, height - halfBorderWith);
        }

        if (mShouldUseRadiusArray) {
            if (mRadiusArray == null) {
                mRadiusArray = new float[8];
            }
            if (mHideRadiusSide == HIDE_RADIUS_SIDE_TOP) {
                mRadiusArray[4] = radius;
                mRadiusArray[5] = radius;
                mRadiusArray[6] = radius;
                mRadiusArray[7] = radius;
            } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_RIGHT) {
                mRadiusArray[0] = radius;
                mRadiusArray[1] = radius;
                mRadiusArray[6] = radius;
                mRadiusArray[7] = radius;
            } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_BOTTOM) {
                mRadiusArray[0] = radius;
                mRadiusArray[1] = radius;
                mRadiusArray[2] = radius;
                mRadiusArray[3] = radius;
            } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_LEFT) {
                mRadiusArray[2] = radius;
                mRadiusArray[3] = radius;
                mRadiusArray[4] = radius;
                mRadiusArray[5] = radius;
            }
        }

        if (needCheckFakeOuterNormalDraw) {
            int layerId = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
            canvas.drawColor(mOuterNormalColor);
            mClipPaint.setColor(mOuterNormalColor);
            mClipPaint.setStyle(Paint.Style.FILL);
            mClipPaint.setXfermode(mMode);
            if (!mShouldUseRadiusArray) {
                canvas.drawRoundRect(mBorderRect, radius, radius, mClipPaint);
            } else {
                drawRoundRect(canvas, mBorderRect, mRadiusArray, mClipPaint);
            }
            mClipPaint.setXfermode(null);
            canvas.restoreToCount(layerId);
        }

        if (needDrawBorder) {
            mClipPaint.setColor(mBorderColor);
            mClipPaint.setStrokeWidth(mBorderWidth);
            mClipPaint.setStyle(Paint.Style.STROKE);
            if (mShouldUseRadiusArray) {
                drawRoundRect(canvas, mBorderRect, mRadiusArray, mClipPaint);
            } else if (radius <= 0) {
                canvas.drawRect(mBorderRect, mClipPaint);
            } else {
                canvas.drawRoundRect(mBorderRect, radius, radius, mClipPaint);
            }
        }
        canvas.restore();
    }

    private void drawRoundRect(Canvas canvas, RectF rect, float[] radiusArray, Paint paint) {
        mPath.reset();
        mPath.addRoundRect(rect, radiusArray, Path.Direction.CW);
        canvas.drawPath(mPath, paint);

    }

    public static boolean useFeature() {
        return Build.VERSION.SDK_INT >= 21;
    }
}