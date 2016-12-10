package com.tenray.coolmall.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by en on 2016/12/6.
 */

public class MyWebSocketClient extends WebSocketClient {
    public static MyWebSocketClient myWebSocketClient;
    public OnReceiveWebSocketMessage onReceiveWebSocketMessage;
    private String appKey;
    public MyWebSocketClient(URI serverUri, Draft draft, String appKey) {
        super(serverUri, draft);
        this.appKey = appKey;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println(handshakedata.toString() + "  handshakedata.getHttpStatusMessage()" + handshakedata.getHttpStatusMessage());
        System.out.println("打开链接");
        try {
            sendMsg("onOpenAppKey=" + appKey);
        } catch (Exception e) {

        }

    }

    @Override
    public void onMessage(String message) {
        if (onReceiveWebSocketMessage != null)
            onReceiveWebSocketMessage.receive(message);
        System.out.println("接受到的消息" + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us")+"code:"+code);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("EEEEEEEEEEEEEEE:"+ex.getMessage());
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
            System.out.println("ws://120.24.172.102:8000/websocket" + e.getMessage());
        }
        return myWebSocketClient;

    }

    public void sendMsg(String msg) {
        if (myWebSocketClient != null && isOpen())
            myWebSocketClient.send(msg);
        else
            System.out.println("myWebSocketClient=null");
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