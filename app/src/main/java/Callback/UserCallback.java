package Callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import Bean.User;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/23.
 */
public abstract class UserCallback extends Callback<User> {
    @Override
    public User parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        User user = new Gson().fromJson(string, new TypeToken<User>() {
        }.getType());
        return user;
    }
}
