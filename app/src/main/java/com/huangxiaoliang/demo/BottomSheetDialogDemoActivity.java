package com.huangxiaoliang.demo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huangxiaoliang.xpopup.BottomSheetDialogConfig;
import com.huangxiaoliang.xpopup.BottomSheetDialogDelegate;
import com.huangxiaoliang.xpopup.XPopup;
import com.huangxiaoliang.xpopup.XPopupCompat;

/**
 * @author HHotHeart
 * @date 2022/5/10 20:20
 * @desc 描述
 */
public class BottomSheetDialogDemoActivity extends AppCompatActivity {
    XPopup<BottomSheetDialogConfig, BottomSheetDialogDelegate> xPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet_dialog_demo);
        setTitle("BottomSheetDialogDemo");

        xPopup = XPopupCompat.get().asBottomSheetDialog(BottomSheetDialogDemoActivity.this)
                .view(R.layout.popup_test)
//                        .widthInPercent(0.8f)
//                        .gravity(Gravity.BOTTOM)
                .radiusSideTop(50)
//                        .heightInPercent(0.8f)
//                        .cornerRadiusTR(50)
                .cancelable(true)
                .themeStyle(R.style.XPopup_BottomSheetDialog)
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
            xPopup.show();
        });
        //dismiss后释放资源，但xPopup实例是值传递，所以xPopup!=null
        findViewById(R.id.btn_test_popup).setOnClickListener(v -> {
            Toast.makeText(
                    BottomSheetDialogDemoActivity.this,
                    "当前实例 Popup == null：" + (xPopup == null),
                    Toast.LENGTH_SHORT)
                    .show();
        });
    }
}