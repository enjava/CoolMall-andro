package com.tenray.coolmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.VideoView;

import com.tenray.coolmall.R;
import com.tenray.coolmall.animation.ButtonAnimation;
import com.tenray.coolmall.application.MyApplication;
import com.tenray.coolmall.util.Constants;
import com.tenray.coolmall.util.SpUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//主页面
public class MainActivity extends Activity implements OnClickListener {
    private final String tag=getClass().getSimpleName();
    private ImageView infoOperatingIV;
    private VideoView mVideoView;
    private String mAdVideoPath;
    private int position;
    private MyApplication mMyApplication;
    private List<String> listMp4=new ArrayList<>();;

    ///mnt/usbhost0/8_4/mm.mp4
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
        initDate();
        mMyApplication = (MyApplication) this.getApplication();
        mAdVideoPath= mMyApplication.getVideoAdPath();
        mVideoView = (VideoView) this.findViewById(R.id.mp4);
        String url=mAdVideoPath+listMp4.get(position);
        //String url="/mnt/usbhost0/8_4/mm.mp4";

        Log.i(tag,"url_position:"+url);
        Log.i(tag,"url_position:"+"listMp4lenth:"+listMp4.size());

        mVideoView.setVideoPath(url);
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (position == (listMp4.size()-1))
                    position = 0;
                else
                    position += 1;
                String url=mAdVideoPath+listMp4.get(position);
                mVideoView.setVideoPath(url);
                mVideoView.start();
            }
        });
        infoOperatingIV = (ImageView) findViewById(R.id.anim_iv);
        anima();

    }

    private void initDate() {
        Set<String>  mAdVideoSets= SpUtil.getSet(this, Constants.AD_VIDEO_NAME,null);

        if (mAdVideoSets==null){
            mAdVideoSets = new HashSet<>();
           String [] sets=new String[]{"a.mp4","b.mp4","c.mp4","d.mp4","e.mp4","f.mp4","g.mp4","h.mp4",
                   "i.mp4","j.mp4","k.mp4","l.mp4","m.mp4","n.mp4","o.mp4","p.mp4","q.mp4","r.mp4",
                   "s.mp4","t.mp4","u.mp4","v.mp4","w.mp4","x.mp4","y.mp4","z.mp4","aa.mp4","ab.mp4",
                   "ac.mp4","ad.mp4","ae.mp4","af.mp4","ag.mp4","ah.mp4","ai.mp4","aj.mp4","ak.mp4","al.mp4"
           };

            for (int u=0;u<sets.length;u++){
                mAdVideoSets.add(sets[u]);
            }
            SpUtil.putSet(this, Constants.AD_VIDEO_NAME,mAdVideoSets);
        }

        listMp4.addAll(mAdVideoSets);

        position = (int) (Math.random() * listMp4.size());
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
        Intent intent=new Intent(this,GoodsActivity.class);
        startActivity(intent);
        //finish();
    }

}
