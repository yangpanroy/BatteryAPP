package Callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import Bean.Trade;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/28.
 */
public abstract class TradeCallback extends Callback<Trade> {
    @Override
    public Trade parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Trade trade = new Gson().fromJson(string, new TypeToken<Trade>() {}.getType());
        return trade;
    }
}
