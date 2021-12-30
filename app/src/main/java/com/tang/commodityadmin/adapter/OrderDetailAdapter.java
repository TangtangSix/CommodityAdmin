package com.tang.commodityadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tang.commodityadmin.R;
import com.tang.commodityadmin.activity.OrderInfoActivity;
import com.tang.commodityadmin.entity.Order;
import com.tang.commodityadmin.entity.OrderDetail;

import java.util.List;

/*
 *文件名: OrderDetailAdapter
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/27 21:47
 *描述: 这是一个示例
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private List<OrderDetail> mOrderDetailList;
    private Context mContext;

    public OrderDetailAdapter(Context context, List<OrderDetail> orderDetailList){
        this.mContext=context;
        this.mOrderDetailList=orderDetailList;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderDetailViewHolder orderDetailViewHolder=null;
//        View view=View.inflate(context,R.layout.item_client,null);
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_order_detail,parent,false);
        orderDetailViewHolder=new OrderDetailViewHolder(view);
        return orderDetailViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail orderDetail=mOrderDetailList.get(position);
        holder.orderDetailNoText.setText(orderDetail.getNo());
        holder.goodsNoText.setText(orderDetail.getGoodsNo());
        holder.orderDetailPriceText.setText("¥"+orderDetail.getPrice());
        holder.orderDetailNumberText.setText("×"+orderDetail.getNumber());
//        holder.orderItemCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.putExtra("order",order);
//                intent.putExtra("type","edit");
//                intent.setClassName("com.tang.commodityadmin", OrderInfoActivity.class.getName());
//                mContext.startActivity(intent);
//                Toast.makeText(mContext,"跳转",Toast.LENGTH_SHORT).show();
//            }
//        });


    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mOrderDetailList.size();
    }





    class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        CardView orderDetailItemCard;
        TextView orderDetailNoText,goodsNoText,orderDetailPriceText,orderDetailNumberText;
        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
//            orderDetailItemCard=itemView.findViewById(R.id.layout_order_item);
            orderDetailNoText = itemView.findViewById(R.id.text_order_detail_no);
            goodsNoText = itemView.findViewById(R.id.text_goods_no);
            orderDetailPriceText=itemView.findViewById(R.id.text_order_detail_price);
            orderDetailNumberText=itemView.findViewById(R.id.text_order_detail_number);
        }

    }

}