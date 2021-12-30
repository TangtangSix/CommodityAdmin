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
import com.tang.commodityadmin.entity.Client;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/*
 *文件名: ClientAdapter
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/25 16:38
 *描述: 客户适配器
 */
public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {
    private List<Client> clientList;
    private Context context;

    public ClientAdapter(Context context, List<Client> clientList){
        this.context=context;
        this.clientList=clientList;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ClientViewHolder clientViewHolder=null;
//        View view=View.inflate(context,R.layout.item_client,null);
        View view= LayoutInflater.from(context).inflate(R.layout.item_client,parent,false);
        clientViewHolder=new ClientViewHolder(view);
        return clientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client=clientList.get(position);
        String url= Constants.BASE_URL +"/goods/image";

        holder.clientTextName.setText(client.getName());

        AssetManager mAssetManager = ((AppCompatActivity)context).getResources().getAssets();
        try {
            holder.clientImage.setImageDrawable(new GifDrawable(mAssetManager, "loading.gif" ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.clientItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("client",client);
                intent.putExtra("type","edit");
                intent.setClassName("com.tang.commodityadmin", ClientInfoActivity.class.getName());
                context.startActivity(intent);
                Toast.makeText(context,"跳转",Toast.LENGTH_SHORT).show();
            }
        });

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
                        handleError(context,holder.clientImage);
                    }

                    @Override
                    public void onResponse(Bitmap bitmap)
                    {
                        if (bitmap==null){
                            handleError(context,holder.clientImage);
                        }
                        else {
                            holder.clientImage.setImageBitmap(bitmap);
                        }
                    }
                });

    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }




    /**
     * 当无法请求到商品时使用默认图片
     *
     * @param Image
     */
    public static void handleError(Context context, ImageView Image){
        AssetManager mAssetManager = ((AppCompatActivity)context).getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = mAssetManager.open("client_undefined.png");
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            Image.setImageBitmap(bitmap);
            inputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    class ClientViewHolder extends RecyclerView.ViewHolder {
        GifImageView clientImage;
        TextView clientTextName;
        CardView clientItem;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            clientItem=itemView.findViewById(R.id.layout_client_item);
            clientImage = itemView.findViewById(R.id.image_client_image);
            clientTextName = itemView.findViewById(R.id.text_client_name);
        }

    }

}
