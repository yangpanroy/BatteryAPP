package com.novadata.batteryapp;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
                        temp = "交易时间" + response.getTimestamp().getDate();
                        tradeTime.setText(temp);

                        if (response.getAttachment() != ""){
                            Bitmap bitmap = response.attachmentToBitmap();
                            tradeAttachment.setImageBitmap(bitmap);
                        }

                        for (int i = 0; i < response.getProductIds().size(); i++)
                        {
                            TextView productId = new TextView(SingleTradeActivity.this);
                            temp = response.getProductIds().get(i);
                            productId.setText("产品编号：" + temp);
                            productId.setTextSize(14);
                            productsIds_LinearLayout.addView(productId);
                        }

                        Log.i("Tag", "GET 详细交易信息成功");
                    }
                });

    }
}
