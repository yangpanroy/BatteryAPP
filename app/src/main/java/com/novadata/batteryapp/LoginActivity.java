package com.novadata.batteryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import Bean.Company;
import Bean.User;
import Callback.UserCallback;
import okhttp3.Call;
import okhttp3.MediaType;
import utils.RefreshTokenUtil;
import utils.UserSQLite;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static LoginActivity loginActivity;
    TextView signIn_Bn;
    EditText username_Et, password_Et;
    private UserSQLite userSQLite = new UserSQLite(MainActivity.mainActivity);
    private String token = userSQLite.getUser().getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        assert getSupportActionBar() != null;
        getSupportActionBar().hide();//隐藏掉整个ActionBar

        signIn_Bn = (TextView)findViewById(R.id.signIn_button);
        username_Et = (EditText)findViewById(R.id.username_editText);
        password_Et = (EditText)findViewById(R.id.password_editText);

        signIn_Bn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signIn_button)
        {
            if ((username_Et.getText().toString().isEmpty())||(password_Et.getText().toString().isEmpty()))
            {
                Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //向后台根据用户名密码返回的信息进行以下逻辑
                doLogin(username_Et.getText().toString(), password_Et.getText().toString());

                /*Bundle bundle=this.getIntent().getExtras();
                switch (username_Et.getText().toString())
                {
                    case "userIP":
                        bundle.putInt("status", USER_COMPANY_IP);
                        this.getIntent().putExtras(bundle);
                        LoginActivity.this.setResult(RESULT_OK, this.getIntent());
                        LoginActivity.this.finish();
                        break;
                    case "userEP":
                        bundle.putInt("status", USER_COMPANY_EP);
                        this.getIntent().putExtras(bundle);
                        LoginActivity.this.setResult(RESULT_OK, this.getIntent());
                        LoginActivity.this.finish();
                        break;
                    case "user4S":
                        bundle.putInt("status", USER_4S);
                        this.getIntent().putExtras(bundle);
                        LoginActivity.this.setResult(RESULT_OK, this.getIntent());
                        LoginActivity.this.finish();
                        break;
                }*/
            }
        }
    }

    private void doLogin(String userName, String password) {

        String url = MainActivity.getBaseUrl() + "v1/login";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("userName", userName)
                .addParams("password", password)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("Tag", "UserCallback Error!");
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(User response, int id) {
                        Log.i("Tag", "UserCallback Success!");
                        //将获得的User信息存入到SQLite中
                        markUser(response.getId(), response.getUserName(), response.getPassword(), response.getCompany(), response.getToken(), response.getState(), response.getCreateDate());
                        //将companyType返回fragment_user进行广播
                        Bundle bundle=LoginActivity.this.getIntent().getExtras();
                        bundle.putInt("status", response.getCompany().getCompanyType());
                        LoginActivity.this.getIntent().putExtras(bundle);
                        LoginActivity.this.setResult(RESULT_OK, LoginActivity.this.getIntent());
                        LoginActivity.this.finish();
                    }
                });

        /*String url = MainActivity.getBaseUrl() + "user";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("Tag", "UserCallback Error!");
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(User response, int id) {
                        Log.i("Tag", "UserCallback Success!");
                        //将获得的User信息存入到SQLite中
                        markUser(response.getId(), response.getUserName(), response.getPassword(), response.getCompany(), response.getToken(), response.getState(), response.getCreateDate());
                        //将companyType返回fragment_user进行广播
                        Bundle bundle=LoginActivity.this.getIntent().getExtras();
                        bundle.putInt("status", response.getCompany().getCompanyType());
                        LoginActivity.this.getIntent().putExtras(bundle);
                        LoginActivity.this.setResult(RESULT_OK, LoginActivity.this.getIntent());
                        LoginActivity.this.finish();
                    }
                });*/

    }

    private void markUser(String id, String userName, String password, Company company, String token, int state, String createDate) {
        UserSQLite userSQLite = new UserSQLite(this);
        userSQLite.addUser(id, userName, password, company, token, state, createDate);
    }


}
