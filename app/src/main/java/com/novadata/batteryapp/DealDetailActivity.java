package com.novadata.batteryapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Bean.Trade;
import Callback.ListTradeCallback;
import adapter.DealDetailItemAdapter;
import adapter.MyItemClickListener;
import okhttp3.Call;
import utils.RefreshTokenUtil;
import utils.UserSQLite;

public class DealDetailActivity extends AppCompatActivity implements View.OnClickListener, MyItemClickListener {

    String companyName;
    private ArrayList<HashMap<String,Object>> listItem = new ArrayList<>();
    private DealDetailItemAdapter ddiAdapter;
    private String baseUrl = MainActivity.getBaseUrl();
    private String filters;
    TextView startTime, endTime, filter_button;

    final int START_DATE_DIALOG = 1, END_DATE_DIALOG = 2;
    int sYear, sMonth, sDay, eYear, eMonth, eDay;
    private String token = MainActivity.getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_detail);

        this.setTitle("我的交易信息");
        assert getSupportActionBar() != null;
        getSupportActionBar().setElevation(0);

        Bundle bundle=this.getIntent().getExtras();
        companyName = bundle.getString("companyName");
        //对企业名进行URL编码
        companyName = URLEncoder.encode(companyName);
        Log.i("NOTICE companyName", companyName);

        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        filter_button = (TextView) findViewById(R.id.filter_button);

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        filter_button.setOnClickListener(this);

        final Calendar ca = Calendar.getInstance();
        sYear = eYear = ca.get(Calendar.YEAR);
        sMonth = eMonth = ca.get(Calendar.MONTH);
        sDay = eDay = ca.get(Calendar.DAY_OF_MONTH);

        initView();
        initList();

    }

    private void initList() {
        //构建filters 获取出库信息
        filters = "?filters=%7Bfrom%3A%20%7B%24regex%3A%20%23%7D%7D&params=" + companyName + "&limit=10&offset=0";

        String url = baseUrl + "trades" + filters;
        OkHttpUtils
                .get()//
                .url(url)//
                .addHeader("Authorization", " Bearer " + token)
                .build()//
                .execute(new ListTradeCallback()//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("Tag", "GET from交易信息失败");
                        Log.i("NOTICE", e.toString());
                        if (id == 401)
                        {
                            token = new RefreshTokenUtil().refreshToken(companyName);
                            MainActivity.setToken(token);
                            Toast.makeText(DealDetailActivity.this, "请求过期，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(List<Trade> response, int id) {
                        listItem.clear();
                        Log.i("GET from交易信息 NOTICE", response.toString());
                        for (int i = 0; i < response.size(); i++) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("detail_item_module_date", response.get(i).createTime);
                            map.put("detail_logistics_source", response.get(i).getFrom() + response.get(i).getFromBranch());
                            map.put("detail_logistics_destination", response.get(i).getTo() + response.get(i).getToBranch());
                            map.put("detail_item_module_id", "ID：" + response.get(i).getId());
                            listItem.add(map);
                        }
                        Log.i("Tag", "GET from交易信息成功");
                    }
                });

        //构建filters 获取入库信息
        filters = "?filters=%7Bto%3A%20%7B%24regex%3A%20%23%7D%7D&params=" + companyName + "&limit=10&offset=0";
        url = baseUrl + "trades" + filters;
        OkHttpUtils
                .get()//
                .url(url)//
                .addHeader("Authorization", " Bearer " + token)
                .build()//
                .execute(new ListTradeCallback()//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("Tag", "GET to交易信息失败");
                        if (id == 401)
                        {
                            token = new RefreshTokenUtil().refreshToken(companyName);
                            MainActivity.setToken(token);
                            Toast.makeText(DealDetailActivity.this, "请求过期，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(List<Trade> response, int id) {
                        Log.i("GET to交易信息 NOTICE", response.toString());
                        for (int i = 0; i < response.size(); i++) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("detail_item_module_date", response.get(i).createTime);
                            map.put("detail_logistics_source", response.get(i).getFrom() + response.get(i).getFromBranch());
                            map.put("detail_logistics_destination", response.get(i).getTo() + response.get(i).getToBranch());
                            map.put("detail_item_module_id", "ID：" + response.get(i).getId());
                            listItem.add(map);
                        }
                        Log.i("Tag", "GET to交易信息成功");
                        ddiAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initView() {
        ddiAdapter = new DealDetailItemAdapter(this, listItem);
        ddiAdapter.setOnItemClickListener(this);
        RecyclerView rv = (RecyclerView) findViewById(R.id.deal_detail_recycleView);

        assert rv != null;
        rv.setAdapter(ddiAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_time:
                showDialog(START_DATE_DIALOG);
                break;
            case R.id.end_time:
                showDialog(END_DATE_DIALOG);
                break;
            case R.id.filter_button:
                //对比时间增加容错
                if(compareTime()){
                    String s1 = startTime.getText().toString();
                    String s2 = endTime.getText().toString();
                    filters = "?filters=%7Bfrom%3A%20%7B%24regex%3A%20%23%7D%2C_created%3A%7B%24gte%3A%23%7D%2C_created%3A%7B%24lte%3A%23%7D%7D&params=" + companyName + "%2C" + s1 + "%2C" + s2 + "&limit=10&offset=0";

                    String url = baseUrl + "trades" + filters;

                    OkHttpUtils
                            .get()
                            .url(url)
                            .addHeader("Authorization", " Bearer " + token)
                            .build()
                            .execute(new ListTradeCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.i("Tag", "GET 筛选后的出库交易信息失败");
                                    Log.i("Tag", e.getMessage());
                                    if (id == 401)
                                    {
                                        token = new RefreshTokenUtil().refreshToken(companyName);
                                        MainActivity.setToken(token);
                                        Toast.makeText(DealDetailActivity.this, "请求过期，请重试", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onResponse(List<Trade> response, int id) {
                                    listItem.clear();
                                    for (int i = 0; i < response.size(); i++) {
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("detail_item_module_date", response.get(i).createTime);
                                        map.put("detail_logistics_source", response.get(i).getFrom() + response.get(i).getFromBranch());
                                        map.put("detail_logistics_destination", response.get(i).getTo() + response.get(i).getToBranch());
                                        map.put("detail_item_module_id",  "ID：" + response.get(i).getId());
                                        listItem.add(map);
                                    }
                                    Log.i("Tag", "GET 筛选后的出库交易信息成功");
                                    Log.i("GET from交易信息 NOTICE", response.toString());
                                }
                            });

                    filters = "?filters=%7Bto%3A%20%7B%24regex%3A%20%23%7D%2C_created%3A%7B%24gte%3A%23%7D%2C_created%3A%7B%24lte%3A%23%7D%7D&params=" + companyName + "%2C" + s1 + "%2C" + s2 + "&offset=0";

                    url = baseUrl + "trades" + filters;

                    OkHttpUtils
                            .get()
                            .url(url)
                            .addHeader("Authorization", " Bearer " + token)
                            .build()
                            .execute(new ListTradeCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.i("Tag", "GET 筛选后的入库交易信息失败");
                                    Log.i("Tag", e.getMessage());
                                    if (id == 401)
                                    {
                                        token = new RefreshTokenUtil().refreshToken(companyName);
                                        MainActivity.setToken(token);
                                        Toast.makeText(DealDetailActivity.this, "请求过期，请重试", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onResponse(List<Trade> response, int id) {
                                    for (int i = 0; i < response.size(); i++) {
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("detail_item_module_date", response.get(i).createTime);
                                        map.put("detail_logistics_source", response.get(i).getFrom() + response.get(i).getFromBranch());
                                        map.put("detail_logistics_destination", response.get(i).getTo() + response.get(i).getToBranch());
                                        map.put("detail_item_module_id",  "ID：" + response.get(i).getId());
                                        listItem.add(map);
                                    }
                                    Log.i("Tag", "GET 筛选后的入库交易信息成功");
                                    Log.i("GET to交易信息 NOTICE", response.toString());
                                    ddiAdapter.notifyDataSetChanged();
                                }
                            });

                }
                break;
        }
    }

    private boolean compareTime() {

        String s1 = startTime.getText().toString();
        String s2 = endTime.getText().toString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        try {
            c1.setTime(df.parse(s1));
            c2.setTime(df.parse(s2));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "请选择时间区间", Toast.LENGTH_SHORT).show();
        }

        switch (c1.compareTo(c2)){
            case 1:
                Toast.makeText(this, "请选择正确的时间区间", Toast.LENGTH_SHORT).show();
                return false;
            case 0:
                return true;
            case -1:
                return true;
        }
        return false;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG:
                return new DatePickerDialog(this, startDateListener, sYear, sMonth, sDay);
            case END_DATE_DIALOG:
                return new DatePickerDialog(this, endDateListener, eYear, eMonth, eDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            sYear = year;
            sMonth = monthOfYear;
            sDay = dayOfMonth;
            String month = String.valueOf(sMonth + 1);
            String day = String.valueOf(sDay);
            if (sMonth + 1 < 10){
                month = "0" + String.valueOf(sMonth + 1);
            }
            if (sDay < 10){
                day = "0" + String.valueOf(sDay);
            }
            startTime.setText(new StringBuffer().append(sYear).append("-").append(month).append("-").append(day));
        }
    };

    private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            eYear = year;
            eMonth = monthOfYear;
            eDay = dayOfMonth;
            String month = String.valueOf(eMonth + 1);
            String day = String.valueOf(eDay);
            if (eMonth + 1 < 10){
                month = "0" + String.valueOf(eMonth + 1);
            }
            if (eDay < 10){
                day = "0" + String.valueOf(eDay);
            }
            endTime.setText(new StringBuffer().append(eYear).append("-").append(month).append("-").append(day));
        }
    };

    @Override
    public void onItemClick(View view, int postion) {

        String selectedId;

        selectedId = listItem.get(postion).get("detail_item_module_id").toString().substring(3);

        Intent intent = new Intent(MainActivity.mainActivity, SingleTradeActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("selectedId", selectedId);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
