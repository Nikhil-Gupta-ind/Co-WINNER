package com.NikhilGupta.co_winner.retrofit;


import com.NikhilGupta.co_winner.centerlocator.models.ResponseData;
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

    // curl -X GET "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=110001&date=31-03-2021" -H "accept: application/json" -H "Accept-Language: hi_IN"

    @GET("appointment/sessions/public/findByPin")
    Call<ResponseData> getSessions(
            @Query("pincode") String pincode,
            @Query("date") String date
    );

    @POST("auth/public/generateOTP")
    Call<SaveTxnId> getOtp(@Body Map<String, String> map);

    @POST("auth/public/confirmOTP")
    Call<UserToken> submitData(@Body Map<String, String> map);

    //    https://cdn-api.co-vin.in/api/v2/registration/certificate/public/download?beneficiary_reference_id=1234567890123
    // sending data as a url parameter
    @Headers({"accept: application/pdf"})
    @GET("registration/certificate/public/download")
    @Streaming
    Call<ResponseBody> downloadPdf(
            @Header("Authorization") String token,
            @Query("beneficiary_reference_id") String referenceId
    );

    // https://cdndemo-api.co-vin.in/api/v2/vaccinator/beneficiaries/findByMobile?mobile_number=9876543210" -H "accept: application/json

}
