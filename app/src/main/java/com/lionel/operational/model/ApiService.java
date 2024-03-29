package com.lionel.operational.model;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("login")
    @FormUrlEncoded
    Call<ApiResponse<AccountModel>> login(
            @Field("email") String email,
            @Field("password") String password
    );
    @GET("getShipment")
    Call<ApiResponse<ShipmentModel>> getShipment(@Query("recordSttNo") String sttNo);
    @FormUrlEncoded
    @POST("acceptShipment")
    Call<ApiResponse> acceptShipment(
            @Field("action") String action,
            @Field("recordSttNo") String recordSttNo,
            @Field("recordGW") String recordGW,
            @Field("recordLength") String recordLength,
            @Field("recordWidth") String recordWidth,
            @Field("recordHeight") String recordHeight
    );
    @GET("getDestination")
    Call<ApiResponse<List<DestinationModel>>> getDestination();
}
