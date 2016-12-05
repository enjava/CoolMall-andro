package com.tenray.coolmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenray.coolmall.R;
import com.tenray.coolmall.util.CountDownTimer;
import com.tenray.coolmall.util.QRCodeUtil;

/**
 * Created by en on 2016/11/10.
 */

public class BuyActivity extends Activity {

    private ImageView mAlipay;
    private ImageView mWeixin;
    private ImageView mXianjin;
    private ImageView mZhifu5;
    private ImageView mZhifu2;
    private ImageView mQRCode;
    private ImageView mProductIV;
    private ImageView mXianjin1;
    private ImageView mXianjin2;
    private Button mReback;
    private TimeCount time;

    private String[] mProductNames = new String[]{
            "利群", "七匹狼", "口水娃", "红牛功能饮料", "王老吉凉茶",
            "百事可乐", "可口可乐330ml", "美汁源爽粒葡萄", "统一绿茶", "哇哈哈营养快线",
            "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线",
            "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线", "哇哈哈营养快线",
            "利群", "七匹狼", "口水娃", "红牛功能饮料", "王老吉凉茶",
            "百事可乐", "可口可乐330ml", "美汁源爽粒葡萄", "统一绿茶", "哇哈哈营养快线"
    };
    private String[] mProductChnanels = new String[]{
            "A1", "A3", "A5", "A7", "B1",
            "B2", "B3", "B4", "B5", "C1",
            "C2", "C3", "C4", "C5", "C6",
            "C7", "C8", "D6", "E3", "E5",
            "C2", "C3", "C4", "C5", "C6",
            "C7", "C8", "D6", "E3", "E5"
    };
    private String[] mProductPrices = new String[]{
            "2.5", "3.0", "2.5", "5.5", "3.0",
            "2.5", "2.5", "3.0", "3.0", "3.5",
            "3.5", "3.5", "3.5", "3.5", "3.5",
            "3.5", "3.5", "3.5", "3.5", "3.5",
            "3.5", "3.5", "3.5", "3.5", "3.5",
            "3.5", "3.5", "3.5", "3.5", "3.5"
    };
    private int[] mProductIDs = new int[]{
            R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5,
            R.mipmap.p6, R.mipmap.p7, R.mipmap.p8, R.mipmap.p9, R.mipmap.p10,
            R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5,
            R.mipmap.p6, R.mipmap.p7, R.mipmap.p8, R.mipmap.p9, R.mipmap.p10,
            R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5,
            R.mipmap.p6, R.mipmap.p7, R.mipmap.p8, R.mipmap.p9, R.mipmap.p10
    };
    //选择的位置
    private int position;

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
        setContentView(R.layout.activity_buy);
        Intent intent = this.getIntent();
        position = intent.getIntExtra("position", 0);
        init();
        initData();
    }

    private void initData() {
        Bitmap bm = QRCodeUtil.createBitmap("http://www.coolmall.cc/");
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
        Bitmap QRCode = QRCodeUtil.addLogo(bm, logo);
        mQRCode.setImageBitmap(QRCode);
    }

    public void init() {
        //支付宝
        mAlipay = (ImageView) findViewById(R.id.imageView7);
        //微信
        mWeixin = (ImageView) findViewById(R.id.imageView6);
        //现金
        mXianjin = (ImageView) findViewById(R.id.imageView5);
        //zhifubao5
        mZhifu5 = (ImageView) findViewById(R.id.imageView4);
        //zhifubao2
        mZhifu2 = (ImageView) findViewById(R.id.imageView3);
        //支付二维码
        mQRCode = (ImageView) findViewById(R.id.imageView2);
        //商品图片
        mProductIV = (ImageView) findViewById(R.id.imageView1);
        mProductIV.setImageResource(mProductIDs[position]);
        //现金图片1
        mXianjin1 = (ImageView) findViewById(R.id.xianjin1);
        //现金图片2
        mXianjin2 = (ImageView) findViewById(R.id.xianjin2);
        mReback = (Button) findViewById(R.id.reback);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        time.start();//开始计时
        //商品名称
        TextView mProductName = (TextView) findViewById(R.id.productName);
        TextView mProductChannel = (TextView) findViewById(R.id.productChannel);
        TextView mProductPrice = (TextView) findViewById(R.id.productPrice);
        mProductName.setText(mProductNames[position]);
        mProductChannel.setText("货道:" + mProductChnanels[position]);
        mProductPrice.setText(mProductPrices[position] + "元");
        mXianjin2.setVisibility(View.INVISIBLE);
        mXianjin1.setVisibility(View.INVISIBLE);
    }

    //微信扫码支付
    public void onWeiXin(View view) {
        mZhifu5.setVisibility(View.VISIBLE);
        mZhifu2.setVisibility(View.VISIBLE);
        mXianjin2.setVisibility(View.INVISIBLE);
        mXianjin1.setVisibility(View.INVISIBLE);
        mWeixin.setImageResource(R.mipmap.weixin3);
        mAlipay.setImageResource(R.mipmap.zhifubao4);
        mXianjin.setImageResource(R.mipmap.xianjin4);
        mZhifu5.setImageResource(R.mipmap.weixin5);
        mZhifu2.setImageResource(R.mipmap.weixin2);
    }

    //支付宝扫码支付
    public void onAlipay(View view) {
        mXianjin2.setVisibility(View.INVISIBLE);
        mXianjin1.setVisibility(View.INVISIBLE);
        mZhifu5.setVisibility(View.VISIBLE);
        mZhifu2.setVisibility(View.VISIBLE);
        mWeixin.setImageResource(R.mipmap.weixin4);
        mAlipay.setImageResource(R.mipmap.zhifubao3);
        mXianjin.setImageResource(R.mipmap.xianjin4);
        mZhifu5.setImageResource(R.mipmap.zhifubao5);
        mZhifu2.setImageResource(R.mipmap.zhifubao2);
    }

    //现金购买
    public void onXianjin(View view) {
        mZhifu5.setVisibility(View.INVISIBLE);
        mZhifu2.setVisibility(View.INVISIBLE);
        mXianjin2.setVisibility(View.VISIBLE);
        mXianjin1.setVisibility(View.VISIBLE);
        mWeixin.setImageResource(R.mipmap.weixin4);
        mAlipay.setImageResource(R.mipmap.zhifubao4);
        mXianjin.setImageResource(R.mipmap.xianjin3);
        mXianjin2.setImageResource(R.mipmap.xianjin2);
        mXianjin1.setImageResource(R.mipmap.xianjin1);
    }

    public void onReback(View view) {
        time.cancel();
        startActivity(new Intent(this, GoodsActivity.class));
        finish();
    }

    public void onOutGoods(View view) {
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(BuyActivity.this, MainActivity.class));
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mReback.setText(millisUntilFinished / 1000 + "秒后返回");
        }

    }

}
