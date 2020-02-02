package nguyenbnt.app.sigmatest.thread;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import nguyenbnt.app.sigmatest.common.Constant;
import nguyenbnt.app.sigmatest.controller.BatteryController;
import nguyenbnt.app.sigmatest.service.BatteryService;

/**
 * This thread is to get battery information after every 9 minutes.
 * If have battery information, put to data list
 */
public class BatteryThread extends Thread implements BatteryController.BatteryControllerCallback {
    private final String TAG = BatteryThread.class.getSimpleName();

    private Context mContext;
    private Timer mTimer = new Timer();
    private int mBateryLevel = -1;
    private BatteryController mBatteryController;
    private boolean isRunning = false;

    /**
     * Constructor
     * @param context
     */
    public BatteryThread(Context context) {
        this.mContext = context;
        this.mTimer = new Timer();
        this.setDaemon(true);

        mBatteryController = new BatteryController(this.mContext, this);
        mBatteryController.registerBroadcast();
    }

    @Override
    public void onUpdateBattery(int battery) {
        mBateryLevel = battery;
    }

    @Override
    public void run() {
        Log.e(TAG, "Thread is started");
        isRunning = true;

        // Use timer to get battery info after 9 minutes
        mTimer.scheduleAtFixedRate(new GetBatteryTask(), Constant.TEN_SECONDS, Constant.NINE_MINUTES);
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
        mBatteryController.unregisterBroadcast();
        mTimer.cancel();
        isRunning = false;
        interrupt();
    }

    /**
     * Task to get battery level every 9 minutes
     */
    class GetBatteryTask extends TimerTask {

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
}
