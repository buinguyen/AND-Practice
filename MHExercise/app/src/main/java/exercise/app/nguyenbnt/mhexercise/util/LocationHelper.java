package exercise.app.nguyenbnt.mhexercise.util;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import exercise.app.nguyenbnt.mhexercise.model.Route;
import exercise.app.nguyenbnt.mhexercise.task.GetRoutesTask;
import exercise.app.nguyenbnt.mhexercise.task.IGetRoutesCallback;

public class LocationHelper {
    private static final String TAG = LocationHelper.class.getSimpleName();

    /**
     * Check whether two locations is same with the odd
     * @param location1
     * @param location2
     * @param odd
     * @return
     */
    public static boolean checkRightLocation(Location location1, Location location2, double odd) {
        if (location1.distanceTo(location2) <= odd) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get total distance of routes
     * @param routeList
     * @return
     */
    public static float getTotalDistance(List<Route> routeList) {
        if (routeList == null) {
            return 0f;
        }
        float total = 0;
        for (Route route : routeList) {
            total += route.distance;
        }
        return total;
    }

    /**
     * The task which get routes the direction from current location to list of positions
     * @param callback
     * @param currentLocation
     * @param locationList
     */
    public static void getRoutes(IGetRoutesCallback callback, Location currentLocation, List<Location> locationList) {
        if (callback == null || locationList == null || locationList.size() == 0 || currentLocation == null ) {
            return;
        }
        GetRoutesTask task = new GetRoutesTask(callback, currentLocation, locationList);
        task.execute();
    }
}
