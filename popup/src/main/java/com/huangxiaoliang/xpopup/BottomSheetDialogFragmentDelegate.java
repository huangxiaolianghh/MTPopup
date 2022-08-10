package com.huangxiaoliang.xpopup;

import static com.huangxiaoliang.xpopup.util.Utils.NO_RES_ID;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.huangxiaoliang.xpopup.manager.DelegateManager;
import com.huangxiaoliang.xpopup.util.Preconditions;

/**
 * @author HHotHeart
 * @date 2022/4/11 16:30
 * @desc BottomSheetDialogFragment代理类
 */
public final class BottomSheetDialogFragmentDelegate extends BaseDelegate<BottomSheetDialogFragmentConfig, BottomSheetDialogFragmentDelegate.InnerBottomSheetDialogFragment> {

    /**
     * BottomSheetDialogFragment实例
     */
    private InnerBottomSheetDialogFragment mBottomSheetDialogFragment;

    /**
     * Dialog的Behavior对象
     */
    private BottomSheetBehavior<FrameLayout> mBottomSheetBehavior;

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
    public boolean isShowing() {
        return getPopup() != null && getPopup().isShowing();
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
        if (getPopup() == null ||
                getPopup().getDialog() == null ||
                getPopup().getDialog().getWindow() == null) {
            return null;
        }
        return getPopup().getDialog().getWindow().getDecorView();
    }

    /**
     * 获取BottomSheetDialog的Behavior对象（Dialog展示后调用）
     *
     * @return Behavior对象
     */
    public BottomSheetBehavior<FrameLayout> getBottomSheetBehavior() {
        return mBottomSheetBehavior;
    }

    /**
     * 初始化BottomSheetDialog的Behavior对象
     *
     * @param bottomSheetBehavior Behavior对象
     */
    public void setBottomSheetBehavior(BottomSheetBehavior<FrameLayout> bottomSheetBehavior) {
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
            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheetView);
            //避免点击外部空白区域崩溃问题：java.lang.IllegalArgumentException: Illegal state argument: 5
            behavior.setHideable(true);
            mDelegate.setBottomSheetBehavior(behavior);
        }

        @Override
        public void onDismiss(@NonNull DialogInterface dialog) {
            super.onDismiss(dialog);
            if (config().getOnDismissListener() != null) {
                config().getOnDismissListener().onDismiss(mDelegate);
            }
            //关闭弹窗同时释放相关资源
            if (mDelegate != null) {
                mDelegate.releasePopup();
            }
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
         * 当前Popup是否在显示
         *
         * @return Popup是否在显示
         */
        public boolean isShowing() {
            if (getDialog() == null) {
                return false;
            }
            return getDialog().isShowing();
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

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            DelegateManager.getInstance().remove(this.hashCode());
        }
    }

    @Override
    protected void releaseDelegate() {
        mBottomSheetDialogFragment = null;
        mBottomSheetBehavior = null;
    }
}