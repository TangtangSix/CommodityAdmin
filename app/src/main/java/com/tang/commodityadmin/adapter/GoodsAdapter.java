package com.tang.commodityadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.okhttp.Request;
import com.tang.commodityadmin.Constants;
import com.tang.commodityadmin.R;
import com.tang.commodityadmin.activity.GoodsInfoActivity;
import com.tang.commodityadmin.entity.Goods;
import com.tang.commodityadmin.service.GoodsService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/*
 *文件名: GoodsAdapter
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/20 13:09
 *描述: 商品适配器
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder>  {
    private List<Goods> goodsList;
    private Context context;

    public GoodsAdapter(Context context, List<Goods> goodsList){
        this.context=context;
        this.goodsList=goodsList;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }
    @NonNull
    @Override
    public GoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GoodsAdapter.GoodsViewHolder goodsViewHolder=null;
        View view= LayoutInflater.from(context).inflate(R.layout.item_goods,parent,false);
        goodsViewHolder=new GoodsAdapter.GoodsViewHolder(view);
        return goodsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {
        Goods goods=goodsList.get(position);
        holder.goodsTextName.setText(goods.getName());
        holder.goodsTextCategory.setText(goods.getCategory());
        holder.goodsTextSellingPrice.setText("¥"+goods.getSellingPrice());
        holder.goodsTextIntroduction.setText((goods.getIntroduction()));

        AssetManager mAssetManager = ((AppCompatActivity)context).getResources().getAssets();
        try {
            holder.goodsImage.setImageDrawable(new GifDrawable(mAssetManager, "loading.gif" ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.goodsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("goods",goods);
                intent.putExtra("type","edit");
                intent.setClassName("com.tang.commodityadmin", GoodsInfoActivity.class.getName());
                context.startActivity(intent);
                Toast.makeText(context,"跳转",Toast.LENGTH_SHORT).show();
            }
        });
        //加载商品图片
        String url= Constants.BASE_URL +"/goods/image";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("fileName",goods.getNo())
                .build()
                .execute(new BitmapCallback()
                {
                    @Override
                    public void onError(Request request, Exception e)
                    {
                        Log.i("loading "+goods.getNo()+" false: ",e.getMessage());
                        handleError(context,holder.goodsImage);
                    }

                    @Override
                    public void onResponse(Bitmap bitmap)
                    {
                        if (bitmap==null){
                            handleError(context,holder.goodsImage);
                        }
                        else {
                            holder.goodsImage.setImageBitmap(bitmap);
                        }
                    }
                });

    }




    /**
     * 当无法请求到商品时使用默认图片
     *
     * @param goodsImage
     */
    public static void handleError(Context context, ImageView goodsImage){
        AssetManager mAssetManager = ((AppCompatActivity)context).getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = mAssetManager.open("undefined.png");
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            goodsImage.setImageBitmap(bitmap);
            inputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        TextView goodsTextName ;
        TextView goodsTextCategory;
        TextView goodsTextSellingPrice;
        TextView goodsTextIntroduction;
        GifImageView goodsImage;
        CardView goodsItem;


        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);
             goodsTextName =itemView.findViewById(R.id.text_goods_name);
             goodsTextCategory=itemView.findViewById(R.id.text_goods_category);
             goodsTextSellingPrice=itemView.findViewById(R.id.text_goods_price);
             goodsTextIntroduction=itemView.findViewById(R.id.text_goods_introduction);
             goodsImage= itemView.findViewById(R.id.image_goods_image);
             goodsItem=itemView.findViewById(R.id.layout_goods_item);
        }

    }
}
