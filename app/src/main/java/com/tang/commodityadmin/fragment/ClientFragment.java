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
import com.tang.commodityadmin.adapter.ClientAdapter;
import com.tang.commodityadmin.entity.Client;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/*
 *文件名: ClientFragment
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/18 16:43
 *描述: 这是一个示例
 */
public class ClientFragment extends Fragment {

    private RecyclerView clientListView;
    private List<Client> clientList=new ArrayList<>();
    private ClientAdapter clientAdapter;
    private SearchView clientSearchView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public ClientFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_client, container, false);


        mSwipeRefreshLayout =view.findViewById(R.id.client_swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#515151"),Color.parseColor("#303F9F"));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //清空原数据
                clientList.clear();
                clientAdapter.notifyDataSetChanged();
                refreshClientList("");
            }
        });

        clientSearchView=view.findViewById(R.id.client_search);
        clientSearchView.setIconifiedByDefault(false);
        //搜索框设置内容监听
        clientSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                clientSearchView.setQuery("",false);
                clientSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                refreshClientList(newText);
                return true;
            }
        });

        clientListView=view.findViewById(R.id.client_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        clientListView.setLayoutManager(layoutManager);

        clientAdapter=new ClientAdapter(getContext(), clientList);
        clientListView.setAdapter(clientAdapter);

        refreshClientList("");
        return view;

    }



    public void refreshClientList(String key){
        String url= Constants.BASE_URL +"/client/search";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("key",key)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("search clients :",e.getMessage());
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("search clients :",response);
                        clientList= JSONArray.parseArray(JSON.toJSON(response).toString(), Client.class);
                        clientAdapter=new ClientAdapter(ClientFragment.this.getContext(),clientList);

                        clientListView.setAdapter(clientAdapter);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

}
