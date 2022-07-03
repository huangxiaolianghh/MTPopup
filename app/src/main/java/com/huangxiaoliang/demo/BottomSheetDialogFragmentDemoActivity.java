package com.huangxiaoliang.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.huangxiaoliang.xpopup.BottomSheetDialogFragmentConfig;
import com.huangxiaoliang.xpopup.BottomSheetDialogFragmentDelegate;
import com.huangxiaoliang.xpopup.XPopup;
import com.huangxiaoliang.xpopup.XPopupCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author HHotHeart
 * @date 2022/5/10 21:20
 * @desc 描述
 */
public class BottomSheetDialogFragmentDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet_dialog_fragment_demo);
        setTitle("BottomSheetDialogFragmentDemo");
        findViewById(R.id.btn_bottomsheet_dialog_fragment1).setOnClickListener(v -> {
            XPopup<BottomSheetDialogFragmentConfig, BottomSheetDialogFragmentDelegate> xPopup = XPopupCompat.get().asBottomSheetDialogFragment(BottomSheetDialogFragmentDemoActivity.this)
                    .view(R.layout.popup_test)
                    .radius(50)
                    .themeStyle(R.style.XPopup_BottomSheetDialog)
                    .managerTag(getSupportFragmentManager(), "HHH")
                    .draggable(true)
                    .gravity(Gravity.CENTER)
                    .widthInPercent(0.8f)
                    .cancelable(false)
//                    .peekHeight(2000)
                    .bottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            Log.e("HHH", String.valueOf(newState));
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                            Log.e("HHH", String.valueOf(slideOffset));

                        }
                    })
                    .clickListener(R.id.btn_left, (popupInterface, view, holder) -> popupInterface.dismiss())
                    .showListener(popupInterface ->
                            Toast.makeText(
                                    BottomSheetDialogFragmentDemoActivity.this,
                                    "正在显示",
                                    Toast.LENGTH_SHORT)
                                    .show())
                    .dismissListener(popupInterface ->
                            Toast.makeText(
                                    BottomSheetDialogFragmentDemoActivity.this,
                                    "消失监听",
                                    Toast.LENGTH_SHORT).show())
                    .create();
            xPopup.show();
//            xPopup.getDelegate().getBottomSheetBehavior().setPeekHeight(2000);
        });

    }
}