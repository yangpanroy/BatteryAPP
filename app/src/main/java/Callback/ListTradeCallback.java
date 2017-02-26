package Callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import Bean.Trade;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/24.
 */
public abstract class ListTradeCallback extends Callback<List<Trade>> {
    @Override
    public List<Trade> parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        List<Trade> trades = new Gson().fromJson(string, new TypeToken<List<Trade>>(){}.getType());
        return trades;
    }
}
