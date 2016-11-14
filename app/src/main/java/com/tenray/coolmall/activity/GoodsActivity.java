package com.tenray.coolmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenray.coolmall.R;

/**
 * Created by en on 2016/11/9.
 */

public class GoodsActivity extends Activity {
    private  GridView gv_home;
    private String[] mProductName=new String[]{"可口可乐(罐装)","和其正凉茶(罐装)","达利园杏仁露","红牛功能饮料","王老吉凉茶",
            "百事可乐","可口可乐330ml", "美汁源爽粒葡萄","统一绿茶","哇哈哈营养快线"};
    private String [] mProductPrice=new String[]{"2.5元","3.0元","2.5元","5.5元","3.0元",
            "2.5元","2.5元","3.0元","3.0元","3.5元"};
    private int[] mProductID = new int[]{R.mipmap.p1,R.mipmap.p2,R.mipmap.p3,R.mipmap.p4,R.mipmap.p5,R.mipmap.p6,R.mipmap.p7,
            R.mipmap.p8,R.mipmap.p9,R.mipmap.p10};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止息屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
           tv_price.setText(mProductPrice[position]);
           iv_icon.setBackgroundResource(mProductID[position]);
           return view;
        }
    }

    public void onCliceHelp(View v){

        startActivity(new Intent (this,HelpActivity.class));
    }


}
