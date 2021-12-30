package com.tang.commodityadmin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.tang.commodityadmin.Constants;
import com.tang.commodityadmin.R;
import com.tang.commodityadmin.adapter.ClientAdapter;
import com.tang.commodityadmin.adapter.OrderDetailAdapter;
import com.tang.commodityadmin.dialog.LoadingDialog;
import com.tang.commodityadmin.entity.Client;
import com.tang.commodityadmin.entity.Order;
import com.tang.commodityadmin.entity.OrderDetail;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoActivity extends AppCompatActivity {
    private TextView clientNoText, clientTelePhoneText, clientAddressText;
    private TextView orderNoText, orderDateText, orderAmount;
    private RecyclerView orderDetailListView;
    private LoadingDialog mLoadingDialog;
    private final boolean[] isProcessed = {false};
    private String type;
    private Order mOrder;
    private Client mClient;
    private List<OrderDetail> mOrderDetailList=new ArrayList<>();
    private OrderDetailAdapter mOrderDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        initView();
        handle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isProcessed[0]) {
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
                mLoadingDialog.dismiss();
            }
        }).start();
    }

    private void initView() {
        clientNoText = findViewById(R.id.text_client_no);
        clientTelePhoneText = findViewById(R.id.text_client_telephone);
        clientAddressText = findViewById(R.id.text_client_address);

        orderNoText = findViewById(R.id.text_order_no);
        orderDateText = findViewById(R.id.text_order_date);
        orderAmount = findViewById(R.id.text_order_amount);

        orderDetailListView = findViewById(R.id.order_detail_list);
        GridLayoutManager layoutManager = new GridLayoutManager(OrderInfoActivity.this, 1);
        orderDetailListView.setLayoutManager(layoutManager);

        mOrderDetailAdapter=new OrderDetailAdapter(OrderInfoActivity.this, mOrderDetailList);
        orderDetailListView.setAdapter(mOrderDetailAdapter);

        mLoadingDialog = new LoadingDialog(OrderInfoActivity.this, R.mipmap.icon_loading);
        mLoadingDialog.show();
    }


    private void handle() {
        Bundle bundle = getIntent().getExtras();
        try {
            type = bundle.get("type").toString();
        } catch (Exception e) {
            Log.e("GoodsInfoActivity", e.getMessage());
            finish();
        }

        if (type != null) {
            if (type.equals("add")) {

            } else if (type.equals("edit")) {
                mOrder = (Order) bundle.get("order");
                if (mOrder==null)
                    finish();
                initOrder();
                initClient();
                initOrderDetail();
                isProcessed[0] = true;
            } else {
                finish();
            }
        }
    }

    private void initOrder(){
        orderNoText.setText(mOrder.getNo());
        orderDateText.setText(mOrder.getDate());
        orderAmount.setText("¥"+mOrder.getAmount());
    }

    private void initClient() {
        if (mOrder != null || mOrder.getClientNo() != null) {
            String url = Constants.BASE_URL + "/client/get";
            OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("no", mOrder.getClientNo())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.e("OrderInfoActivity", "search client error " + e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            mClient = JSONObject.parseObject(response, Client.class);
                            Log.i("OrderInfoActivity", "search client " + response);
                            if (mClient != null) {
                                if (mClient.getNo() != null) {
                                    clientNoText.setText(mClient.getNo());
                                }
                                if (mClient.getTelephone() != null) {
                                    clientTelePhoneText.setText(mClient.getTelephone());
                                }
                                if (mClient.getAddress() != null) {
                                    clientAddressText.setText(mClient.getAddress());
                                }
                            }
                        }
                    });
        }
    }

    private void initOrderDetail(){
        if (mOrder!=null || mOrder.getNo()!=null){
            String url = Constants.BASE_URL +"/orderDetail/searchByOrderNo";
            OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("OrderNo", mOrder.getNo())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.e("OrderInfoActivity", "search orderDetail error " + e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.i("OrderInfoActivity", "search orderDetail " + response);
                            mOrderDetailList=JSONObject.parseArray(response,OrderDetail.class);
                            mOrderDetailAdapter=new OrderDetailAdapter(OrderInfoActivity.this,mOrderDetailList);
                            orderDetailListView.setAdapter(mOrderDetailAdapter);
                        }
                    });

        }
    }
}