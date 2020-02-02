package exercise.app.nguyenbnt.mhexercise.task;

import java.util.List;

import exercise.app.nguyenbnt.mhexercise.model.Route;

public interface IGetRoutesCallback {
    void onRouteResult(List<Route> routeList);
}
