package exercise.app.nguyenbnt.mhexercise.task;

import android.location.Location;
import android.util.Log;

import java.util.List;

import exercise.app.nguyenbnt.mhexercise.model.Route;

public class DijkstraGetRoutes extends AbstractGetRoutes {

    public DijkstraGetRoutes(Location currentLocation, List<Location> locationList) {
        Log.i(getTag(), "Use DijkstraGetRoutes algorithm");

        find(currentLocation, locationList);
    }

    @Override
    TYPE getType() {
        return TYPE.DIJKSTRA;
    }

    @Override
    String getTag() {
        return DijkstraGetRoutes.class.getSimpleName();
    }

    @Override
    void start() {
        float infinity = Float.MAX_VALUE;
        float len[] = new float[size];
        boolean visited[] = new boolean[size];
        int previous[] = new int[size];
        int start = 0;

        // Initialize
        for (int i = 0; i < size; i++) {
            len[i] = infinity;
            visited[i] = false;
            previous[i] = start;
        }
        for (int i = 0; i < size; i++) {
            if (matrix[start][i] > 0) {
                len[i] = matrix[start][i];
            }
        }

        int index = 0;
        len[index] = 0;
        visited[index] = true;

        for (int x = 0; x < size; x++) {
            // Find nearest location
            float min = infinity;
            for (int i = 0; i < size; i++) {
                if (visited[i] == false && len[i] < min) {
                    min = len[i];
                    index = i;
                }
            }
            visited[index] = true;

            // Update len
            for (int i = 0; i < size; i++) {
                if (visited[i] == false && matrix[index][i] > 0 && (len[index] + matrix[index][i]) < len[i]) {
                    len[i] = len[index] + matrix[index][i];
                    previous[i] = index;
                }
            }
        }

        // Make route data
        for (int i = 0; i < size; i++) {
            if (i != start) {
                Route route = new Route();
                route.start = locations.get(previous[i]);
                route.end = locations.get(i);
                route.distance = matrix[i][previous[i]];
                routes.add(route);
            }
        }
    }

    @Override
    public List<Route> getRoutes() {
        return routes;
    }
}
