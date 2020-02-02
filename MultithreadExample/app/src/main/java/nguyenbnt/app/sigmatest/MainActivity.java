package nguyenbnt.app.sigmatest;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import nguyenbnt.app.sigmatest.common.Constant;
import nguyenbnt.app.sigmatest.service.BatteryService;
import nguyenbnt.app.sigmatest.service.DataReceiverService;
import nguyenbnt.app.sigmatest.common.Util;
import nguyenbnt.app.sigmatest.service.LocationService;
import nguyenbnt.app.sigmatest.thread.BatteryThread;
import nguyenbnt.app.sigmatest.thread.DataReceiverThread;
import nguyenbnt.app.sigmatest.thread.LocationThread;

/**
 * This is main activity
 * Provide buttons to start, stop threads
 * Show results
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private AlertDialog mDialog;

    private TextView mTvPostingStatus;
    private TextView mTvBattery;
    private TextView mTvLocation;

    private LocationThread mLocationThread = null;
    private BatteryThread mBatteryThread = null;
    private DataReceiverThread mDataReceiverThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);
        Button btnStop = findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(this);

        mTvPostingStatus = findViewById(R.id.tv_posting_status);
        mTvBattery = findViewById(R.id.tv_battery);
        mTvBattery.setMovementMethod(new ScrollingMovementMethod());
        mTvLocation = findViewById(R.id.tv_location);
        mTvLocation.setMovementMethod(new ScrollingMovementMethod());

        registerPostingSignal();
        registerReceiveData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.checkLocationPermissions(getApplicationContext()) == false) {
            showPermissionAlert();
        } else {
            LocationManager locationManager = (LocationManager) getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);
            if (Util.isGpsEnabled(locationManager) == false){
                showTurnOnGpsAlert();
            } else {
                Util.notifyUser(this, R.string.notify_enable_gps);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");

        stopThreads();
        unregisterPostingSignal();
        unregisterReceiveData();
    }

    /**
     * Update posting data status on UI
     */
    private void updatePostingStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String status = getString(R.string.text_posting_status, dateFormat.format(date));
                mTvPostingStatus.setText(status);
            }
        });
    }

    /**
     * Update location information on UI
     * @param latitude
     * @param longitude
     */
    private void updateLocation(final double latitude, final double longitude) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String content = mTvLocation.getText().toString();
                String status = getString(R.string.text_location_status, content, latitude, longitude);
                mTvLocation.setText(status);
            }
        });
    }

    /**
     * Update battery information on UI
     * @param battery
     */
    private void updateBattery(final int battery) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String content = mTvBattery.getText().toString();
                String status = getString(R.string.text_battery_status, content, battery);
                mTvBattery.setText(status);
            }
        });
    }

    /**
     * Start a new service
     * @param intent
     */
    public void startNewService(Intent intent) {
        if (intent == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    /**
     * Start threads T1, T2, T3
     * Call stopServices() to stop
     */
    public void startThreads() {
        mLocationThread = new LocationThread(mContext);
        if (mLocationThread.isRunning() == false) {
            mLocationThread.start();
        }

        mBatteryThread = new BatteryThread(mContext);
        if (mBatteryThread.isRunning() == false) {
            mBatteryThread.start();
        }

        mDataReceiverThread = new DataReceiverThread(mContext);
        if (mDataReceiverThread.isRunning() == false) {
            mDataReceiverThread.start();
        }
    }

    /**
     * Stop threads T1, T2, T3
     */
    public void stopThreads() {
        if (mLocationThread != null && mLocationThread.isRunning()) {
            mLocationThread.stopThread();
        }
        if (mBatteryThread != null && mBatteryThread.isRunning()) {
            mBatteryThread.stopThread();
        }
        if (mDataReceiverThread != null && mDataReceiverThread.isRunning()) {
            mDataReceiverThread.stopThread();
        }
    }

    /**
     * Start services
     * Call stopServices() to stop
     */
    public void startServices() {
        if (!isMyServiceRunning(LocationService.class)) {
            startNewService(new Intent(this, LocationService.class));
        }
        if (!isMyServiceRunning(BatteryService.class)) {
            startNewService(new Intent(this, BatteryService.class));
        }
        if (!isMyServiceRunning(DataReceiverService.class)) {
            startNewService(new Intent(this, DataReceiverService.class));
        }
    }

    /**
     * Stop services
     */
    public void stopServices() {
        if (isMyServiceRunning(LocationService.class)) {
            stopService(new Intent(this, LocationService.class));
        }
        if (isMyServiceRunning(BatteryService.class)) {
            stopService(new Intent(this, BatteryService.class));
        }
        if (isMyServiceRunning(DataReceiverService.class)) {
            stopService(new Intent(this, DataReceiverService.class));
        }
    }

    /**
     * Show permission alert dialog which notifies user about location permission
     */
    public void showPermissionAlert() {
        mDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.permission_alert_title)
                .setMessage(R.string.permission_alert_msg)
                .setPositiveButton(R.string.permission_alert_positive_text, mOnClickPositiveListener)
                .setNegativeButton(R.string.permission_alert_negative_text, mOnClickNegativeListener)
                .create();
        mDialog.show();
    }

    /**
     * Show dialog which notifies user turn on GPS
     */
    public void showTurnOnGpsAlert() {
        mDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.location_alert_title)
                .setMessage(R.string.location_alert_msg)
                .setPositiveButton(R.string.location_alert_positive_text, mOnClickPositiveLocationListener)
                .create();
        mDialog.show();
    }

    public void hideAlert() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * Check whether a service is running or not
     * @param serviceClass
     * @return
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Register listen posting status
     */
    private void registerPostingSignal() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mPostingReceiver,
                new IntentFilter(Constant.INTENT_FILTER_POSTING));
    }

    /**
     * Unregister listen posting status
     */
    private void unregisterPostingSignal() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPostingReceiver);
    }

    /**
     * Register listen to receive battery and location information
     */
    private void registerReceiveData() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.INTENT_FILTER_LOCATION);
        intentFilter.addAction(Constant.INTENT_FILTER_BATTERY);
        LocalBroadcastManager.getInstance(this).registerReceiver(mDataReceiver, intentFilter);
    }

    /**
     * Unregister listen to receive battery and location information
     */
    private void unregisterReceiveData() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mDataReceiver);
    }

    /**
     * BroadcastReceiver for posting request
     */
    private BroadcastReceiver mPostingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String data = bundle.getString(Constant.KEY_DATA);
                updatePostingStatus();
            }
        }
    };

    /**
     * Receive battery and location information from broadcast
     */
    private BroadcastReceiver mDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized (this) {
                if (intent.getAction().equals(Constant.INTENT_FILTER_BATTERY)) {
                    Bundle bundle = intent.getExtras();
                    int battery = bundle.getInt(Constant.KEY_BATTERY, -1);
                    if (battery == -1) {
                        Log.i(TAG, "BATTERY: Data is invalid");
                    } else {
                        // Update to UI
                        updateBattery(battery);
                    }
                } else if (intent.getAction().equals(Constant.INTENT_FILTER_LOCATION)) {
                    Bundle bundle = intent.getExtras();
                    Location location = bundle.getParcelable(Constant.KEY_LOCATION);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        if (latitude == -1 || longitude == -1) {
                            Log.i(TAG, "LOCATION: Data is invalid");
                        } else {
                            // Update to UI
                            updateLocation(latitude, longitude);
                        }
                    }
                }
            }
        }
    };

    /**
     * Click OK on permission dialog
     */
    private DialogInterface.OnClickListener
            mOnClickPositiveListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            hideAlert();
            if (mContext != null) {
                Util.requestLocationPermissions((Activity) mContext);
            }
        }
    };

    /**
     * Click Quit on permission dialog
     */
    private DialogInterface.OnClickListener
            mOnClickNegativeListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            hideAlert();
            if (mContext != null) {
                ((Activity) mContext).finishAffinity();
            }
        }
    };

    /**
     * Click OK on location dialog
     */
    private DialogInterface.OnClickListener
            mOnClickPositiveLocationListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            hideAlert();
            if (mContext != null) {
                Util.requestTurnOnGps(mContext);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constant.LOCATION_PERMISSION_CODE) {
            // Do nothing
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                startThreads();
                break;
            case R.id.btn_stop:
                stopThreads();
                break;
        }
    }
}
