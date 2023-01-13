package com.huangxiaoliang.popup;

import static androidx.fragment.app.DialogFragment.STYLE_NORMAL;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

/**
 * @author huangxiaolianghh
 * @date 2022/4/2 11:42
 * @desc DialogFragment 弹框配置
 */
public final class DialogFragmentConfig extends BaseConfig<DialogFragmentConfig,
        DialogFragmentDelegate> implements Parcelable {

    private FragmentManager mManager;
    private String mTag;

    @PopupCompat.DialogFragmentStyle
    private int mDialogFragmentStyle = STYLE_NORMAL;

    protected static DialogFragmentConfig get(Context context) {
        return new DialogFragmentConfig(context);
    }

    protected DialogFragmentConfig(Context context) {
        super(context);
    }

    public DialogFragmentConfig managerTag(@NonNull FragmentManager manager, @Nullable String tag) {
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

    public int getDialogFragmentStyle() {
        return mDialogFragmentStyle;
    }

    public DialogFragmentConfig dialogFragmentStyle(@PopupCompat.DialogFragmentStyle int dialogFragmentStyle) {
        mDialogFragmentStyle = dialogFragmentStyle;
        return this;
    }

    @Override
    public Popup<DialogFragmentDelegate> create() {
        return new Popup<>(this, DialogFragmentDelegate.class, PopupCompat.DialogFragment);
    }

    protected DialogFragmentConfig(Parcel in) {
        mTag = in.readString();
    }

    public static final Creator<DialogFragmentConfig> CREATOR = new Creator<DialogFragmentConfig>() {
        @Override
        public DialogFragmentConfig createFromParcel(Parcel in) {
            return new DialogFragmentConfig(in);
        }

        @Override
        public DialogFragmentConfig[] newArray(int size) {
            return new DialogFragmentConfig[size];
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