package com.huangxiaoliang.xpopup;


/**
 * <pre>@author HHotHeart</pre>
 * <pre>@date 2022/7/1 15:24</pre>
 * <pre>@desc Popup生命周期绑定接口</pre>
 */
public interface XPopupLifecycleObserver {

    /**
     * 被观察者onStart()
     *
     * @param popup IPopup
     */
    default void onStart(IPopup popup) {

    }

    /**
     * 被观察者onResume()
     *
     * @param popup IPopup
     */
    default void onResume(IPopup popup) {

    }

    /**
     * 被观察者onPause()
     *
     * @param popup IPopup
     */
    default void onPause(IPopup popup) {

    }

    /**
     * 被观察者onStop()
     *
     * @param popup IPopup
     */
    default void onStop(IPopup popup) {

    }

    /**
     * 被观察者onDestroy()
     *
     * @param popup IPopup
     */
    default void onDestroy(IPopup popup) {

    }
}
