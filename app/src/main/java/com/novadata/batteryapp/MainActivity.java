package com.novadata.batteryapp;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import adapter.MyFragmentPagerAdapter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

//    private static final String baseUrl = "http://192.168.191.1:3000/";
    private static String baseUrl = "http://222.199.193.107:9000/v1/";

    public static String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        MainActivity.baseUrl = baseUrl;
    }

    private TextView main_username;
    public static MainActivity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        assert getSupportActionBar() != null;
        getSupportActionBar().hide();//隐藏掉整个ActionBar
        setContentView(R.layout.activity_main);

        //初始化视图
        initViews();

    }

    public void setMain_username(String username)
    {
        main_username.setText(username);
    }

    private void initViews() {

        //使用适配器将ViewPager与Fragment绑定在一起
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        assert mViewPager != null;
        mViewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        assert mTabLayout != null;
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        TabLayout.Tab main_tab = mTabLayout.getTabAt(0);
        TabLayout.Tab deal_tab = mTabLayout.getTabAt(1);
        TabLayout.Tab search_tab = mTabLayout.getTabAt(2);
        TabLayout.Tab user_tab = mTabLayout.getTabAt(3);

        main_username = (TextView)findViewById(R.id.main_userName);

        /*//设置Tab的图标
        main_tab.setIcon(R.drawable.ic_home_selector);
        deal_tab.setIcon(R.drawable.ic_deal_selector);
        search_tab.setIcon(R.drawable.ic_search_selector);
        user_tab.setIcon(R.drawable.ic_user_selector);*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }
}
