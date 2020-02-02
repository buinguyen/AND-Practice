package nguyenbnt.app.sigmatest.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import nguyenbnt.app.sigmatest.common.Constant;
import nguyenbnt.app.sigmatest.common.Util;
import nguyenbnt.app.sigmatest.controller.LocationController;

/**
 * This service is to get location information after every 6 minutes.
 * If have location information, put to data list
 */
public class LocationService extends Service implements LocationController.LocationControllerCallback{
    private final String TAG = LocationService.class.getSimpleName();

    private Timer mTimer = new Timer();
    private LocationController mLocationController;
    private boolean isRunning = false;
    private Location mLastLocation;

    @Override
    public void onUpdateLocation(Location location, boolean initial) {
        mLastLocation = location;
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
                Log.d(TAG, "getLastKnownLocation = (" + location.getLatitude() +
                        " - " + location.getLongitude() + ")");

                // Send location information to data receiver
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.KEY_LOCATION, location);
                Intent intent = new Intent(Constant.INTENT_FILTER_LOCATION);
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

            mLocationController = new LocationController(this, this);
            mLocationController.requestLocationUpdates();
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");

        if (Util.checkLocationPermissions(getApplicationContext()) == false) {
            Toast.makeText(getApplicationContext(), "Location permission is not granted!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Use timer to get location info after 6 minutes
        mTimer.scheduleAtFixedRate(new GetLocationTask(getApplicationContext()),
                Constant.TEN_SECONDS, Constant.SIX_MINUTES);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();

        mTimer.cancel();
        mLocationController.removeLocationUpdates();
        isRunning = false;
    }
}
