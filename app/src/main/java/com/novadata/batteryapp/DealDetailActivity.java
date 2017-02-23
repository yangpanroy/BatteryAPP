package com.novadata.batteryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Bean.Import_Export_Item;
import Callback.ListImportExportItemCallback;
import adapter.DealDetailItemAdapter;
import layout.SpaceItemDecoration;
import okhttp3.Call;

public class DealDetailActivity extends AppCompatActivity {

    String battery_code;
    private RecyclerView rv;
    private ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
    private DealDetailItemAdapter ddiAdapter;
    private String baseUrl = MainActivity.getBaseUrl();
    private List<Import_Export_Item> currentIEItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_detail);

        this.setTitle("出入库详细信息");

        Bundle bundle=this.getIntent().getExtras();
        battery_code = bundle.getString("battery_code");

        initList();
    }

    private void initList() {
        String url = baseUrl + "Import_Export_Item";
        OkHttpUtils
                .get()//
                .url(url)//
                .build()//
                .execute(new ListImportExportItemCallback()//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("Tag", "ListImportExportItemCallback Error");
                    }

                    @Override
                    public void onResponse(List<Import_Export_Item> response, int id) {
                        if (response.size() > 0) {
                            currentIEItemList = response;
                            listItem.removeAll(listItem);
                            for (int i = 0; i < response.size(); i++) {
                                if (response.get(i).getItem_module_id().equals(battery_code))
                                {
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("detail_item_module_id", "模组：" + response.get(i).getItem_module_id());
                                    map.put("detail_logistics_source", response.get(i).getLogistics_source());
                                    map.put("detail_logistics_destination", response.get(i).getLogistics_destination());
                                    listItem.add(map);
                                }
                            }
                            initView();
                            Log.i("Tag", "ListImportExportItemCallback Success");
                        } else {
                            Log.i("Tag", "ListImportExportItemCallback Empty");
                        }
                    }
                });
    }

    private void initView() {
        ddiAdapter = new DealDetailItemAdapter(this, listItem);

        rv = (RecyclerView) findViewById(R.id.deal_detail_recycleView);

        assert rv != null;
        rv.setAdapter(ddiAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_space);
        rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

    }
    
}
