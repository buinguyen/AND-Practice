package exercise.app.nguyenbnt.mhexercise;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import exercise.app.nguyenbnt.mhexercise.common.Constant;
import exercise.app.nguyenbnt.mhexercise.model.Position;
import exercise.app.nguyenbnt.mhexercise.model.PositionsResponse;
import exercise.app.nguyenbnt.mhexercise.model.Route;
import exercise.app.nguyenbnt.mhexercise.network.RetrofitClient;
import exercise.app.nguyenbnt.mhexercise.task.IGetRoutesCallback;
import exercise.app.nguyenbnt.mhexercise.util.AndroidHelper;
import exercise.app.nguyenbnt.mhexercise.util.LocationHelper;
import exercise.app.nguyenbnt.mhexercise.util.PermissionUtils;
import exercise.app.nguyenbnt.mhexercise.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static exercise.app.nguyenbnt.mhexercise.common.Constant.DEFAULT_ZOOM;
import static exercise.app.nguyenbnt.mhexercise.common.Constant.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static exercise.app.nguyenbnt.mhexercise.common.Constant.REQUEST_CHECK_SETTINGS;
import static exercise.app.nguyenbnt.mhexercise.common.Constant.UPDATE_INTERVAL_IN_MILLISECONDS;

public class MapsActivity extends AppCompatActivity implements  OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        IGetRoutesCallback {
    private static final String TAG = MapsActivity.class.getSimpleName();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Context mContext;
    private AlertDialog mDialog;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private int mNotificationId = 0;
    private volatile List<Location> mLocationList;
    private List<Marker> mMarkerList;
    private Polyline mPolyline;

    private int mScreenWidth;
    private int mScreenHeight;

    private Unbinder mUnbinder;

    @BindView(R.id.tv_total_distance)
    TextView mTvTotalDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUnbinder = ButterKnife.bind(this);

        mContext = this;

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        DisplayMetrics metrics =  new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;

        getPositionList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");

        mUnbinder.unbind();

