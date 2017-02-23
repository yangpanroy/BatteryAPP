package layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.novadata.batteryapp.DealDetailActivity;
import com.novadata.batteryapp.MainActivity;
import com.novadata.batteryapp.R;
import com.novadata.batteryapp.ScanActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import Bean.Import_Export_Item;
import Callback.ListImportExportItemCallback;
import Callback.MyStringCallback;
import adapter.ImportExportItemAdapter;
import adapter.MyItemClickListener;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import utils.PhotoSaver;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class fragment_deal extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, MyItemClickListener {


    private View view;
    private LocalBroadcastManager broadcastManager;
    private LinearLayout default_layout, company_layout, the_4s_layout;
    private TextView start2Scan_Tv, photo_Tv, confirm_Tv;
    private ImageView photo_Iv;
    private RadioGroup rg;
    private Bitmap bitmap;
    private EditText carEt;
    private RecyclerView rv;
    private ImportExportItemAdapter ieItemAdapter;
    private ArrayList<HashMap<String,Object>> listItem = new ArrayList<>();
    private String zxingResult, companyName;
    private List<Import_Export_Item> currentIEItemList;
    private TextView userCompany, user4SCompany;

    int login_status = -1;
    int status_IO;
    static final int IMPORT = 0, EXPORT = 1;
    static final int DEFAULT_STATUS = -1, USER_4S = 1, USER_COMPANY_IP = 0, USER_COMPANY_EP = 2;
    final static int REQUEST_PHOTO = 0, REQUEST_SCAN = 1;
    private static final String PATH = "/sdcard/battery/photos";
    private String baseUrl = MainActivity.getBaseUrl();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        int deal_layout = R.layout.fragment_deal;
        view = inflater.inflate(deal_layout, container, false);

        checkLayout(login_status);

        return view;
    }

    private void init4SView() {
        photo_Tv = (TextView) view.findViewById(R.id.photo_button);
        confirm_Tv = (TextView) view.findViewById(R.id.confirm_button);
        photo_Iv = (ImageView) view.findViewById(R.id.photo);
        carEt = (EditText) view.findViewById(R.id.car_editText);
        user4SCompany = (TextView) view.findViewById(R.id.user_4s_company);

        photo_Tv.setVisibility(View.VISIBLE);
        confirm_Tv.setVisibility(View.GONE);

        user4SCompany.setText("授权企业：" + companyName);

        photo_Tv.setOnClickListener(this);
        confirm_Tv.setOnClickListener(this);
    }

    private void initCompanyView() {
        start2Scan_Tv = (TextView) view.findViewById(R.id.scan_button);
        rg = (RadioGroup) view.findViewById(R.id.company_RadioGroup);
        userCompany = (TextView) view.findViewById(R.id.user_company);

        userCompany.setText("授权企业：" + companyName);

        getList();//GET整个出入库扫描信息

        start2Scan_Tv.setOnClickListener(this);
        rg.setOnCheckedChangeListener(this);
    }

    private void checkLayout(int login_status) {
        default_layout = (LinearLayout) view.findViewById(R.id.deal_default_layout);
        company_layout = (LinearLayout) view.findViewById(R.id.deal_company_layout);
        the_4s_layout = (LinearLayout) view.findViewById(R.id.deal_4s_layout);

        switch (login_status){
            case USER_COMPANY_IP:{
                Log.i("Tag", "USER_COMPANY_IP has received");
                companyName = fragment_user.importCompany;
                initCompanyView();
                default_layout.setVisibility(View.GONE);
                the_4s_layout.setVisibility(View.GONE);
                break;
            }
            case USER_COMPANY_EP:{
                companyName = fragment_user.exportCompany;
                initCompanyView();
                Log.i("Tag", "USER_COMPANY_EP has received");
                default_layout.setVisibility(View.GONE);
                the_4s_layout.setVisibility(View.GONE);
                break;
            }
            case USER_4S:{
                companyName = fragment_user.fourSCompany;
                init4SView();
                Log.i("Tag", "USER_4S has received");
                default_layout.setVisibility(View.GONE);
                company_layout.setVisibility(View.GONE);
                break;
            }
            default:{
                Log.i("Tag", "DEFAULT_STATUS has received");
                company_layout.setVisibility(View.GONE);
                the_4s_layout.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播
        registerReceiver();
    }

    /**
     * 注册广播接收器
     */
    private void registerReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Login_status");
        broadcastManager.registerReceiver(mAdDownLoadReceiver, intentFilter);
    }

    private BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            login_status = intent.getIntExtra("login_status", DEFAULT_STATUS);
        }
    };
    /**
     * 注销广播
     */
    @Override public void onDetach() {
        super.onDetach();
        broadcastManager.unregisterReceiver(mAdDownLoadReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scan_button://点击扫描二维码按钮
                Intent intent = new Intent(MainActivity.mainActivity, ScanActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("result", "result");
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_SCAN);
                break;
            case R.id.photo_button:
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//实例化Intent对象,使用MediaStore的ACTION_IMAGE_CAPTURE常量调用系统相机
                startActivityForResult(intent2, REQUEST_PHOTO);//开启相机，传入上面的Intent对象
                break;
            case R.id.confirm_button:
                if (!Objects.equals(carEt.getText().toString(), "")){
                    if (bitmap != null){
                        String photoName = PhotoSaver.createPhotoName();
                        PhotoSaver.savePhoto2SDCard(PATH, photoName, bitmap);
                        Toast.makeText(getActivity(), "照片存储成功！路径为 " + PATH, Toast.LENGTH_SHORT).show();
                        photo_Tv.setVisibility(View.VISIBLE);
                        confirm_Tv.setVisibility(View.GONE);
                        photo_Iv.setImageDrawable(getResources().getDrawable(R.drawable.contract));
                        //TODO 将车架号和合同照片共同上传

                    }
                    else {
                        Toast.makeText(getActivity(), "请拍摄合同照片后再确认提交", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "请输入车架号信息后再确认提交", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){
            case R.id.import_radioButton:
                status_IO = IMPORT;
                Log.i("Tag", "IMPORT status has been selected");
                break;
            case R.id.export_radioButton:
                status_IO = EXPORT;
                Log.i("Tag", "EXPORT status has been selected");
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bundle bundle=data.getExtras();
            if (requestCode == REQUEST_SCAN){
                zxingResult = bundle.getString("result");//获得扫描的二维码信息
                initList(status_IO, zxingResult, companyName);
            }
            if (requestCode == REQUEST_PHOTO){
                bitmap=(Bitmap) bundle.get("data");//从附加值中获取返回的图像
                photo_Iv.setImageBitmap(bitmap);//显示缩略图像
                if (photo_Iv != null){
                    photo_Tv.setVisibility(View.GONE);
                    confirm_Tv.setVisibility(View.VISIBLE);
                }
            }

        } if(resultCode == RESULT_CANCELED) {
//            Toast.makeText(getActivity(), "扫描结束", Toast.LENGTH_SHORT).show();
        }
    }

    private void initList(int status_IO, String zxingResult, String company) {
        String url = baseUrl + "Import_Export_Item";
        if (status_IO == IMPORT)//入库
        {
            for (int i = 0; i < currentIEItemList.size(); i++)
            {
                if (currentIEItemList.get(i).getItem_module_id().equals(zxingResult))
                {
                    if (currentIEItemList.get(i).getLogistics_destination().equals("待入库"))
                    {
                        String sourceCompany = currentIEItemList.get(i).getLogistics_source();
                        OkHttpUtils
                                .put()
                                .url(url + "/" + (i+1))//将id为i+1的数据进行更改
                                .requestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(new Import_Export_Item(zxingResult, sourceCompany, company))))//
                                .build()//
                                .execute(new MyStringCallback());
                        //再GET整个出入库扫描信息
                        getList();
                        return;
                    }
                }
            }
            Toast.makeText(getActivity(), "无法完成入库操作！", Toast.LENGTH_SHORT).show();
        }
        if (status_IO == EXPORT)//出库
        {
            for (int i = 0; i < currentIEItemList.size(); i++)
            {
                if (currentIEItemList.get(i).getItem_module_id().equals(zxingResult))
                {
                    if (currentIEItemList.get(i).getLogistics_destination().equals("待入库"))
                    {
                       Toast.makeText(getActivity(), "模组已出库，请勿重复操作！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            //先POST到json server
            OkHttpUtils
                    .postString()
                    .url(url)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .content(new Gson().toJson(new Import_Export_Item(zxingResult, company, "待入库")))
                    .build()
                    .execute(new MyStringCallback());
            //再GET整个出入库扫描信息
            getList();
        }
    }


    public void getList() {
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
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("item_module_id", "模组：" + response.get(i).getItem_module_id());
                                map.put("logistics_source", response.get(i).getLogistics_source());
                                map.put("logistics_destination", response.get(i).getLogistics_destination());
                                listItem.add(map);
                            }
                            initView();
                            Log.i("Tag", "ListImportExportItemCallback Success");
                        } else {
                            Log.i("Tag", "ListImportExportItemCallback Empty");
                        }
                    }
                });
    }

    public void initView(){
        //为ListView绑定适配器
        ieItemAdapter = new ImportExportItemAdapter(getActivity(), listItem);
        ieItemAdapter.setOnItemClickListener(this);

        rv = (RecyclerView) view.findViewById(R.id.import_export_recycleView);
        rv.setAdapter(ieItemAdapter);
        //使用线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        //设置Item之间的间距
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_space);
        rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    @Override
    public void onItemClick(View view, int position) {//点击事件的回调函数
        String battery_code;
        battery_code = currentIEItemList.get(position).getItem_module_id();
        Intent intent = new Intent(MainActivity.mainActivity, DealDetailActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("battery_code", battery_code);
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);
    }

}
