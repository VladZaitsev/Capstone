package com.baikaleg.v3.cookingaid.ui.storage;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.baikaleg.v3.cookingaid.data.Repository;

import javax.inject.Inject;

public class StorageViewModelFactory implements ViewModelProvider.Factory {

    private final Repository repository;

    @Inject
    public StorageViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StorageViewModel.class)) {
            return (T) new StorageViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
