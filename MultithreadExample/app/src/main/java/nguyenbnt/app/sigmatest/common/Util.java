package nguyenbnt.app.sigmatest.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import nguyenbnt.app.sigmatest.R;

public class Util {
    private static final String TAG = "Util";

    /**
     * Check whether location permission is granted or not
     * @param context application context
     * @return
     */
    public static boolean checkLocationPermissions(Context context) {
        if (context == null) {
            return false;
        }
        boolean isGranted = true;
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            for (String permission : Constant.LOCATION_PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false;
                    break;
                }
            }
        }
        return isGranted;
    }

    /**
     * Request location permission
     * @param activity
     */
    public static void requestLocationPermissions(Activity activity) {
        if (activity == null) {
            Log.e(TAG, "requestLocationPermissions():  activity is null");
            return;
        }
        ActivityCompat.requestPermissions(activity, Constant.LOCATION_PERMISSIONS,
                Constant.LOCATION_PERMISSION_CODE);
    }

    /**
     * Method to Check location is enable or disable
     * @param locationManager
     * @return
     */
    public static boolean isGpsEnabled(LocationManager locationManager) {
        boolean gpsStatus = false;
        if (locationManager != null) {
            gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.i(TAG, "isGpsEnabled: " + gpsStatus);
        }
        return gpsStatus;
     }

     /**
     * Request turn on GPS on phone
     * @param context
     */
    public static void requestTurnOnGps(Context context) {
        if (context == null) {
            Log.e(TAG, "requestTurnOnGps():  context is null");
            return;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (Util.isGpsEnabled(locationManager) == false) {
            if (context instanceof Activity) {
                Util.notifyUser((Activity) context, R.string.notify_turn_on_location);
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
            }
        }
    }

    /**
     * Notify a message to user by Toast
     * @param activity
     * @param message
     */
    public static void notifyUser(final Activity activity, final String message) {
        if (activity == null) {
            return;
        }
        Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Notify a message to user by Toast
     * @param activity
     * @param messageId
     */
    public static void notifyUser(final Activity activity, final int messageId) {
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), messageId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Notify a message to user by Toast
     * @param appContext
     * @param message
     */
    public static void notifyUser(final Context appContext, final String message) {
        if (appContext == null) {
            return;
        }
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Notify a message to user by Toast
     * @param appContext
     * @param messageId
     */
    public static void notifyUser(final Context appContext, final int messageId) {
        if (appContext == null) {
            return;
        }
        Toast.makeText(appContext, messageId, Toast.LENGTH_SHORT).show();
    }
}
