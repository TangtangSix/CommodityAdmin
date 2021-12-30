package com.tang.commodityadmin.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.tang.commodityadmin.Constants;
import com.tang.commodityadmin.R;
import com.tang.commodityadmin.adapter.OrderAdapter;
import com.tang.commodityadmin.entity.Order;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/*
 *文件名: OrderFragment
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/18 16:43
 *描述: 这是一个示例
 */
public class OrderFragment  extends Fragment {
    private RecyclerView orderListView;
    private List<Order> mOrderList=new ArrayList<>();
    private OrderAdapter mOrderAdapter;
    private SearchView orderSearchView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public OrderFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_order, container, false);
        mSwipeRefreshLayout=view.findViewById(R.id.order_swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#515151"),Color.parseColor("#303F9F"));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //清空原数据
                mOrderList.clear();
                mOrderAdapter.notifyDataSetChanged();
                refreshOrderList("");
            }
        });

        orderSearchView=view.findViewById(R.id.order_search);
        orderSearchView.setIconifiedByDefault(true);
        //搜索框设置内容监听
        orderSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                refreshOrderList(newText);
                return false;
            }
        });

        orderListView=view.findViewById(R.id.order_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        orderListView.setLayoutManager(layoutManager);

        mOrderAdapter=new OrderAdapter(getContext(), mOrderList);
        orderListView.setAdapter(mOrderAdapter);

        refreshOrderList("");


        return view;
    }

    public void refreshOrderList(String key){
        String url= Constants.BASE_URL +"/order/search";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("key",key)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("search order :",e.getMessage());
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("search order :",response);
                        mOrderList= JSONArray.parseArray(JSON.toJSON(response).toString(), Order.class);
                        mOrderAdapter=new OrderAdapter(OrderFragment.this.getContext(),mOrderList);

                        orderListView.setAdapter(mOrderAdapter);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}
