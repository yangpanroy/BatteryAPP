package Callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import Bean.Car;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/1.
 */
public abstract class CarCallback extends Callback<Car> {
    @Override
    public Car parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Car car = new Gson().fromJson(string, new TypeToken<Car>() {
        }.getType());
        return car;
    }
}
