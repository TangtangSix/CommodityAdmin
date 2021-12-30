package com.tang.commodityadmin.entity;

import java.io.Serializable;

/*
 *文件名: Client
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/19 13:36
 *描述: 这是一个示例
 */
public class Client implements Serializable {
    String no;
    String name;
    String telephone;
    String password;
    String address;
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public String toString() {
        return "Client{" +
                "no='" + no + '\'' +
                ", name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

