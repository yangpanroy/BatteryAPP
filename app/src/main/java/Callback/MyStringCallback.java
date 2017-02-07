package Callback;

import android.util.Log;

import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class MyStringCallback extends StringCallback
{
    //MyStringCallback用于向json server处理有关字符串的请求（Json/Http/Https 字符串callback）

    @Override
    public void onError(Call call, Exception e, int id)
    {
        e.printStackTrace();
    }

    @Override
    public void onResponse(String response, int id)
    {
        Log.i("Tag", "MyStringCallback success");

        switch (id)
        {
            case 100:
                Log.i("http", "http callback success");
                break;
            case 101:
                Log.i("https", "https callback success");
                break;
        }
    }
}
