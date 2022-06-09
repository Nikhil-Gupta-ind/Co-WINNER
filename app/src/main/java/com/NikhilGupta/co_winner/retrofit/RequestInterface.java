package com.NikhilGupta.co_winner.retrofit;


import com.NikhilGupta.co_winner.centerlocator.models.CentersResponse;
import com.NikhilGupta.co_winner.centerlocator.models.SessionsResponse;
import com.NikhilGupta.co_winner.login.SaveTxnId;
import com.NikhilGupta.co_winner.login.UserToken;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface RequestInterface {

    // curl -X GET "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=110001&date=31-03-2021"
    // -H "accept: application/json" -H "Accept-Language: hi_IN"

    @GET("appointment/sessions/public/findByPin")
    Call<SessionsResponse> getSessions(
            @Query("pincode") String pincode,
            @Query("date") String date
    );

    // curl -X GET "https://cdn-api.co-vin.in/api/v2/appointment/centers/public/findByLatLong?lat=28.72&long=77.14"
    // -H "accept: application/json" -H "Accept-Language: hi_IN"

    @GET("appointment/centers/public/findByLatLong")
    Call<CentersResponse>  getCentersByLocation(
            @Query("lat") double lat,
            @Query("long") double longitude
    );

    // curl -X POST "https://cdn-api.co-vin.in/api/v2/auth/public/generateOTP"
    // -H "accept: application/json" -H "Content-Type: application/json" -d "{\"mobile\":\"9876543210\"}"

    @POST("auth/public/generateOTP")
    Call<SaveTxnId> getOtp(@Body Map<String, String> map);

    // curl -X POST "https://cdn-api.co-vin.in/api/v2/auth/public/confirmOTP"
    // -H "accept: application/json" -H "Content-Type: application/json" -d "{\"otp\":\"8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92\",\"txnId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"}"

    @POST("auth/public/confirmOTP")
    Call<UserToken> submitData(@Body Map<String, String> map);

    // curl -X GET "https://cdn-api.co-vin.in/api/v2/registration/certificate/public/download?beneficiary_reference_id=1234567890123"
    // -H "accept: application/pdf"
    @Headers({"accept: application/pdf"})
    @GET("registration/certificate/public/download")
    @Streaming
    Call<ResponseBody> downloadPdf(
            @Header("Authorization") String token,
            @Query("beneficiary_reference_id") String referenceId
    );

    // https://cdndemo-api.co-vin.in/api/v2/vaccinator/beneficiaries/findByMobile?mobile_number=9876543210" -H "accept: application/json

}
