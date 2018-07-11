package com.baikaleg.v3.cookingaid.data.dagger.modules;

import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.FragmentScoped;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductDialog;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductEventNavigator;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductModelFactory;
import com.baikaleg.v3.cookingaid.ui.storage.StorageActivity;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public interface StorageModule {

    @Provides
    static String provideProductUUID(StorageActivity activity) {
        return activity.getSelectedUUID();
    }

    @Provides
    static int provideDialogUUID(StorageActivity activity) {
        return 8;
    }

    @FragmentScoped
    @ContributesAndroidInjector
    AddEditProductDialog addEditFragmentDialog();

    @ActivityScoped
    @Provides
    static AddEditProductModelFactory provideRecipeListViewModelFactory(Repository repository) {
        return new AddEditProductModelFactory(repository);
    }
}
