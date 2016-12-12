package com.tenray.coolmall.util;

import com.tenray.coolmall.entity.Advertise;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by en on 2016/9/24.
 */
public class JSONUtils {

    // {"id": 2,"url": "菠萝","time": 29.11,"timeRange": 29.11,"area": "菠萝"},
    // {"id": 3,"url": "苹果","time": 16.24,"timeRange": 29.11,"area": "菠萝"}]}
    public static boolean updateAdvertise=false;//是否有新的数据需要更新
    public static boolean isStop=true;
    public static int k=0;
    public  static List<Advertise> jsonList(){

      List<Advertise> listjson=  new ArrayList<Advertise>();
        try {
            URL url = new URL("http://www.zhn.party/picinfo.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             //请求超时
             connection.setConnectTimeout(2000);
            //读取超时
             connection.setReadTimeout(2000);
            int responseCode = connection.getResponseCode();
          //  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");// 时间格式
            if (responseCode == 200) {
                InputStream inputStream = connection.getInputStream();
                String jsonData = StreamUtil.streamToString(inputStream);
                Advertise advertise=null;
                JSONArray jsonObjs = new JSONObject(jsonData).getJSONArray("picinfo");
                for (int i = 0; i < jsonObjs.length(); i++) {

                    JSONObject temp = (JSONObject) jsonObjs.get(i);
                    advertise = new Advertise();
                    advertise.setId(temp.getInt("id"));//id

                    advertise.setURl(temp.getString("url"));//图片地址

                    advertise.setTime(temp.getInt("time"));

                    //advertise.setTimeRange(sdf.parse(temp.getString("timeRange")));//时间段

                    //advertise.setArea(temp.getString("area"));//地域

                    listjson.add(advertise);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listjson;
    }

    public static List<Advertise> advertiseList;

    public static List<Advertise> getAdvertiseList() {
        if (advertiseList==null)
            parseListURL();
        return advertiseList;
    }

    public static void setAdvertiseList(List<Advertise> advertiseList) {
        JSONUtils.advertiseList = advertiseList;
    }

    public static void parseListURL()
    {
        new Thread() {
            public void run() {
                advertiseList= JSONUtils.jsonList();
                for (Advertise advertise:advertiseList) {
                    //System.out.println(advertise.toString());
                }
            }
        }.start();

    }

    public static void parseupdateListURL()
    {
        new Thread() {
            public void run() {
                advertiseList= JSONUtils.jsonList();
                updateAdvertise=true;
                for (Advertise advertise:advertiseList) {
                   // System.out.println(advertise.toString());
                }
            }
        }.start();

    }
}
