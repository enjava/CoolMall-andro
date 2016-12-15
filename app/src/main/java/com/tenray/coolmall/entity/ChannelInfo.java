package com.tenray.coolmall.entity;

import java.util.Set;

/**
 * Created by en on 2016/12/1.
 */

public class ChannelInfo {
    private String name;//货道名
    private int price;//价格
    private String productName;//商品名
    private int stock;//库存
    private int volume;//容量
    private long id;//商品ID
    private String  productImage;//商品图片

    public ChannelInfo(Set<String> sets) {
        if (sets==null)
            return;
        for (String set:sets){
            if(set.indexOf("price:")!=-1)
                this.price= Integer.parseInt(set.replace("price:",""));
            if(set.indexOf("channelname:")!=-1)
                this.name=set.replace("channelname:","");
            if(set.indexOf("stock:")!=-1)
                this.stock= Integer.parseInt(set.replace("stock:",""));
            if(set.indexOf("volume:")!=-1)
                this.volume= Integer.parseInt(set.replace("volume:",""));
            if(set.indexOf("id:")!=-1)
                this.id=Long.parseLong(set.replace("id:",""));
            if(set.indexOf("image:")!=-1)
                this.productImage=set.replace("image:","");
            if(set.indexOf("productname:")!=-1)
                this.productName=set.replace("productname:","");
        }
    }

    public ChannelInfo() {
        super();
    }

    public ChannelInfo(String name, int price, String productName, int stock, int volume, long id, String productImage) {
        this.name = name;
        this.price = price;
        this.productName = productName;
        this.stock = stock;
        this.volume = volume;
        this.id = id;
        this.productImage = productImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @Override
    public String toString() {
        return "ChannelInfo{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", productName='" + productName + '\'' +
                ", stock=" + stock +
                ", volume=" + volume +
                ", id=" + id +
                ", productImage='" + productImage + '\'' +
                '}';
    }
}
