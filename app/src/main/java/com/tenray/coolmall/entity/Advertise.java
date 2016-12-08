package com.tenray.coolmall.entity;

import java.util.Date;

/**
 * Created by en on 2016/9/24.
 * 广告信息bean
 */
public class Advertise {
     // {"picupdate": [{"id": 1,"url": "香蕉","time": 228.45,"url": "菠萝"},
     // {"id": 2,"url": "菠萝","time": 29.11,"timeRange": 29.11,"area": "菠萝"},
     // {"id": 3,"url": "苹果","time": 16.24,"timeRange": 29.11,"area": "菠萝"}]}
    private int id;//id
    private String URl;//图片地址
    private int time;//轮播时间  单位秒

    private Date timeRange;//时间段
    private String area;//区域

    public Advertise(int id, String URl, int time, Date timeRange, String area) {
        this.id = id;
        this.URl = URl;
        this.time = time;
        this.timeRange = timeRange;
        this.area = area;
    }

    public Advertise() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getURl() {
        return URl;
    }

    public void setURl(String URl) {
        this.URl = URl;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Date getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(Date timeRange) {
        this.timeRange = timeRange;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "id="+id+"url:"+URl+"time:"+time;
    }
}
