package com.tang.commodityadmin;

/*
 *文件名: Constant
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/23 9:52
 *描述: 这是一个示例
 */
public interface Constants {
//     String BASE_URL= "http://192.168.43.115:8080";//服务器ip
     String BASE_URL= "http://47.108.251.118:8081";//服务器ip
     int PHOTO_REQUEST_CAMERA = 1;// 拍照

     int CROP_PHOTO = 2;// 裁剪
     int ARRAY_PHOTO = 3;// 相册选取

     String  unit[]={"克","千克","升","厘米","平方厘米","平方米","毫升","米"};
     String  category[]={"女装","家居厨具","手机","母婴童装","电脑办公","男装","箱包","运动户外","食品酒饮"};


     int ADD_GOODS=1;
     int EDIT_GOODS=2;


}
