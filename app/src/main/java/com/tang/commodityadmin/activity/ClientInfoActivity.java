package com.tang.commodityadmin.activity;

import static com.tang.commodityadmin.adapter.ClientAdapter.handleError;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.tang.commodityadmin.Constants;
import com.tang.commodityadmin.R;
import com.tang.commodityadmin.dialog.LoadingDialog;
import com.tang.commodityadmin.entity.Client;
import com.tang.commodityadmin.entity.Goods;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.Arrays;
import java.util.Date;

public class ClientInfoActivity extends AppCompatActivity {
    private TextView  clientNoText,clientNameText,clientTelePhoneText,clientAddressText;
    private ImageButton cancelBtn;
    private ImageView clientImage;
    private LoadingDialog loadingDialog;
    private final boolean[] isProcessed={false};
    private Client client;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        client=new Client();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isProcessed[0]){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadingDialog.dismiss();
            }
        }).start();
        handle();
    }

    private void initView(){
        loadingDialog=new LoadingDialog(this,R.mipmap.icon_loading);
        loadingDialog.show();

        cancelBtn=findViewById(R.id.btn_cancel_client);
        clientNoText=findViewById(R.id.text_client_no);
        clientNameText=findViewById(R.id.text_client_name);
        clientTelePhoneText=findViewById(R.id.text_client_telephone);
        clientAddressText=findViewById(R.id.text_client_address);
        clientImage=findViewById(R.id.client_image);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void handle(){
        Bundle bundle=getIntent().getExtras();
        try {
            type=bundle.get("type").toString();
        }
        catch (Exception e){
            Log.e("GoodsInfoActivity",e.getMessage());
            finish();
        }

        if (type!=null){
            if (type.equals("add")){

            }
            else if(type.equals("edit")){
                client= (Client) bundle.get("client");
                clientNoText.setText(client.getNo());
                clientNameText.setText(client.getName());
                clientTelePhoneText.setText(client.getTelephone());
                clientAddressText.setText(client.getAddress());
                initClientImage();
                isProcessed[0]=true;
            }
            else {
                finish();
            }
        }

    }

    private void initClientImage(){
        if (client==null)
            return;
        String url= Constants.BASE_URL +"/goods/image";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("fileName",client.getNo())
                .build()
                .execute(new BitmapCallback()
                {
                    @Override
                    public void onError(Request request, Exception e)
                    {
                        Log.i("loading "+client.getNo()+" false: ",e.getMessage());
                        handleError(ClientInfoActivity.this,clientImage);
                    }

                    @Override
                    public void onResponse(Bitmap bitmap)
                    {
                        if (bitmap==null){
                            handleError(ClientInfoActivity.this,clientImage);
                        }
                        else {
                            clientImage.setImageBitmap(bitmap);
                        }
                    }
                });
    }
}