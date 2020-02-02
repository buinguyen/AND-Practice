package exercise.app.nguyenbnt.mhexercise.task;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import exercise.app.nguyenbnt.mhexercise.MapsActivity;
import exercise.app.nguyenbnt.mhexercise.model.Distance;
import exercise.app.nguyenbnt.mhexercise.model.DistanceResponse;
import exercise.app.nguyenbnt.mhexercise.model.Route;
import exercise.app.nguyenbnt.mhexercise.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class AbstractGetRoutes {

    protected List<Location> locations;
    protected int size;
    protected float matrix[][];
    List<Route> routes;

    public enum TYPE {
        DIJKSTRA, /* Dijkstra algorithm */
        GREEDY, /* greedy algorithm*/
        BRANCH_AND_BOUND /*Branch and bound algorithm*/
    }

    protected void find(Location currentLocation, List<Location> locationList) {
        locations = new ArrayList<>();
        locations.add(0, currentLocation);
        locations.addAll(locationList);

        size = locations.size();

        // Make adjacent matrix
        matrix = new float[size][size];

        for (int i = 0; i < size - 1; i++) {
            matrix[i][i] = 0;
            for (int j = i + 1; j < size; j++) {
                float distance = -1;
                // Try to use Google API to get distance of two locations.
                DistanceResponse distanceResponse =
                        RetrofitClient.getInstance().getDistanceInfo(locations.get(i), locations.get(j));
                if (distanceResponse != null) {
                    // If successful, use this distance
                    distance = distanceResponse.getRows().get(0).getElements().get(0).getDistance().getValue();
                } else {
                    // If not successful, use Location API.
                    distance = locations.get(i).distanceTo(locations.get(j));
                }
                matrix[i][j] = matrix[j][i] = distance;
            }
        }

        routes = new ArrayList<>();

        start();
    }

    abstract TYPE getType();

    abstract String getTag();

    abstract void start();

    public abstract List<Route> getRoutes();
}
