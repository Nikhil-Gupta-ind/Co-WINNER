package com.NikhilGupta.co_winner.centerlocator.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.NikhilGupta.co_winner.centerlocator.models.CentersResponse;
import com.NikhilGupta.co_winner.centerlocator.models.SessionsResponse;
import com.NikhilGupta.co_winner.centerlocator.repository.CentersRepository;

public class CentersViewModel extends ViewModel {
    private CentersRepository  centersRepository;

    public CentersViewModel(CentersRepository centersRepository) {
        this.centersRepository = centersRepository;
    }

    public void getSessions(String pincode, String date) {
        centersRepository.getSessions(pincode, date);
    }

    public LiveData<SessionsResponse> getSessionsLiveData() {
        return centersRepository.getSessionsLiveData();
    }

    public void getCentersByLocation(double lat, double longitude) {
        centersRepository.getCentersByLocation(lat, longitude);
    }

    public LiveData<CentersResponse> getCentersLiveData() {
        return centersRepository.getCentersResponseLiveData();
    }
}
