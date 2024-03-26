package com.lionel.operational.model;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("getDestination")
    Call<ApiResponse<List<DestinationModel>>> getDestination();
}
