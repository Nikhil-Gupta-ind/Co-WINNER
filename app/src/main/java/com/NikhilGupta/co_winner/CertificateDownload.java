package com.NikhilGupta.co_winner;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface CertificateDownload {

    //    https://cdn-api.co-vin.in/api/v2/registration/certificate/public/download?beneficiary_reference_id=1234567890123
    // sending data as a url parameter
    @Headers({
            "accept: application/pdf"
    })
    @GET("download")
    @Streaming
    Call<ResponseBody> downloadPdf(@Header("Authorization") String token, @Query("beneficiary_reference_id") String referenceId);
}
