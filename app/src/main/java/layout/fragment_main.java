package layout;

import android.content.Intent;
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
import com.novadata.batteryapp.MainActivity;
import com.novadata.batteryapp.R;
import com.novadata.batteryapp.SearchResultActivity;
import com.youth.banner.Banner;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Bean.Main_Search_History_Item;
import Bean.Scan;
import Callback.ListScanCallback;
import adapter.MyItemClickListener;
import adapter.SearchHistoryItemAdapter;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Call;
import utils.HistorySQLite;
import utils.RefreshTokenUtil;
import utils.UserSQLite;

public class fragment_main extends Fragment implements MyItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate{

    private View view;
    private Banner banner;
    private BGARefreshLayout mainRefreshLayout;
    private String baseUrl = MainActivity.getBaseUrl();
    public static fragment_main fragmentMain;
    private UserSQLite userSQLite = new UserSQLite(MainActivity.mainActivity);
    private String token = userSQLite.getUser().getToken();

    //设置Item内组件资源
    private ArrayList<HashMap<String,Object>> listItem = new ArrayList<>();
    private SearchHistoryItemAdapter shItemAdapter;

    //设置图片资源:url或本地资源
    List<String> Banner_image_url = new ArrayList<>();
    private List<Main_Search_History_Item> searchHistoryList;
    private List<Main_Search_History_Item> tempList;

