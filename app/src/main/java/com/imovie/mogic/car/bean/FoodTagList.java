package com.imovie.mogic.car.bean;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class FoodTagList implements Serializable {
    private int id;
    private String createTime;
    private boolean isSelect = false;
    private long goodsId;
    private String name;
    private double packPrice;
    private double price;
    private int quantity;
    private String goodsTags;
    private String imageUrl;
    private int packGroupId;
    private long selectId = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(double packPrice) {
        this.packPrice = packPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getGoodsTags() {
        return goodsTags;
    }

    public void setGoodsTags(String goodsTags) {
        this.goodsTags = goodsTags;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPackGroupId() {
        return packGroupId;
    }

    public void setPackGroupId(int packGroupId) {
        this.packGroupId = packGroupId;
    }

    public long getSelectId() {
        return selectId;
    }

    public void setSelectId(long selectId) {
        this.selectId = selectId;
    }


}


