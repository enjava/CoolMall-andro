package com.tenray.coolmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.tenray.coolmall.R;
import com.tenray.coolmall.util.CountDownTimer;

/**
 * Created by en on 2016/11/11.
 */

public class HelpActivity extends Activity {
    private TimeCount time;
    private Button mReback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止息屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide
                        // nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_help);
        initUI();
    }

    private void initUI() {
        mReback = (Button) findViewById(R.id.reback);
        time = new TimeCount(30000, 1000);//构造CountDownTimer对象
        time.start();//开始计时
    }

    public void onReback(View view) {
        time.cancel();
        startActivity(new Intent(this, GoodsActivity.class));
        finish();
    }

    public void goumai(View view) {
        time.cancel();
        startActivity(new Intent(this, GoodsActivity.class));
        finish();
    }

    public void quhuo(View view) {
        time.cancel();
        startActivity(new Intent(this, QuhuoActivity.class));
        finish();
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(HelpActivity.this, MainActivity.class));
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mReback.setText(millisUntilFinished / 1000 + "秒后返回");

        }
    }

}
