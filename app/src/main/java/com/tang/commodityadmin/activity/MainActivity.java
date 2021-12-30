package com.tang.commodityadmin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.tang.commodityadmin.R;
import com.tang.commodityadmin.dialog.LoadingDialog;
import com.tang.commodityadmin.fragment.ClientFragment;
import com.tang.commodityadmin.fragment.GoodsFragment;
import com.tang.commodityadmin.fragment.OrderFragment;
import com.tang.commodityadmin.fragment.UserFragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    private TextView goodsManageText;
    private TextView orderManageText;
    private TextView clientManageText;
    private ImageView goodsIcon;
    private ImageView orderIcon;
    private ImageView clientIcon;
    private ViewPager viewPager;
    private LoadingDialog loadingDialog;
    private SharedPreferences loginPreferences;

    private FragmentAdapter fragmentAdapter;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private GoodsFragment goodsFragment;
    private OrderFragment orderFragment;
    private ClientFragment clientFragment;


    private int currentFragment=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        askPermissions();
        initView();
        loadingDialog=new LoadingDialog(this,R.mipmap.icon_loading);
        initFragment();


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadingDialog.dismiss();
            }
        }).start();
        if (currentFragment==0){
            goodsFragment.refreshGoodsList("");
        }
        else if (currentFragment==1){
            orderFragment.refreshOrderList("");
        }
        else if(currentFragment==2){
            clientFragment.refreshClientList("");
        }
    }

    /**
     * 底部菜单的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods:
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.order:
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.client:
                viewPager.setCurrentItem(2, true);
                break;
            case R.id.btn_exit:
                loginPreferences = getSharedPreferences("login", MODE_PRIVATE);
                if (loginPreferences.getBoolean("autoLogin",false)){
                    SharedPreferences.Editor editor = loginPreferences.edit();
                    editor.putBoolean("autoLogin", false);
                    editor.commit();
                }
                Intent intent=new Intent();
                intent.setClassName(getPackageName(),LoginActivity.class.getName());
                startActivity(intent);
                finish();
                break;
        }

    }

    /**
     * 生成Toolbar的menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_operation, menu);//这里是调用menu文件夹中的main.xml，在登陆界面label右上角的三角里显示其他功能
        return true;
    }


    /**
     * 绘制Toolbar右边按钮menu的item的图标
     *
     * @param view
     * @param menu
     * @return
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu){
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {

                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    };



    /**
     * 初始化控件
     */
    private void initView(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        Drawable moreIcon = ContextCompat.getDrawable(toolbar.getContext(), R.drawable.abc_ic_menu_overflow_material);
        toolbar.setOverflowIcon(moreIcon);
        NavigationView navigationview = findViewById(R.id.navigation_view);
        final DrawerLayout drawer =  findViewById(R.id.drawer_layout);

        goodsManageText =findViewById(R.id.goods_text);
        orderManageText =findViewById(R.id.order_text);
        clientManageText =findViewById(R.id.client_text);

        goodsIcon=findViewById(R.id.goods_icon);
        orderIcon=findViewById(R.id.order_icon);
        clientIcon=findViewById(R.id.client_icon);

        //设置监听
        findViewById(R.id.goods).setOnClickListener(this::onClick);
        findViewById(R.id.order).setOnClickListener(this::onClick);
        findViewById(R.id.client).setOnClickListener(this::onClick);
        findViewById(R.id.btn_exit).setOnClickListener(this::onClick);

        viewPager=findViewById(R.id.mainViewPager);
        goodsFragment=new GoodsFragment();
        orderFragment=new OrderFragment();
        clientFragment=new ClientFragment();


        fragmentList.add(goodsFragment);
        fragmentList.add(orderFragment);
        fragmentList.add(clientFragment);


        loadingDialog = new LoadingDialog(this,R.mipmap.icon_loading);


        LinearLayout llAndroid = (LinearLayout) navigationview.getMenu().findItem(R.id.message_single).getActionView();
        TextView msg=  llAndroid.findViewById(R.id.msg_bg);

        //将toolbar与ActionBar关联
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //设置监听
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_add_operation:
                        //商品界面
                        if (currentFragment==0){
                            Intent intent =new Intent();
                            intent.putExtra("type","add");
                            intent.setClassName("com.tang.commodityadmin",GoodsInfoActivity.class.getName());
                            startActivity(intent);
                        }

                        //订单界面
                        else if(currentFragment==1){
                            Toast.makeText(getApplicationContext(), "无法操作", Toast.LENGTH_LONG).show();
                        }

                        //用户界面
                        else if(currentFragment==2){
                            Toast.makeText(getApplicationContext(), "无法操作", Toast.LENGTH_LONG).show();
                        }
                        else {
                            break;
                        }


                        break;
                }
                return false;
            }
        });

        /*---------------------------添加头布局和尾布局-----------------------------*/
        //获取xml头布局view
        View headerView = navigationview.getHeaderView(0);
        //添加头布局的另外一种方式
        //View headview=navigationview.inflateHeaderView(R.layout.navigationview_header);
        //寻找头部里面的控件
        ImageView imageView = headerView.findViewById(R.id.image_user);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "点击了头像", Toast.LENGTH_LONG).show();
            }
        });
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
//        ColorStateList csl = (ColorStateList) getResources().getColorStateList(R.color.nav_menu_text_color);
//        //设置item的条目颜色
//        navigationview.setItemTextColor(csl);
//        //去掉默认颜色显示原来颜色  设置为null显示本来图片的颜色
//        navigationview.setItemIconTintList(csl);

        //设置单个消息数量

        msg.setText("99+");

        //设置条目点击监听
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //安卓
                Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                //设置哪个按钮被选中
