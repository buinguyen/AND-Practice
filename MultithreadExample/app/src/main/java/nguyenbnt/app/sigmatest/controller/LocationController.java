package nguyenbnt.app.sigmatest.controller;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import nguyenbnt.app.sigmatest.common.Constant;
import nguyenbnt.app.sigmatest.common.Util;

/**
 * This class is to control some action relate to location
 */
public class LocationController {
    private final String TAG = LocationController.class.getSimpleName();

    private Context mContext;
    private LocationManager mLocationManager;
    private LocationControllerCallback mCallback;
    private Location mLastLocation;

    private String[] mLocationProvider = {
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER};
    private LocationListener[] mLocationListeners = new LocationListener[]{
            new MyLocationListener(LocationManager.GPS_PROVIDER),
            new MyLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    /**
     * Callback to communicate with thread/service whenever location updated
     */
    public interface LocationControllerCallback {
        void onUpdateLocation(Location location, boolean initial);
    }

    /**
     * Listener to catch location update
     */
    class MyLocationListener implements LocationListener {
        public MyLocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            if (mCallback != null) {
                mCallback.onUpdateLocation(location, false);
            }
        }

        @Override
        public void onStatusChanged(String provider, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    /**
     * Constructor
     * @param context
     * @param callback
     */
    public LocationController(Context context, LocationControllerCallback callback) {
        this.mContext = context;
        this.mCallback = callback;

        initializeLocationManager();
    }

    /**
     * Init LocationManager
     */
    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    /**
     * Request to listen location update from system
     */
    public void requestLocationUpdates() {
        if (mLocationManager == null) {
            Log.e(TAG, "LocationManager is null");
            return;
        }

        for (int i = 0; i < mLocationListeners.length; i++) {
            try {
                mLocationManager.requestLocationUpdates(mLocationProvider[i],
                        Constant.FIVE_SECONDS, Constant.LOCATION_DISTANCE, mLocationListeners[i]);
            } catch (SecurityException e) {
                Log.i(TAG, e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.i(TAG, e.getMessage());
            }
        }
    }

    /**
     * Remove listen location update from system
     */
    public void removeLocationUpdates() {
        if (mLocationManager == null) {
            Log.e(TAG, "LocationManager is null");
            return;
        }

        for (int i = 0; i < mLocationListeners.length; i++) {
            try {
                mLocationManager.removeUpdates(mLocationListeners[i]);
            } catch (Exception e) {
                Log.i(TAG, "Failed to remove location listener, ignore", e);
            }
        }
    }
}
