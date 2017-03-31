package layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.novadata.batteryapp.DealDetailActivity;
import com.novadata.batteryapp.LoginActivity;
import com.novadata.batteryapp.MainActivity;
import com.novadata.batteryapp.R;
import com.novadata.batteryapp.UrlSettingActivity;

import Bean.User;
import utils.HistorySQLite;
import utils.TradeExportSQLite;
import utils.UserSQLite;

import static android.app.Activity.RESULT_OK;

public class fragment_user extends Fragment implements  View.OnClickListener {

    View view;
    TextView loginButton, logoutButton, user_name, user_detail;

    private static int login_status = -1;

    static final int DEFAULT_STATUS = -1, USER_4S = 1, USER_COMPANY_CAR = 0, USER_COMPANY_BATTERY = 2;
    public static final String creditCode_IP = "911199R", creditCode_EP = "91889R", creditCode_4S = "91888R";
    public static final String importCompany = "深圳比克汽车公司", exportCompany = "深圳电池厂商", fourSCompany = "北京速驰4S店";
    public static final String importCompanyId = "507f191e810c19729de860ec", exportCompanyId = "58b7eda21ff4a3361c0dd62c", fourSCompanyId = "507f191e810c19729de860ea";
    public static final String importCompanyBranch = "第一分公司", exportCompanyBranch = "第二分公司", fourSCompanyBranch = "海淀分店";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user, container, false);

        loginButton = (TextView) view.findViewById(R.id.logIn_button);
        logoutButton = (TextView) view.findViewById(R.id.logOut_button);
        user_name = (TextView) view.findViewById(R.id.user_name);
        user_detail = (TextView) view.findViewById(R.id.user_detail);

        RelativeLayout myDeal_item = (RelativeLayout) view.findViewById(R.id.myDeal_item);
        RelativeLayout setting_item = (RelativeLayout) view.findViewById(R.id.setting_item);
        RelativeLayout clean_item = (RelativeLayout) view.findViewById(R.id.clean_item);

        loginButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        myDeal_item.setOnClickListener(this);
        setting_item.setOnClickListener(this);
        clean_item.setOnClickListener(this);

        login_status = MainActivity.getLogin_status();
        doLogIn(login_status);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logIn_button:
                Intent intent = new Intent(MainActivity.mainActivity, LoginActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("status", DEFAULT_STATUS);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                break;
            case R.id.logOut_button:
                login_status = DEFAULT_STATUS;
                doLogOut();
                break;
            case R.id.myDeal_item:
                if (login_status != DEFAULT_STATUS)
                {
                    Intent intent2 = new Intent(MainActivity.mainActivity, DealDetailActivity.class);
                    Bundle bundle2=new Bundle();
                    bundle2.putString("companyName", user_name.getText().toString());
                    intent2.putExtras(bundle2);
                    startActivity(intent2);
                }
                break;
            case R.id.setting_item:
                startActivity(new Intent(MainActivity.mainActivity, UrlSettingActivity.class));
                break;
            case R.id.clean_item:
                HistorySQLite historySQLite = new HistorySQLite(getActivity());
                historySQLite.deleteAllHistory();
                fragment_main.fragmentMain.clearList();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Bundle bundle=data.getExtras();
            if (requestCode == 0)
            {
                login_status = bundle.getInt("status");
                MainActivity.setLogin_status(login_status);
                doLogIn(login_status);
            }
        }
    }

    private void doLogIn(int login_status) {
        switch (login_status)
        {
            case USER_COMPANY_CAR:
                loginButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
                user_detail.setText("点击查看详细信息");
                user_name.setText(MainActivity.getCompanyName());
                MainActivity.mainActivity.setMain_username(MainActivity.getCompanyName());
                break;
            case USER_COMPANY_BATTERY:
                loginButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
                user_detail.setText("点击查看详细信息");
                user_name.setText(MainActivity.getCompanyName());
                MainActivity.mainActivity.setMain_username(MainActivity.getCompanyName());
                break;
            case USER_4S:
                loginButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
                user_detail.setText("点击查看详细信息");
                user_name.setText(MainActivity.getCompanyName());
                MainActivity.mainActivity.setMain_username(MainActivity.getCompanyName());
                break;
            case DEFAULT_STATUS:
                loginButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.GONE);
                user_detail.setText("登录查看详细信息");
                user_name.setText("未登录");
                MainActivity.mainActivity.setMain_username("未登录");
                break;
        }
    }

    private void doLogOut() {
        //清空所有已经登录的用户名和状态
        doLogIn(DEFAULT_STATUS);
        fragment_deal.setDefaultStatusIO();
        //恢复登录状态，使得交易界面变为默认
        MainActivity.setLogin_status(DEFAULT_STATUS);
        //清空SQLite数据库trade表信息
        TradeExportSQLite tradeExportSQLite = new TradeExportSQLite(MainActivity.mainActivity);
        tradeExportSQLite.deleteAllTrade();
    }

}
