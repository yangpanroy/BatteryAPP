package com.novadata.batteryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;

import Bean.User;
import Callback.UserCallback;
import okhttp3.Call;
import utils.UserSQLite;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    static final int DEFAULT_STATUS = -1, USER_4S = 1, USER_COMPANY_IP = 0, USER_COMPANY_EP = 2;

    TextView signIn_Bn;
    EditText username_Et, password_Et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        String url = MainActivity.getBaseUrl() + "login/token";
        UserSQLite userSQLite = new UserSQLite(MainActivity.mainActivity);
        OkHttpUtils
                .post()
                .addParams("userName", userName)
                .addParams("password", password)
                .url(url)
                .addHeader("Authorization", " Bearer " + userSQLite.getUser().getToken())
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("Tag", "UserCallback Error!");
                        Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(User response, int id) {
                        Log.i("Tag", "UserCallback Success!");
                        //将获得的User信息存入到SQLite中
                        markUser(response.getUserName(), response.getPassword(), response.getCompanyType(), response.getCompanyName(),
                                response.getCompanyId(), response.getToken(), response.getState(), response.getCreateDate());
                        //将companyType返回fragment_user进行广播
                        Bundle bundle=LoginActivity.this.getIntent().getExtras();
                        bundle.putInt("status", response.getCompanyType());
                        LoginActivity.this.getIntent().putExtras(bundle);
                        LoginActivity.this.setResult(RESULT_OK, LoginActivity.this.getIntent());
                        LoginActivity.this.finish();
                    }
                });
    }

    private void markUser(String userName, String password, int companyType, String companyName, String companyId, String token, int state, String createDate) {
        UserSQLite userSQLite = new UserSQLite(this);
        userSQLite.addUser(userName, password, companyType, companyName, companyId, token, state, createDate);
    }


}
