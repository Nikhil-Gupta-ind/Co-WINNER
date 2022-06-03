package com.NikhilGupta.co_winner.centerlocator.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.NikhilGupta.co_winner.centerlocator.repository.CentersRepository;

public class CentersViewModelFactory implements ViewModelProvider.Factory {

    private final CentersRepository repository;

    public CentersViewModelFactory(CentersRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CentersViewModel(repository);
    }
}
