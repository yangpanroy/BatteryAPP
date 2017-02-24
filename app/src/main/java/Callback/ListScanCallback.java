package Callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import Bean.Scan;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/24.
 */
public abstract class ListScanCallback extends Callback<List<Scan>> {
    @Override
    public List<Scan> parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        List<Scan> scans = new Gson().fromJson(string, new TypeToken<List<Scan>>(){}.getType());
        return scans;
    }
}
