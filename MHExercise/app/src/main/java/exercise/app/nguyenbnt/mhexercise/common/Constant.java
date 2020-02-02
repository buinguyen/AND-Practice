package exercise.app.nguyenbnt.mhexercise.common;

import exercise.app.nguyenbnt.mhexercise.model.Position;

public class Constant {
    public static final float DEFAULT_ZOOM = 5f;

    public static final double ODD_IN_METER = 10000.0; // Odd for detect move to any location
    public static final int BOUND_PADDING = 200;
    public static final String NOTIFICATION_CHANNEL_ID = "mhexercise";

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 30000;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;

    public static final int REQUEST_CHECK_SETTINGS = 0x1;

    public static final String BASE_URL = "https://maps.googleapis.com/";

    public static final Position[] POSITIONS = new Position[]{
            new Position("Ha Noi", 21.030414, 105.836255),
            new Position("Ha Giang", 22.809972, 104.9722411),
            new Position("Hai Phong", 20.839214, 106.665495),
            new Position("Ha Long", 20.974476, 107.012520),
            new Position("Son La", 21.331945, 103.904935),
            new Position("Hoa Binh", 20.818757, 105.344959),
    };

    public static final Position[] POSITIONS_2 = new Position[]{
            new Position("Ha Noi", 21.030414, 105.836255),
            new Position("Ha Giang", 22.809972, 104.9722411),
            new Position("Hai Phong", 20.839214, 106.665495),
            new Position("Ha Long", 20.974476, 107.012520),
            new Position("Son La", 21.331945, 103.904935),
            new Position("Hoa Binh", 20.818757, 105.344959),
            new Position("Thai Binh", 20.455035, 106.3369989),
            new Position("Thanh Hoa", 19.806740, 105.790199),
            new Position("Dien Bien", 21.414696, 103.021074),
            new Position("Vinh", 18.659208, 105.694246),
            new Position("Da Lat", 11.948078, 108.464026),
            new Position("Da Nang", 16.023499, 108.187671),
            new Position("Kon Tum", 14.366046, 108.001779),
            new Position("Nha Trang", 12.251575, 109.185807),
    };
}
