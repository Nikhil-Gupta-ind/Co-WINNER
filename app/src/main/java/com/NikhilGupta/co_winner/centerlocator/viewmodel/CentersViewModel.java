package com.NikhilGupta.co_winner.centerlocator.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.NikhilGupta.co_winner.centerlocator.models.ResponseData;
import com.NikhilGupta.co_winner.centerlocator.repository.CentersRepository;

public class CentersViewModel extends ViewModel {
    private CentersRepository  centersRepository;

    public CentersViewModel(CentersRepository centersRepository) {
        this.centersRepository = centersRepository;
    }

    public void getSessions(String pincode, String date) {
        Log.d("Test", "getSessions: ");
        centersRepository.getSessions(pincode, date);
    }

    public LiveData<ResponseData> getSessionsLiveData() {
        Log.d("Test", "getSessionsLiveData: ");
        return centersRepository.getSessionsLiveData();
    }
}
