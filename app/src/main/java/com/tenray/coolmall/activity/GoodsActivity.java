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
import com.tenray.coolmall.util.CountDownTimer;

/**
 * Created by en on 2016/11/9.
 */

public class GoodsActivity extends Activity {
    private TimeCount time;
    private Button mReback;
    private  GridView gv_home;
    private String[] mProductName=new String[]{
            "利群","七匹狼","口水娃","红牛功能饮料","王老吉凉茶",
            "百事可乐","可口可乐330ml", "美汁源爽粒葡萄","统一绿茶","哇哈哈营养快线",
            "哇哈哈营养快线","哇哈哈营养快线","哇哈哈营养快线","哇哈哈营养快线","哇哈哈营养快线" ,
            "哇哈哈营养快线","哇哈哈营养快线","哇哈哈营养快线","哇哈哈营养快线","哇哈哈营养快线",
            "利群","七匹狼","口水娃","红牛功能饮料","王老吉凉茶",
            "百事可乐","可口可乐330ml", "美汁源爽粒葡萄","统一绿茶","哇哈哈营养快线"
    };
    private String [] mProductChnanel=new String[]{
            "A1","A3","A5","A7","B1",
            "B2","B3","B4","B5","C1",
            "C2","C3","C4","C5","C6",
            "C7","C8","D6","E3","E5",
            "C2","C3","C4","C5","C6",
            "C7","C8","D6","E3","E5"
    };
    private String [] mProductPrice=new String[]{
            "2.5","3.0","2.5","5.5","3.0",
            "2.5","2.5","3.0","3.0","3.5",
            "3.5","3.5","3.5","3.5","3.5",
            "3.5","3.5","3.5","3.5","3.5",
            "3.5","3.5","3.5","3.5","3.5",
            "3.5","3.5","3.5","3.5","3.5"
    };
    private int[] mProductID = new int[]{
            R.mipmap.p1,R.mipmap.p2,R.mipmap.p3,R.mipmap.p4,R.mipmap.p5,
            R.mipmap.p6,R.mipmap.p7,R.mipmap.p8,R.mipmap.p9,R.mipmap.p10,
            R.mipmap.p1,R.mipmap.p2,R.mipmap.p3,R.mipmap.p4,R.mipmap.p5,
            R.mipmap.p6,R.mipmap.p7,R.mipmap.p8,R.mipmap.p9,R.mipmap.p10,
            R.mipmap.p1,R.mipmap.p2,R.mipmap.p3,R.mipmap.p4,R.mipmap.p5,
            R.mipmap.p6,R.mipmap.p7,R.mipmap.p8,R.mipmap.p9,R.mipmap.p10
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
        setContentView(R.layout.activity_goods);
        initUI();
    }
    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv);
        gv_home.setAdapter(new Madapter());

        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("position", position);
                intent.setClass(getApplicationContext(), BuyActivity.class);
                startActivity(intent);
            }
        });
        mReback = (Button) findViewById(R.id.reback);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        time.start();//开始计时
    }

    public void onReback(View view) {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    public void bangzhu(View view) {
        startActivity(new Intent (this,HelpActivity.class));
        finish();
    }

    class Madapter extends BaseAdapter {

        @Override
        public int getCount() {
            // 条目的总数 文字组数 == 图片张数
            return mProductName.length;
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
               tv_price.setText("货道["+mProductChnanel[position]+"]:"+mProductPrice[position]+"元");
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
            startActivity(new Intent(GoodsActivity.this,MainActivity.class));
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
    @Override
    public void finish(){
        if (time!=null)
            time.cancel();
        super.finish();
    }

}
