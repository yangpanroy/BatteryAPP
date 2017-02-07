package Callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import Bean.Search_Result_Item;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/7.
 */
public abstract class ListSearchResultItemCallback extends Callback<List<Search_Result_Item>> {
    @Override
    public List<Search_Result_Item> parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        List<Search_Result_Item> searchResultItems = new Gson().fromJson(string, new TypeToken<List<Search_Result_Item>>(){}.getType());
        return searchResultItems;
    }
}
