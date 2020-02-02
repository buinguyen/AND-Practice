package nguyenbnt.app.sigmatest.common;

import android.Manifest;

public class Constant {
    public static final int FIVE_SECONDS = 1000 * 5;
    public static final int TEN_SECONDS = 1000 * 10;
    public static final int TWO_MINUTES = 1000 * 60 * 2;
    public static final int SIX_MINUTES = 1000 * 6;
    public static final int NINE_MINUTES = 1000 * 9;
    public static final float LOCATION_DISTANCE = 1f;
    public static final int MAX_DATA_LIST = 5;
    public static final int LOCATION_PERMISSION_CODE = 100;
    public static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    public static final String INTENT_FILTER_LOCATION = "intent_filter_location";
    public static final String INTENT_FILTER_BATTERY = "intent_filter_battery";
    public static final String INTENT_FILTER_POSTING = "intent_filter_data";
    public static final String KEY_LOCATION = "key_location";
    public static final String KEY_BATTERY = "key_battery";
    public static final String KEY_DATA = "key_data";

    public static final String BASE_URL = "http://sigma-solutions.eu/";
}
