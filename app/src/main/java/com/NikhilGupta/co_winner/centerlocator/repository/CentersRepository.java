package com.NikhilGupta.co_winner.centerlocator.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.NikhilGupta.co_winner.centerlocator.models.ResponseData;
import com.NikhilGupta.co_winner.retrofit.RequestInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentersRepository {
    private final RequestInterface requestInterface;
    private final MutableLiveData<ResponseData> sessionsLiveData = new MutableLiveData<>();

    public CentersRepository(RequestInterface requestInterface) {
        this.requestInterface = requestInterface;
    }

    public void getSessions(String pincode, String date) {
        requestInterface.getSessions(pincode, date)
                .enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseData> call, @NonNull Response<ResponseData> response) {
                        if (response.body() != null && response.code() == 200) sessionsLiveData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseData> call, @NonNull Throwable t) {
                        sessionsLiveData.postValue(null);
                    }
                });
    }

    public LiveData<ResponseData> getSessionsLiveData() {
        Log.d("Test", "getSessionsLiveDataRepo: ");
        return sessionsLiveData;
    }
}
