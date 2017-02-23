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

import Bean.Search_Result_Item;
import Callback.ListSearchResultItemCallback;
import adapter.MyItemClickListener;
import adapter.SearchResultAdapter;
import layout.SpaceItemDecoration;
import okhttp3.Call;

public class SearchResultActivity extends AppCompatActivity implements MyItemClickListener{

    String battery_code;
    TextView module_code, battery_match_head;
    RecyclerView search_result_Rv;
    private ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
    private SearchResultAdapter srAdapter;
    private String baseUrl = MainActivity.getBaseUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        this.setTitle("查询结果");

        Bundle bundle=this.getIntent().getExtras();
        battery_code = bundle.getString("battery_code");

        initList();
    }

    private void initList() {
        String url = baseUrl + "search_result_item";
        OkHttpUtils
                .get()//
                .url(url)//
                .build()//
                .execute(new ListSearchResultItemCallback()//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("Tag", "ListSearchResultItemCallback Error");
                    }

                    @Override
                    public void onResponse(List<Search_Result_Item> response, int id) {
                        if (response.size() > 0) {
                            listItem.removeAll(listItem);
                            for (int i = 0; i < response.size(); i++) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("ItemTitle", response.get(i).getTitle());
                                map.put("ItemText1", response.get(i).getDate());
                                map.put("ItemText2", response.get(i).getText1());
                                map.put("ItemText3", response.get(i).getText2());
                                map.put("ItemText4", response.get(i).getText3());
                                map.put("ItemText5", response.get(i).getService_phone());
                                listItem.add(map);
                            }
                            initView();
                            initData();
                            Log.i("Tag", "ListSearchResultItemCallback Success");

                        } else {
                            Log.i("Tag", "ListSearchResultItemCallback Empty");
                        }
                    }
                });
    }

    private void initView() {
        module_code = (TextView) findViewById(R.id.searchResult_head_text1);
        battery_match_head = (TextView) findViewById(R.id.searchResult_head_text6);

        srAdapter = new SearchResultAdapter(this, listItem);
        srAdapter.setOnItemClickListener(this);

        search_result_Rv = (RecyclerView) findViewById(R.id.search_result_recycleView);

        assert search_result_Rv != null;
        search_result_Rv.setAdapter(srAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        search_result_Rv.setLayoutManager(layoutManager);
        search_result_Rv.setHasFixedSize(true);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_space);
        search_result_Rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

    }

    private void initData() {
        module_code.setText("模组编号：" + battery_code);
        battery_match_head.setText("电池包匹配：" + battery_code);

    }

    @Override
    public void onItemClick(View view, int postion) {//点击事件的回调函数
        //TODO 定义Item的点击响应事件
        System.out.println("点击了第" + postion + "行");
        Toast.makeText(this, "点击了第" + postion + "行信息", Toast.LENGTH_SHORT).show();
    }

}
