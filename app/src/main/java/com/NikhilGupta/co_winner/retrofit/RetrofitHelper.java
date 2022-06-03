package com.NikhilGupta.co_winner.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    final static String BASE_URL = "https://cdn-api.co-vin.in/api/v2/";

    public static Retrofit getInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .client(getOkHttpClient().build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private static OkHttpClient.Builder getOkHttpClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
//                        .addHeader("Authorization", mToken)
                    .build();
            return chain.proceed(request);
        });

        return okHttpClientBuilder;
    }
}
