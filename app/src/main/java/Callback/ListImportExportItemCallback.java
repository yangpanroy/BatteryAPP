package Callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import Bean.Import_Export_Item;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/7.
 */
public abstract class ListImportExportItemCallback extends Callback<List<Import_Export_Item>> {
    @Override
    public List<Import_Export_Item> parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        List<Import_Export_Item> import_Export_Items = new Gson().fromJson(string, new TypeToken<List<Import_Export_Item>>(){}.getType());
        return import_Export_Items;
    }
}
