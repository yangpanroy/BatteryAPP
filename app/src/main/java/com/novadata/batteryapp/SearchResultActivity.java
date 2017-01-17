package com.novadata.batteryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.MyItemClickListener;
import adapter.SearchResultAdapter;
import layout.SpaceItemDecoration;
import utils.JsonLoader;

public class SearchResultActivity extends AppCompatActivity implements MyItemClickListener{

    String battery_code;
    TextView module_code, battery_match_head;
    RecyclerView search_result_Rv;
    private ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
    private SearchResultAdapter srAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        this.setTitle("查询结果");

        Bundle bundle=this.getIntent().getExtras();
        battery_code = bundle.getString("battery_code");

        JsonLoader jsonLoader = new JsonLoader("db.json");
        listItem = jsonLoader.loadSearchResultJson2container("search_result_item", listItem);

        initView();
        initData();

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
