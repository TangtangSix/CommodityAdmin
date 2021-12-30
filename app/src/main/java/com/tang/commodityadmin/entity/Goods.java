package com.tang.commodityadmin.entity;

/*
 *文件名: Goods
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/18 17:07
 *描述: 这是一个示例
 */

import java.io.Serializable;

public class Goods implements Serializable {
    String no;//编号
    String brandName;//品牌名称
    String name;//名称
    String unit;//单位
    double sellingPrice;//销售价
    double costPrice;//成本价
    String introduction;//商品介绍
    int stock;//库存
    String category;//分类

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "no='" + no + '\'' +
                ", brandName='" + brandName + '\'' +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", sellingPrice=" + sellingPrice +
                ", costPrice=" + costPrice +
                ", introduction='" + introduction + '\'' +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                '}';
    }
}
