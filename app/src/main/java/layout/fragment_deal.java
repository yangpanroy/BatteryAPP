package layout;

import android.content.Intent;
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
import java.util.Objects;

import Bean.Car;
import Bean.Deal2DCode;
import Bean.Package;
import Bean.Scan;
import Bean.Trade;
import Callback.CarCallback;
import Callback.MyStringCallback;
import Callback.PackageCallback;
import adapter.ImportExportItemAdapter;
import adapter.MyItemClickListener;
import okhttp3.Call;
import okhttp3.MediaType;
import utils.MD5Util;
import utils.PhotoSaver;
import utils.RefreshTokenUtil;
import utils.TradeExportSQLite;
import utils.UserSQLite;

import static android.app.Activity.RESULT_OK;

public class fragment_deal extends Fragment implements View.OnClickListener, MyItemClickListener {

    private View view;
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
    private TextView initHint, completeButton, scanDeal2DCodeButton;
    private RecyclerView rv;
    private LinearLayout default_layout;
    private LinearLayout company_layout;
    private LinearLayout the_4s_layout;

    int login_status = DEFAULT_STATUS;
    static int status_IO = -1;
    static final int IMPORT = 0, EXPORT = 1;
    static final int DEFAULT_STATUS = -1, USER_COMPANY_BATTERY = 0, USER_COMPANY_CAR = 1, USER_4S = 2;
    final static int REQUEST_PHOTO = 0, REQUEST_SCAN_PACKAGE = 1, REQUEST_DEAL = 3, REQUEST_GENERATE = 4;
    private static final String PATH = Environment.getExternalStorageDirectory().getPath()+"/battery/photos";
    private String baseUrl = MainActivity.getBaseUrl();
    private UserSQLite userSQLite = new UserSQLite(MainActivity.mainActivity);
    private String token;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        int deal_layout = R.layout.fragment_deal;
        view = inflater.inflate(deal_layout, container, false);

        login_status = MainActivity.getLogin_status();
        assert userSQLite != null;
        token = userSQLite.getUser().getToken();
        checkLayout(login_status);

