package com.novadata.batteryapp;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import Bean.User;
import adapter.MyFragmentPagerAdapter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import utils.UserSQLite;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    static final int DEFAULT_STATUS = -1, USER_COMPANY_BATTERY = 0, USER_COMPANY_CAR = 1, USER_4S = 2;

    private static String baseUrl = "http://battery.jinlinglan.com/v1/";

    public static String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        MainActivity.baseUrl = baseUrl;
    }

    private TextView main_username;
    public static MainActivity mainActivity;

    private static int login_status = DEFAULT_STATUS;
    private static String companyId;
    private static String companyName;
    private static String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        assert getSupportActionBar() != null;
        getSupportActionBar().hide();//隐藏掉整个ActionBar
        setContentView(R.layout.activity_main);

        //初始化视图
        initViews();
        checkUser();

    }

    public static void setCompanyId(String companyId) {
        MainActivity.companyId = companyId;
    }

    public static int getLogin_status() {
        return login_status;
    }

    public static void setLogin_status(int login_status) {
        MainActivity.login_status = login_status;
    }

    public static String getCompanyName() {
        return companyName;
    }

    public static void setCompanyName(String companyName) {
        MainActivity.companyName = companyName;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        MainActivity.token = token;
    }

    private void checkUser() {
        //查询数据库，读取最近一次登陆的信息，对各个界面赋值
        UserSQLite userSQLite = new UserSQLite(MainActivity.mainActivity);
        User user = userSQLite.getUser();
        if (user.getUserName() != null)
        {
            login_status = user.getCompany().getCompanyType();
            setLogin_status(login_status);
            setCompanyId(user.getCompany().getId());
            setCompanyName(user.getCompany().getCompanyName());
            setMain_username(getCompanyName());
            setToken(user.getToken());
        }

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

    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
