package com.lionel.operational.model;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    @FormUrlEncoded
    Call<ApiResponse<AccountModel>> login(
            @Field("email") String field1,
            @Field("password") String field2
    );
    @GET("getDestination")
    Call<ApiResponse<List<DestinationModel>>> getDestination();
}