//                menuItem.setChecked(true);
                //关闭侧边栏
//                drawer.closeDrawers();
                return false;
            }
        });

    }


    /**
     * 初始化fragment
     *
     */
    private void initFragment(){
        fragmentAdapter=new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
        viewPager.setOffscreenPageLimit(4);//ViewPager的缓存为4帧
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);//初始设置ViewPager选中第一帧
        goodsManageText.setTextColor(Color.parseColor("#1296db"));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
//                titleText.setText(titles[position]);
                changeTextColor(position);
                currentFragment=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/

            }
        });
    }



    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeTextColor(int position) {
        if (position == 0) {
            goodsManageText.setTextColor(Color.parseColor("#1296db"));
            orderManageText.setTextColor(Color.parseColor("#000000"));
            clientManageText.setTextColor(Color.parseColor("#000000"));
//            userManageText.setTextColor(Color.parseColor("#000000"));

            goodsIcon.setImageDrawable(getDrawable(R.mipmap.icon_goods));
            orderIcon.setImageDrawable(getDrawable(R.mipmap.icon_order_dark));
            clientIcon.setImageDrawable(getDrawable(R.mipmap.icon_client_dark));
//            userIcon.setImageDrawable(getDrawable(R.mipmap.icon_user_dark));

        } else if (position == 1) {
            orderManageText.setTextColor(Color.parseColor("#1296db"));
            goodsManageText.setTextColor(Color.parseColor("#000000"));
            clientManageText.setTextColor(Color.parseColor("#000000"));
//            userManageText.setTextColor(Color.parseColor("#000000"));

            goodsIcon.setImageDrawable(getDrawable(R.mipmap.icon_goods_dark));
            orderIcon.setImageDrawable(getDrawable(R.mipmap.icon_order));
            clientIcon.setImageDrawable(getDrawable(R.mipmap.icon_client_dark));
//            userIcon.setImageDrawable(getDrawable(R.mipmap.icon_user_dark));
        } else if (position == 2) {
            clientManageText.setTextColor(Color.parseColor("#1296db"));
            goodsManageText.setTextColor(Color.parseColor("#000000"));
            orderManageText.setTextColor(Color.parseColor("#000000"));
//            userManageText.setTextColor(Color.parseColor("#000000"));

            goodsIcon.setImageDrawable(getDrawable(R.mipmap.icon_goods_dark));
            orderIcon.setImageDrawable(getDrawable(R.mipmap.icon_order_dark));
            clientIcon.setImageDrawable(getDrawable(R.mipmap.icon_client));
//            userIcon.setImageDrawable(getDrawable(R.mipmap.icon_user_dark));
        } else if (position == 3) {
//            userManageText.setTextColor(Color.parseColor("#1296db"));
            goodsManageText.setTextColor(Color.parseColor("#000000"));
            orderManageText.setTextColor(Color.parseColor("#000000"));
            clientManageText.setTextColor(Color.parseColor("#000000"));

            goodsIcon.setImageDrawable(getDrawable(R.mipmap.icon_goods_dark));
            orderIcon.setImageDrawable(getDrawable(R.mipmap.icon_order_dark));
            clientIcon.setImageDrawable(getDrawable(R.mipmap.icon_client_dark));
//            userIcon.setImageDrawable(getDrawable(R.mipmap.icon_user));
        }
    }

    protected void askPermissions() {

        String[] permissions = {

                "android.permission.CALL_PHONE","android.permission.SEND_SMS",
                "android.permission.CAMERA","android.permission.ACCESS_FINE_LOCATION",
                "android.permission.READ_PHONE_STATE","android.permission.ACCESS_WIFI_STATE",
                "android.permission.INTERNET",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission..ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.READ_PHONE_STATE",
                "android.permission.ACCESS_FINE_LOCATION",

        };

        int requestCode = 200;

        requestPermissions(permissions, requestCode);

    }
}