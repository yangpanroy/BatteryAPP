package com.novadata.batteryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Bean.Scan;
import Bean.Package;
import Callback.ListScanCallback;
import Callback.PackageCallback;
import adapter.MyItemClickListener;
import adapter.SearchResultAdapter;
import layout.SpaceItemDecoration;
import okhttp3.Call;
import utils.HistorySQLite;

public class SearchResultActivity extends AppCompatActivity implements MyItemClickListener{

    private static final int RESULT_EMPTY = 2;
    private String battery_code, carId;
    private TextView module_code, manufacturer, date, type, battery_match_head, phone;
    private ArrayList<HashMap<String,Object>> listItem = new ArrayList<>();
    private String baseUrl = MainActivity.getBaseUrl();
    private String module_num, module_date, module_manufacturer, latest_date, latest_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        this.setTitle("查询结果");

        Bundle bundle=this.getIntent().getExtras();
        battery_code = bundle.getString("battery_code");

        module_code = (TextView) findViewById(R.id.searchResult_head_text1);
        manufacturer = (TextView) findViewById(R.id.searchResult_head_text2);
        date = (TextView) findViewById(R.id.searchResult_head_text3);
        type = (TextView) findViewById(R.id.searchResult_head_text5);
        battery_match_head = (TextView) findViewById(R.id.searchResult_head_text6);
        phone = (TextView) findViewById(R.id.searchResult_head_text7);

        initList();
    }

    private void initList() {
        String url = baseUrl + "packages/" + battery_code;
        OkHttpUtils
                .get()//
                .url(url)//
                .build()//
                .execute(new PackageCallback()//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        SearchResultActivity.this.setResult(RESULT_EMPTY, SearchResultActivity.this.getIntent());
                        SearchResultActivity.this.finish();

                        Log.i("Tag", "PackageCallback 失败");
                    }

                    @Override
                    public void onResponse(Package response, int id) {

                        String tempString;

                        module_num = response.getid();
                        tempString = "编号：" + module_num;
                        module_code.setText(tempString);
                        module_manufacturer = response.getManufacturer();
                        tempString = "生产企业：" + module_manufacturer;
                        manufacturer.setText(tempString);
                        module_date = response.getTimestamp().getDate();
                        tempString = "生产日期：" + module_date;
                        date.setText(tempString);
                        tempString = "参数：" + response.getPackageSpec();
                        type.setText(tempString);
                        tempString = "匹配：" + response.getid();
                        battery_match_head.setText(tempString);
                        tempString = "售后电话：" + response.getPhone();
                        phone.setText(tempString);

                        carId = response.getVin();

                        initScans();

                        Log.i("Tag", "PackageCallback 成功");
                        Log.i("Package {id}",response.toString());
                    }
                });

    }

    private void initScans() {
        String url = baseUrl + "scans?filters=%7B%22barcode%22%3A%22" + battery_code + "%22%7D&limit=10&offset=0";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new ListScanCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("Tag", "ListScanCallback 失败" + e.getMessage());
                        Toast.makeText(SearchResultActivity.this, "未找到扫描记录", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(List<Scan> response, int id) {

                        listItem.clear();
                        for (int i = 0; i < response.size(); i++) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("ItemTitle", response.get(i).getScanner() + response.get(i).getScanBranch());
                            map.put("ItemText1", "交付日期：" + response.get(i).createTime);
                            map.put("ItemText2", "电池包：" + battery_code);
                            map.put("ItemText4", "匹配车架号：" + carId);
                            if (carId == null) {
                                map.put("ItemText3", "汽车匹配状态：未匹配");
                            } else {
                                map.put("ItemText3", "汽车匹配状态：已匹配");
                            }
                            listItem.add(map);
                        }
                        initView();

                        latest_date = response.get(response.size() - 1).createTime;
                        latest_place = response.get(response.size() - 1).getScanner() + response.get(response.size() - 1).getScanBranch();

                        markSearchHistory(module_num, module_date, module_manufacturer, latest_date, latest_place);

                        Log.i("Tag", "ListScanCallback 成功");
                        Log.i("Tag", response.toString());
                    }
                });
    }

    private void initView() {
        SearchResultAdapter srAdapter = new SearchResultAdapter(this, listItem);
        srAdapter.setOnItemClickListener(this);

        RecyclerView search_result_Rv = (RecyclerView) findViewById(R.id.search_result_recycleView);

        assert search_result_Rv != null;
        search_result_Rv.setAdapter(srAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        search_result_Rv.setLayoutManager(layoutManager);
        search_result_Rv.setHasFixedSize(true);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_space);
        search_result_Rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

    }

    private void markSearchHistory(String module_num, String module_date, String module_manufacturer, String latest_date, String latest_place) {
        //使用SQLite数据库存储本次的搜索信息
        HistorySQLite historySQLite = new HistorySQLite(MainActivity.mainActivity);
        historySQLite.addHistory(module_num, module_date, module_manufacturer, latest_date, latest_place);
    }

    @Override
    public void onItemClick(View view, int postion) {//点击事件的回调函数

    }

}
