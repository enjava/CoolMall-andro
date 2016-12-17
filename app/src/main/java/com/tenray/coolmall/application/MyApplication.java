package com.tenray.coolmall.application;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.tenray.coolmall.entity.ChannelInfo;
import com.tenray.coolmall.serialport.FrameOrder;
import com.tenray.coolmall.serialport.FrameUtil;
import com.tenray.coolmall.serialport.SerialPortUtil;
import com.tenray.coolmall.service.PollingService;
import com.tenray.coolmall.util.CommonUtil;
import com.tenray.coolmall.util.Constants;
import com.tenray.coolmall.util.FileUtils;
import com.tenray.coolmall.util.LogWriterUtil;
import com.tenray.coolmall.util.Md5Util;
import com.tenray.coolmall.util.PollingUtils;
import com.tenray.coolmall.util.SpUtil;
import com.tenray.coolmall.util.ToastUtil;
import com.tenray.coolmall.websocket.MyWebSocketClient;
import com.tenray.coolmall.websocket.OnReceiveMessage;
import com.tenray.coolmall.websocket.OnReceiveWebSocketMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by en on 2016/11/30.
 */

public class MyApplication extends Application {
    private final String tag= getClass().getSimpleName();
    private  MyWebSocketClient mWebSocketClient;

    private  OnReceiveMessage mOnReceiveMessage;
    private SerialPortUtil mSerialport = null;
    private List<String> channels = new ArrayList<>();
    private String returnStr = "";
    private static String backStr = "";
    private LogWriterUtil mLogWriter;
    private String filePath = "";
    private Date clientTime;
    //日志存放路径
    private String logPath = "";
    //广告视频存放路径
    private String videoAdPath = "";
    //广告视频存放路径
    private String imageAdPath = "";
    //商品图片存放路径
    private String imagePath = "";
    //日志文件名
    private String fileName = "";
    private boolean first = true;
    private static String orderCode;
    Map<String,ChannelInfo>  channelInfos =new ConcurrentHashMap<>();
    //流水号 变化说明有成功交易
    private static int mSerialNumber = 0;
    //同步时间
    private static final int iSynTime = 101;
    //测试连接
    private static final int TEST_LINK = 1;
    //轮询主板
    private static final int ROLL_PANEL = 2;
    //获取主板货道数据
    private static final int DATA_PANEL = 30;
    //上位机发送货道数据给主板
    private static final int SYN_DATA_PANEL = 31;
    //上位机控制出货
    private static final int CONTROL_OUT_GOODS = 34;
    //上位机发送交易数据给主板
    private static final int TRADE_DATA_PANEL = 36;
    //上位机同步主板时间
    private static final int SYN_TIME = 37;
    //上位机清除上货事件标志
    private static final int CLEAN_UPGOODS_EVENT = 38;
    //上位机通知主板写货道数据到存储器
    private static final int WRITE_DATA_STORAGE = 39;
    private static String appkey;
    private String comPath;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TEST_LINK:
                    //backStr = "";
                    break;
                case ROLL_PANEL:
                    log("ROLL_PANEL" + returnStr);
                    break;
                case DATA_PANEL:
                    setChannelData();
                    break;
                case SYN_DATA_PANEL:
                    break;
                case CONTROL_OUT_GOODS:
                    Log.i(tag,"CONTROL_OUT_GOODS"+returnStr);
                    break;
                case TRADE_DATA_PANEL:
                    //轮询36命令
                    //分析数据
                    analysisTradeData();
                    break;
                case SYN_TIME:
                    break;
                case CLEAN_UPGOODS_EVENT:
                    //清除上货事件
                    log("CLEAN_UPGOODS_EVENT" + returnStr);
                    break;
                case WRITE_DATA_STORAGE:
                    break;
                case iSynTime:
                    synTime();
                    break;
                default:
                    Log.i(tag,"returnStr"+returnStr);
                    Log.i(tag, "测试" + msg.what);
                    break;
            }
        }
    };
    //分析交易数据(轮询)
    long i36=0;
    private void analysisTradeData() {
        String str = returnStr;
        if (i36%33==0)
            Log.i(tag,str+"i36:"+i36);
        i36++;
        String[] args = str.split(" ");
        if (args.length<24)
            return;
        String[] num = new String[]{args[8], args[9], args[10], args[11]};
        String[] price = new String[]{args[12], args[13], args[14], args[15]};
        String channel = args[16];
        String[] payTypes = new String[]{args[20], args[21], args[22], args[23]};
        if (!"36".equals(args[5]))
            return;
        //流水号
        int number = FrameUtil.hiInt4String(num);
        int mPrice = FrameUtil.hiInt4String(price);
        int payType = FrameUtil.hiInt4String(payTypes);

        if (number != mSerialNumber ) {
            mSerialNumber = number;
            if (first)
            {
                first=false;
                return;
            }
            //发送广播
            Intent intent = new Intent();
            intent.setAction("tenray.outgoods.success");
            String tradedata = "价格:[" + mPrice + "] 流程号:[" + number + "] 货道:[" + channel + "] 支付方式:[" + payType + "]";
            intent.putExtra("tradedata", tradedata);
            Log.i(tag,"普通广播发送前");

            this.sendBroadcast(intent);   //普通广播发送

            Log.i(tag,"普通广播发送后");
            log("tradedata"+tradedata);
        }
        //log(str);
    }
    @Override
    public void onCreate() {
        // 程序创建的时候执行
        Log.d(TAG, "onCreate");
        super.onCreate();
        clientTime=new Date();
        comPath = SpUtil.getString(this, Constants.COM_PATH, "");
        if (TextUtils.isEmpty(comPath)) {
            comPath = "/dev/ttyS2";
            SpUtil.putString(this, Constants.COM_PATH, "/dev/ttyS2");
        }
        if (FileUtils.isSdcardExist()) {
            filePath = Environment.getExternalStorageDirectory() + File.separator + "CoolMall" + File.separator;

            logPath= filePath+  "log" + File.separator;

            videoAdPath=filePath+  "ad" + File.separator+"video"+ File.separator;

            imageAdPath=filePath+  "ad" + File.separator+"image"+ File.separator;

            imagePath=filePath+"product"+ File.separator+"image"+ File.separator;

            FileUtils.createDirFile(logPath);//记录日志
            FileUtils.createDirFile(imageAdPath);//广告图片
            FileUtils.createDirFile(videoAdPath);//广告视频
            FileUtils.createDirFile(imagePath);//商品图片
            fileName = CommonUtil.formatDate("yyyy-MM-dd") + ".Log";
            initLogWriterUtil();

          List<String> lsit= FileUtils. getExtSDCardPath();

            //region Description 获取外置SD卡

            for (String string:lsit){
                Log.i(tag,"获取外置SD卡:"+string);
            }

            // endregion
        }
        //同步时间
        Message msg = Message.obtain();
        msg.what = iSynTime;
        mHandler.sendMessage(msg);
        initChannel();

        PollingUtils.startPollingService(this, 500, PollingService.class, PollingService.ACTION);
  }

    //初始化货道
    public void initChannel() {
        String[] mProductChnanels = new String[]{
                "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8",
                "B1", "B2", "B3", "B4", "B5",
                "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8",
                "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8",
                "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8",
                "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8"
        };
        runChannel(mProductChnanels);
    }
    //线程写货道数据的SP
    public void runChannel(final String[] mProductChnanels) {
        new Thread() {

            @Override
            public void run() {
                appkey=SpUtil.getString(MyApplication.this,Constants.MACHINE_ID,"");
                if (TextUtils.isEmpty(appkey)) {
                    appkey = getMachineID();
                    SpUtil.putString(MyApplication.this, Constants.MACHINE_ID, getMachineID());
                }
                Set<String> channelSet = new HashSet<String>();
                for (int i = 0; i < mProductChnanels.length; i++) {
                    channelSet.add(mProductChnanels[i]);
                    Set<String> sets= SpUtil.getSet(MyApplication.this, mProductChnanels[i], null) ;
                    if (sets==null)
                        channels.add(mProductChnanels[i]);
                    else
                        channelInfos.put(mProductChnanels[i],new ChannelInfo(sets));
                }
                initWebSocketClient();
            }

        }.start();

    }
    public void initWebSocketClient()
    {
        if (!TextUtils.isEmpty(appkey)) {
            mWebSocketClient = MyWebSocketClient.initClient(appkey);
            mWebSocketClient.setOnReceiveWebSocketMessage(new MyOnReceiveWebSocketMessage());
            Log.i(tag,"appkey=" + appkey);
            log("初始化MyWebSocketClient");

        }
    }

    //初始化 log
    public void initLogWriterUtil() {
        File logf = new File(logPath + fileName);
        try {
            mLogWriter = LogWriterUtil.open(logf.getAbsolutePath(), true);
            log("---------程序开始执行-------"+logf.getAbsolutePath());
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d(TAG, "onTerminate");
        super.onTerminate();
        //关闭串口
        if (mSerialport != null)
            mSerialport.closeSerialPort();
        PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
    }

    //初始化串口
    public void initSerialport(String comName) {
        Log.d(TAG, "initSerialport");
        mSerialport = SerialPortUtil.getInstance(comName);
        mSerialport.setOnDataReceiveListener(new MyOnDataReceiveListener());
    }

    public void synTime() {
        if (mSerialport == null) {
            initSerialport(comPath);
        }
        if (mSerialport.getmSerialPort() == null) {
            initSerialport(comPath);
            ToastUtil.show(this, "串口打开失败，请检查串口设置是否正确");
            return;
        }
        int itimes = SpUtil.getInt(this, Constants.FRAME_NUMBER, 0);
        String ml = FrameOrder.getSynTime(itimes, new Date());
        byte[] abc = FrameUtil.hexStringToBytes(FrameUtil.getCRCStr(ml));
        if (itimes > 65000)
            itimes = 0;
        SpUtil.putInt(this, Constants.FRAME_NUMBER, ++itimes);
        try {
            mSerialport.sendToPort(abc);
        } catch (Exception e) {
            ToastUtil.show(this, "更改时间失败");
        }
    }

    public void setComPath(String comPath) {
        this.comPath = comPath;
        SpUtil.putString(this, Constants.COM_PATH, comPath);
        initSerialport(comPath);
    }
    //发送串口数据
    public synchronized boolean sendToPort(byte[] bytes, String order) {
        orderCode = "";
        boolean result = false;
        try {
            if (mSerialport.getmSerialPort() != null) {
                backStr="";
                mSerialport.sendToPort(bytes);
                for (int i=0;i<40;i++) {
                    if (orderCode.equals(order))
                        return true;
                    Thread.sleep(10);
                }

            }
        } catch (Exception e) { }
        return result;
    }

    public void setChannelData() {
        String[] args = returnStr.split(" ");
        Log.i(tag,returnStr);
        if (args[5].equals("30")) {
            String chanelName = args[8];
            String[] prices = new String[]{args[20], args[21], args[22], args[23]};
            String[] stocks = new String[]{args[28], args[29], args[30], args[31]};
            String[] volumes = new String[]{args[12], args[13], args[14], args[15]};
            int price = FrameUtil.hiInt4String(prices);
            int stock = FrameUtil.hiInt4String(stocks);
            int volume = FrameUtil.hiInt4String(volumes);
            Set<String> sets = new HashSet<>();
            sets.add("price:" + price);
            sets.add("stock:" + stock);
            sets.add("volume:" + volume);
            channelInfos.put(chanelName,new ChannelInfo(sets));
            SpUtil.putSet(this, chanelName, sets);
        }
    }

    public int spFrameNumber() {
        int itimes = SpUtil.getInt(this, Constants.FRAME_NUMBER, 0);
        if (itimes > 65000)
            itimes = 0;
        SpUtil.putInt(this, Constants.FRAME_NUMBER, ++itimes);
        return itimes;
    }

    //region
    public void log(final String msg) {
        new Thread(){
            @Override
            public void run() {
                try {
                    if (FileUtils.isSdcardExist()) {
                        String logFileName = CommonUtil.formatDate("yyyy-MM-dd") + ".Log";
                        if (!logFileName.equals(fileName)) {
                            fileName = logFileName;
                            initLogWriterUtil();
                        }
                        mLogWriter.print(msg);
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }.start();

    }
    //endregion

    public List<String> getChannels() {
        return channels;
    }

    // 数据接收
    class MyOnDataReceiveListener implements SerialPortUtil.OnDataReceiveListener {

        @Override
        public void onDataReceive(byte[] buffer, int size) {
            byte[] byt = new byte[size];
            for (int l = 0; l < size; l++) {
                byt[l] = buffer[l];
            }

            String stringBack = FrameUtil.fomatStr16(FrameUtil.bytesToHexString(byt)).toUpperCase() + " ";
            backStr += stringBack;
            if (FrameUtil.checkBack(backStr)) {
                returnStr = backStr.trim();
                backStr = "";
                String[] args = returnStr.split(" ");
                if (args.length > 4) {
                    Message msg = Message.obtain();
                    orderCode = args[5];
                    switch (args[5]) {
                        case "01":
                            //1测试驱动板链接 值0x01
                            break;
                        case "02":
                            //2查询主板状态 值0x02
                            msg.what = ROLL_PANEL;
                            break;
                        case "30":
                            msg.what = DATA_PANEL;
                            //3 获取主板货道数据 值0x30
                            break;
                        case "31":
                            //4 上位机发送货道数据给主板 值0x31
                            break;
                        case "34":
                            msg.what = CONTROL_OUT_GOODS;
                            //5 上位机控制出货 值0x34
                            break;
                        case "36":
                            //6 上位机发送交易数据给主板 值0x36
                            msg.what = TRADE_DATA_PANEL;
                            break;
                        case "37":
                            msg.what = 37;
                            //7 同步时间  值0x37
                            break;
                        case "38":
                            msg.what = CLEAN_UPGOODS_EVENT;
                            //8 上位机清除上货事件标志  值0x38
                            break;
                        case "39":
                            //9 上位机通知主板写货道数据到存储器 值0x39
                            msg.what = WRITE_DATA_STORAGE;
                            break;
                        default:
                            break;
                    }
                    mHandler.sendMessage(msg);
                }
            }

        }
    }


    public String getMachineID(){

        //1
        TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();

        //2
        String m_szDevIDShort = "86" + //we make this look like a valid IMEI

                Build.BOARD.length()%10 +
                Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 +
                Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 +
                Build.HOST.length()%10 +
                Build.ID.length()%10 +
                Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 +
                Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 +
                Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 digits
        //3
        String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //4

        WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

        BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String m_szBTMAC=null;
        if (m_BluetoothAdapter!=null)
            m_szBTMAC = m_BluetoothAdapter.getAddress();
        String md= "szImei:"+szImei+"m_szDevIDShort:"+m_szDevIDShort+"m_szAndroidID:"+m_szAndroidID+"m_szWLANMAC:"+m_szWLANMAC+"m_szBTMAC:"+m_szBTMAC;
        return  Md5Util.getMachineID(md);
    }

    public Map<String, ChannelInfo> getChannelInfos() {
        return channelInfos;
    }


    public void  sendMsg(String msg) {
        clientTime=new Date();
        mWebSocketClient.sendMsg(msg);
    }

   class MyOnReceiveWebSocketMessage implements OnReceiveWebSocketMessage{

       @Override
       public void receive(String message) {
           Log.i(tag,"MyOnReceiveWebSocketMessage1:"+message);
           if (message.indexOf("trade_status=TRADE_SUCCESS&")==0){
               //支付成功
               String msg=message.replace("trade_status=TRADE_SUCCESS&","");
               String []args=msg.split("&");
               int  totalMoney=(int)(Double.parseDouble(args[2])*100);
               String  channel=args[3];
               String payType=args[4];
               byte[] bytes=FrameOrder.getBytesOutGoods(spFrameNumber(),channel,totalMoney,Integer.parseInt(payType));
               if (mOnReceiveMessage!=null)
                   mOnReceiveMessage.receive("outGoods");
               sendToPort(bytes,"34");
           }else {
               if (mOnReceiveMessage!=null) {
                   Log.i(tag,"MyOnReceiveWebSocketMessage2:"+message);
                   mOnReceiveMessage.receive(message);
               }
               else {
                   Log.i(tag,"MyOnReceiveWebSocketMessage3:"+message);
               }
           }
       }
   }

    public static String getAppkey() {
        return appkey;
    }

    public SerialPortUtil getmSerialport() {
        return mSerialport;
    }

    public String getVideoAdPath() {
        return videoAdPath;
    }

    public String getImageAdPath() {
        return imageAdPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isSocketConnect(){
        return mWebSocketClient.isOpen()&&!mWebSocketClient.isClosed();

    }

    public Date getClientTime() {
        return clientTime;
    }

    public void setOnReceiveMessage(OnReceiveMessage onReceiveMessage) {
        this.mOnReceiveMessage = onReceiveMessage;
    }
}
