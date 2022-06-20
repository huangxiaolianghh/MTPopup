package com.huangxiaoliang.xpopup;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

/**
 * @author HHotHeart
 * @date 2022/5/4 22:39
 * @desc 底部可拖拽BottomSheetDialogFragment
 */
public final class BottomSheetDialogFragmentConfig extends BottomSheetBehaviorConfig<BottomSheetDialogFragmentConfig> implements Parcelable {

    private FragmentManager mManager;
    private String mTag;

    protected static BottomSheetDialogFragmentConfig get(Context context) {
        return new BottomSheetDialogFragmentConfig(context);
    }

    protected BottomSheetDialogFragmentConfig(Context context) {
        super(context);
        gravity(Gravity.BOTTOM);
    }

    @Override
    public BottomSheetDialogFragmentConfig gravity(int gravity) {
        return super.gravity(Gravity.BOTTOM);
    }

    public BottomSheetDialogFragmentConfig managerTag(@NonNull FragmentManager manager, @Nullable String tag) {
        mManager = manager;
        mTag = tag;
        return this;
    }

    public FragmentManager getManager() {
        return mManager;
    }

    public String getTag() {
        return mTag;
    }


    @SuppressWarnings("rawtypes")
    @Override
    public XPopup<BottomSheetDialogFragmentConfig, BottomSheetDialogFragmentDelegate> create() {
        return new XPopup<BottomSheetDialogFragmentConfig, BottomSheetDialogFragmentDelegate>(
                this,
                BottomSheetDialogFragmentDelegate.class,
                XPopupCompat.BottomSheetDialogFragment);

    }

    protected BottomSheetDialogFragmentConfig(Parcel in) {
        mTag = in.readString();
    }

    public static final Creator<BottomSheetDialogFragmentConfig> CREATOR = new Creator<BottomSheetDialogFragmentConfig>() {
        @Override
        public BottomSheetDialogFragmentConfig createFromParcel(Parcel in) {
            return new BottomSheetDialogFragmentConfig(in);
        }

        @Override
        public BottomSheetDialogFragmentConfig[] newArray(int size) {
            return new BottomSheetDialogFragmentConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTag);
    }
}