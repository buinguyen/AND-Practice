package nguyenbnt.app.sigmatest.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IPostService {
    @POST("test")
    @FormUrlEncoded
    Call<ResponseBody> postData(@Field("data") String jsonData);
}
