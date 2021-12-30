package com.tang.commodityadmin.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
import com.tang.commodityadmin.adapter.GoodsAdapter;
import com.tang.commodityadmin.dialog.LoadingDialog;
import com.tang.commodityadmin.entity.Goods;
import com.tang.commodityadmin.service.GoodsService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoodsFragment extends Fragment{
    private SearchView goodsSearchView;
    private RecyclerView goodsListView;
    private List<Goods> goodsList=new ArrayList<>();
    private GoodsAdapter goodsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    LoadingDialog loadingDialog;

    public GoodsFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_goods, container, false);
        loadingDialog = new LoadingDialog(getContext(),R.mipmap.icon_loading);
        loadingDialog.show();

        swipeRefreshLayout=view.findViewById(R.id.goods_swipe_refresh);
        goodsSearchView=view.findViewById(R.id.goods_search);
        goodsListView=view.findViewById(R.id.goods_list);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        goodsListView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#515151"),Color.parseColor("#303F9F"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //清空原数据
                goodsList.clear();
                if (goodsAdapter!=null)
                    goodsAdapter.notifyDataSetChanged();
                refreshGoodsList("");
            }
        });



        goodsSearchView.setIconifiedByDefault(false);


        //搜索框设置内容监听
        goodsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                goodsSearchView.setQuery("",false);
                goodsSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                refreshGoodsList(newText);

                return false;
            }
        });
        return view;

    }

    public void refreshGoodsList(String key){
        String url= Constants.BASE_URL +"/goods/search";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("key",key)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("GoodsService: searchGoods",e.getMessage());
                        loadingDialog.dismiss();
                        Toast.makeText(GoodsFragment.this.getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("GoodsService: searchGoods",response);
                        goodsList= JSONArray.parseArray(JSON.toJSON(response).toString(),Goods.class);
                        goodsAdapter=new GoodsAdapter(GoodsFragment.this.getContext(),goodsList);
                        goodsListView.setAdapter(goodsAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                        loadingDialog.dismiss();
                    }
                });
    }

}
