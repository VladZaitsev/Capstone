package com.baikaleg.v3.cookingaid.ui.recipes;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


import com.baikaleg.v3.cookingaid.data.Repository;

import javax.inject.Inject;

public class RecipesViewModelFactory implements ViewModelProvider.Factory {

    private final Repository repository;

    @Inject
    public RecipesViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipesViewModel.class)) {
            return (T) new RecipesViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}