    private int currentIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);
        banner = (Banner) view.findViewById(R.id.banner);

        initBanner();
        initRefreshLayout(mainRefreshLayout);
        initData();
        initView();
        initList();

        return view;
    }

    private void initRefreshLayout(BGARefreshLayout refreshLayout) {
        mainRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.mainRefreshLayout);
        // 为BGARefreshLayout 设置代理
        assert mainRefreshLayout != null;
        mainRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(MainActivity.mainActivity, false);
        // 设置下拉刷新和上拉加载更多的风格
        mainRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    private void initData() {
        HistorySQLite historySQLite = new HistorySQLite(getActivity());
        searchHistoryList = new ArrayList<>();
        tempList = new ArrayList<>();
        tempList.addAll(historySQLite.getHistoryList());

        reversedList();
    }

    //颠倒list顺序，用户输入的信息会从上依次往下显示
    private void reversedList(){
        searchHistoryList.clear();
        for(int i = tempList.size() - 1 ; i >= 0 ; i --){
            searchHistoryList.add(tempList.get(i));
        }
    }

    private void initList() {
        beginRefreshing();
    }

    public void initView(){
        //为ListView绑定适配器
        shItemAdapter = new SearchHistoryItemAdapter(getActivity(), listItem);
        shItemAdapter.setOnItemClickListener(this);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        rv.setAdapter(shItemAdapter);
        //使用线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        //设置Item之间的间距
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_space);
        rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    private void initBanner() {
       /* String url = baseUrl + "banner_image_url";
        OkHttpUtils
                .get()//
                .url(url)//
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
                            Banner_image_url.clear();
                            for (int i = 0; i < response.size(); i++)
                            {
                                Banner_image_url.add(response.get(i));
                            }
                            //设置banner广告图样式
                            initBannerStyle();
                            Log.i("Tag", "ListBannerImageUrlCallback Success");

                        }else
                        {
                            Log.i("Tag", "ListBannerImageUrlCallback Empty");
                        }

                    }
                });*/

        Banner_image_url.clear();
        Banner_image_url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487839011&di=1e19ddf9f8f6d96c2df4b6662108d373&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F00%2F69%2F40%2Fe1546306ce3a9e5e90ec38d1d95da605.jpg");
        Banner_image_url.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1854960094,2162562040&fm=23&gp=0.jpg");
        Banner_image_url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487243928533&di=db15db68751f916558142ce9b328a96e&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2Fqk%2Fback_origin_pic%2F00%2F02%2F71%2Ff74c80cf3ee713147771aadeeedb9197.jpg");
        Banner_image_url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487838668&di=d27a50599257c06ead92d8b616cd0894&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2Fqk%2Fback_origin_pic%2F00%2F02%2F72%2F299fab7a8ed92c76103bf0e90f983b31.jpg");

        //设置banner广告图样式
        initBannerStyle();
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
        String selectedId;

        selectedId = listItem.get(position).get("ItemText1").toString().substring(3);

        Intent intent = new Intent(MainActivity.mainActivity, SearchResultActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("battery_code", selectedId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void clearList(){
        tempList.clear();
        reversedList();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        listItem.clear();
        for (int i = 0; i < searchHistoryList.size(); i++)
        {
            currentIndex = i;
            HashMap<String, Object> map = new HashMap<>();
            map.put("ItemText1", "编号：" + searchHistoryList.get(i).getModule_num());
            map.put("ItemText2", "生产日期：" + searchHistoryList.get(i).getProduce_date());
            map.put("ItemText3", "生产企业：" + searchHistoryList.get(i).getProducer());
            map.put("ItemText4", "最近流通时间：" + searchHistoryList.get(i).getLatest_logistics_date());
            map.put("ItemText5", "最近流通地点：" + searchHistoryList.get(i).getLatest_logistics_place());
            listItem.add(map);

            String battery_code = searchHistoryList.get(i).getModule_num();
            String url = baseUrl + "scans?filters=%7B%22barcode%22%3A%22" + battery_code + "%22%7D";
            OkHttpUtils
                    .get()
                    .url(url)
                    .addHeader("Authorization", " Bearer " + token)
                    .build()
                    .execute(new ListScanCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.i("Tag", "ListScanCallback 失败" + e.getMessage() + " 错误代码：" + id);
                           // Toast.makeText(MainActivity.mainActivity, "未找到扫描记录", Toast.LENGTH_LONG).show();
                            if (id == 401)
                            {
                                String userName = userSQLite.getUser().getUserName();
                                token = new RefreshTokenUtil().refreshToken(userName);
                                Toast.makeText(MainActivity.mainActivity, "请求过期，请重试", Toast.LENGTH_SHORT).show();
                            }
                            mainRefreshLayout.endRefreshing();
                        }

                        @Override
                        public void onResponse(List<Scan> response, int id) {
                            if(response.size() != 0)
                            {
                                String module_num, module_date, module_manufacturer, latest_date, latest_place;

                                module_num = searchHistoryList.get(currentIndex).getModule_num();
                                module_date = searchHistoryList.get(currentIndex).getProduce_date();
                                module_manufacturer = searchHistoryList.get(currentIndex).getProducer();
                                latest_date = response.get(response.size() - 1).createTime;
                                latest_place = response.get(response.size() - 1).getScanner() + response.get(response.size() - 1).getScanBranch();

                                //更新SQLite中相应的电池包最新信息
                                SearchResultActivity.markSearchHistory(module_num, module_date, module_manufacturer, latest_date, latest_place);

                                listItem.remove(listItem.size()-1);
                                HashMap<String, Object> map1 = new HashMap<>();
                                map1.put("ItemText1", "编号：" + module_num);
                                map1.put("ItemText2", "生产日期：" + module_date);
                                map1.put("ItemText3", "生产企业：" + module_manufacturer);
                                map1.put("ItemText4", "最近流通时间：" + latest_date);
                                map1.put("ItemText5", "最近流通地点：" + latest_place);
                                listItem.add(map1);

                                Log.i("Tag", "ListScanCallback 成功");
                                Log.i("Tag", response.toString());
                            }
                        }
                    });

        }
        shItemAdapter.notifyDataSetChanged();
        mainRefreshLayout.endRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    // 通过代码方式控制进入正在刷新状态。应用场景：某些应用在 activity 的 onStart 方法中调用，自动进入正在刷新状态获取最新数据
    public void beginRefreshing() {
        mainRefreshLayout.beginRefreshing();
    }
}
