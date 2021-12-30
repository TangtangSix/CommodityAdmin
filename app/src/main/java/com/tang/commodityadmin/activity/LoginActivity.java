package com.tang.commodityadmin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.tang.commodityadmin.Constants;
import com.tang.commodityadmin.R;
import com.tang.commodityadmin.dialog.LoadingDialog;
import com.tang.commodityadmin.entity.User;
import com.tang.commodityadmin.http.HttpConnect;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button but_login;
    private SharedPreferences loginPreferences;
    private EditText userName;
    private EditText password;
    private CheckBox remember,autoLogin;
    private LoadingDialog loadingDialog;
    private TextChangeListener textChangeListener=new TextChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //获取权限
        if (shouldAskPermissions()) {
            askPermissions();
        }

        initView();


        //获取是否记住密码
        if (loginPreferences.getBoolean("remember", false)) {
            userName.setText(loginPreferences.getString("userName", ""));
            password.setText(loginPreferences.getString("password", ""));
            remember.setChecked(true);
        }

        if (loginPreferences.getBoolean("autoLogin",false)){
            Intent intent = new Intent();
            intent.setClassName("com.tang.commodityadmin", MainActivity.class.getName());
            startActivity(intent);
            finish();
        }
        else {
            autoLogin.setChecked(false);
        }
    }

    private void initView(){
        loadingDialog=new LoadingDialog(this,R.mipmap.icon_loading);
        loginPreferences = getSharedPreferences("login", MODE_PRIVATE);

        userName = findViewById(R.id.userName);
        userName.addTextChangedListener(textChangeListener);

        password = findViewById(R.id.password);
        password.addTextChangedListener(textChangeListener);

        remember = findViewById(R.id.check_remember_me);
        autoLogin =findViewById(R.id.check_auto_login);
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    remember.setChecked(true);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = this.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = this.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }

        but_login = findViewById(R.id.btn_login);
        but_login.setEnabled(false);
        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                login();
            }
        });
    }

    private void login(){
        SharedPreferences.Editor editor = loginPreferences.edit();
        String url = Constants.BASE_URL + "/user/find";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("userName", userName.getText().toString())
                .addParams("password", password.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("LoginActivity", e.getMessage());
                        Toast.makeText(LoginActivity.this,"请求错误",Toast.LENGTH_SHORT);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        User user = JSONObject.parseObject(response, User.class);
                        if (user != null) {
                            if (remember.isChecked()) {
                                editor.putString("userName", userName.getText().toString());
                                editor.putString("password", password.getText().toString());
                                editor.putBoolean("remember", true);
                            } else {
                                editor.putBoolean("remember", false);
                            }
                            editor.putBoolean("autoLogin",autoLogin.isChecked());
                            editor.commit();
                            Intent intent = new Intent();
                            intent.setClassName("com.tang.commodityadmin", MainActivity.class.getName());
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            editor.clear();
                            editor.commit();
                            Toast.makeText(LoginActivity.this, "账号密码错误", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }

    /**
     * 判断版本号是否大于Android6.0
     *
     * @return
     */
    protected boolean shouldAskPermissions() {

        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    /**
     * 获取权限
     */
    protected void askPermissions() {

        String[] permissions = {

                "android.permission.CALL_PHONE", "android.permission.SEND_SMS",
                "android.permission.CAMERA", "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_WIFI_STATE",
                "android.permission.INTERNET",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission..ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.READ_PHONE_STATE",
                "android.permission.ACCESS_FINE_LOCATION",

        };

        int requestCode = 200;

        requestPermissions(permissions, requestCode);

    }

    private class TextChangeListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
           but_login.setEnabled(userName.getText().length()>0 && password.getText().length()>0);
        }
    }

}