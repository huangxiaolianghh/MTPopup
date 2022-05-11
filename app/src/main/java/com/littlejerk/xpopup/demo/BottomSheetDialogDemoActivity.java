package com.littlejerk.xpopup.demo;

import android.os.Bundle;
import android.widget.Toast;

import com.littlejerk.xpopup.R;
import com.littlejerk.xpopup.XPopupCompat;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author HHotHeart
 * @date 2022/5/10 20:20
 * @desc 描述
 */
public class BottomSheetDialogDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet_dialog_demo);
        setTitle("BottomSheetDialogDemo");

        findViewById(R.id.btn_bottomsheet_dialog).setOnClickListener(v ->
                XPopupCompat.get().asBottomSheetDialog(BottomSheetDialogDemoActivity.this)
                        .view(R.layout.popup_test)
//                        .widthInPercent(0.8f)
//                        .gravity(Gravity.BOTTOM)
                        .radiusSideTop(50)
//                        .heightInPercent(0.8f)
//                        .cornerRadiusTR(50)
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
                        .create()
                        .show());
    }
}