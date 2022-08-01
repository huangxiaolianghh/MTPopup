# XPopUp（popup-ktx版本更新中）
[![](https://jitpack.io/v/HHotHeart/XPopUp.svg)](https://jitpack.io/#HHotHeart/XPopUp)<br>Android一个简单强大的弹框处理框架，支持链式调用，提供了比较全面的api，并且覆盖Android几乎所有的弹框类型，如Dialog、BottomSheetDialog、DialogFragment、BottomSheetDialogFragment、PopupWindow、DialogActivity，总结起来就是调用简单、功能强大。

# 功能特点

 - 层次结构分明，分工明确
 - 采用链式调用一点到底
 - 支持Android多种弹框：**Dialog**、**BottomSheetDialog**、**DialogFragment**、**BottomSheetDialogFragment**、**PopupWindow**、**DialogActivity**
 - 支持设置弹窗的主题样式、宽高（最大宽高及屏幕宽高比等）、背景透明度、圆角、显示位置、显示消失动画
 - 支持设置弹窗ContentView相关控件View的属性，如点击、长按事件等
 - 支持设置弹窗关闭的逻辑，如返回键、点击外部空白区域
 - 支持设置弹窗的显示及关闭事件监听
 - 支持设置弹窗自动消失
 - 支持Liffecycle设置，生命周期监听
 - ktx版本更新中...
 - ...

# 项目引入该库
在你的 Project build.gradle文件中添加：

```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
在你的 Module build.gradle文件中添加：

```java
dependencies {
	        implementation 'com.github.huangxiaolianghh.XPopUp:popup:1.0.1'
	}
```

# 如何调用
我们可以通过XPopupCompat asXXX系列的方法来展示不同的弹框类型，如：
 - asDialog
 - asBottomSheetDialog
 - asDialogFragment
 - asBottomSheetDialogFragment
 - asPopupWindow
 - asDialogActivity

下面以展示Dialog进行说明：
```java
            XPopupCompat.get().asDialog(DialogDemoActivity.this)
                    .themeStyle(R.style.XPopup_Dialog)    //主题样式
                    .view(R.layout.popup_test)            //可传View
                    .radius(50)                           //设置四个圆角
//                    //设置弹窗上（下、左、右）方圆角，
//                    .radiusSideTop(50)
                    .animStyle(R.style.PopupEnterExpandExitShrinkAnimation)  //动画
                    //宽高比
                    .widthInPercent(0.8f)
//                    .heightInPercent(0.8f)
//                    //最大宽高
//                    .maxWidth(800)
//                    .maxHeight(800)
//                    //框架默认是width=ViewGroup.LayoutParams.MATCH_PARENT，height=ViewGroup.LayoutParams.WRAP_CONTENT
//                    .width(ViewGroup.LayoutParams.MATCH_PARENT)
//                    .height(ViewGroup.LayoutParams.WRAP_CONTENT)
//                    .matchHeight()
//                    .matchWidth()
//                    .wrapHeight()
//                    .wrapWidth()
//                    .dimAmount(0f)                     //不设置背景透明度，默认0.5f
                    .cancelable(false)                   //返回键dismiss
                    .cancelableOutside(false)            //点击外部区域dismiss
                    .autoDismissTime(5000)               //5秒后自动dismiss
                    .gravity(Gravity.CENTER)             //设置显示位置
                    //添加View点击事件，也支持添加多个View的点击事件
                    .clickListener(R.id.btn_left, (popupInterface, view, holder) -> {
                        popupInterface.dismiss();
                        Toast.makeText(
                                DialogDemoActivity.this,
                                "点击了" + ((Button) holder.getView(R.id.btn_left)).getText(),
                                Toast.LENGTH_SHORT)
                                .show();
                    })
                    //添加多个View长按事件，也支持添加单个View的点击事件
                    .longClickIds(R.id.btn_left, R.id.btn_right)
                    .longClickIdsListener((popupInterface, view, holder) -> {
                        if (view.getId() == R.id.btn_left) {
                            Toast.makeText(
                                    DialogDemoActivity.this,
                                    "长按了" + ((Button) holder.getView(R.id.btn_left)).getText(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        } else if (view.getId() == R.id.btn_right) {
                            Toast.makeText(
                                    DialogDemoActivity.this,
                                    "长按了" + ((Button) holder.getView(R.id.btn_right)).getText(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                        return true;
                    })
                    //关闭事件监听
                    .dismissListener(popupInterface ->
                            Toast.makeText(
                                    DialogDemoActivity.this,
                                    "消失监听",
                                    Toast.LENGTH_SHORT)
                                    .show())
                    //显示事件监听
                    .showListener(popupInterface ->
                            Toast.makeText(
                                    DialogDemoActivity.this,
                                    "显示监听",
                                    Toast.LENGTH_SHORT)
                                    .show())
                    //View绑定事件监听
                    .bindViewListener((XPopupViewHolder holder) -> {
                        //holder可以对象弹窗所有控件操作，在这里处理弹窗内部相关逻辑
                        holder.setText(R.id.tv_popup_title, "XPopup");
                    })
                    .create()
                    .show();
```

如果我们需要在弹窗展示期间改变ContentView一些控件的属性，这时我们可以经过Config配置对象的create()方法获取XPopup对象实例，通过其就可以拿到ContentView的XPopupViewHolder对象，进而改变相关控件的属性，如：
```java
            XPopup<DialogConfig, DialogDelegate> xPopup = XPopupCompat.get().asDialog(DialogDemoActivity.this)
                    .view(R.layout.popup_test)
                    .gravity(Gravity.BOTTOM)
                    .autoDismissTime(10000)
                    .create();
            xPopup.show();
            new Handler(Looper.getMainLooper()).postDelayed(() ->
                    xPopup.getPopupViewHolder().setText(R.id.tv_popup_title, "获取xPopup对象，更新标题"), 5000);
```

XPopup实现了lifecycle，添加被观察者和实现XPopupLifecycleObserver接口即可，如：

```java
		   XPopupCompat.get().asDialog(DialogDemoActivity.this)
                    .view(R.layout.popup_test)
                    .gravity(Gravity.BOTTOM)
                    .cancelable(false)
                    .cancelableOutside(false)
                    .observeOn(getLifecycle(), new XPopupLifecycleObserver() {
                        @Override
                        public void onPause(IPopup popup) {
                            popup.dismiss();
                            Toast.makeText(
                                    DialogDemoActivity.this,
                                    "onPause()--->消失",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .create()
                    .show();
```

这里只重写了onPause()，其它生命周期的回调直接重写即可。

对于Dialog，如果Activity关闭前并没有关闭dialog，则会造成内存泄漏，这里我们简单实现了onDestroy()关闭dialog的开关方法：observeOn(Lifecycle lifecycle, boolean dismissObserverOnDestroy)，如：

```java
		   XPopupCompat.get().asDialog(DialogDemoActivity.this)
                    .view(R.layout.popup_test)
                    .gravity(Gravity.BOTTOM)
                    .cancelable(false)
                    .cancelableOutside(false)
                    .clickListener(R.id.btn_right, (popupInterface, view, holder) -> finish())
                    .bindViewListener(holder -> holder.setText(R.id.btn_right, "关闭页面"))
                    .observeOn(getLifecycle(), true)
                    .create()
                    .show();
```

这里特别说明下XPopup dismiss掉，会把代理delegate释放，也会将观察者的关系解除，此时的XPopup对象就没有作用，调用里面的方法会抛异常！若想再次使用，直接调用Config的create()方法重新初始化XPopup对象。

因为XPopup实现了LifecycleObserver观察者，这意味着这几种弹窗类型都可以感知被观察者的生命周期，Dialog感知宿主的生命周期对我们来说是非常有意义的事情，而其它弹窗类型我们可以酌情使用LifecycleObserver，归根结底，我们要懂得LifecycleObserver的原理才不会出错。

至于其它类型弹框的使用和动画的设置可以通过demo去了解，这里就不细说了。

# XPopup属性配置说明

基本公共属性
| 属性 | 设置方法 | 说明|
|---|--|--|
| mContext | 构造函数BaseConfig(Context context) | 上下文 |
| mLifecycle<br>mXPopupLifecycleObserver<br>mDismissObserverOnDestroy | observeOn(Lifecycle lifecycle, XPopupLifecycleObserver popupLifecycleObserver)<br>observeOn(Lifecycle lifecycle, boolean dismissObserverOnDestroy) | 被观察者生命周期的回调<br>设置onDestroy()回调关闭XPopup的开关 |
|mThemeStyle|themeStyle(@StyleRes int themeStyle)| 主题样式，PopupWindow不支持此属性 |
| mAnimStyle | animStyle(@StyleRes int animStyle) | 动画 |
| mContentView| view(View contentView)<br>view(@LayoutRes int contentViewResId) | 内容View|
| mOnBindViewListener| bindViewListener(XPopupInterface.OnBindViewListener listener) | View绑定到XPopup前的监听器|
| mOnShowListener| showListener(XPopupInterface.OnShowListener onShowListener) | XPopup显示监听器|
| mOnDismissListener| dismissListener(XPopupInterface.OnDismissListener onDismissListener) | XPopup关闭监听器|
| mClickIds| clickIds(@IdRes int... clickIds) | 点击控件id集合|
| mOnClickListener| clickIdsListener(@NonNull XPopupInterface.OnClickListener onClickListener) | 控件点击事件监听器|
| mLongClickIds| longClickIds(@IdRes int... longClickIds) | 长按控件id集合|
| mOnLongClickListener| longClickIdsListener(@NonNull XPopupInterface.OnLongClickListener onLongClickListener) | 控件长按事件监听器|
| mWidth| width(int width)<br>matchWidth()<br>wrapWidth() | 宽，默认ViewGroup.LayoutParams.MATCH_PARENT|
| mHeight| height(int height)<br>matchHeight()<br>wrapHeight() | 高，默认ViewGroup.LayoutParams.WRAP_CONTENT|
| mMaxWidth| maxWidth(int maxWidth) | 最大宽|
| mMaxHeight| maxHeight(int maxHeight) | 最大高|
| mWidthInPercent| widthInPercent(float widthInPercent) | 屏幕宽度比|
| mHeightInPercent| heightInPercent(float heightInPercent) | 屏幕高度比|
| mGravity| gravity(int gravity)| 弹窗位置，BottomSheet系列弹窗的位置恒为Gravity.BOTTOM|
| mBackgroundDrawable| backgroundDrawable(Drawable backgroundDrawable) | 弹窗window的背景|
| mDimAmount| dimAmount(float dimAmount) | 弹窗周围的亮度，默认0.5f |
| mAutoDismissTime| autoDismissTime(long autoDismissTime) | x秒后自动消失|
| mCancelable| cancelable(boolean cancelable) | 返回键事件关闭弹窗，默认true，对于PopupWindow无效，调用者自行处理其此事件|
| mCancelableOutside| cancelableOutside(boolean cancelableOutside) | 点击外部区域关闭弹窗，默认true|
| mRadius<br>mRadiusSideLeft<br>mRadiusSideTop<br>mRadiusSideRight<br>mRadiusSideBottom| radius(int radius)<br>radiusSideLeft(int radiusSideLeft)<br>radiusSideTop(int radiusSideTop)<br>radiusSideRight(int radiusSideRight)<br>radiusSideBottom(int radiusSideBottom) | 圆角<br>左边圆角<br>上边圆角<br>右边圆角<br>下边圆角|


这里只列举XPopup的公共属性，至于不同的弹窗类型，其对应的Config定义了特有的属性，这里就不列举出来了，详细的属性可以去看XXXConfig系列源码。

<a href="https://blog.csdn.net/HHHceo">我的博客</a>
<a href="https://blog.csdn.net/HHHceo/article/details/124658074">XPopUp，Android一个简单强大的弹框</a>
