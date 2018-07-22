package com.baikaleg.v3.cookingaid.data.dagger.modules;

import android.app.Application;

import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.FragmentScoped;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductDialog;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductModelFactory;
import com.baikaleg.v3.cookingaid.ui.storage.StorageActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public interface StorageModule {

    @Provides
    @Named("productId")
    static int provideProductID(StorageActivity activity) {
        return 0;
    }

    @Provides
    @Named("dialogId")
    static int provideDialogID(StorageActivity activity) {
        return 0;
    }

    @FragmentScoped
    @ContributesAndroidInjector
    AddEditProductDialog addEditFragmentDialog();

    @ActivityScoped
    @Provides
    static AddEditProductModelFactory provideRecipeListViewModelFactory(Repository repository, Application application) {
        return new AddEditProductModelFactory(repository, application);
    }
}
