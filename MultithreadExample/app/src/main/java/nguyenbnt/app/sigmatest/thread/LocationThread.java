package nguyenbnt.app.sigmatest.thread;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import nguyenbnt.app.sigmatest.common.Constant;
import nguyenbnt.app.sigmatest.common.Util;
import nguyenbnt.app.sigmatest.controller.LocationController;

/**
 * This thread is to get location information after every 6 minutes.
 * If have location information, put to data list
 */
public class LocationThread extends Thread implements LocationController.LocationControllerCallback{
    private final String TAG = LocationThread.class.getSimpleName();

    private Context mContext;
    private Timer mTimer;
    private LocationController mLocationController;
    private boolean isRunning = false;
    private Location mLastLocation;

    /**
     * Constructor
     * @param context
     */
    public LocationThread(Context context) {
        this.mContext = context;
        this.mTimer = new Timer();
        this.setDaemon(true);

        mLocationController = new LocationController(this.mContext, this);
        mLocationController.requestLocationUpdates();
    }

    @Override
    public void onUpdateLocation(Location location, boolean initial) {
        mLastLocation = location;
    }

    @Override
    public void run() {
        Log.e(TAG, "Thread is started");
        isRunning = true;

        if (Util.checkLocationPermissions(mContext) == false) {
            Toast.makeText(mContext, "Location permission is not granted!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Use timer to get location info after 6 minutes
        mTimer.scheduleAtFixedRate(new GetLocationTask(mContext), Constant.TEN_SECONDS, Constant.SIX_MINUTES);
    }

    /**
     * Stop thread
     */
    public void stopThread() {
        Log.e(TAG, "Thread is stopped");
        mLocationController.removeLocationUpdates();
        mTimer.cancel();
        isRunning = false;
        interrupt();
    }

    /**
     * Check whether this thread is running or not
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Task to get locations every 6 minutes
     */
    class GetLocationTask extends TimerTask {

        private Context mContext;

        public GetLocationTask(Context context) {
            this.mContext = context;
        }

        @Override
        public void run() {
            Location location = mLastLocation;
            if (location != null) {
                Log.d(TAG, "THREAD = (" + location.getLatitude() +
                        " - " + location.getLongitude() + ")");
                // Send location information to data receiver
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.KEY_LOCATION, location);
                Intent intent = new Intent(Constant.INTENT_FILTER_LOCATION);
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            } else {
                Log.d(TAG, "Location is null");
            }
        }
    }
}
