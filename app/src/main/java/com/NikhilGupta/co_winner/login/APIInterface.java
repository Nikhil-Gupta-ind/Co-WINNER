package com.NikhilGupta.co_winner.login;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {
    @POST("generateOTP")
    Call<SaveTxnId> getOtp(@Body Map<String, String> map);

    //    @FormUrlEncoded
    @POST("confirmOTP")
    Call<UserToken> submitData(@Body Map<String, String> map);
}
