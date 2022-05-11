package com.littlejerk.xpopup.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import com.littlejerk.xpopup.BaseDelegate;
import com.littlejerk.xpopup.XPopupInterface;

/**
 * @author HHotHeart
 * @date 2022/4/14 09:31
 * @desc Popup代理类管理者
 */
@SuppressWarnings("rawtypes")
public final class DelegateManager {
    private static DelegateManager instance;
    private SparseArray<BaseDelegate> delegateMap = new SparseArray<>();
    private XPopupInterface.OnDialogActivityInit mOnDialogActivityInit;

    public static synchronized DelegateManager getInstance() {
        if (instance == null) {
            instance = new DelegateManager();
        }

        return instance;
    }

    private DelegateManager() {
    }

    public void put(int hashCode, BaseDelegate delegate) {
        this.delegateMap.put(hashCode, delegate);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseDelegate> T get(int hashCode) {
        return (T) delegateMap.get(hashCode);
    }

    public void remove(int hashCode) {
        this.delegateMap.remove(hashCode);
    }

    public void handle(Context context, Class<? extends Activity> cls, XPopupInterface.OnDialogActivityInit result) {
        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        mOnDialogActivityInit = result;
    }

    public void sendResult(Activity activity) {
        if (mOnDialogActivityInit != null) {
            mOnDialogActivityInit.onInit(activity);
            mOnDialogActivityInit = null;
        }
    }
}