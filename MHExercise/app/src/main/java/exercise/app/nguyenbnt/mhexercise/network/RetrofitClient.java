package exercise.app.nguyenbnt.mhexercise.network;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exercise.app.nguyenbnt.mhexercise.common.Constant;
import exercise.app.nguyenbnt.mhexercise.model.DistanceResponse;
import exercise.app.nguyenbnt.mhexercise.model.PositionsResponse;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = RetrofitClient.class.getSimpleName();

    private Context mContext;
    private Retrofit retrofit = null;
    private IServerApi services = null;

    private static RetrofitClient sInstance;

    public static RetrofitClient getInstance() {
        if (sInstance == null) {
            synchronized(RetrofitClient.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitClient();
                }
            }
        }
        return sInstance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public interface RetrofitClientCallback {
        void onData(String data);
    }

    private RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofit = retrofitBuilder.baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        services = retrofit.create(IServerApi.class);
    }

    public void getDistanceInfo(Callback<DistanceResponse> callback, Location loc1, Location loc2) {
        String origin = loc1.getLatitude() + "," + loc1.getLongitude();
        String destination = loc2.getLatitude() + "," + loc2.getLongitude();

        Map<String, String> mapQuery = new HashMap<>();
        mapQuery.put("units", "imperial");
        mapQuery.put("origins", origin);
        mapQuery.put("destinations", destination);

        if (services != null) {
            Call<DistanceResponse> call = services.getDistanceInfo(mapQuery);
            call.enqueue(callback);
        }
    }

    public DistanceResponse getDistanceInfo(Location loc1, Location loc2) {
        String origin = loc1.getLatitude() + "," + loc1.getLongitude();
        String destination = loc2.getLatitude() + "," + loc2.getLongitude();

        Map<String, String> mapQuery = new HashMap<>();
        mapQuery.put("units", "imperial");
        mapQuery.put("origins", origin);
        mapQuery.put("destinations", destination);

        DistanceResponse distanceResponse = null;
        if (services != null) {
            Call<DistanceResponse> call = services.getDistanceInfo(mapQuery);
            try {
                distanceResponse = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return distanceResponse;
    }

    public void getPositionList(Callback<PositionsResponse> callback) {
        if (services != null) {
            Call<PositionsResponse> call = services.getPositionList();
            call.enqueue(callback);
        }
    }
}