        stopLocationUpdates();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        if (PermissionUtils.checkLocationPermission(this) == false) {
            showPermissionAlert();
        } else {
            initMap();

            initLocationClient();
        }
    }

    private void initMap() {
        Log.i(TAG, "initMap()");
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void initLocationClient() {
        Log.i(TAG, "initLocationClient()");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        startLocationUpdates();
    }

    private void parseLocationList(List<Position> list) {
        if (mLocationList != null) {
            mLocationList.clear();
        } else {
            mLocationList = new ArrayList<>();
        }
        if (list == null) {
            return;
        }
        for (Position pos : list) {
            Location location = new Location(pos.name);
            location.setLatitude(pos.latitude);
            location.setLongitude(pos.longitude);
            mLocationList.add(location);
        }
    }

    private void getPositionList() {
        Log.i(TAG, "Start getPositionList()");
        RetrofitClient.getInstance().getPositionList(new Callback<PositionsResponse>() {
            @Override
            public void onResponse(Call<PositionsResponse> call, Response<PositionsResponse> response) {
                Log.i(TAG, "onResponse URL: " + call.request().url());

                List<Position> positionList = null;
                if (response == null || response.body() == null || response.body().getPosList() == null) {
                    // Use dummy data
                    positionList = getDummyPositionList();
                } else {
                    // Parse data from reponse
                    positionList = response.body().getPosList();
                }

                parseLocationList(positionList);
            }

            @Override
            public void onFailure(Call<PositionsResponse> call, Throwable t) {
                Log.i(TAG, "onFailure URL: " + call.request().url());
                // Use dummy data
                List<Position> positionList = getDummyPositionList();
                parseLocationList(positionList);
            }
        });
    }

    /**
     * Dummy list of positions
     */
    private List<Position> getDummyPositionList() {
        Log.i(TAG, "Use dummy data");
        List<Position> list = new ArrayList<>();
        list.addAll(Arrays.asList(Constant.POSITIONS));
        return list;
    }

    @Override
    public synchronized void onRouteResult(List<Route> routeList) {
        if (routeList == null || routeList.size() == 0) {
            Log.e(TAG, "NO ROUTE");
        }

        float totalDistance = LocationHelper.getTotalDistance(routeList);

        updateTotalDistance(totalDistance);

        drawRoutes(routeList);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();

                if (mLocationList == null) {
                    return;
                }

                addMarkersToMap(mLocationList, mCurrentLocation);
                addMarkersBound(mLocationList);

                checkRightLocation(mCurrentLocation);

                if (mLocationList != null) {
                    Log.i(TAG, "Location list size = " + mLocationList.size());
                }
                LocationHelper.getRoutes(MapsActivity.this, mCurrentLocation, mLocationList);
            }
        };
    }

    /** These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.i(TAG, "onMapReady");
        mMap = map;

        configMap();
        addMapEvent();
    }

    private void configMap() {
        if (mMap == null) {
            return;
        }

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setMapType(MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void addMapEvent() {
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);
    }

    /**
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    private void updateMyLocationListener() {
        try {
            mFusedLocationClient
                    .getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        mCurrentLocation = (Location) task.getResult();
                    } else {
                        // Do nothing
                    }
                    moveToLocation(mCurrentLocation, DEFAULT_ZOOM);
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Move maps camera to any location
     * @param location
     * @param zoom
     */
    private void moveToLocation(Location location, float zoom) {
        if (location == null) {
            return;
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then location updates will be requested
     */
    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    //@SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        try {
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, Looper.myLooper());
                        } catch (SecurityException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Utils.notifyUser(getApplicationContext(), errorMessage);
                        }
                    }
                });
    }

    /**
     * Stop location updates
     */
    public void stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates()");
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, "Remove location updates.");
                    }
                });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Utils.notifyUser(this, "MyLocation button clicked");
        // Return false so that we don't consume the event and the default behavior still occurs
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Utils.notifyUser(this, "Current location:\n" + location);
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
     * Hide permission alert dialog
     */
    public void hideAlert() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * Click OK on permission dialog
     */
    private DialogInterface.OnClickListener
            mOnClickPositiveListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            hideAlert();

            PermissionUtils.requestPermission(MapsActivity.this, LOCATION_PERMISSION_REQUEST_CODE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

    }

    /**
     * Mark list of positions on maps
     * @param positionList
     * @param currenLocation
     */
    private void addMarkersToMap(List<Location> positionList, Location currenLocation) {
        if (positionList == null || positionList.size() == 0 || currenLocation == null) {
            return;
        }
        if (mMarkerList == null) {
            mMarkerList = new ArrayList<>();
        }
        for (Marker marker : mMarkerList) {
            marker.remove();
        }
        mMarkerList.clear();

        for (Location loc : positionList) {
            float result = loc.distanceTo(currenLocation);
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(loc.getProvider())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .snippet(getString(R.string.snippet_distance, result))
            );
            mMarkerList.add(marker);
        }
    }

    /**
     * Add markers bound for maps
     * @param positionList
     */
    private void addMarkersBound(List<Location> positionList) {
        if (positionList == null || positionList.size() == 0) {
            return;
        }
        LatLngBounds.Builder buider = new LatLngBounds.Builder();
        for (Location loc : positionList) {
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            buider.include(latLng);
        }
        LatLngBounds bound = buider.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bound, mScreenWidth,
                mScreenHeight, Constant.BOUND_PADDING));
    }

    /**
     * Update total distance between list of positions
     * @param total
     */
    private void updateTotalDistance(final float total) {
        Log.i(TAG, "updateTotalDistance()");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvTotalDistance.setText(String.valueOf(total));
            }
        });
    }

    /**
     * Draw route between list of locations
     * @param routeList
     */
    private void drawRoutes(List<Route> routeList) {
        if (mPolyline != null) {
            mPolyline.remove();
        }
        for (Route route : routeList) {
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.width(5);
            lineOptions.color(Color.BLUE);
            lineOptions.add(new LatLng(route.start.getLatitude(), route.start.getLongitude()));
            lineOptions.add(new LatLng(route.end.getLatitude(), route.end.getLongitude()));
            mPolyline = mMap.addPolyline(lineOptions);
        }
    }

    /**
     * Draw route between list of locations
     * Find directions with Google API.
     * @param routeList
     */
    private void drawRoutesWithGoogleApi(List<Route> routeList) {

    }

    private void getDirectionsByGoogleApi() {

    }

    private void checkRightLocation(Location currentLocation) {
        if (mLocationList == null || currentLocation == null) {
            return;
        }
        for (Location location : mLocationList) {
            if (LocationHelper.checkRightLocation(currentLocation, location, Constant.ODD_IN_METER)){
                AndroidHelper.pushNotification(getApplicationContext(), location, mNotificationId++);
                break;
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Do nothing
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // Do nothing
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        // Do nothing
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        // Do nothing
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        // Do nothing
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        // Do nothing
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // Do nothing
    }
}
