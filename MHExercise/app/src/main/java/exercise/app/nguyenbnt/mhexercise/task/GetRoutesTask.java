package exercise.app.nguyenbnt.mhexercise.task;

import android.location.Location;
import android.os.AsyncTask;

import java.util.List;

import exercise.app.nguyenbnt.mhexercise.model.Route;

public class GetRoutesTask extends AsyncTask<Void, Void, List<Route>> {
    private final String TAG = GetRoutesTask.class.getSimpleName();

    private final int SMALL_SIZE = 12;

    private IGetRoutesCallback mCallback;
    private Location mCurrentLocation;
    private List<Location> mDestination;

    public GetRoutesTask(IGetRoutesCallback callback, Location currentLocation, List<Location> destinations) {
        this.mCallback = callback;
        this.mCurrentLocation = currentLocation;
        this.mDestination = destinations;
    }

    @Override
    protected List<Route> doInBackground(Void... objects) {
        AbstractGetRoutes getRoutes = null;
        if (mDestination.size() <= SMALL_SIZE) {
            getRoutes = new BABGetRoutes(mCurrentLocation, mDestination);
        } else {
            getRoutes = new GreedyGetRoutes(mCurrentLocation, mDestination);
        }

        return getRoutes.getRoutes();
    }

    @Override
    protected void onPostExecute(List<Route> result) {
        if (mCallback != null) {
            mCallback.onRouteResult(result);
        }
    }
}
