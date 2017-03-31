package utils;

import android.util.Log;

import com.novadata.batteryapp.LoginActivity;
import com.novadata.batteryapp.MainActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import Callback.MyStringCallback;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/3/29.
 */
public class RefreshTokenUtil {

    public RefreshTokenUtil() {
    }

    private String token = "";

    public String refreshToken(String companyName)
    {
        String url = MainActivity.getBaseUrl() + "login/refresh";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new MyStringCallback(){
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        Log.i("Tag", "Refresh Token ERROR!");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        super.onResponse(response, id);
                        Log.i("Tag", "Refresh Token SUCCESS!");
                        token = response;
                    }
                });
        UserSQLite userSQLite = new UserSQLite(LoginActivity.loginActivity);
        userSQLite.updateUser(companyName, token);
        return token;
    }

}
