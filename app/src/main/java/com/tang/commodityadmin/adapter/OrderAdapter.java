package com.tang.commodityadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.okhttp.Request;
import com.tang.commodityadmin.Constants;
import com.tang.commodityadmin.R;
import com.tang.commodityadmin.activity.ClientInfoActivity;
import com.tang.commodityadmin.activity.OrderInfoActivity;
import com.tang.commodityadmin.entity.Client;
import com.tang.commodityadmin.entity.Order;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/*
 *文件名: OrderAdapter
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/27 19:04
 *描述: 这是一个示例
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> mOrderList;
    private Context mContext;

    public OrderAdapter(Context context, List<Order> orderList){
        this.mContext=context;
        this.mOrderList=orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderViewHolder orderViewHolder=null;
//        View view=View.inflate(context,R.layout.item_client,null);
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_order,parent,false);
        orderViewHolder=new OrderViewHolder(view);
        return orderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order=mOrderList.get(position);
        holder.orderNoText.append(order.getNo());
        holder.orderDateText.append(order.getDate());
        holder.orderAmountText.append("¥"+order.getAmount());
        holder.orderItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("order",order);
                intent.putExtra("type","edit");
                intent.setClassName("com.tang.commodityadmin", OrderInfoActivity.class.getName());
                mContext.startActivity(intent);
                Toast.makeText(mContext,"跳转",Toast.LENGTH_SHORT).show();
            }
        });


    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }





    class OrderViewHolder extends RecyclerView.ViewHolder {
        CardView orderItemCard;
        TextView orderNoText,orderDateText,orderAmountText;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderItemCard=itemView.findViewById(R.id.layout_order_item);
            orderNoText = itemView.findViewById(R.id.text_order_no);
            orderDateText = itemView.findViewById(R.id.text_order_date);
            orderAmountText=itemView.findViewById(R.id.text_order_amount);
        }

    }

}

