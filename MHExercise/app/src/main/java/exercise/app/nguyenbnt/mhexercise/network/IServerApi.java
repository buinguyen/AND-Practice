package exercise.app.nguyenbnt.mhexercise.network;

import java.util.Map;

import exercise.app.nguyenbnt.mhexercise.model.DistanceResponse;
import exercise.app.nguyenbnt.mhexercise.model.PositionsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface IServerApi {

    /**
     * Dummy API to get position list
     * @return
     */
    @GET("positions")
    Call<PositionsResponse> getPositionList();

    @GET("maps/api/distancematrix/json")
    Call<DistanceResponse> getDistanceInfo(@QueryMap Map<String, String> parameters);
}
