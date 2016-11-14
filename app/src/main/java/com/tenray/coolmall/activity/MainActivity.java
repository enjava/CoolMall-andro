package com.tenray.coolmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.tenray.coolmall.R;
import com.tenray.coolmall.animation.ButtonAnimation;

;

public class MainActivity extends Activity implements OnClickListener {
    private ImageView infoOperatingIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止息屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        infoOperatingIV= (ImageView) findViewById(R.id.anim_iv);
        infoOperatingIV.setOnClickListener(this);
        anima();

    }
    public void anima(){
        if (infoOperatingIV==null)
            return;
        ButtonAnimation rotation = new ButtonAnimation(360, 0,90,  0, 310.0f, false);
        rotation.setDuration(3000);//动画持续时间
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());//设置加速度
        rotation.setRepeatCount(Animation.INFINITE);//重复次数
        rotation.setRepeatMode(Animation.RESTART);//重复动画
        infoOperatingIV.startAnimation(rotation);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.anim_iv:
                Intent intent=new Intent(this,GoodsActivity.class);
                startActivity(intent);
                break;
        }

    }
}
