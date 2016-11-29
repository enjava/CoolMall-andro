package com.tenray.coolmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.tenray.coolmall.R;
import com.tenray.coolmall.util.CountDownTimerUtil;

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
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        time.start();//开始计时
    }

    public void onReback(View view) {
        startActivity(new Intent(this,GoodsActivity.class));
        time.cancel();
        finish();
    }

    public void goumai(View view) {
        startActivity(new Intent(this,GoodsActivity.class));
        time.cancel();
        finish();
    }

    public void quhuo(View view) {
    }

    class TimeCount extends CountDownTimerUtil {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {
            startActivity(new Intent(HelpActivity.this,MainActivity.class));
            finish();
            //计时完毕时触发
//            checking.setText("重新验证");
//            checking.setClickable(true);
        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
//            checking.setClickable(false);
            mReback.setText(millisUntilFinished /1000+"秒后返回");
        }
    }

}
