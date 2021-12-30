package com.tang.commodityadmin.entity;

/*
 *文件名: OrderDetail
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/19 13:38
 *描述: 这是一个示例
 */
public class OrderDetail {
    String no;
    String orderNo;
    String goodsNo;
    int number;
    double amount;
    double price;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "no='" + no + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", goodsNo='" + goodsNo + '\'' +
                ", number=" + number +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
}

