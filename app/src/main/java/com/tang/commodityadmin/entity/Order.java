package com.tang.commodityadmin.entity;

import java.io.Serializable;

/*
 *文件名: Order
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/19 13:37
 *描述: 这是一个示例
 */
public class Order implements Serializable {
    String no;
    String clientNo;
    String date;
    double amount;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "no='" + no + '\'' +
                ", clientNo='" + clientNo + '\'' +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                '}';
    }
}

