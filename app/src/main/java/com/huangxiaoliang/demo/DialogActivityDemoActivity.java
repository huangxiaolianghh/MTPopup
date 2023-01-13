package com.huangxiaoliang.demo;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.huangxiaoliang.popup.PopupCompat;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author huangxiaolianghh
 * @date 2022/5/10 21:25
 * @desc 描述
 */
public class DialogActivityDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_activity_demo);
        setTitle("DialogActivityDemo");
        findViewById(R.id.btn_dialog_activity).setOnClickListener(v -> showDialog());
    }

    private void showDialog() {
        PopupCompat.get().asDialogActivity(DialogActivityDemoActivity.this)
                .view(R.layout.popup_test)
                .radius(50)
                .themeStyle(R.style.MyDialogActivityTheme)
                .cancelableOutside(true)
                .widthInPercent(0.8f)
                .animStyle(R.style.ActivityEnterRightExitLeftAnimation)
                .gravity(Gravity.BOTTOM)
//                .cancelable(false)
//                .dimAmount(1f)
//                .matchHeight()
//                .matchWidth()
                .showListener(popupInterface ->
                        Toast.makeText(
                                DialogActivityDemoActivity.this,
                                "正在显示",
                                Toast.LENGTH_SHORT)
                                .show())
                .dismissListener(popupInterface ->
                        Toast.makeText(
                                DialogActivityDemoActivity.this,
                                "消失",
                                Toast.LENGTH_SHORT)
                                .show())
                .clickListener(R.id.btn_right, (popupInterface, view, holder) -> showDialog())
                .clickListener(R.id.btn_left, (popupInterface, view, holder) -> popupInterface.dismiss())
                .create()
                .show();
    }
}