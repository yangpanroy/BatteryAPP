package Callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.List;

import Bean.Main_Search_History_Item;
import okhttp3.Response;

public abstract class ListMainSearchHistoryItemCallback extends Callback<List<Main_Search_History_Item>>
{

    @Override
    public List<Main_Search_History_Item> parseNetworkResponse(Response response, int id) throws IOException {
        String string = response.body().string();
        List<Main_Search_History_Item> mainSearchHistoryItems = new Gson().fromJson(string, new TypeToken<List<Main_Search_History_Item>>(){}.getType());
        return mainSearchHistoryItems;
    }
}
