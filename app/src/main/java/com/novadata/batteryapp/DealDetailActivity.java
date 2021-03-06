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
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Call;
import utils.RefreshTokenUtil;

public class DealDetailActivity extends AppCompatActivity implements View.OnClickListener, MyItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate{

    private String companyName;
    private BGARefreshLayout mRefreshLayout;
    private ArrayList<HashMap<String,Object>> listItem = new ArrayList<>();
    private DealDetailItemAdapter ddiAdapter;
    private String baseUrl = MainActivity.getBaseUrl();
    private String filtersFrom, filtersTo;
    private TextView startTime;
    private TextView endTime;

    final int START_DATE_DIALOG = 1, END_DATE_DIALOG = 2;
    int sYear, sMonth, sDay, eYear, eMonth, eDay;
    private String token = MainActivity.getToken();
    private int indexOfCurrentItem = 0;
    private List<Trade> tradeList = new ArrayList<>();
    private int tradeListSize = tradeList.size();

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
        TextView filter_button = (TextView) findViewById(R.id.filter_button);

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        assert filter_button != null;
        filter_button.setOnClickListener(this);

        final Calendar ca = Calendar.getInstance();
        sYear = eYear = ca.get(Calendar.YEAR);
        sMonth = eMonth = ca.get(Calendar.MONTH);
        sDay = eDay = ca.get(Calendar.DAY_OF_MONTH);

        initRefreshLayout(mRefreshLayout);
        initView();
        initList();

    }

    private void initRefreshLayout(BGARefreshLayout refreshLayout) {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.refreshLayout);
        // 为BGARefreshLayout 设置代理
        assert mRefreshLayout != null;
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        String loadingMoreText = "正在加载...";
        refreshViewHolder.setLoadingMoreText(loadingMoreText);
    }

    private void initList() {
        beginRefreshing();
    }

    private void updateData() {
        if (tradeListSize == tradeList.size())
        {
            Toast.makeText(DealDetailActivity.this, "没有更多的数据了", Toast.LENGTH_SHORT).show();
        }else
        {
            tradeListSize = tradeList.size();
            listItem.clear();
            for (int i = 0; i < tradeList.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("detail_item_module_date", tradeList.get(i).createTime);
                map.put("detail_logistics_source", tradeList.get(i).getFrom() + tradeList.get(i).getFromBranch());
                map.put("detail_logistics_destination", tradeList.get(i).getTo() + tradeList.get(i).getToBranch());
                map.put("detail_item_module_id", "ID：" + tradeList.get(i).getId());
                listItem.add(map);
            }
            ddiAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        ddiAdapter = new DealDetailItemAdapter(this, listItem);
        ddiAdapter.setOnItemClickListener(this);
        RecyclerView rv = (RecyclerView) findViewById(R.id.deal_detail_recycleView);

        assert rv != null;
        rv.setAdapter(ddiAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
                    filtersFrom = "?filters=%7Bfrom%3A%20%7B%24regex%3A%20%23%7D%2C_created%3A%7B%24gte%3A%23%7D%2C_created%3A%7B%24lte%3A%23%7D%7D&params=" + companyName + "%2C" + s1 + "%2C" + s2 + "&limit=10&offset=0";
                    filtersTo = "?filters=%7Bto%3A%20%7B%24regex%3A%20%23%7D%2C_created%3A%7B%24gte%3A%23%7D%2C_created%3A%7B%24lte%3A%23%7D%7D&params=" + companyName + "%2C" + s1 + "%2C" + s2 + "&offset=0";
                    beginRefreshing();
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
    public void onItemClick(View view, int position) {

        String selectedId;

        selectedId = listItem.get(position).get("detail_item_module_id").toString().substring(3);

        Intent intent = new Intent(MainActivity.mainActivity, SingleTradeActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("selectedId", selectedId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        Log.i("TAG","下拉刷新");
        indexOfCurrentItem = 0;

        tradeList.clear();
        //构建filters 获取出库信息
        filtersFrom = "?filters=%7Bfrom%3A%20%7B%24regex%3A%20%23%7D%7D&params=" + companyName + "&limit=10&offset=0";
        filtersTo = "?filters=%7Bto%3A%20%7B%24regex%3A%20%23%7D%7D&params=" + companyName + "&limit=10&offset=0";
        String url = baseUrl + "trades" + filtersFrom;
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
                        else if (id ==404)
                        {
                            Toast.makeText(DealDetailActivity.this, "没有网络连接", Toast.LENGTH_SHORT).show();
                        }
                        mRefreshLayout.endRefreshing();
                    }

                    @Override
                    public void onResponse(List<Trade> response, int id) {
                        if (response.size()!=0)
                        {
                            tradeList.addAll(response);
                            updateData();
                            Log.i("GET from交易信息 NOTICE", response.toString());
                            Log.i("Tag", "GET from交易信息成功");
                            mRefreshLayout.endRefreshing();
                        }
                    }
                });

        //构建filters 获取入库信息

        url = baseUrl + "trades" + filtersTo;
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
                        }  else if (id ==404)
                        {
                            Toast.makeText(DealDetailActivity.this, "没有网络连接", Toast.LENGTH_SHORT).show();
                        }
                        mRefreshLayout.endRefreshing();
                    }

                    @Override
                    public void onResponse(List<Trade> response, int id) {
                        Log.i("GET to交易信息 NOTICE", response.toString());
                        if (response.size()!=0)
                        {
                            tradeList.addAll(response);
                            updateData();
                            Log.i("Tag", "GET to交易信息成功");
                        }
                        mRefreshLayout.endRefreshing();
                    }
                });
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        Log.i("TAG","上拉加载");
        indexOfCurrentItem = indexOfCurrentItem + 10;
        //构建filters 获取出库信息
        filtersFrom = filtersFrom.substring(0,filtersFrom.length() - 1) + indexOfCurrentItem;

        String url = baseUrl + "trades" + filtersFrom;
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
                        else if (id ==404)
                        {
                            Toast.makeText(DealDetailActivity.this, "没有网络连接", Toast.LENGTH_SHORT).show();
                        }
                        mRefreshLayout.endLoadingMore();
                    }

                    @Override
                    public void onResponse(List<Trade> response, int id) {
                        Log.i("GET from交易信息 NOTICE", response.toString());
                        tradeList.addAll(response);
                        updateData();
                        Log.i("Tag", "GET from交易信息成功");
                        mRefreshLayout.endLoadingMore();
                    }
                });

        //构建filters 获取入库信息
        filtersTo = filtersTo.substring(0, filtersTo.length() - 1) + indexOfCurrentItem;
        url = baseUrl + "trades" + filtersTo;
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
                        }  else if (id ==404)
                        {
                            Toast.makeText(DealDetailActivity.this, "没有网络连接", Toast.LENGTH_SHORT).show();
                        }
                        mRefreshLayout.endLoadingMore();
                    }

                    @Override
                    public void onResponse(List<Trade> response, int id) {
                        Log.i("GET to交易信息 NOTICE", response.toString());
                        tradeList.addAll(response);
                        updateData();
                        Log.i("Tag", "GET to交易信息成功");
                        mRefreshLayout.endLoadingMore();
                    }
                });
        return true;
    }

    // 通过代码方式控制进入正在刷新状态。应用场景：某些应用在 activity 的 onStart 方法中调用，自动进入正在刷新状态获取最新数据
    public void beginRefreshing() {
        mRefreshLayout.beginRefreshing();
    }

}
