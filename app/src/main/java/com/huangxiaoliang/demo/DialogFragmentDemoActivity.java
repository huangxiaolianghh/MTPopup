package com.huangxiaoliang.demo;

import android.os.Bundle;
import android.widget.Toast;

import com.huangxiaoliang.xpopup.XPopupCompat;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author HHotHeart
 * @date 2022/5/10 21:25
 * @desc 描述
 */
public class DialogFragmentDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fragment_demo);
        setTitle("DialogFragmentDemo");
        findViewById(R.id.btn_dialog_fragment1).setOnClickListener(v ->
                XPopupCompat.get().asDialogFragment(DialogFragmentDemoActivity.this)
                        .view(R.layout.popup_test)
                        .radius(50)
                        .themeStyle(R.style.XPopup_Dialog)
                        .managerTag(getSupportFragmentManager(), "HHH")
                        .animStyle(R.style.PopupEnterRightExitLeftAnimation)
                        .dimAmount(1f)
                        .cancelable(true)
                        .widthInPercent(0.8f)
                        .clickListener(R.id.btn_left, (popupInterface, view, holder) -> {
                            popupInterface.dismiss();
                        })
                        .showListener(popupInterface ->
                                Toast.makeText(
                                        DialogFragmentDemoActivity.this,
                                        "正在显示",
                                        Toast.LENGTH_SHORT)
                                        .show())
                        .dismissListener(popupInterface ->
                                Toast.makeText(
                                        DialogFragmentDemoActivity.this,
                                        "消失监听",
                                        Toast.LENGTH_SHORT)
                                        .show())
                        .create()
                        .show());
    }
}