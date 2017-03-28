package com.novadata.batteryapp;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import Bean.Trade;
import Callback.TradeCallback;
import okhttp3.Call;

public class SingleTradeActivity extends AppCompatActivity {

    private String selectedId;
    private TextView tradeId, fromId, from, toId, to, tradeTime;
    private ImageView tradeAttachment;
    private LinearLayout productsIds_LinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_trade);
        this.setTitle("详细交易信息");

        Bundle bundle=this.getIntent().getExtras();
        selectedId = bundle.getString("selectedId");

        Log.i("NOTICE selectedId", selectedId);

        initView();

    }

    private void initView() {

        tradeId = (TextView) findViewById(R.id.tradeId);
        fromId = (TextView) findViewById(R.id.fromId);
        from = (TextView) findViewById(R.id.from);
        toId = (TextView) findViewById(R.id.toId);
        to = (TextView) findViewById(R.id.to);
        tradeTime = (TextView) findViewById(R.id.tradeTime);
        tradeAttachment = (ImageView) findViewById(R.id.tradeAttachment);
        productsIds_LinearLayout = (LinearLayout) findViewById(R.id.productsIds_LinearLayout);

        tradeAttachment.setVisibility(View.GONE);

        doGET();

    }

    private void doGET() {

        String url = MainActivity.getBaseUrl() + "trades/" + selectedId;

        OkHttpUtils
                .get()//
                .url(url)//
                .build()//
                .execute(new TradeCallback()//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("Tag", "GET 详细交易信息失败");
                    }

                    @Override
                    public void onResponse(Trade response, int id) {
                        String temp = "ID：" + response.getId();
                        tradeId.setText(temp);
                        temp = "卖方ID：" + response.getFromId();
                        fromId.setText(temp);
                        temp = "卖方：" + response.getFrom() + response.getFromBranch();
                        from.setText(temp);
                        temp = "买方ID：" + response.getToId();
                        toId.setText(temp);
                        temp = "买方：" + response.getTo() + response.getToBranch();
                        to.setText(temp);
                        temp = "交易时间：" + response.createTime;
                        tradeTime.setText(temp);

                        if (response.getAttachment() != ""){
                            Bitmap bitmap = response.attachmentToBitmap();
                            tradeAttachment.setImageBitmap(bitmap);
                            tradeAttachment.setVisibility(View.VISIBLE);
                            temp = "买方：" + response.getTo();
                            to.setText(temp);
                        }
                        for (int i = 0; i < response.getPackages().size(); i++)
                        {
                            TextView packagesId = new TextView(SingleTradeActivity.this);
                            temp = response.getPackages().get(i).getid();
                            packagesId.setText("电池包编号：" + temp);
                            packagesId.setTextSize(17);
                            productsIds_LinearLayout.addView(packagesId);
                            for (int j = 0; j < response.getPackages().get(i).getModules().size(); j++){
                                TextView moduleId = new TextView(SingleTradeActivity.this);
                                temp = response.getPackages().get(i).getModules().get(j).getid();
                                moduleId.setText("----电池模组编号：" + temp);
                                moduleId.setTextSize(15);
                                productsIds_LinearLayout.addView(moduleId);
                            }
                        }

                        Log.i("Tag", "GET 详细交易信息成功");
                    }
                });

    }
}
