package com.tenray.coolmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.tenray.coolmall.R;
import com.tenray.coolmall.util.CountDownTimer;
import com.tenray.coolmall.util.ToastUtil;

/**
 * Created by en on 2016/11/29.
 */

public class QuhuoActivity extends Activity {
    private String input="";
    private Button mBtnEd;
    private Button mReback;
    private TimeCount time;
   private Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           switch (msg.what){
               case 0:
                   input+="0";
                   mBtnEd.setText(input);
                   break;
               case 1:
                   input+="1";
                   mBtnEd.setText(input);
                   break;
               case 2:
                   input+="2";
                   mBtnEd.setText(input);
                   break;
               case 3:
                   input+="3";
                   mBtnEd.setText(input);
                   break;
               case 4:
                   input+="4";
                   mBtnEd.setText(input);
                   break;
               case 5:
                   input+="5";
                   mBtnEd.setText(input);
                   break;
               case 6:
                   input+="6";
                   mBtnEd.setText(input);
                   break;
               case 7:
                   input+="7";
                   mBtnEd.setText(input);
                   break;
               case 8:
                   input+="8";
                   mBtnEd.setText(input);
                   break;
               case 9:
                   input+="9";
                   mBtnEd.setText(input);
                   break;
               case 10:
                   input+="*";
                   mBtnEd.setText(input);
                   break;
               case 11:
                   input+="#";
                   mBtnEd.setText(input);
                   break;
               case 20:
                   if(!TextUtils.isEmpty(input)) {
                       input = input.substring(0, input.length() - 1);
                       mBtnEd.setText(input);
                   }
                   break;
               case 21:
                   input="";
                   mBtnEd.setText(input);
                   break;
               case 22:
                   if (TextUtils.isEmpty(input))
                       ToastUtil.showTop(getApplicationContext(),"取货码不能为空!",0,150);
                   else
                   if(input.equals("#000000#"))
                   {
                       startActivity(new Intent(getApplicationContext(),ComActivity.class));
                   }else
                       ToastUtil.showTop(getApplicationContext(),"您输入的取货码不存在!",0,150);
                   break;
           }
       }
   };

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
        setContentView(R.layout.activity_quhuo);
        initUI();
    }

    private void initUI(){
        mBtnEd = (Button) findViewById(R.id.btn_ed);
        mReback = (Button) findViewById(R.id.reback);
        time = new TimeCount(30000, 1000);//构造CountDownTimer对象
        time.start();//开始计时
    }

    public void onClick(View v) {
        Message msg=Message.obtain();
        switch (v.getId()){

           case R.id.btn0:
               msg.what=0;
               handler.sendMessage(msg);
                break;
            case R.id.btn1:
                msg.what=1;
                handler.sendMessage(msg);
                break;
            case R.id.btn2:
                msg.what=2;
                handler.sendMessage(msg);
                break;
            case R.id.btn3:
                msg.what=3;
                handler.sendMessage(msg);
                break;
            case R.id.btn4:
                msg.what=4;
                handler.sendMessage(msg);
                break;
            case R.id.btn5:
                msg.what=5;
                handler.sendMessage(msg);
                break;
            case R.id.btn6:
                msg.what=6;
                handler.sendMessage(msg);
                break;
            case R.id.btn7:
                msg.what=7;
                handler.sendMessage(msg);
                break;
            case R.id.btn8:
                msg.what=8;
                handler.sendMessage(msg);
                break;
            case R.id.btn9:
                msg.what=9;
                handler.sendMessage(msg);
                break;
            case R.id.btnX:
                msg.what=10;
                handler.sendMessage(msg);
                break;
            case R.id.btnY:
                msg.what=11;
                handler.sendMessage(msg);
                break;
            case R.id.btnDel:
                msg.what=20;
                handler.sendMessage(msg);
                break;
            case R.id.btnDelAll:
                msg.what=21;
                handler.sendMessage(msg);
                break;
            case R.id.btnOK:
                msg.what=22;
                handler.sendMessage(msg);
                break;
        }

    }

    public void goumai(View view) {
        time.cancel();
        startActivity(new Intent(this, GoodsActivity.class));
        finish();
    }

    public void bangzhu(View view) {
        time.cancel();
        startActivity(new Intent(this, HelpActivity.class));
        finish();
    }
    public void quhuo(View view) {
    }

    public void onReback(View view) {
        time.cancel();
        startActivity(new Intent(this, GoodsActivity.class));
        finish();
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(QuhuoActivity.this, MainActivity.class));
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mReback.setText(millisUntilFinished / 1000 + "秒后返回");
        }

    }
}
