package com.tenray.coolmall.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenray.coolmall.R;
import com.tenray.coolmall.application.MyApplication;
import com.tenray.coolmall.entity.ChannelInfo;
import com.tenray.coolmall.serialport.FrameOrder;
import com.tenray.coolmall.util.CountDownTimer;
import com.tenray.coolmall.util.QRCodeUtil;
import com.tenray.coolmall.util.ToastUtil;
import com.tenray.coolmall.websocket.OnReceiveMessage;

import java.util.Map;

import static com.tenray.coolmall.R.id.productName;

/**
 * Created by en on 2016/11/10.
 */

public class BuyActivity extends Activity {
    private ImageView mZhifu5;
    private ImageView mZhifu2;
    private ImageView mQRCode;
    private ImageView mProductIV;
    private ImageView mXianjin1;
    private ImageView mXianjin2;
    private TextView mTextv;
    private Button mReback;
    private TimeCount time;
    private MyApplication myApplication;
    private Map<String ,ChannelInfo> channelInfos;
    private  MyReceiver myReceiver;
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
    //选择的位置
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication= (MyApplication) this.getApplication();
        channelInfos=myApplication.getChannelInfos();
        myApplication.setOnReceiveMessage(new MyOnReceiveMessage());
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
        // 注册广播接收
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("tenray.outgoods.success");    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(myReceiver, filter);


    }
   //支付宝
    private void initData() {
        if(bmal==null&&!TextUtils.isEmpty(QRurl)) {
             bmal = QRCodeUtil.createBitmap(QRurl);
            //Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
            //Bitmap QRCode = QRCodeUtil.addLogo(bm, bm);
            mQRCode.setVisibility(View.VISIBLE);
            mTextv.setText("");
            mQRCode.setImageBitmap(bmal);
        }  else  if(bmal!=null) {
            mQRCode.setVisibility(View.VISIBLE);
            mTextv.setText("");
            mQRCode.setImageBitmap(bmal);
        }else {
            mQRCode.setVisibility(View.INVISIBLE);
        }
    }
    private Bitmap bmal;
    private  Bitmap bmwx;
    //微信初始化
    private void initData2() {
        if(bmwx==null&&!TextUtils.isEmpty(WXQRurl)) {
            bmwx = QRCodeUtil.createBitmap(WXQRurl);
            mQRCode.setVisibility(View.VISIBLE);
            mTextv.setText("");
            mQRCode.setImageBitmap(bmwx);
        }
        else  if(bmwx!=null) {
            mQRCode.setVisibility(View.VISIBLE);
            mTextv.setText("");
            mQRCode.setImageBitmap(bmwx);
        }else {
            mQRCode.setVisibility(View.INVISIBLE);
        }
    }
    private  String  mChannel="";
    private int  totalMoney;
    public void init() {

        //zhifubao5
        mZhifu5 = (ImageView) findViewById(R.id.imageView4);
        //zhifubao2
        mZhifu2 = (ImageView) findViewById(R.id.imageView3);
        //支付二维码
        mQRCode = (ImageView) findViewById(R.id.imageView2);
        //生产二维码状态
        mTextv = (TextView) findViewById(R.id.textv2);
        //商品图片
        mProductIV = (ImageView) findViewById(R.id.imageView1);
        mProductIV.setImageResource(mProductID[position]);
        //现金图片1
        mXianjin1 = (ImageView) findViewById(R.id.xianjin1);
        //现金图片2
        mXianjin2 = (ImageView) findViewById(R.id.xianjin2);
        mReback = (Button) findViewById(R.id.reback);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        time.start();//开始计时
        //商品名称
        TextView mProductNames = (TextView) findViewById(productName);
        TextView mProductChannel = (TextView) findViewById(R.id.productChannel);
        TextView mProductPrice = (TextView) findViewById(R.id.productPrice);
        mProductNames.setText(mProductName[position]);
        mProductChannel.setText("货道:" + mProductChnanel[position]);
        money=channelInfos.get(mProductChnanel[position]).getPrice();
        money=money/100;
        System.out.println("+channelInfos.get(mProductChnanel[position]).getPrice():"+channelInfos.get(mProductChnanel[position]).getPrice());
        mProductPrice.setText( money+ "元");
        mZhifu5.setVisibility(View.INVISIBLE);
        mZhifu2.setVisibility(View.INVISIBLE);
        mXianjin2.setVisibility(View.VISIBLE);
        mXianjin1.setVisibility(View.VISIBLE);
        mQRCode.setVisibility(View.INVISIBLE);
        mXianjin2.setImageResource(R.mipmap.xianjin2);
        mXianjin1.setImageResource(R.mipmap.xianjin1);
        //发送投币指令
        totalMoney=(int)(money*100);
        mChannel=mProductChnanel[position];
        byte[] bytes= FrameOrder.getBytesOutGoods(myApplication.spFrameNumber(),mChannel,totalMoney,0);
        myApplication.sendToPort(bytes,"34");

    }

    private double money;
    //微信扫码支付
    public void onWeiXin(View view) {
        if (mtbot)
            return;
        mZhifu5.setVisibility(View.VISIBLE);
        mZhifu2.setVisibility(View.VISIBLE);
        mXianjin2.setVisibility(View.INVISIBLE);
        mXianjin1.setVisibility(View.INVISIBLE);
        mZhifu5.setImageResource(R.mipmap.weixin5);
        mZhifu2.setImageResource(R.mipmap.weixin2);
        if (TextUtils.isEmpty(WXQRurl))
            createQR("requset_wxqr_code&");
        else
           // initData2
        initData2();
    }

    //支付宝扫码支付
    public void onAlipay(View view) {
        if (mtbot)
            return;
        mXianjin2.setVisibility(View.INVISIBLE);
        mXianjin1.setVisibility(View.INVISIBLE);
        mZhifu5.setVisibility(View.VISIBLE);
        mZhifu2.setVisibility(View.VISIBLE);
        mZhifu5.setImageResource(R.mipmap.zhifubao5);
        mZhifu2.setImageResource(R.mipmap.zhifubao2);
        if (TextUtils.isEmpty(QRurl))
            createQR("requset_qr_code&");
        else
          initData();

    }
    //向服务器请求二维码
    private void createQR(String requset_qr_code){
        //String requset_qr_code="requset_qr_code&";
        String productName=mProductName[position];
        String totalMoney = money+"";
        String channel=mProductChnanel[position];
        String str=requset_qr_code+productName+"&"+totalMoney+"&"+channel;
        if (myApplication.isSocketConnect())
            myApplication.sendMsg(str);
        else {
            Message msg = Message.obtain();
            msg.what = 404;
            mHandler.sendMessage(msg);
        }

    }
    //现金购买
    public void onXianjin(View view) {
        if (mtbot)
            return;
        mZhifu5.setVisibility(View.INVISIBLE);
        mZhifu2.setVisibility(View.INVISIBLE);
        mXianjin2.setVisibility(View.VISIBLE);
        mXianjin1.setVisibility(View.VISIBLE);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);

    }

    public void kubi(View view) {
        ToastUtil.show(this,"酷比购买正在开发中,近期将会上线,谢谢您的关注!");
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
    //支付宝连接
    private  String QRurl="";
    //微信连接
    private  String WXQRurl="";
    private class MyOnReceiveMessage implements OnReceiveMessage {
        @Override
        public void receive(String message) {
            Message msg=Message.obtain();
            System.out.println("MyOnReceiveMessage1:"+message);
             if (message.equals("outGoods"))
                 msg.what=10;
            else if (message.indexOf("response_qr_code=")==0){
               QRurl=message.replace("response_qr_code=","");
               if(QRurl.indexOf("http")==-1)
                   msg.what=404;
               else
                   msg.what=1;
           }
             else if (message.indexOf("response_wxqr_code=")==0){
                 WXQRurl=message.replace("response_wxqr_code=","");
                 if(WXQRurl.indexOf("weixin")==-1)
                     msg.what=404;
                 else
                     msg.what=2;
             }
            else if (message.indexOf("trade_status=WAIT_BUYER_PAY")==0)
           {
               System.out.println("MyOnReceiveMessage2:"+message);
               msg.what=9;
           }
            mHandler.sendMessage(msg);
        }
    }
    public  class MyReceiver extends BroadcastReceiver//作为内部类的广播接收者
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if ("tenray.outgoods.success".equals(action))
            {

                String data = intent.getStringExtra("tradedata");
                System.out.println("BroadcastReceiverBuyActivity:"+data);
                if (!TextUtils.isEmpty(data)&&data.indexOf("流程号")!=-1) {
                    Message msg = Message.obtain();
                    msg.what = 11;
                    mHandler.sendMessage(msg);

                    System.out.println("PollingService:" + data);
                    Message msg2 = Message.obtain();
                    msg2.what = 12;
                    mHandler.sendMessageDelayed(msg2, 5000);
                }
            }
        }
    }
    private  boolean mtbot=false;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
               case 0:
                    break;
                case 1:
                    initData();
                    //生产支付二维码
                    break;
                case 2:
                    initData2();
                    //生产支付二维码
                    break;
                case 404:
                    mTextv.setText("网络异常...");
                    //发送失败
                    break;
                case 9:
                    //扫码成功等待支付
                    mQRCode.setVisibility(View.INVISIBLE);
                    mTextv.setText("扫码成功等待支付...");
                    break;
                case 10:
                    //正在出货
                    mQRCode.setVisibility(View.INVISIBLE);
                    mtbot=true;
                    mTextv.setText("支付成功,正在出货...");
                    break;
                case 12:
                    time.cancel();
                    startActivity(new Intent(BuyActivity.this, GoodsActivity.class));
                    finish();
                    break;
                case 11:
                    //正在出货
                    mQRCode.setVisibility(View.INVISIBLE);
                    mtbot=false;
                    mTextv.setText("成功出货,请取货...");
                    break;
            }
        }
    };
}
