package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.novadata.batteryapp.R;
import com.youth.banner.Banner;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Bean.Main_Search_History_Item;
import Callback.ListBannerImageUrlCallback;
import Callback.ListMainSearchHistoryItemCallback;
import adapter.MyItemClickListener;
import adapter.SearchHistoryItemAdapter;
import okhttp3.Call;

public class fragment_main extends Fragment implements MyItemClickListener{

    private View view;
    private Banner banner;

    //设置Item内组件资源
    private ArrayList<HashMap<String,Object>> listItem = new ArrayList<>();

    //设置图片资源:url或本地资源
    List<String> Banner_image_url = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);
        banner = (Banner) view.findViewById(R.id.banner);

        initBanner();
        initList();

        return view;
    }

    private void initList() {
        OkHttpUtils
                .get()//
                .url("http://192.168.191.1:3000/search_history_item")//
                .build()//
                .execute(new ListMainSearchHistoryItemCallback()//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("Tag", "ListMainSearchHistoryItemCallback Error");
                    }

                    @Override
                    public void onResponse(List<Main_Search_History_Item> response, int id) {
                        if (response.size() > 0) {
                            listItem.removeAll(listItem);
                            for (int i = 0; i < response.size(); i++) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("ItemTitle", response.get(i).getTitle());
                                map.put("ItemText1", "编号：" + response.get(i).getModule_num());
                                map.put("ItemText2", "生产日期：" + response.get(i).getProduce_date());
                                map.put("ItemText3", "生产企业：" + response.get(i).getProducer());
                                map.put("ItemText4", "最近流通时间：" + response.get(i).getLatest_logistics_date());
                                map.put("ItemText5", "最近流通地点：" + response.get(i).getLatest_logistics_place());
                                map.put("ItemImage", response.get(i).getModule_image());
                                listItem.add(map);
                                initView();
                            }
                            Log.i("Tag", "ListMainSearchHistoryItemCallback Success");

                        } else {
                            Log.i("Tag", "ListMainSearchHistoryItemCallback Empty");
                        }
                    }
                });
    }

    public void initView(){
        //为ListView绑定适配器
        SearchHistoryItemAdapter shItemAdapter = new SearchHistoryItemAdapter(getActivity(), listItem);
        shItemAdapter.setOnItemClickListener(this);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        rv.setAdapter(shItemAdapter);
        //使用线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        //Rv.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));//用类设置分割线
        //Rv.addItemDecoration(new DividerItemDecoration(this, R.drawable.list_divider)); //用已有图片设置分割线

        //设置Item之间的间距
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_space);
        rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    private void initBanner() {
        OkHttpUtils
                .get()//
                .url("http://192.168.191.1:3000/banner_image_url")//
                .build()//
                .execute(new ListBannerImageUrlCallback()//
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        Log.i("Tag", "ListBannerImageUrlCallback Error");
                    }

                    @Override
                    public void onResponse(List<String> response, int id)
                    {
                        if (response.size() > 0)
                        {
                            Banner_image_url.removeAll(Banner_image_url);
                            for (int i = 0; i < response.size(); i++)
                            {
                                Banner_image_url.add(response.get(i));
                                //设置banner广告图样式
                                initBannerStyle();
                            }
                            Log.i("Tag", "ListBannerImageUrlCallback Success");

                        }else
                        {
                            Log.i("Tag", "ListBannerImageUrlCallback Empty");
                        }

                    }
                });
    }

    private void initBannerStyle() {
        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        //可选样式如下:
        //1. Banner.CIRCLE_INDICATOR    显示圆形指示器
        //2. Banner.NUM_INDICATOR    显示数字指示器
        //3. Banner.NUM_INDICATOR_TITLE    显示数字指示器和标题
        //4. Banner.CIRCLE_INDICATOR_TITLE    显示圆形指示器和标题
        banner.setBannerStyle(Banner.CIRCLE_INDICATOR);

        //设置轮播样式（没有标题默认为右边,有标题时默认左边）
        //可选样式:
        //Banner.LEFT    指示器居左
        //Banner.CENTER    指示器居中
        //Banner.RIGHT    指示器居右
        banner.setIndicatorGravity(Banner.CENTER);

        //设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
        //banner.setBannerTitle(titles);

        //设置是否自动轮播（不设置则默认自动）
        banner.isAutoPlay(true)    ;

        //设置轮播图片间隔时间（不设置默认为2000）
        banner.setDelayTime(4000);
        //设置图片资源:可选图片网址/资源文件，默认用Glide加载,也可自定义图片的加载框架
        //所有设置参数方法都放在此方法之前执行
        //banner.setImages(images);

        //自定义图片加载框架
        banner.setImages(Banner_image_url, new Banner.OnLoadImageListener() {
            @Override
            public void OnLoadImage(ImageView Imageview, Object url) {
//                Toast.makeText(getActivity(),"加载中", Toast.LENGTH_LONG).show();
                Glide.with(getActivity()).load(url).into(Imageview);
//                Toast.makeText(getActivity(), "加载完", Toast.LENGTH_LONG).show();
            }
        });
        //设置点击事件，下标是从1开始
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                //TODO 定义广告图的点击响应事件
                Toast.makeText(getActivity(), "你点击了：" + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {//点击事件的回调函数
        //TODO 定义Item的点击响应事件
        System.out.println("点击了第" + position + "行");
        Toast.makeText(getActivity(), "点击了第"+position+"行模组信息", Toast.LENGTH_SHORT).show();
    }

}
