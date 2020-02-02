package nguyenbnt.app.sigmatest.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * This class is to control some action relate to battery
 */
public class BatteryController {
    private final String TAG = BatteryController.class.getSimpleName();

    private Context mContext;
    private BatteryControllerCallback mCallback;

    /**
     * Receiver to receive battery information from system.
      */
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            if (mCallback != null) {
                mCallback.onUpdateBattery(battery);
            }
        }
    };

    /**
     * Callback to communicate with thread/service whenever battery updated
      */
    public interface BatteryControllerCallback {
        void onUpdateBattery(int battery);
    }

    /**
     * Constructor
     * @param context
     * @param callback
     */
    public BatteryController(Context context, BatteryControllerCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    /**
     * Register a receiver to listen battery changed
     */
    public void registerBroadcast() {
        mContext.registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    /**
     * Unregister a receiver to listen battery changed
     */
    public void unregisterBroadcast() {
        mContext.unregisterReceiver(mBatInfoReceiver);
    }
}
