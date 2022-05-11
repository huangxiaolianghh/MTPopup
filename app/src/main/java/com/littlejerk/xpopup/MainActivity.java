package com.littlejerk.xpopup;

import android.content.Intent;
import android.os.Bundle;

import com.littlejerk.xpopup.demo.BottomSheetDialogDemoActivity;
import com.littlejerk.xpopup.demo.BottomSheetDialogFragmentDemoActivity;
import com.littlejerk.xpopup.demo.DialogActivityDemoActivity;
import com.littlejerk.xpopup.demo.DialogDemoActivity;
import com.littlejerk.xpopup.demo.DialogFragmentDemoActivity;
import com.littlejerk.xpopup.demo.PopupWindowDemoActivity;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author HHotHeart
 * @date 2022/5/10 20:20
 * @desc 描述
 */
public class MainActivity extends AppCompatActivity {

    private String mMessage = "测试MessageDialogBuilder测试MessageDialogBuilder测试Message" +
            "DialogBuilder测试MessageDialogBuilder测试MessageDialogBuilder测试MessageDialogBuilder测试MessageDialogBuilder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(
                v -> MainActivity.this.startActivity(new Intent(MainActivity.this, DialogDemoActivity.class)));
        findViewById(R.id.btn2).setOnClickListener(
                v -> MainActivity.this.startActivity(new Intent(MainActivity.this, BottomSheetDialogDemoActivity.class)));

        findViewById(R.id.btn3).setOnClickListener(
                v -> MainActivity.this.startActivity(new Intent(MainActivity.this, PopupWindowDemoActivity.class)));

        findViewById(R.id.btn4).setOnClickListener(
                v -> MainActivity.this.startActivity(new Intent(MainActivity.this, DialogFragmentDemoActivity.class)));
        findViewById(R.id.btn5).setOnClickListener(
                v -> MainActivity.this.startActivity(new Intent(MainActivity.this, BottomSheetDialogFragmentDemoActivity.class)));

        findViewById(R.id.btn6).setOnClickListener(
                v -> MainActivity.this.startActivity(new Intent(MainActivity.this, DialogActivityDemoActivity.class)));

    }
}