package com.novadata.batteryapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class GenerateDeal2DCodeActivity extends AppCompatActivity {

    Bitmap bitmapDeal2DCode;
    String deal2DCodeContent;
    ImageView deal2DCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_deal_2dcode);
        this.setTitle("交易二维码");

        Bundle bundle=this.getIntent().getExtras();
        deal2DCodeContent = bundle.getString("deal2DCodeContent");

        deal2DCode = (ImageView) findViewById(R.id.deal_2DCode);

        Thread deal2DCodeViewHandler = new Thread(new GenerateDeal2DCodeHandler());
        deal2DCodeViewHandler.start();
    }

    class GenerateDeal2DCodeHandler implements Runnable {
        @Override
        public void run() {
            bitmapDeal2DCode = QRCodeEncoder.syncEncodeQRCode(deal2DCodeContent, 250);
            //发送消息，通知UI组件显示图片

            handler.sendEmptyMessage(0);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                deal2DCode.setImageBitmap(bitmapDeal2DCode);
            }
        }
    };

}
