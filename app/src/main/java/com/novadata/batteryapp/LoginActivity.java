package com.novadata.batteryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
            if (username_Et.getText().toString().isEmpty())
            {
                Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Bundle bundle=this.getIntent().getExtras();
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
                }
            }
        }
    }
}
