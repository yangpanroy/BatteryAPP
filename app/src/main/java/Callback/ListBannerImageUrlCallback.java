package Callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/6.
 */
public abstract class ListBannerImageUrlCallback extends Callback<List<String>> {
    @Override
    public List<String> parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        List bannerImageUrl = new Gson().fromJson(string, List.class);
        List<String> bannerImageUrls = new ArrayList<String>();
        for (int i = 0; i < bannerImageUrl.size(); i++)
        {
            bannerImageUrls.add(String.valueOf(bannerImageUrl.get(i)));
        }
        return bannerImageUrls;
    }
}
