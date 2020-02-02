package nguyenbnt.app.sigmatest.controller;

import android.content.Context;
import android.util.Log;

import nguyenbnt.app.sigmatest.common.Constant;
import nguyenbnt.app.sigmatest.service.IPostService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * This class provides API to post data to server
 */
public class RetrofitClient {
    private final String TAG = RetrofitClient.class.getSimpleName();
    private Retrofit retrofit = null;

    private IPostService services = null;

    /**
     * Constructor
     */
    public RetrofitClient() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(Constant.BASE_URL);
        retrofit = retrofitBuilder.build();
        services = retrofit.create(IPostService.class);
    }

    /**
     * Post data to server using Retrofit client
     * @param callback
     * @param data
     */
    public void postData(Callback<ResponseBody> callback, String data) {
        Log.d(TAG, "postData()");
        if (services != null) {
            Log.d(TAG, "Start posting data");
            Call<ResponseBody> call = services.postData(data);
            call.enqueue(callback);
        }
    }
}
