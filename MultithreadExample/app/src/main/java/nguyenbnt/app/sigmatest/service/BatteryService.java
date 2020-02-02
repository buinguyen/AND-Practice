package nguyenbnt.app.sigmatest.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import nguyenbnt.app.sigmatest.common.Constant;
import nguyenbnt.app.sigmatest.controller.BatteryController;

/**
 * This service is to get battery information after every 9 minutes.
 * If have battery information, put to data list
 */
public class BatteryService extends Service implements BatteryController.BatteryControllerCallback{
    private final String TAG = BatteryService.class.getSimpleName();

    private Timer mTimer = new Timer();
    private int mBateryLevel = -1;
    private BatteryController mBatteryController;
    private boolean isRunning = false;

    @Override
    public void onUpdateBattery(int battery) {
        mBateryLevel = battery;
    }

    /**
     * Task to get battery level every 9 minutes
     */
    class GetBatteryTask extends TimerTask {

        private Context mContext;

        public GetBatteryTask(Context context) {
            this.mContext = context;
        }

        @Override
        public void run() {
            Log.i(TAG, "Get battery level: " + mBateryLevel);
            if (mBateryLevel != -1) {
                // Send battery information to data receiver
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.KEY_BATTERY, mBateryLevel);
                Intent intent = new Intent(Constant.INTENT_FILTER_BATTERY);
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");
        super.onStartCommand(intent, flags, startId);

        if (!isRunning) {
            isRunning = true;

            mBatteryController = new BatteryController(this, this);
            mBatteryController.registerBroadcast();
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");

        // Use timer to get battery info after 9 minutes
        mTimer.scheduleAtFixedRate(new GetBatteryTask(this),
                Constant.TEN_SECONDS, Constant.NINE_MINUTES);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();

        mTimer.cancel();
        mBatteryController.unregisterBroadcast();
        isRunning = false;
    }
}
