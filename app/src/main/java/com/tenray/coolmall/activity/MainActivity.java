package com.tenray.coolmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.VideoView;

import com.tenray.coolmall.R;
import com.tenray.coolmall.animation.ButtonAnimation;

;

public class MainActivity extends Activity implements OnClickListener {
    private ImageView infoOperatingIV;
    private VideoView mVideoView;
    private int[] mMp4 = new int[] { R.raw.a, R.raw.b, R.raw.c, R.raw.d, R.raw.e, R.raw.f };
    //private int[] mMp4 = new int[] { };
    int position = (int) (Math.random() * mMp4.length);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止息屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 设置屏幕始终在前面，不然点击鼠标，重新出现虚拟按键
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide
                        // nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_main);
        mVideoView = (VideoView) this.findViewById(R.id.mp4);
        System.out.println("position"+position);
        String uri = "android.resource://" + getPackageName() + "/" + mMp4[position];
        mVideoView.setVideoPath(uri);
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (position == (mMp4.length-1))
                    position = 0;
                else
                    position += 1;
                String uri = "android.resource://" + getPackageName() + "/" + mMp4[position];
                mVideoView.setVideoPath(uri);
                mVideoView.start();
            }
        });
        infoOperatingIV = (ImageView) findViewById(R.id.anim_iv);
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
//        switch (v.getId()){
//            case R.id.anim_iv:
//                Intent intent=new Intent(this,GoodsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//        }

        Intent intent=new Intent(this,GoodsActivity.class);
        startActivity(intent);
        finish();
       // break;

    }

}
