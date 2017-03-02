package layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
import com.google.gson.reflect.TypeToken;
import com.novadata.batteryapp.GenerateDeal2DCodeActivity;
import com.novadata.batteryapp.MainActivity;
import com.novadata.batteryapp.R;
import com.novadata.batteryapp.ScanActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Bean.*;
import Bean.Package;
import Callback.CarCallback;
import Callback.MyStringCallback;
import adapter.ImportExportItemAdapter;
import adapter.MyItemClickListener;
import okhttp3.Call;
import okhttp3.MediaType;
import utils.PhotoSaver;

import static android.app.Activity.RESULT_OK;

public class fragment_deal extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, MyItemClickListener {

    private View view;
    private LocalBroadcastManager broadcastManager;
    private TextView photo_Tv;
    private TextView confirm_Tv;
    private ImageView photo_Iv;
    private Bitmap bitmap;
    private EditText carEt, consumerNameEt, consumerIdEt;
    private ArrayList<HashMap<String,Object>> listItem = new ArrayList<>();
    private String companyName;
    private String companyId;
    private String companyBranch;
    private String companyCreditCode;
    public ArrayList<String> listProductIds = new ArrayList<>();
    private ArrayList<Package> listPackage = new ArrayList<>();
    TextView initHint;
    RecyclerView rv;

    int login_status = -1;
    int status_IO;
    static final int IMPORT = 0, EXPORT = 1;
    static final int DEFAULT_STATUS = -1, USER_4S = 1, USER_COMPANY_IP = 0, USER_COMPANY_EP = 2;
    final static int REQUEST_PHOTO = 0, REQUEST_SCAN_PACKAGE = 1, REQUEST_SCAN_MODULE = 2, REQUEST_DEAL = 3, REQUEST_GENERATE = 4;
    private static final String PATH = Environment.getExternalStorageDirectory().getPath()+"/battery/photos";
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
        consumerNameEt = (EditText) view.findViewById(R.id.consumer_name_editText);
        consumerIdEt = (EditText) view.findViewById(R.id.consumer_id_editText);
        TextView user4SCompany = (TextView) view.findViewById(R.id.user_4s_company);

        photo_Tv.setVisibility(View.VISIBLE);
        confirm_Tv.setVisibility(View.GONE);

        String user4SCompanyText = "授权企业：" + companyName;
        user4SCompany.setText(user4SCompanyText);

        photo_Tv.setOnClickListener(this);
        confirm_Tv.setOnClickListener(this);
    }

    private void initCompanyView() {
        TextView scanPackage_Tv = (TextView) view.findViewById(R.id.scanPackage_button);
        TextView scanModule_Tv = (TextView) view.findViewById(R.id.scanModule_button);
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.company_RadioGroup);
        TextView userCompany = (TextView) view.findViewById(R.id.user_company);
        TextView completeButton = (TextView) view.findViewById(R.id.complete_button);
        initHint = (TextView) view.findViewById(R.id.init_hint);
        rv = (RecyclerView) view.findViewById(R.id.import_export_recycleView);

        String userCompanyText = "授权企业：" + companyName;
        userCompany.setText(userCompanyText);

