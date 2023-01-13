package com.huangxiaoliang.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author huangxiaolianghh
 * @date 2022/5/10 20:20
 * @desc 描述
 */
public class MainActivity extends AppCompatActivity {

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