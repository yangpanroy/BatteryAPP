package layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novadata.batteryapp.LoginActivity;
import com.novadata.batteryapp.MainActivity;
import com.novadata.batteryapp.R;

import static android.app.Activity.RESULT_OK;

public class fragment_user extends Fragment implements  View.OnClickListener {

    View view;
    TextView loginButton, logoutButton, user_name, user_detail;

    int login_status = -1;

    static final int DEFAULT_STATUS = -1, USER_4S = 1, USER_COMPANY_IP = 0, USER_COMPANY_EP = 2;
    public static String importCompany = "深圳比克汽车公司", exportCompany = "上海腾飞汽车公司", fourSCompany = "北京速驰4S店";

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

        loginButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

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
                Intent intent = new Intent("Login_status");
                intent.putExtra("login_status", login_status);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                doLogIn(login_status);
            }
        }
    }

    private void doLogIn(int login_status) {
        switch (login_status)
        {
            case USER_COMPANY_IP:
                loginButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
                user_detail.setText("点击查看详细信息");
                user_name.setText(importCompany);
                MainActivity.mainActivity.setMain_username(importCompany);
                break;
            case USER_COMPANY_EP:
                loginButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
                user_detail.setText("点击查看详细信息");
                user_name.setText(exportCompany);
                MainActivity.mainActivity.setMain_username(exportCompany);
                break;
            case USER_4S:
                loginButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
                user_detail.setText("点击查看详细信息");
                user_name.setText(fourSCompany);
                MainActivity.mainActivity.setMain_username(fourSCompany);
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
        //广播登录状态，使得交易界面变为默认
        Intent intent = new Intent("Login_status");
        intent.putExtra("login_status", DEFAULT_STATUS);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

}
