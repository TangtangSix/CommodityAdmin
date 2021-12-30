package com.tang.commodityadmin.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tang.commodityadmin.R;

/*
 *文件名: LoadProgressDialog
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/26 22:15
 *描述: 这是一个示例
 */



import android.util.Log;



public class LoadingDialog extends Dialog {

    private static final String TAG = "LoadingDialog";

    private int mImageId;
    private boolean mCancelable;
    private RotateAnimation mRotateAnimation;

    public LoadingDialog(@NonNull Context context, int imageId) {
        this(context,R.style.LoadingDialog,imageId,false);
    }

    public LoadingDialog(@NonNull Context context, int themeResId, int imageId, boolean cancelable) {
        super(context, themeResId);
        mImageId = imageId;
        mCancelable = cancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_loading);
        // 设置窗口大小
        WindowManager windowManager = getWindow().getWindowManager();
        int screenWidth = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            screenWidth = windowManager.getCurrentWindowMetrics().getBounds().width();
        }
        else {
            screenWidth=windowManager.getDefaultDisplay().getWidth();
        }
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        // 设置窗口背景透明度
        //attributes.alpha = 0.3f;
        // 设置窗口宽高为屏幕的5分之一
        attributes.width = screenWidth/5;
        attributes.height = attributes.width;
        getWindow().setAttributes(attributes);
        setCancelable(mCancelable);

        ImageView dialog_image = findViewById(R.id.dialog_image);

        dialog_image.setImageResource(mImageId);
        // 先对imageView进行测量，以便拿到它的宽高（否则getMeasuredWidth为0）
        dialog_image.measure(0,0);
        mRotateAnimation = new RotateAnimation(0,360,dialog_image.getMeasuredWidth()/2,dialog_image.getMeasuredHeight()/2);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        //时间
        mRotateAnimation.setDuration(2000);
        mRotateAnimation.setRepeatCount(-1);
        dialog_image.startAnimation(mRotateAnimation);
    }

    @Override
    public void dismiss() {
        mRotateAnimation.cancel();
        super.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            // 屏蔽返回键
            return mCancelable;
        }
        return super.onKeyDown(keyCode, event);
    }
}

