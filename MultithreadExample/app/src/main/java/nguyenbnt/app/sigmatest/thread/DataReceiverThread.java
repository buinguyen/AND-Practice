package nguyenbnt.app.sigmatest.thread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import nguyenbnt.app.sigmatest.common.Constant;
import nguyenbnt.app.sigmatest.common.Util;
import nguyenbnt.app.sigmatest.controller.RetrofitClient;
import nguyenbnt.app.sigmatest.model.Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * This thread is to receive battery and location information, and save to data list.
 * If data list has more than 5 items, send to server
 */
public class DataReceiverThread extends Thread {
    private final String TAG = DataReceiverThread.class.getSimpleName();

    private Context mContext;
    private RetrofitClient mController;
    private boolean isRunning = false;

    private List<Data> mDataList = null;

    public DataReceiverThread(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<>();
        mController = new RetrofitClient();
        registerBroadcast();
    }

    @Override
    public void run() {
        Log.e(TAG, "Thread is started");
        isRunning = true;
    }

    /**
     * Check whether this thread is running or not
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Stop thread
     */
    public void stopThread() {
        Log.e(TAG, "Thread is stopped");
        unregisterBroadcast();
        isRunning = false;
        interrupt();
    }

    /**
     * Use to send broadcast to activity to update UI
     * @param data
     */
    public void sendBroadcastPosting(String data) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_DATA, data);
        Intent intent = new Intent(Constant.INTENT_FILTER_POSTING);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    /**
     * Register listen location/battery changed
     */
    public void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.INTENT_FILTER_LOCATION);
        intentFilter.addAction(Constant.INTENT_FILTER_BATTERY);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mDataReceiver, intentFilter);
    }

    /**
     * Unregister listen location/battery changed
     */
    public void unregisterBroadcast() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mDataReceiver);
    }

    /**
     * In order to add data to list L
     * @param battery
     * @param latitude
     * @param longitude
     */
    public void addDataToList(int battery, double latitude, double longitude) {
        if (mDataList == null) {
            Log.e(TAG, "mDataList is null");
            return;
        }
        Data data = new Data(battery, latitude, longitude);
        mDataList.add(data);
    }

    /**
     * Broadcast receiver which receives location and battery data
     */
    private BroadcastReceiver mDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int battery = -1;
            double latitude = -1;
            double longitude = -1;
            synchronized (mDataList) {
                Log.i(TAG, "mDataReceiver onReceive()");

                if (intent.getAction().equals(Constant.INTENT_FILTER_BATTERY)) {
                    Bundle bundle = intent.getExtras();
                    battery = bundle.getInt(Constant.KEY_BATTERY, -1);
                    if (battery == -1) {
                        Log.i(TAG, "BATTERY: Data is invalid");
                        return;
                    }
                } else if (intent.getAction().equals(Constant.INTENT_FILTER_LOCATION)) {
                    Bundle bundle = intent.getExtras();
                    Location location = bundle.getParcelable(Constant.KEY_LOCATION);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        if (latitude == -1 || longitude == -1) {
                            Log.i(TAG, "LOCATION: Data is invalid");
                            return;
                        }
                    } else {
                        return;
                    }
                }

                // Add to list L
                addDataToList(battery, latitude, longitude);

                // Check to post data to server
                if (mDataList != null && mDataList.size() > Constant.MAX_DATA_LIST) {
                    String data = new Gson().toJson(mDataList);
                    mDataList.clear();

                    // Post data
                    onPostData(data);
                }
            }
        }
    };

    /**
     * This callback is called when posting data to server finished
     */
    retrofit2.Callback<ResponseBody> mPostCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
            if (mContext != null) {
                Util.notifyUser(mContext.getApplicationContext(), response.toString());
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            if (mContext != null) {
                Util.notifyUser(mContext.getApplicationContext(), t.toString());
            }
        }
    };

    /**
     * Posting data to server
     * @param data
     */
    public void onPostData(String data) {
        mController.postData(mPostCallback, data);
        sendBroadcastPosting(data);
    }
}
