package com.huangxiaoliang.demo;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.huangxiaoliang.popup.PopupCompat;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author huangxiaolianghh
 * @date 2022/5/10 20:21
 * @desc 描述
 */
public class PopupWindowDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window_demo);
        setTitle("PopupWindowDemo");
        //没有themeStyle，返回键事件暂时没实现
        findViewById(R.id.btn_popupwinow).setOnClickListener(v ->
                PopupCompat.get().asPopupWindow(PopupWindowDemoActivity.this)
                        .view(R.layout.popup_test)
                        .radius(50)
                        .dimAmount(0.7f)
                        .cancelableOutside(true)
                        .widthInPercent(0.8f)
                        .animStyle(R.style.PopupEnterRightExitLeftAnimation)
                        .applyAtLocation(findViewById(R.id.btn_popupwinow))
                        .gravity(Gravity.BOTTOM)
//                        .matchHeight()
//                        .matchWidth()
//                        .maxWidth(800)
//                        .wrapWidth()
                        .matchWidth()
//                        .cancelable(false)
//                        .touchable(false)
//                        .focusable(false)
                        .clickListener(R.id.btn_left, (popupInterface, view, holder) -> popupInterface.dismiss())
                        .dismissListener(popupInterface ->
                                Toast.makeText(
                                        PopupWindowDemoActivity.this,
                                        "消失监听",
                                        Toast.LENGTH_SHORT)
                                        .show())
                        .showListener(popupInterface ->
                                Toast.makeText(
                                        PopupWindowDemoActivity.this,
                                        "正在显示",
                                        Toast.LENGTH_SHORT)
                                        .show())
                        .create()
                        .show());
    }
}