package com.huangxiaoliang.popup;


/**
 * <pre>@author huangxiaolianghh</pre>
 * <pre>@date 2022/7/1 15:24</pre>
 * <pre>@desc Popup生命周期绑定接口</pre>
 */
public interface PopupLifecycleObserver<Delegate extends BaseDelegate<?, ?>> {

    /**
     * 被观察者onStart()
     *
     * @param popup IPopup
     */
    default void onStart(IPopup<Delegate> popup) {

    }

    /**
     * 被观察者onResume()
     *
     * @param popup IPopup
     */
    default void onResume(IPopup<Delegate> popup) {

    }

    /**
     * 被观察者onPause()
     *
     * @param popup IPopup
     */
    default void onPause(IPopup<Delegate> popup) {

    }

    /**
     * 被观察者onStop()
     *
     * @param popup IPopup
     */
    default void onStop(IPopup<Delegate> popup) {

    }

    /**
     * 被观察者onDestroy()
     *
     * @param popup IPopup
     */
    default void onDestroy(IPopup<Delegate> popup) {

    }
}
