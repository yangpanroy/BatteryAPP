package com.novadata.batteryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UrlSettingActivity extends AppCompatActivity implements View.OnClickListener {

    EditText urlEt;
    Button urlBn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_setting);

        urlEt = (EditText) findViewById(R.id.url_Et);
        urlBn = (Button) findViewById(R.id.url_Bn);

        assert urlBn != null;
        urlBn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.url_Bn)
        {
            if (urlEt.getText() == null)
            {
                Toast.makeText(this, "请输入url后确认", Toast.LENGTH_LONG).show();
            }
            else
            {
                MainActivity.mainActivity.setBaseUrl(urlEt.getText().toString());
                Toast.makeText(this, "设置url成功", Toast.LENGTH_LONG).show();
                Log.i("Tag", "当前BaseUrl为" + MainActivity.getBaseUrl());
                this.finish();
            }
        }
    }
}