        return view;
    }

    private void init4SView() {
        TextView photo_Tv = (TextView) view.findViewById(R.id.photo_button);
        TextView confirm_Tv = (TextView) view.findViewById(R.id.confirm_button);
        photo_Iv = (ImageView) view.findViewById(R.id.photo);
        carEt = (EditText) view.findViewById(R.id.car_editText);
        consumerNameEt = (EditText) view.findViewById(R.id.consumer_name_editText);
        consumerIdEt = (EditText) view.findViewById(R.id.consumer_id_editText);
        TextView user4SCompany = (TextView) view.findViewById(R.id.user_4s_company);
        TextView changeStatusIO_Tv = (TextView) view.findViewById(R.id.statusIOChange_button);
        ImageView emptyButton1 = (ImageView) view.findViewById(R.id.emptyButton1);
        ImageView emptyButton2 = (ImageView) view.findViewById(R.id.emptyButton2);
        ImageView emptyButton3 = (ImageView) view.findViewById(R.id.emptyButton3);

        String user4SCompanyText = "授权企业：" + companyName;
        user4SCompany.setText(user4SCompanyText);

        photo_Tv.setOnClickListener(this);
        confirm_Tv.setOnClickListener(this);
        changeStatusIO_Tv.setOnClickListener(this);
        emptyButton1.setOnClickListener(this);
        emptyButton2.setOnClickListener(this);
        emptyButton3.setOnClickListener(this);
    }

    private void initCompanyView() {
        TextView scanPackage_Tv = (TextView) view.findViewById(R.id.scanPackage_button);
        TextView userCompany = (TextView) view.findViewById(R.id.user_company);
        completeButton = (TextView) view.findViewById(R.id.complete_button);
        scanDeal2DCodeButton = (TextView) view.findViewById(R.id.scan_deal2DCode_button);
        initHint = (TextView) view.findViewById(R.id.init_hint);
        rv = (RecyclerView) view.findViewById(R.id.import_export_recycleView);
        TextView changeStatusIO_Tv = (TextView) view.findViewById(R.id.changeStatusIO_button);

        String userCompanyText = "授权企业：" + companyName;
        userCompany.setText(userCompanyText);

        initHint.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);

        completeButton.setVisibility(View.VISIBLE);
        scanDeal2DCodeButton.setVisibility(View.GONE);

        completeButton.setOnClickListener(this);
        scanDeal2DCodeButton.setOnClickListener(this);
        scanPackage_Tv.setOnClickListener(this);
        changeStatusIO_Tv.setOnClickListener(this);
    }

    private void checkLayout(int login_status) {
        default_layout = (LinearLayout) view.findViewById(R.id.deal_default_layout);
        company_layout = (LinearLayout) view.findViewById(R.id.deal_company_layout);
        the_4s_layout = (LinearLayout) view.findViewById(R.id.deal_4s_layout);

        ImageView import_button = (ImageView) view.findViewById(R.id.import_button);
        ImageView export_button = (ImageView) view.findViewById(R.id.export_button);

        switch (login_status){
            case USER_COMPANY_CAR:{
                //TODO 实现登录功能后更改这里
                companyName = userSQLite.getUser().getCompany().getCompanyName();
                companyId = userSQLite.getUser().getCompany().getId();
                companyBranch = userSQLite.getUser().getCompany().getBranches().toString();
                companyCreditCode = userSQLite.getUser().getCompany().getCreditCode();
                initCompanyView();
                Log.i("Tag", "USER_COMPANY_CAR has received");
                import_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        status_IO = IMPORT;
                        Log.i("Tag", "IMPORT status has been selected");
                        default_layout.setVisibility(View.GONE);
                        the_4s_layout.setVisibility(View.GONE);
                        company_layout.setVisibility(View.VISIBLE);
                    }
                });
                export_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        status_IO = EXPORT;
                        Log.i("Tag", "EXPORT status has been selected");
                        default_layout.setVisibility(View.GONE);
                        the_4s_layout.setVisibility(View.GONE);
                        company_layout.setVisibility(View.VISIBLE);
                    }
                });
                //用status_IO控制是否选择了出入库，保证刷新UI时能够直接进入交易界面不用重复选择出入库按钮
                if (status_IO != -1){
                    default_layout.setVisibility(View.GONE);
                    the_4s_layout.setVisibility(View.GONE);
                    company_layout.setVisibility(View.VISIBLE);
                    if (status_IO == EXPORT)
                    {
                        //查询SQLite如果有未完成的交易信息，则将界面设置为刚扫描完所有电池包，等待交易的状态
                        TradeExportSQLite tradeExportSQLite = new TradeExportSQLite(MainActivity.mainActivity);
                        if (!Objects.equals(tradeExportSQLite.getTrade(), ""))
                        {
                            wait2Trade(tradeExportSQLite.getTrade());
                        }
                    }
                }
                break;
            }
            case USER_COMPANY_BATTERY:{
                //TODO 实现登录功能后更改这里
                companyName = userSQLite.getUser().getCompany().getCompanyName();
                companyId = userSQLite.getUser().getCompany().getId();
                companyBranch = userSQLite.getUser().getCompany().getBranches().toString();
                companyCreditCode = userSQLite.getUser().getCompany().getCreditCode();
                status_IO = EXPORT;
                Log.i("Tag", "EXPORT status has been selected");
                initCompanyView();
                Log.i("Tag", "USER_COMPANY_BATTERY has received");
                default_layout.setVisibility(View.GONE);
                the_4s_layout.setVisibility(View.GONE);
                company_layout.setVisibility(View.VISIBLE);
                //查询SQLite如果有未完成的交易信息，则将界面设置为刚扫描完所有电池包，等待交易的状态
                TradeExportSQLite tradeExportSQLite = new TradeExportSQLite(MainActivity.mainActivity);
                if (!Objects.equals(tradeExportSQLite.getTrade(), ""))
                {
                    wait2Trade(tradeExportSQLite.getTrade());
                }
                break;
            }
            case USER_4S:{
                //TODO 实现登录功能后更改这里
                companyName = userSQLite.getUser().getCompany().getCompanyName();
                companyId = userSQLite.getUser().getCompany().getId();
                companyBranch = userSQLite.getUser().getCompany().getBranches().toString();
                companyCreditCode = userSQLite.getUser().getCompany().getCreditCode();
                import_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initCompanyView();
                        Log.i("Tag", "USER_4S TRADE AS USER_COMPANY_CAR has received");
                        status_IO = IMPORT;
                        Log.i("Tag", "IMPORT status has been selected");
                        default_layout.setVisibility(View.GONE);
                        the_4s_layout.setVisibility(View.GONE);
                        company_layout.setVisibility(View.VISIBLE);
                    }
                });
                export_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        init4SView();
                        Log.i("Tag", "USER_4S TRADE AS USER_4S has received");
                        status_IO = EXPORT;
                        Log.i("Tag", "EXPORT status has been selected");
                        default_layout.setVisibility(View.GONE);
                        company_layout.setVisibility(View.GONE);
                        the_4s_layout.setVisibility(View.VISIBLE);
                    }
                });
                //用status_IO控制是否选择了出入库，保证刷新UI时能够直接进入交易界面不用重复选择出入库按钮
                if (status_IO == IMPORT){
                    initCompanyView();
                    Log.i("Tag", "USER_4S TRADE AS USER_COMPANY_CAR has received");
                    default_layout.setVisibility(View.GONE);
                    the_4s_layout.setVisibility(View.GONE);
                    company_layout.setVisibility(View.VISIBLE);
                }
                if (status_IO == EXPORT){
                    init4SView();
                    Log.i("Tag", "USER_4S TRADE AS USER_4S has received");
                    default_layout.setVisibility(View.GONE);
                    company_layout.setVisibility(View.GONE);
                    the_4s_layout.setVisibility(View.VISIBLE);
                }
                break;
            }
            default:{
                Log.i("Tag", "DEFAULT_STATUS has received");
                status_IO = -1;
                company_layout.setVisibility(View.GONE);
                the_4s_layout.setVisibility(View.GONE);
                default_layout.setVisibility(View.VISIBLE);
                import_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "请登录后再进行交易！", Toast.LENGTH_LONG).show();
                    }
                });
                export_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "请登录后再进行交易！", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            }
        }
    }

    private void wait2Trade(String tradeInfo) {
        completeButton.setVisibility(View.GONE);
        scanDeal2DCodeButton.setVisibility(View.VISIBLE);

        String[] packageIds = tradeInfo.split("#");
        listProductIds.clear();
        //把packageIds中的电池包id全部赋值给listProductIds
        Collections.addAll(listProductIds, packageIds);
        initList(listProductIds);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scan_deal2DCode_button://点击扫描交易二维码按钮
                //卖家扫描买家生成的交易二维码
                Intent intent = new Intent(MainActivity.mainActivity, ScanActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("deal2DCodeContentRecv", "deal2DCodeContentRecv");
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_DEAL);
                break;
            case R.id.complete_button://点击厂商交易界面完成交易按钮
                if (status_IO == IMPORT)
                {
                    //买家生成交易二维码
                    if (listProductIds != null){
                        //listProductIds数组记录了这批待交易的电池模组的二维码，先将其排序以便比对
                        Collections.sort(listProductIds);
                        //将信息序列化
                        Gson gson = new Gson();
                        //TODO 更新签名 传入
                        //MD5加密本次交易的商品信息放入二维码内容中，用于比对交易商品是否匹配
                        String deal2DCodeContent = gson.toJson(new Deal2DCode(companyCreditCode, "toSignature", MD5Util.MD5(listProductIds.toString())));
                        //清空记录的电池包号，并更新UI
                        listProductIds.clear();
                        initList(listProductIds);
                        listPackage.clear();
                        initHint.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                        //跳转到生成交易二维码界面
                        Intent intent4 = new Intent(MainActivity.mainActivity, GenerateDeal2DCodeActivity.class);
                        Bundle bundle4=new Bundle();
                        bundle4.putString("deal2DCodeContent", deal2DCodeContent);
                        intent4.putExtras(bundle4);
                        startActivityForResult(intent4, REQUEST_GENERATE);
                    }
                    else {
                        Toast.makeText(getActivity(), "扫描信息不完整", Toast.LENGTH_SHORT).show();
                    }
                }
                if (status_IO == EXPORT)
                {
                    completeButton.setVisibility(View.GONE);
                    scanDeal2DCodeButton.setVisibility(View.VISIBLE);
                    //SQLite存储listPackage等交易信息
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < listProductIds.size(); i++)
                    {
                        sb.append(listProductIds.get(i));
                        sb.append("#");
                    }
                    //将sb.toString()存储到SQLite中
                    TradeExportSQLite tradeExportSQLite = new TradeExportSQLite(MainActivity.mainActivity);
                    tradeExportSQLite.addTrade(sb.toString());
                }
                break;
            case R.id.scanPackage_button://点击扫描电池包按钮
                Intent intent2 = new Intent(MainActivity.mainActivity, ScanActivity.class);
                Bundle bundle2=new Bundle();
                bundle2.putString("result", "result");
                intent2.putExtras(bundle2);
                startActivityForResult(intent2, REQUEST_SCAN_PACKAGE);
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
                        //将车架号、购车人姓名、购车人身份证号码和合同照片共同上传
                        //GET /cars/{id} 用其中的packages上报
                        String carId = carEt.getText().toString();
                        String url = baseUrl + "cars/" + carId;
                        OkHttpUtils
                                .get()
                                .url(url)
                                .addHeader("Authorization"," Bearer " + token)
                                .build()
                                .execute(new CarCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Log.i("Tag", "GET /cars/{id} 信息失败");
                                        if (id == 401)
                                        {
                                            String userName = userSQLite.getUser().getUserName();
                                            token = new RefreshTokenUtil().refreshToken(userName);
                                            Toast.makeText(MainActivity.mainActivity, "请求过期，请重试", Toast.LENGTH_SHORT).show();
                                        }
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
                                                .addHeader("Authorization", " Bearer " + token)
                                                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                                                .content(new Gson().toJson(trade))
                                                .build()
                                                .execute(new MyStringCallback(){
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {
                                                        super.onError(call, e, id);
                                                        Log.i("Tag", "4S店交易界面 POST /trades 失败！");
                                                        if (id == 401)
                                                        {
                                                            String userName = userSQLite.getUser().getUserName();
                                                            token = new RefreshTokenUtil().refreshToken(userName);
                                                            Toast.makeText(MainActivity.mainActivity, "请求过期，请重试", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        super.onResponse(response, id);
                                                        Log.i("Tag", "4S店交易界面 POST /trades 成功！");
                                                        Toast.makeText(getActivity(), "4S店交易成功", Toast.LENGTH_SHORT).show();
                                                        photo_Iv.setImageDrawable(getResources().getDrawable(R.drawable.contract));
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
            case R.id.changeStatusIO_button:
                setDefaultStatusIO();
                default_layout.setVisibility(View.VISIBLE);
                listPackage.clear();
                listProductIds.clear();
                initList(listProductIds);
                initHint.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
                break;
            case R.id.statusIOChange_button:
                setDefaultStatusIO();
                default_layout.setVisibility(View.VISIBLE);
                listPackage.clear();
                listProductIds.clear();
                initList(listProductIds);
                initHint.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
                carEt.setText("");
                consumerIdEt.setText("");
                consumerNameEt.setText("");
                photo_Iv.setImageResource(R.drawable.contract);
                break;
            case R.id.emptyButton1:
                consumerNameEt.setText("");
                break;
            case R.id.emptyButton2:
                consumerIdEt.setText("");
                break;
            case R.id.emptyButton3:
                carEt.setText("");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bundle bundle=data.getExtras();
            if (requestCode == REQUEST_SCAN_PACKAGE){
                String zxingResult = bundle.getString("result");
                //上传扫描记录
                Scan scan = new Scan(companyId, companyName, companyBranch, zxingResult);
                String url = baseUrl + "scans";

                String s = new Gson().toJson(scan);
                Log.i("TAG", s);

                OkHttpUtils
                        .postString()
                        .url(url)
                        .addHeader("Authorization", " Bearer " + token)
                        .mediaType(MediaType.parse("application/json; charset=utf-8"))
                        .content(new Gson().toJson(scan))
                        .build()
                        .execute(new MyStringCallback() {
                            @Override
                            public void onResponse(String response, int id) {
                                super.onResponse(response, id);
                                Log.i("Tag", "POST /scans 成功！");
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                super.onError(call, e, id);
                                Log.i("Tag", "POST /scans 失败！");
                                if (id == 401)
                                {
                                    String userName = userSQLite.getUser().getUserName();
                                    token = new RefreshTokenUtil().refreshToken(userName);
                                    Toast.makeText(MainActivity.mainActivity, "请求过期，请重试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                //更新UI
                listProductIds.add(zxingResult);
                initList(listProductIds);
            }
            if (requestCode == REQUEST_PHOTO){
                bitmap=(Bitmap) bundle.get("data");//从附加值中获取返回的图像
                photo_Iv.setImageBitmap(bitmap);//显示缩略图像
            }
            if (requestCode == REQUEST_DEAL)
            {
                completeButton.setVisibility(View.VISIBLE);
                scanDeal2DCodeButton.setVisibility(View.GONE);
                String deal2DCodeContent = bundle.getString("deal2DCodeContentRecv"); //获得扫描的交易二维码的信息

                //处理交易二维码内的信息并上报
                //二维码信息中包括买方签名、交易时间和一批电池包的二维码数组
                Deal2DCode deal2DCode = new Deal2DCode();
                try{
                    deal2DCode = new Gson().fromJson(deal2DCodeContent, new TypeToken<Deal2DCode>() {}.getType());
                }catch (Exception e){
                    Log.i("Tag", "JSON 格式或语法错误");
                }
                assert deal2DCode != null;
                Log.i("listPackage  NOTICE", listPackage.toString());
                Log.i("deal2DCodeContentNOTICE", deal2DCode.toString());
                //listProductIds数组记录了这批待交易的电池模组的二维码，先将其排序以便比对
                Collections.sort(listProductIds);
                //比对二维码中的数组和自己扫描的数组，一致后才上传
                if (Objects.equals(deal2DCode.getPackageListMD5Code(), MD5Util.MD5(listProductIds.toString())))
                {
                    String toId = "",to = "",toBranch = "";
                    //TODO 实现登陆后，此处应向后台查询获得数据
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
                    //获得扫描的package信息，用于生成trade对象并上报
                    for (int i = 0; i < listProductIds.size(); i++)
                    {
                        String url = baseUrl + "packages/" + listProductIds.get(i);
                        OkHttpUtils
                                .get()
                                .url(url)
                                .addHeader("Authorization", " Bearer " + token)
                                .build()
                                .execute(new PackageCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Log.i("Tag", "PackageCallback Error!");
                                        if (id == 401)
                                        {
                                            String userName = userSQLite.getUser().getUserName();
                                            token = new RefreshTokenUtil().refreshToken(userName);
                                            Toast.makeText(MainActivity.mainActivity, "请求过期，请重试", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onResponse(Package response, int id) {
                                        Log.i("Tag", "PackageCallback Success!");
                                        //向package数组中增加Package对象
                                        listPackage.add(response);
                                    }
                                });
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
                            .addHeader("Authorization", " Bearer " + token)
                            .mediaType(MediaType.parse("application/json; charset=utf-8"))
                            .content(new Gson().toJson(trade))
                            .build()
                            .execute(new MyStringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    super.onError(call, e, id);
                                    Toast.makeText(getActivity(), "交易失败请重试！", Toast.LENGTH_LONG).show();
                                    Log.i("Tag", "厂商交易界面 POST /trades 失败！");
                                    Log.i("Tag", new Gson().toJson(trade));
                                    if (id == 401)
                                    {
                                        String userName = userSQLite.getUser().getUserName();
                                        token = new RefreshTokenUtil().refreshToken(userName);
                                        Toast.makeText(MainActivity.mainActivity, "请求过期，请重试", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    super.onResponse(response, id);
                                    Toast.makeText(getActivity(), "交易成功！", Toast.LENGTH_LONG).show();
                                    Log.i("Tag", "厂商交易界面 POST /trades 成功！");
                                }
                            });
                    listPackage.clear();
                    //交易完毕后清空待交易信息
                    TradeExportSQLite tradeExportSQLite = new TradeExportSQLite(MainActivity.mainActivity);
                    tradeExportSQLite.deleteAllTrade();
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

    public static void setDefaultStatusIO(){
        status_IO = -1;
    }

}
