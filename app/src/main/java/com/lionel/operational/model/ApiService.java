package com.lionel.operational.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("operation_employee_login.php")
    @FormUrlEncoded
    Call<ApiResponse<AccountModel>> login(
            @Field("username") String email,
            @Field("password") String password
    );

    @POST("operation_employee_get_menu.php")
    @FormUrlEncoded
    Call<ApiResponse<AccountModel>> getMenu(
            @Field("username") String token
    );

    @GET("operation_acceptance.php")
    Call<ApiResponse<ShipmentModel>> getShipment(
            @Query("recordSttNo") String sttNo,
            @Query("action") String action);

    @GET("operation_console.php")
    Call<ApiResponse<ShipmentModel>> getShipmentConsole(
            @Query("recordSttNo") String sttNo,
            @Query("action") String action,
            @Query("recordDestCity") String recordDestCity);

    @FormUrlEncoded
    @POST("operation_acceptance.php")
    Call<ApiResponse> acceptShipment(
            @Field("action") String action,
            @Field("recordSttNo") String recordSttNo,
            @Field("recordGW") String recordGW,
            @Field("recordLength") String recordLength,
            @Field("recordWidth") String recordWidth,
            @Field("recordHeight") String recordHeight
    );

    @GET("operation_city.php")
    Call<ApiResponse<List<DestinationModel>>> getDestination(
            @Query("action") String action
    );

    @GET("operation_city.php")
    Call<ApiResponse<List<CityModel>>> getCity(
            @Query("action") String action
    );

    @FormUrlEncoded
    @POST("operation_console.php")
    Call<ApiResponse> submitConsole(
            @Field("action") String action,
            @Field("recordConsoleNo") String recordConsoleNo,
            @Field("recordEmployeeName") String recordEmployeeName,
            @Field("recordBranchId") String recordBranchId,
            @Field("recordDestCity") String recordDestCity,
            @Field("recordDestBranchId") String recordDestBranchId,
            @Field("recordSttNo[]") List<String> recordSttNo
    );

    @GET("operation_shipping_method.php")
    Call<ApiResponse<List<ShippingMethodModel>>> getShippingMethod(
            @Query("action") String action
    );

    @GET("operation_shipping_agent.php")
    Call<ApiResponse<List<ShippingAgentModel>>> getShippingAgent(
            @Query("action") String action,
            @Query("recordUserBranchId") String recordUserBranchId,
            @Query("recordAgentType") String recordAgentType

    );

    @GET("operation_waybill.php")
    Call<ApiResponse<List<ShippingLinerModel>>> getShippingLiner(
            @Query("action") String action,
            @Query("recordShippingMethod") String recordShippingMethod
    );

    @GET("operation_waybill.php")
    Call<ApiResponse<List<ServiceModel>>> getService(
            @Query("action") String action,
            @Query("recordShippingMethod") String recordShippingMethod
    );

    @GET("operation_waybill.php")
    Call<ApiResponse<List<ShipmentModel>>> getWayBillShipment(
            @Query("action") String action,
            @Query("recordBarcode") String recordBarcode,
            @Query("recordDestBranchId") String recordDestBranchId
    );

    @FormUrlEncoded
    @POST("operation_waybill.php")
    Call<ApiResponse> submitWaybill(
            @Field("action") String action,
            @Field("recordShippingMethod") String shippingMethod,
            @Field("recordShippingAgentId") String shippingAgentId,
            @Field("recordDestBranchId") String destBranchId,
            @Field("recordLinerId") String linerId,
            @Field("recordServiceSelect") String serviceSelect,
            @Field("recordBranchId") String branchId,
            @Field("recordEmployeeName") String employeeName,
            @Field("recordShipment") String shipmentItems);

    @GET("operation_stob.php")
    Call<ApiResponse<List<WayBillModel>>> getStobWayBill(
            @Query("action") String action,
            @Query("recordShippingMethod") String method
    );

    @FormUrlEncoded
    @POST("operation_stob.php")
    Call<ApiResponse> submitStob(
            @Field("action") String action,
            @Field("recordBranchId") String recordBranchId,
            @Field("recordOrigin") String recordOrigin,
            @Field("recordShippingAgentId") String recordShippingAgentId,
            @Field("recordShippingMethod") String recordShippingMethod,
            @Field("recordCarLicenseNo") String recordCarLicenseNo,
            @Field("recordSealNo") String recordSealNo,
            @Field("recordEmployeeName") String recordEmployeeName,
            @Field("recordWaybillNo[]") List<String> recordWaybillNo
    );
}