//        getList();//GET整个出入库扫描信息
        initHint.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);

        completeButton.setOnClickListener(this);
        scanPackage_Tv.setOnClickListener(this);
        scanModule_Tv.setOnClickListener(this);
        rg.setOnCheckedChangeListener(this);
    }

    private void checkLayout(int login_status) {
        LinearLayout default_layout = (LinearLayout) view.findViewById(R.id.deal_default_layout);
        LinearLayout company_layout = (LinearLayout) view.findViewById(R.id.deal_company_layout);
        LinearLayout the_4s_layout = (LinearLayout) view.findViewById(R.id.deal_4s_layout);

        switch (login_status){
            case USER_COMPANY_IP:{
                Log.i("Tag", "USER_COMPANY_IP has received");
                //TODO 实现登录功能后更改这里
                companyName = fragment_user.importCompany;
                companyBranch = fragment_user.importCompanyBranch;
                companyId = fragment_user.importCompanyId;
                companyCreditCode = fragment_user.creditCode_IP;
                initCompanyView();
                default_layout.setVisibility(View.GONE);
                the_4s_layout.setVisibility(View.GONE);
                break;
            }
            case USER_COMPANY_EP:{
                //TODO 实现登录功能后更改这里
                companyName = fragment_user.exportCompany;
                companyBranch = fragment_user.exportCompanyBranch;
                companyId = fragment_user.exportCompanyId;
                companyCreditCode = fragment_user.creditCode_EP;
                initCompanyView();
                Log.i("Tag", "USER_COMPANY_EP has received");
                default_layout.setVisibility(View.GONE);
                the_4s_layout.setVisibility(View.GONE);
                break;
            }
            case USER_4S:{
                //TODO 实现登录功能后更改这里
                companyName = fragment_user.fourSCompany;
                companyBranch = fragment_user.fourSCompanyBranch;
                companyId = fragment_user.fourSCompanyId;
                companyCreditCode = fragment_user.creditCode_4S;
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
            case R.id.complete_button://点击厂商交易界面完成交易按钮
                if (status_IO == IMPORT)
                {
                    //买家生成交易二维码
                    if (listPackage != null){
                        //将信息序列化
                        Gson gson = new Gson();
                        //TODO 更新签名 传入
                        String deal2DCodeContent = gson.toJson(new Deal2DCode(companyCreditCode, "toSignature", true));
                        //清空记录的模组号\包号
                        listProductIds.clear();
                        initList(listProductIds);
                        listPackage.clear();
                        initHint.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                        //跳转到生成交易二维码界面
                        Intent intent = new Intent(MainActivity.mainActivity, GenerateDeal2DCodeActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("deal2DCodeContent", deal2DCodeContent);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REQUEST_GENERATE);
                    }
                    else {
                        Toast.makeText(getActivity(), "扫描信息不完整", Toast.LENGTH_SHORT).show();
                    }
                }
                if (status_IO == EXPORT)
                {
                    //卖家扫描买家生成的交易二维码
                    Intent intent = new Intent(MainActivity.mainActivity, ScanActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("deal2DCodeContentRecv", "deal2DCodeContentRecv");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_DEAL);
                }
                break;
            case R.id.scanPackage_button://点击扫描电池包按钮
                Intent intent = new Intent(MainActivity.mainActivity, ScanActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("result", "result");
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_SCAN_PACKAGE);
                break;
            case R.id.scanModule_button://点击扫描电池包按钮
                Intent intent2 = new Intent(MainActivity.mainActivity, ScanActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("result", "result");
                intent2.putExtras(bundle2);
                startActivityForResult(intent2, REQUEST_SCAN_MODULE);
                break;
            case R.id.photo_button://点击拍照合同按钮
                Intent intent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//实例化Intent对象,使用MediaStore的ACTION_IMAGE_CAPTURE常量调用系统相机
                startActivityForResult(intent3, REQUEST_PHOTO);//开启相机，传入上面的Intent对象
                break;
            case R.id.confirm_button://点击4S店交易确认提交按钮
                if (carEt.getText().toString().isEmpty() || consumerNameEt.getText().toString().isEmpty() || consumerIdEt.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "请输入购车信息后再确认提交", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (bitmap != null){
                        //先将合同照片存储下来
                        String photoName = PhotoSaver.createPhotoName();
                        PhotoSaver.savePhoto2SDCard(PATH, photoName, bitmap);
                        Toast.makeText(getActivity(), "照片存储成功！路径为 " + PATH, Toast.LENGTH_SHORT).show();
                        photo_Tv.setVisibility(View.VISIBLE);
                        confirm_Tv.setVisibility(View.GONE);
                        photo_Iv.setImageDrawable(getResources().getDrawable(R.drawable.contract));
                        //将车架号、购车人姓名、购车人身份证号码和合同照片共同上传
                        //GET /cars/{id} 用其中的packages上报
                        String carId = carEt.getText().toString();
                        String url = baseUrl + "cars/" + carId;
                        OkHttpUtils
                                .get()
                                .url(url)
                                .build()
                                .execute(new CarCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Log.i("Tag", "GET /cars/{id} 信息失败");
                                    }

                                    @Override
                                    public void onResponse(Car response, int id) {
                                        listPackage = response.getPackages();
                                        Log.i("Tag", "GET /cars/{id} 信息成功");
                                        //获得消费者姓名和身份证号码
                                        String consumerName = consumerNameEt.getText().toString();
                                        String consumerId = consumerIdEt.getText().toString();
                                        //将bitmap处理成Base64字符串
                                        String base64 = bitmapToBase64(bitmap);
                                        //生成要上传的trade信息
                                        //TODO 更新签名 传入
                                        Trade trade = new Trade(companyId, companyName, companyBranch, "fromSignature",
                                                consumerId, consumerName, consumerName, "",
                                                base64, listPackage);//新建一个trade并POST attachment要换成base64字符串
                                        //POST trade信息
                                        String url = baseUrl + "trades";
                                        Log.i("Tag", new Gson().toJson(trade));

                                        OkHttpUtils
                                                .postString()
                                                .url(url)
                                                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                                                .content(new Gson().toJson(trade))
                                                .build()
                                                .execute(new MyStringCallback(){
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {
                                                        super.onError(call, e, id);
                                                        Log.i("Tag", "4S店交易界面 POST /trades 失败！");
                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        super.onResponse(response, id);
                                                        Log.i("Tag", "4S店交易界面 POST /trades 成功！");
                                                        Toast.makeText(getActivity(), "4S店交易成功", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        //提交完成后清空数组
                                        listProductIds.clear();
                                        listPackage.clear();
                                    }
                                });
                    }
                    else {
                        Toast.makeText(getActivity(), "请拍摄合同照片后再确认提交", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    /**
     * bitmap转为base64
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
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
            if ((requestCode == REQUEST_SCAN_PACKAGE)||(requestCode == REQUEST_SCAN_MODULE)){
                String zxingResult = bundle.getString("result");
                //上传扫描记录
                Scan scan = new Scan(companyId, companyName, companyBranch, zxingResult);
                String url = baseUrl + "scans";

                String s = new Gson().toJson(scan);
                Log.i("TAG", s);

                OkHttpUtils
                        .postString()
                        .url(url)
                        .mediaType(MediaType.parse("application/json; charset=utf-8"))
                        .content(new Gson().toJson(scan))
                        .build()
                        .execute(new MyStringCallback(){
                            @Override
                            public void onResponse(String response, int id) {
                                super.onResponse(response, id);
                                Log.i("Tag", "POST /scans 成功！");
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                super.onError(call, e, id);
                                Log.i("Tag", "POST /scans 失败！");
                            }
                        });

                if(requestCode == REQUEST_SCAN_MODULE)
                {
                    //更新UI
                    listProductIds.add(zxingResult);
                    initList(listProductIds);
                }
                if (requestCode == REQUEST_SCAN_PACKAGE)
                {
                    String packageId = zxingResult;
                    Toast.makeText(getActivity(), "已扫描到电池包：" + packageId, Toast.LENGTH_LONG);
                    ArrayList<Module> listModule = new ArrayList<>();
                    //listProductIds数组记录了这批待交易的电池模组的二维码，先将其排序以便比对
                    Collections.sort(listProductIds);
                    for (int i = 0; i < listProductIds.size(); i++){
                        Module module = new Module(listProductIds.get(i));
                        listModule.add(module);
                    }
                    //清空模组号
                    listProductIds.clear();
                    initList(listProductIds);
                    listPackage.add(new Package(packageId, listModule));
                }
            }
            if (requestCode == REQUEST_PHOTO){
                bitmap=(Bitmap) bundle.get("data");//从附加值中获取返回的图像
                photo_Iv.setImageBitmap(bitmap);//显示缩略图像
                if (photo_Iv != null){
                    photo_Tv.setVisibility(View.GONE);
                    confirm_Tv.setVisibility(View.VISIBLE);
                }
            }
            if (requestCode == REQUEST_DEAL)
            {
                String deal2DCodeContent = bundle.getString("deal2DCodeContentRecv"); //获得扫描的交易二维码的信息

                //处理交易二维码内的信息并上报
                //二维码信息中包括买方签名、交易时间和一批电池包的二维码数组
                //比对二维码中的数组和自己扫描的数组，一致后才上传
                Deal2DCode deal2DCode = new Gson().fromJson(deal2DCodeContent, new TypeToken<Deal2DCode>() {}.getType());
                assert deal2DCode != null;
                Log.i("listPackage  NOTICE", listPackage.toString());
                Log.i("deal2DCodeContentNOTICE", deal2DCode.toString());
                if ( deal2DCode.getIsCommon())
                {
                    String toId = "",to = "",toBranch = "";
                    switch (deal2DCode.getCreditCode()){
                        case fragment_user.creditCode_IP:
                            toId = fragment_user.importCompanyId;
                            to = fragment_user.importCompany;
                            toBranch = fragment_user.importCompanyBranch;
                            break;
                        case fragment_user.creditCode_EP:
                            toId = fragment_user.exportCompanyId;
                            to = fragment_user.exportCompany;
                            toBranch = fragment_user.exportCompanyBranch;
                            break;
                        case fragment_user.creditCode_4S:
                            toId = fragment_user.fourSCompanyId;
                            to = fragment_user.fourSCompany;
                            toBranch = fragment_user.fourSCompanyBranch;
                            break;
                    }
                    //TODO 更新签名 传入
                    final Trade trade = new Trade(companyId, companyName, companyBranch, "fromSignature",
                            toId, to, toBranch, deal2DCode.getToSignature(),
                            "", listPackage);//新建一个trade并POST
                    //POST trade信息
                    String url = baseUrl + "trades";

                    OkHttpUtils
                            .postString()
                            .url(url)
                            .mediaType(MediaType.parse("application/json; charset=utf-8"))
                            .content(new Gson().toJson(trade))
                            .build()
                            .execute(new MyStringCallback(){
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    super.onError(call, e, id);
                                    Toast.makeText(getActivity(), "交易失败请重试！", Toast.LENGTH_LONG).show();
                                    Log.i("Tag", "厂商交易界面 POST /trades 失败！");
                                    Log.i("Tag", new Gson().toJson(trade));
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    super.onResponse(response, id);
                                    Toast.makeText(getActivity(), "交易成功！", Toast.LENGTH_LONG).show();
                                    Log.i("Tag", "厂商交易界面 POST /trades 成功！");
                                }
                            });
                    listPackage.clear();
                }
                else
                {
                    Toast.makeText(getActivity(), "交易不匹配，请重新扫描", Toast.LENGTH_LONG).show();
                }
                //清空扫描的模组号
                listProductIds.clear();
                initList(listProductIds);
                initHint.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }

        }
    }

    private void initList(ArrayList<String> listProductIds) {
        initHint.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);

        listItem.clear();
        for (int i = 0; i < listProductIds.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("item_module_id", listProductIds.get(i));
            listItem.add(map);
        }
        initView();
    }

    public void initView(){
        //为ListView绑定适配器
        ImportExportItemAdapter ieItemAdapter = new ImportExportItemAdapter(getActivity(), listItem);
        ieItemAdapter.setOnItemClickListener(this);

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

    }

}
