package com.NikhilGupta.co_winner.centerlocator.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.NikhilGupta.co_winner.centerlocator.models.CentersResponse;
import com.NikhilGupta.co_winner.centerlocator.models.SessionsResponse;
import com.NikhilGupta.co_winner.retrofit.RequestInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentersRepository {
    private final RequestInterface requestInterface;
    private final MutableLiveData<SessionsResponse> sessionsLiveData = new MutableLiveData<>();
    private final MutableLiveData<CentersResponse> centersResponseLiveData = new MutableLiveData<>();

    public CentersRepository(RequestInterface requestInterface) {
        this.requestInterface = requestInterface;
    }

    public void getSessions(String pincode, String date) {
        requestInterface.getSessions(pincode, date)
                .enqueue(new Callback<SessionsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SessionsResponse> call, @NonNull Response<SessionsResponse> response) {
                        if (response.body() != null && response.code() == 200)
                            sessionsLiveData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<SessionsResponse> call, @NonNull Throwable t) {
                        sessionsLiveData.postValue(null);
                    }
                });
    }

    public LiveData<SessionsResponse> getSessionsLiveData() {
        return sessionsLiveData;
    }

    public void getCentersByLocation(double lat, double longitude) {
        requestInterface.getCentersByLocation(lat, longitude)
                .enqueue(new Callback<CentersResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CentersResponse> call, @NonNull Response<CentersResponse> response) {
                        if (response.body() != null && response.code() == 200)
                            centersResponseLiveData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<CentersResponse> call, @NonNull Throwable t) {
                        centersResponseLiveData.postValue(null);
                    }
                });
    }

    public MutableLiveData<CentersResponse> getCentersResponseLiveData() {
        return centersResponseLiveData;
    }
}
