package layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.novadata.batteryapp.LoginActivity;
import com.novadata.batteryapp.R;

public class fragment_user extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    View view;
    private RadioGroup rg;
    TextView loginButton, back2UserSurfaceButton;
    RelativeLayout debugItem;

    LinearLayout userSurface, devSurface;

    static final int DEFAULT_STATUS = -1, USER_4S = 1, USER_COMPANY = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user, container, false);
        rg = (RadioGroup) view.findViewById(R.id.login_status_rg);
        rg.setOnCheckedChangeListener(this);

        loginButton = (TextView) view.findViewById(R.id.logIn_button);
        back2UserSurfaceButton = (TextView) view.findViewById(R.id.back2userSurface);
        debugItem = (RelativeLayout) view.findViewById(R.id.debug_item);
        userSurface = (LinearLayout) view.findViewById(R.id.user_surface);
        devSurface = (LinearLayout) view.findViewById(R.id.dev_surface);

        loginButton.setOnClickListener(this);
        back2UserSurfaceButton.setOnClickListener(this);
        debugItem.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Intent intent = new Intent("Login_status");
        switch (checkedId){
            case R.id.default_radioButton:
                intent.putExtra("login_status", DEFAULT_STATUS);
                Log.i("Tag","DEFAULT_STATUS");
                break;
            case R.id.user_company_radioButton:
                intent.putExtra("login_status", USER_COMPANY);
                Log.i("Tag", "USER_COMPANY");
                break;
            case R.id.user_4s_radioButton:
                intent.putExtra("login_status", USER_4S);
                Log.i("Tag", "USER_4S");
                break;
        }
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logIn_button:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.debug_item:
                userSurface.setVisibility(View.GONE);
                devSurface.setVisibility(View.VISIBLE);
                break;
            case R.id.back2userSurface:
                userSurface.setVisibility(View.VISIBLE);
                devSurface.setVisibility(View.GONE);
                break;
        }
    }
}
