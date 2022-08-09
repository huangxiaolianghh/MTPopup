package com.huangxiaoliang.xpopup;

import static com.huangxiaoliang.xpopup.util.Utils.NO_RES_ID;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.huangxiaoliang.xpopup.manager.DelegateManager;
import com.huangxiaoliang.xpopup.util.Preconditions;

/**
 * @author HHotHeart
 * @date 2022/4/6 20:09
 * @desc DialogFragment代理类
 */
public final class DialogFragmentDelegate extends BaseDelegate<DialogFragmentConfig, DialogFragment> {

    private InnerDialogFragment mDialogFragment;

    /**
     * 构造方法
     *
     * @param config DialogFragmentConfig
     */
    public DialogFragmentDelegate(@NonNull DialogFragmentConfig config) {
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
        mDialogFragment = InnerDialogFragment.newInstance(this);
        int themeStyle = config().getThemeStyle() == NO_RES_ID ? R.style.XPopup_Dialog : config().getThemeStyle();
        mDialogFragment.setStyle(config().getDialogFragmentStyle(), themeStyle);
    }

    @Override
    public InnerDialogFragment getPopup() {
        return mDialogFragment;
    }

    @Override
    protected void applyParamsToPopup() {
    }

    @Override
    public boolean isShowing() {
        return getPopup() != null && getPopup().isShowing();
    }

    @Override
    void showPopup() {
        Preconditions.checkNotNull(config().getManager(), "please call config managerTag(manager, tag)");
        mDialogFragment.showNow(config().getManager(), config().getTag());
    }

    @Override
    void dismissPopup() {
        mDialogFragment.dismiss();
    }

    /**
     * 相关注意项，见{@link InnerDialogFragment#getDecorView()}
     *
     * @return DecorView
     */
    @Override
    public View getDecorView() {
        if (getPopup() == null || getPopup().getDialog() == null
                || getPopup().getDialog().getWindow() == null) {
            return null;
        }
        return getPopup().getDialog().getWindow().getDecorView();
    }

    /**
     * DialogFragment
     */
    public static class InnerDialogFragment extends DialogFragment {

        private static final String TAG = "CusFragmentDialog";

        private static final String KEY_DIALOG_FRAGMENT_CONFIG = "DialogFragment_Config";
        private DialogFragmentDelegate mDelegate;
        private DialogFragmentConfig mConfig;
        private View mContentView;

        /**
         * 静态方法构造DialogFragment
         *
         * @param delegate 代理对象
         * @return DialogFragment
         */
        public static InnerDialogFragment newInstance(@NonNull DialogFragmentDelegate delegate) {
            InnerDialogFragment fragmentDialog = new InnerDialogFragment();
            DelegateManager.getInstance().put(fragmentDialog.hashCode(), delegate);
            return fragmentDialog;
        }

        /**
         * 无参构造函数
         */
        public InnerDialogFragment() {
        }

        private DialogFragmentConfig config() {
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
            Log.e(TAG, "onCreateView");
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
        }

        @Override
        public void dismiss() {
            if (config().getOnDismissListener() != null) {
                config().getOnDismissListener().onDismiss(mDelegate);
            }
            super.dismiss();
            //兼顾其它dismiss关闭情况
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
        mDialogFragment = null;
    }
}