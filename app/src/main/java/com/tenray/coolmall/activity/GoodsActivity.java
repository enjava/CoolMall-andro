package com.tenray.coolmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenray.coolmall.R;
import com.tenray.coolmall.application.MyApplication;
import com.tenray.coolmall.entity.ChannelInfo;
import com.tenray.coolmall.util.CountDownTimer;

import java.util.Map;

/**
 * Created by en on 2016/11/9.
 */

public class GoodsActivity extends Activity {
    private final String tag= getClass().getSimpleName();
    private TimeCount time;
    private Button mReback;
    private GridView gv_home;
    private MyApplication myApplication;
    private Map<String ,ChannelInfo> channelInfos;

    private String[] mProductName = new String[]{
            "利群", "七匹狼", "口水娃", "红牛功能饮料", "王老吉凉茶",
            "百事可乐", "可口可乐330ml", "美汁源爽粒葡萄", "统一绿茶", "哇哈哈营养快线",
            "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线",
            "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线",
            "利群", "七匹狼", "口水娃", "红牛功能饮料", "王老吉凉茶",
            "百事可乐", "可口可乐330ml", "美汁源爽粒葡萄", "统一绿茶", "哇哈哈营养快线",
            "利群", "七匹狼", "口水娃", "红牛功能饮料", "王老吉凉茶",
            "百事可乐", "可口可乐330ml", "美汁源爽粒葡萄", "统一绿茶", "哇哈哈营养快线",
            "百事可乐", "可口可乐330ml", "美汁源爽粒葡萄", "统一绿茶", "哇哈哈营养快线"
    };
    private String[] mProductChnanel = new String[]{
            "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8",
            "B1", "B2", "B3", "B4", "B5",
            "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8",
            "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8",
            "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8",
            "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8"
    };
    private int[] mProductID = new int[]{
            R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5,
            R.mipmap.p6, R.mipmap.p7, R.mipmap.p8, R.mipmap.p9, R.mipmap.p10,
            R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5,
            R.mipmap.p6, R.mipmap.p7, R.mipmap.p8, R.mipmap.p9, R.mipmap.p10,
            R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5,
            R.mipmap.p6, R.mipmap.p7, R.mipmap.p8, R.mipmap.p9, R.mipmap.p10,
            R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5,
            R.mipmap.p6, R.mipmap.p7, R.mipmap.p8, R.mipmap.p9, R.mipmap.p10,
            R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication= (MyApplication) this.getApplication();
        channelInfos=myApplication.getChannelInfos();
        //防止息屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide
                        // nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_goods);
        initUI();
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv);
        gv_home.setAdapter(new Madapter());

        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                intent.setClass(getApplicationContext(), BuyActivity.class);
                startActivity(intent);
                time.cancel();
                finish();
            }
        });
        mReback = (Button) findViewById(R.id.reback);
        time = new TimeCount(30000, 1000);//构造CountDownTimer对象
        time.start();//开始计时
    }

    public void onReback(View view) {
        startActivity(new Intent(this, MainActivity.class));
        time.cancel();
        finish();
    }

    public void bangzhu(View view) {
        startActivity(new Intent(this, HelpActivity.class));
        time.cancel();
        finish();
    }
    public void quhuo(View view) {
        time.cancel();
        startActivity(new Intent(this, QuhuoActivity.class));
        finish();
    }

    public void goumai(View view) {
    }

    class Madapter extends BaseAdapter {

        @Override
        public int getCount() {
            // 条目的总数 文字组数 == 图片张数
            return mProductChnanel.length;
        }

        @Override
        public Object getItem(int position) {
            return mProductName[position];
        }

        @Override
        public long getItemId(int position) {
            return mProductID[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.gridview_item_iv);
            tv_name.setText(mProductName[position]);
            double d=channelInfos.get(mProductChnanel[position]).getPrice();
            d=d/100;
            tv_price.setText("货道[" + mProductChnanel[position] + "]:" + d + "元");
            iv_icon.setBackgroundResource(mProductID[position]);
            return view;
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(GoodsActivity.this, MainActivity.class));
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mReback.setText(millisUntilFinished / 1000 + "秒后返回");
            //System.out.println("  millisUntilFinished:" + millisUntilFinished);
        }

    }

}
