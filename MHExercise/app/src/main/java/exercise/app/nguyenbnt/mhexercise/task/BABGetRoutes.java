package exercise.app.nguyenbnt.mhexercise.task;

import android.location.Location;
import android.util.Log;

import java.util.List;

import exercise.app.nguyenbnt.mhexercise.model.Route;

public class BABGetRoutes extends AbstractGetRoutes {

    private boolean marks[]; // mark array
    private int result[];
    private int bestConfig[]; // best solution

    private float minDistance = Float.MAX_VALUE; // min value go through all vertices
    private float cost = 0;
    private int start;

    public BABGetRoutes(Location currentLocation, List<Location> locationList) {
        Log.i(getTag(), "Use BABGetRoutes algorithm");

        find(currentLocation, locationList);
    }

    @Override
    TYPE getType() {
        return TYPE.BRANCH_AND_BOUND;
    }

    @Override
    String getTag() {
        return BABGetRoutes.class.getSimpleName();
    }

    @Override
    void start() {
        marks = new boolean[size];
        result = new int[2 * size];
        bestConfig = new int[2 * size];

        start = 0;
        marks[start] = true;
        result[0] = start;

        tryToFind(1); // call from start index, stop when equals with size
    }

    @Override
    public List<Route> getRoutes() {
        Route route = null;
        for (int i = 0; i < size - 1; i++) {
            route = new Route();
            route.start = locations.get(bestConfig[i]);
            route.end = locations.get(bestConfig[i+1]);
            route.distance = route.start.distanceTo(route.end);
            routes.add(route);
        }
        route = new Route();
        route.start = locations.get(bestConfig[size - 1]);
        route.end = locations.get(bestConfig[0]);
        route.distance = route.start.distanceTo(route.end);
        routes.add(route);

        return routes;
    }

    public void tryToFind(int i) {
        if (i == size) {
            // if this is better solution, save it
            if (cost + matrix[result[i - 1]][result[0]] < minDistance) {
                minDistance = cost + matrix[result[i - 1]][result[0]];
                for (int k = 0; k < size; k++)
                    bestConfig[k] = result[k];
            }
        } else {
            // handle all vertices that can go through if has not yet mark
            for (int j = 0; j < size; j++) {
                if (marks[j] == false && cost + matrix[result[i - 1]][j] < minDistance) {
                    // save result
                    result[i] = j;
                    marks[j] = true;
                    cost += matrix[result[i - 1]][j];

                    // go to next vertex
                    tryToFind(i + 1);

                    // reset save result
                    marks[j] = false;
                    cost -= matrix[result[i - 1]][j];
                }
            }
        }
    }
}
