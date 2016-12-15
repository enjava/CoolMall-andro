package com.tenray.coolmall.websocket;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by en on 2016/12/6.
 */

public class MyWebSocketClient extends WebSocketClient {
    private static final String tag= MyWebSocketClient.class.getSimpleName();
    public static MyWebSocketClient myWebSocketClient;
    public OnReceiveWebSocketMessage onReceiveWebSocketMessage;
    private String appKey;
    public MyWebSocketClient(URI serverUri, Draft draft, String appKey) {
        super(serverUri, draft);
        this.appKey = appKey;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i(tag,"打开链接onOpen");
        sendMsg("onOpenAppKey=" + appKey);

    }

    @Override
    public void onMessage(String message) {
        if (onReceiveWebSocketMessage != null)
            onReceiveWebSocketMessage.receive(message);
        Log.i(tag,"接受到的消息" + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i(tag,"Connection closed by " + (remote ? "remote peer " : "us")+"code:"+code);
    }

    @Override
    public void onError(Exception ex) {
        Log.i(tag,"EEEEEEEEEEEEEEE:"+ex.getMessage());
        ex.printStackTrace();
    }
     public static boolean isOpen(){
         if (myWebSocketClient==null)
             return false;
        return myWebSocketClient.getConnection().isOpen();
    }
    public static boolean isClosed(){
        if (myWebSocketClient==null)
            return false;
        return myWebSocketClient.getConnection().isClosed();
    }
    public static MyWebSocketClient initClient(String appKey) {
     if (myWebSocketClient != null&&isOpen()&&!isClosed())
          return myWebSocketClient;
        try {
            myWebSocketClient = new MyWebSocketClient(new URI("ws://120.24.172.102:8000/websocket"), new Draft_17(), appKey);
            myWebSocketClient.connect();
        } catch (Exception e) {
            Log.i(tag,"ws://120.24.172.102:8000/websocket" + e.getMessage());
        }
        return myWebSocketClient;

    }

    public void sendMsg(String msg) {
        if (myWebSocketClient != null && isOpen())
            myWebSocketClient.send(msg);
        else
            Log.i(tag,"myWebSocketClient=null");
    }

    //断开连接
    public void closeConnect() {
        try {
            myWebSocketClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myWebSocketClient = null;
        }

    }
    private boolean isStop = true;

    public void setOnReceiveWebSocketMessage(OnReceiveWebSocketMessage onReceiveWebSocketMessage) {
        this.onReceiveWebSocketMessage = onReceiveWebSocketMessage;
    }
}