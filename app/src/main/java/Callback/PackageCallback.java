package Callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import Bean.Package;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/24.
 */
public abstract class PackageCallback extends Callback<Package> {
    @Override
    public Package parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Package aPackage = new Gson().fromJson(string, new TypeToken<Package>() {}.getType());
        return aPackage;
    }
}
