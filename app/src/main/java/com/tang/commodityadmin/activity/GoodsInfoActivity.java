package com.tang.commodityadmin.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.tang.commodityadmin.Constants;
import com.tang.commodityadmin.R;
import com.tang.commodityadmin.adapter.GoodsAdapter;
import com.tang.commodityadmin.dialog.ConfirmDialog;
import com.tang.commodityadmin.dialog.LoadingDialog;
import com.tang.commodityadmin.entity.Goods;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Arrays;
import java.util.Date;




public class GoodsInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText goodsNameText,goodsBrandText,goodsIntroductionText,goodsSellingPriceText,goodsCostPriceText,goodsStockText;
    private ImageView goodsNameClearImage,goodsBrandClearImage,goodsIntroductionClearImage,goodsImageView;
    private Button goodsSubmitBtn,goodsEditBtn,goodsDeleteBtn;
    private ImageButton goodsCancelBtn;
    private Dialog dialog;
    private LoadingDialog loadingDialog;
    private ConfirmDialog confirmDialog;
    private final boolean[] isProcessed={false};

    private MaterialSpinner goodsCategorySpinner,goodsUnitSpinner;

    private Uri imageUri;
    public static File tempFile;

    private String filename;
    private Goods goods;
    private String type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        goods=new Goods();
        isProcessed[0]=false;
        initDialog();
        loadingDialog.show();
        Log.i("LoadingDialog","show");
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * ????????????????????????
     *
     */
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
                goodsEditBtn.setVisibility(View.GONE);
                goodsDeleteBtn.setVisibility(View.GONE);
                DateFormat sdf = new android.icu.text.SimpleDateFormat("yyMMddhhmmss");
                String no = "SP"+sdf.format(new Date());
                goods.setNo(no);
                isProcessed[0]=true;
            }
            else if(type.equals("edit")){
                goods=(Goods)bundle.get("goods");
                uploadGoodsImage();
                goodsNameText.setText(goods.getName());
                goodsBrandText.setText(goods.getBrandName());

                goodsUnitSpinner.setSelectedIndex(Arrays.binarySearch(Constants.unit,goods.getUnit()));
                goodsCategorySpinner.setSelectedIndex(Arrays.binarySearch(Constants.category,goods.getCategory()));

                goodsSellingPriceText.setText(goods.getSellingPrice()+"");
                goodsCostPriceText.setText(goods.getCostPrice()+"");
                goodsIntroductionText.setText(goods.getIntroduction());
                goodsStockText.setText(goods.getStock()+"");

                ableView(false);
                isProcessed[0]=true;
            }
            else {
                finish();
            }
        }

    }

    private void uploadGoodsImage() {
        String url=Constants.BASE_URL+"/goods/image";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("fileName",goods.getNo())
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        GoodsAdapter.handleError(GoodsInfoActivity.this,goodsImageView);
                    }

                    @Override
                    public void onResponse(Bitmap response) {
                        if(response==null){
                            GoodsAdapter.handleError(GoodsInfoActivity.this,goodsImageView);
                        }
                        else{
                            goodsImageView.setImageBitmap(response);
                        }
                    }
                });
    }

    private void initView() {
        // TODO Auto-generated method stub
        goodsNameText = findViewById(R.id.edit_text_goods_name);
        goodsBrandText=findViewById(R.id.edit_text_goods_brand);
        goodsIntroductionText=findViewById(R.id.edit_text_goods_introduction);
        goodsSellingPriceText=findViewById(R.id.edit_text_goods_selling_price);
        goodsCostPriceText=findViewById(R.id.edit_text_goods_cost_price);
        goodsStockText=findViewById(R.id.edit_text_goods_stock_name);

        goodsNameClearImage=findViewById(R.id.image_goods_name_clear);
        goodsBrandClearImage=findViewById(R.id.image_goods_brand_clear);
        goodsIntroductionClearImage=findViewById(R.id.image_goods_introduction_clear);
        goodsImageView=findViewById(R.id.image_goods_image_upload);

        goodsUnitSpinner=findViewById(R.id.spinner_goods_unit);
        goodsCategorySpinner = findViewById(R.id.spinner_goods_category);

        goodsSubmitBtn=findViewById(R.id.btn_submit_goods);
        goodsEditBtn=findViewById(R.id.btn_edit_goods);
        goodsDeleteBtn=findViewById(R.id.btn_delete_goods);

        goodsCancelBtn=findViewById(R.id.btn_cancel_goods);


        // ?????????????????????
        addTextListener(goodsNameText,goodsNameClearImage);
        addTextListener(goodsBrandText,goodsBrandClearImage);
        addTextListener(goodsIntroductionText,goodsIntroductionClearImage);
        addTextListener(goodsSellingPriceText,null);
        addTextListener(goodsCostPriceText,null);
        addTextListener(goodsStockText,null);

        goodsSubmitBtn.setOnClickListener(this);
        goodsSubmitBtn.setEnabled(false);
        goodsEditBtn.setOnClickListener(this);
        goodsDeleteBtn.setOnClickListener(this);
        goodsCancelBtn.setOnClickListener(this);

        goodsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        goodsUnitSpinner.setItems(Constants.unit);
        goods.setUnit(Constants.unit[0]);
        goodsUnitSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(goods!=null)
                    goods.setUnit(item+"");
            }
        });
        goodsCategorySpinner.setItems(Constants.category);
        goods.setCategory(Constants.category[0]);
        goodsCategorySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if(goods!=null)
                    goods.setCategory(item+"");
            }
        });

    }


    /**
     * ?????????????????????
     *
     */
    private void initDialog() {
        dialog=new Dialog(this,R.style.MyDialogStyleBottom);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.dialog_upload, null);
        //???????????????
        root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
        root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
        dialog.setContentView(root);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // ?????????????????????????????????
        lp.x = 0; // ?????????X??????
        lp.y = 0; // ?????????Y??????
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // ??????
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // ?????????
        dialogWindow.setAttributes(lp);

        loadingDialog=new LoadingDialog(this,R.mipmap.icon_loading);
        confirmDialog=confirmDialog = new ConfirmDialog(this);
    }


    private void showDialog(){
        if (dialog!=null)
            dialog.show();
    }

    private void cancelDialog(){
        if (dialog!=null)
            dialog.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_camera:
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //??????WRITE_EXTERNAL_STORAGE??????
                    //Util.showToast(this,"?????????????????????");
                    Toast.makeText(this,"?????????????????????",Toast.LENGTH_SHORT).show();
                    return;
                }
                openCamera(this);//??????????????????
                cancelDialog();
                break;

            case R.id.btn_choose_img:
                openGallery();//?????????????????????
                cancelDialog();
                break;

            case R.id.btn_cancel_goods:
                finish();
                break;

            case R.id.btn_submit_goods:
                confirmDialog.setContentText("????????????"+" ?");
                confirmDialog.setOnDialogClickListener(new ConfirmDialog.OnDialogClickListener() {
                    @Override
                    public void onOKClick() {
                        confirmDialog.dismiss();
                        submitGoods();
                    }

                    @Override
                    public void onCancelClick() {
                        confirmDialog.dismiss();
                    }
                });
                confirmDialog.setCancelable(false);//????????????????????????
                confirmDialog.show();

                break;

            case R.id.btn_edit_goods:
                goodsEditBtn.setVisibility(View.GONE);
                goodsDeleteBtn.setVisibility(View.GONE);
                ableView(true);
                break;

            case R.id.btn_delete_goods:
                confirmDialog.setContentText("??????????????????: "+goods.getName()+" ?");
                confirmDialog.setOnDialogClickListener(new ConfirmDialog.OnDialogClickListener() {
                    @Override
                    public void onOKClick() {
                        confirmDialog.dismiss();
                        deleteGoods();
                    }

                    @Override
                    public void onCancelClick() {
                        confirmDialog.dismiss();
                    }
                });
                confirmDialog.setCancelable(false);//????????????????????????
                confirmDialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.PHOTO_REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    photoClip(true);
                }
                ableView(true);
                break;
            case Constants.CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        goodsImageView.setImageBitmap(bitmap);
                    } catch ( FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                ableView(true);

                break;
            case Constants.ARRAY_PHOTO:
                if (resultCode == RESULT_OK) {
                    if(data != null) {
                        Uri uri = data.getData();
                        imageUri = uri;
                    }
                    photoClip(false);
                    break;
                }
                ableView(true);

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    /**
     * dak??????
     *
     * @param activity
     */
    public void openCamera(Activity activity) {
        //??????????????????
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // ????????????
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ???????????????????????????????????????????????????
        if (hasSdcard()) {
            filename = System.currentTimeMillis()+"";
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    filename + ".jpg");
            if (currentapiVersion < 24) {
                // ??????????????????uri
                imageUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                //??????android7.0 ???????????????????????????
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                //??????????????????????????????????????????
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //??????WRITE_EXTERNAL_STORAGE??????
                    //Util.showToast(this,"?????????????????????");
                    Toast.makeText(this,"?????????????????????",Toast.LENGTH_SHORT).show();
                    return;
                }
                imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
        }
        // ??????????????????????????????Activity???????????????PHOTO_REQUEST_CAREMA
        activity.startActivityForResult(intent, Constants.PHOTO_REQUEST_CAMERA);
    }

    /*
     * ??????sdcard???????????????
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * ????????????
     */
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.ARRAY_PHOTO);
    }

    /**
     * ????????????
     *
     * @param isCamera ????????????????????????,?????????????????????
     */
    private void photoClip(Boolean isCamera) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        if (!isCamera){
            filename=System.currentTimeMillis()+"";
            imageUri = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + filename + ".jpg");
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constants.CROP_PHOTO);
    }


    /**
     * ??????????????????
     *
     */
    private void submitGoods(){

        goods.setName(goodsNameText.getText().toString());
        goods.setBrandName(goodsBrandText.getText().toString());
        goods.setSellingPrice(Double.parseDouble(goodsSellingPriceText.getText().toString()));
        goods.setCostPrice(Double.parseDouble(goodsCostPriceText.getText().toString()));
        goods.setIntroduction(goodsIntroductionText.getText().toString());
        goods.setStock(Integer.parseInt(goodsStockText.getText().toString()));

        submitGoodsImage();
        if(type.equals("add")){
            insertGoods();
        }
        else{
            updateGoods();
        }

    }

    private void submitGoodsImage() {
        String url= Constants.BASE_URL+"/goods/upload";
        if (filename!=null) {
            File file = new File(Environment.getExternalStorageDirectory(),
                    filename + ".jpg");
            if (file != null) {
                OkHttpUtils
                        .post()
                        .url(url)
                        .addFile("file", goods.getNo() + ".jpg", file)
                        .addParams("fileName", goods.getNo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                Log.e("submitGoods", e.getMessage());
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.i("submitGoods", response);
                            }
                        });
            }
        }
    }

    private void insertGoods() {
        String url= Constants.BASE_URL +"/goods/insert";
        OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json"))
                .content(new Gson().toJson(goods))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("insert goods error; ",e.getMessage());
                        Toast.makeText(getApplicationContext(),"????????????",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("insertGoods",response);
                        if (Integer.parseInt(response)>0){
                            Toast.makeText(getApplicationContext(),"????????????",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void updateGoods() {
        String url= Constants.BASE_URL +"/goods/update";
        OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json"))
                .content(new Gson().toJson(goods))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("insert goods error; ",e.getMessage());
                        Toast.makeText(getApplicationContext(),"????????????",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("insertGoods",response);
                        if (Integer.parseInt(response)>0){
                            Toast.makeText(getApplicationContext(),"????????????",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void deleteGoods(){
        String url=Constants.BASE_URL+"/goods/delete";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("no",goods.getNo())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("delete goods error: ",e.getMessage());
                        Toast.makeText(getApplicationContext(),"????????????: ",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"????????????",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }




    private void ableView(Boolean canAble){

        goodsImageView.setEnabled(canAble);

        goodsNameText.setEnabled(canAble);

        goodsBrandText.setEnabled(canAble);

        goodsUnitSpinner.setEnabled(canAble);

        goodsSellingPriceText.setEnabled(canAble);

        goodsCostPriceText.setEnabled(canAble);

        goodsIntroductionText.setEnabled(canAble);

        goodsStockText.setEnabled(canAble);

        goodsCategorySpinner.setEnabled(canAble);
        if (canAble){
            goodsNameClearImage.setVisibility(View.VISIBLE);
            goodsBrandClearImage.setVisibility(View.VISIBLE);
            goodsIntroductionClearImage.setVisibility(View.VISIBLE);
        }
        else{
            goodsNameClearImage.setVisibility(View.INVISIBLE);
            goodsBrandClearImage.setVisibility(View.INVISIBLE);
            goodsIntroductionClearImage.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * EditText ??????????????????
     *
     * @param e1 EditText
     * @param m1 ?????? EditText?????????
     */
    private  void addTextListener(final EditText e1, final ImageView m1) {

        e1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                // ?????????????????????????????????0???????????????clear?????????
                String s1 = s + "";
                if(m1!=null){
                    if (s.length() > 0) {
                        m1.setVisibility(View.VISIBLE);
                    } else {
                        m1.setVisibility(View.INVISIBLE);
                    }
                }

                canSubmit();
            }
        });

        if(m1!=null){
            m1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // ???????????????
                    e1.setText("");

                }
            });
        }


    }

    private void canSubmit(){
        goodsSubmitBtn.setEnabled(goodsNameText.getText().length()>0 &&
                goodsBrandText.getText().length()>0 &&
                goodsSellingPriceText.getText().length()>0 &&
                goodsCostPriceText.getText().length()>0 &&
                goodsIntroductionText.getText().length()>0 &&
                goodsStockText.getText().length()>0);
    }
}