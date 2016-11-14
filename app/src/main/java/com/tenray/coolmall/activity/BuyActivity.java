package com.tenray.coolmall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.tenray.coolmall.R;

/**
 * Created by en on 2016/11/10.
 */

public class BuyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止息屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_buy);
    }
}
