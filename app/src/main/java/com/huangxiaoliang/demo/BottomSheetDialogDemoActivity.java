package com.huangxiaoliang.demo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huangxiaoliang.popup.BottomSheetDialogDelegate;
import com.huangxiaoliang.popup.Popup;
import com.huangxiaoliang.popup.PopupCompat;

/**
 * @author huangxiaolianghh
 * @date 2022/5/10 20:20
 * @desc 描述
 */
public class BottomSheetDialogDemoActivity extends AppCompatActivity {
    Popup<BottomSheetDialogDelegate> popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet_dialog_demo);
        setTitle("BottomSheetDialogDemo");

        popup = PopupCompat.get().asBottomSheetDialog(BottomSheetDialogDemoActivity.this)
                .view(R.layout.popup_test)
//                        .widthInPercent(0.8f)
//                        .gravity(Gravity.BOTTOM)
                .radiusSideTop(50)
//                        .heightInPercent(0.8f)
//                        .cornerRadiusTR(50)
                .cancelable(true)
                .themeStyle(R.style.MTPopup_BottomSheetDialog)
//                        .animStyle(R.style.EnterRightExitLeftAnimation)
//                        .matchWidth()
//                        .maxHeight(300)
//                        .maxWidth(580)
//                        .heightInPercent(0.8f)
//                        .height(ViewGroup.LayoutParams.MATCH_PARENT)
//                        .maxWidth((int) (0.5f* Utils.getScreenWidth(DialogDemoActivity.this)))
                .dismissListener(popupInterface ->
                        Toast.makeText(
                                BottomSheetDialogDemoActivity.this,
                                "消失监听",
                                Toast.LENGTH_SHORT)
                                .show())
                .create();

        findViewById(R.id.btn_bottomsheet_dialog).setOnClickListener(v -> {
            popup.show();
        });
        //dismiss后释放资源，但Popup实例是值传递，所以popup!=null
        findViewById(R.id.btn_test_popup).setOnClickListener(v -> {
            Toast.makeText(
                    BottomSheetDialogDemoActivity.this,
                    "当前实例 Popup == null：" + (popup == null),
                    Toast.LENGTH_SHORT)
                    .show();
        });
    }
}