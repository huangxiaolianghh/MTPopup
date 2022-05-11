package com.littlejerk.xpopup;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.littlejerk.xpopup.manager.DelegateManager;
import com.littlejerk.xpopup.util.Preconditions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import static com.littlejerk.xpopup.util.Utils.NO_RES_ID;

/**
 * @author HHotHeart
 * @date 2022/4/11 16:30
 * @desc BottomSheetDialogFragment代理类
 */
@SuppressWarnings("rawtypes")
public final class BottomSheetDialogFragmentDelegate extends BaseDelegate<BottomSheetDialogFragmentConfig, BottomSheetDialogFragmentDelegate.InnerBottomSheetDialogFragment> {

    /**
     * BottomSheetDialogFragment实例
     */
    private InnerBottomSheetDialogFragment mBottomSheetDialogFragment;

    /**
     * Dialog的Behavior对象
     */
    private BottomSheetBehavior mBottomSheetBehavior;

    /**
     * 构造方法
     *
     * @param config DialogFragmentConfig
     */
    public BottomSheetDialogFragmentDelegate(@NonNull BottomSheetDialogFragmentConfig config) {
        super(config);
    }

    @Override
    protected void decorateView() {
        ApplyParamsManager.decorateView(config());
    }

    @Override
    protected void bindListener() {
        Preconditions.checkNotNull(config().getXPopupRootView(), "please init DialogFragment root view");
        ApplyParamsManager.bindListener(this, config());
    }

    @Override
    protected void buildPopupAfterDecorateView() {
        mBottomSheetDialogFragment = InnerBottomSheetDialogFragment.newInstance(this);
        int themeStyle = config().getThemeStyle() == NO_RES_ID ? R.style.XPopup_BottomSheetDialog : config().getThemeStyle();
        mBottomSheetDialogFragment.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, themeStyle);
    }

    @Override
    public InnerBottomSheetDialogFragment getPopup() {
        return mBottomSheetDialogFragment;
    }

    @Override
    protected void applyParamsToPopup() {
        //BottomSheetDialogFragment对于宽高的设置暂时不生效，内容布局多高就多高
    }

    @Override
    void showPopup() {
        Preconditions.checkNotNull(config().getManager(), "please call config managerTag(manager, tag)");
        mBottomSheetDialogFragment.showNow(config().getManager(), config().getTag());
    }

    @Override
    void dismissPopup() {
        mBottomSheetDialogFragment.dismiss();
    }

    @Override
    public View getDecorView() {
        if (getPopup() == null || getPopup().getDialog() == null
                || getPopup().getDialog().getWindow() == null) {
            return null;
        }
        return getPopup().getDialog().getWindow().getDecorView();
    }

    /**
     * 获取BottomSheetDialog的Behavior对象（Dialog展示后调用）
     *
     * @return Behavior对象
     */
    public BottomSheetBehavior getBottomSheetBehavior() {
        return mBottomSheetBehavior;
    }

    /**
     * 初始化BottomSheetDialog的Behavior对象
     *
     * @param bottomSheetBehavior Behavior对象
     */
    public void setBottomSheetBehavior(BottomSheetBehavior bottomSheetBehavior) {
        mBottomSheetBehavior = bottomSheetBehavior;
    }

    /**
     * BottomSheetDialogFragment
     */
    public static class InnerBottomSheetDialogFragment extends BottomSheetDialogFragment {

        private static final String TAG = "BottomSheetDialogFragment";

        private static final String KEY_DIALOG_FRAGMENT_CONFIG = "BottomSheetDialogFragment_Config";
        private BottomSheetDialogFragmentDelegate mDelegate;
        private BottomSheetDialogFragmentConfig mConfig;
        private View mContentView;

        /**
         * 静态方法构造DialogFragment
         *
         * @param delegate 代理对象
         * @return DialogFragment
         */
        public static InnerBottomSheetDialogFragment newInstance(@NonNull BottomSheetDialogFragmentDelegate delegate) {
            InnerBottomSheetDialogFragment fragmentDialog = new InnerBottomSheetDialogFragment();
            DelegateManager.getInstance().put(fragmentDialog.hashCode(), delegate);
            return fragmentDialog;
        }

        /**
         * 无参构造方法
         */
        public InnerBottomSheetDialogFragment() {
        }

        private BottomSheetDialogFragmentConfig config() {
            return mConfig;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //屏幕翻转保存的数据
            if (savedInstanceState != null) {
                mConfig = savedInstanceState.getParcelable(KEY_DIALOG_FRAGMENT_CONFIG);
                return;
            }
            mDelegate = DelegateManager.getInstance().get(this.hashCode());
            mConfig = mDelegate.config();
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            //屏幕翻转保存数据
            outState.putParcelable(KEY_DIALOG_FRAGMENT_CONFIG, mConfig);
            super.onSaveInstanceState(outState);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return super.onCreateDialog(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if (mContentView == null) {
                mContentView = config().getXPopupRootView();
            }
            //横竖屏切换会报错：java.lang.IllegalStateException: DialogFragment can not be attached to a container view
            if (mContentView.getParent() != null) {
                (((ViewGroup) mContentView.getParent())).removeView(mContentView);
            }
            return mContentView;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onStart() {
            super.onStart();
            Preconditions.checkNotNull(getDialog(), "please check DialogFragment:getDialog()==null");
            ApplyParamsManager.applyParamsToPopup(mDelegate, getDialog(), getDecorView(), config());
            FrameLayout bottomSheetView = getDialog().findViewById(R.id.design_bottom_sheet);
            if (bottomSheetView == null) {
                return;
            }
            ApplyParamsManager.updateBottomSheetDialogLayout(bottomSheetView, config());
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheetView);
            mDelegate.setBottomSheetBehavior(behavior);
        }

        @Override
        public void dismiss() {
            if (config().getOnDismissListener() != null) {
                config().getOnDismissListener().onDismiss(mDelegate);
            }
            super.dismiss();
        }

        @Override
        public void showNow(@NonNull FragmentManager manager, @Nullable String tag) {
            super.showNow(manager, tag);
            if (config().getOnShowListener() != null) {
                config().getOnShowListener().onShow(mDelegate);
            }
        }

        @Nullable
        @Override
        public BottomSheetDialog getDialog() {
            return (BottomSheetDialog) super.getDialog();
        }

        /**
         * 获取DecorView，onStart()之后才会有值
         *
         * @return DecorView
         */
        public View getDecorView() {
            if (getDialog() == null || getDialog().getWindow() == null) {
                return null;
            }
            return getDialog().getWindow().getDecorView();
        }

    }
}