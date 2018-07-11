package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.ui.recipes.RecipesViewModel;

import javax.inject.Inject;

public class AddEditProductModelFactory implements ViewModelProvider.Factory {

    private final Repository repository;
   // private final AddEditProductEventNavigator navigator;

    @Inject
    public AddEditProductModelFactory(Repository repository) {
        this.repository = repository;
        //this.navigator = navigator;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddEditProductModel.class)) {
            return (T) new AddEditProductModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
