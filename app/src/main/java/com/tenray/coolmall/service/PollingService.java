package com.tenray.coolmall.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.TextUtils;

import com.tenray.coolmall.application.MyApplication;
import com.tenray.coolmall.serialport.FrameOrder;
import com.tenray.coolmall.serialport.FrameUtil;

import java.util.List;

import android_serialport_api.SerialPort;


/**
 * Created by en on 2016/11/26.
 * 轮询服务
 */

public class PollingService extends Service {
    public static final String ACTION = "com.tenray.coolmall.service.PollingService";
   private List<String> channels;
    private int listSize=-1;
    private static int rollTimes = FrameUtil.nextInt();
    private MyApplication myApplication;
    private  SerialPort serialPort=null;
    private PollReceiver pollReceiver;  //广播实例
    @Override
    public IBinder onBind(Intent intent) {
        myApplication.log("PollingService-onBind");
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        myApplication = (MyApplication) getApplication();
        myApplication.log("PollingService-onCreate");
        // 注册广播接收
        pollReceiver = new PollReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("tenray.outgoods.success");    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(pollReceiver, filter);
    }
    @Override
    public boolean onUnbind(Intent intent)
    {
       unregisterReceiver(pollReceiver);
        myApplication.log("PollingService-onUnbind");
        System.out.println("Service:onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new PollingThread().start();
        //myApplication.log("PollingService-onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        myApplication.log("PollingService-onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        myApplication.log("PollingService-stopService");
        return super.stopService(name);
    }

    //初始化通知栏配置
    private void initNotifiManager() {

    }
    private void sendRollCommand() {
        if (serialPort==null)
        {
            if (myApplication.serialport!=null)
                serialPort=myApplication.serialport.getmSerialPort();
            return;
        }
        rollTimes++;
        if (rollTimes > 65500)
            rollTimes = 0;
        try {
            if (listSize==-1||channels.size()>(listSize+1)){
                channels=  myApplication.getChannels();
                listSize++;
                if (channels!=null&&channels.size()>0){
                    byte[] bytes = FrameOrder.getBytesPanel(rollTimes,channels.get(listSize));
                    myApplication.sendToPort(bytes, "30");
                }
            }
            else {
                    //发送交易数据给主板
                   if (rollTimes%20==0) {
                       if (!myApplication.isSocketConnect())
                           myApplication.initWebSocketClient();
                   }
                    byte[] bytes = FrameOrder.getBytesTradeDate(rollTimes);
                    myApplication.sendToPort(bytes, "36");
            }
        } catch (Exception e) {
            System.out.println("aaaaaaaaaaaaaaaaaaa");
        }
    }

    /**
     * Polling thread
     * 模拟向Server轮询的异步线程
     *
     * @Author en
     * @Create 2016-11-26 上午 09:18:34
     */
    long count = 0;

    class PollingThread extends Thread {
        @Override
        public void run() {
            sendRollCommand();
            count++;
            //当除计数能被5整时弹出通知
            if (count % 50 == 0) {
                System.out.println("New message!" + count);
            }
        }
    }


    public  class PollReceiver extends BroadcastReceiver//作为内部类的广播接收者
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals("tenray.outgoods.success"))
            {
                String data = intent.getStringExtra("tradedata");
                if (!TextUtils.isEmpty(data)&&data.indexOf("流程号")!=-1) {
                    System.out.println("PollingService:" + data);
                    myApplication.log(data);
                }
            }
        }
    }
}
