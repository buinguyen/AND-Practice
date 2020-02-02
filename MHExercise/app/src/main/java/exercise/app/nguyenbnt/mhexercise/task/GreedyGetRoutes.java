package exercise.app.nguyenbnt.mhexercise.task;

import android.location.Location;
import android.util.Log;

import java.util.List;

import exercise.app.nguyenbnt.mhexercise.model.Route;

public class GreedyGetRoutes extends AbstractGetRoutes {

    public GreedyGetRoutes(Location currentLocation, List<Location> locationList) {
        Log.i(getTag(), "Use GreedyGetRoutes algorithm");

        find(currentLocation, locationList);
    }

    @Override
    TYPE getType() {
        return TYPE.GREEDY;
    }

    @Override
    String getTag() {
        return GreedyGetRoutes.class.getSimpleName();
    }

    @Override
    void start() {
        float infinity = Float.MAX_VALUE;
        float len[] = new float[size]; // Distance from i to spanning tree
        boolean visited[] = new boolean[size]; // true: inside spanning tree, false: outside spanning tree
        int next[] = new int[size]; // Direction from i to next vertex is next[i]

        // Initialize
        int start = 0;
        for (int i = 0; i < size; i++) {
            if (matrix[start][i] > 0) {
                len[i] = matrix[start][i];
            }
            next[i] = 0;
        }

        visited[start] = true;
        int prev = start;
        for (int z = 0; z < size; z++) {
            // Find the vertex is nearest compare with spanning tree
            float min = infinity;
            int index = -1;
            for (int i = 0; i < size; i++) {
                if (visited[i] == false && len[i] < min) {
                    min = len[i];
                    index = i;
                }
            }
            if (index == -1) {
                break;
            }

            // Add to spanning tree
            visited[index] = true;
            next[prev] = index;
            prev = index;
            // Re-calculate the distance from the outside vertex to spanning tree
            min = infinity;
            for (int i = 0; i < size; i++) {
                if (visited[i] == false && matrix[index][i] > 0) {
                    len[i] = matrix[index][i];
                }
            }

        }

        // Make route data
        for (int i = 0; i < next.length; i++) {
            Route route = new Route();
            route.start = locations.get(i);
            route.end = locations.get(next[i]);
            route.distance = matrix[i][next[i]];
            routes.add(route);
        }
    }

    @Override
    public List<Route> getRoutes() {
        return routes;
    }
}
