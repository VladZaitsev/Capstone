package com.baikaleg.v3.cookingaid.data.dagger;

import android.app.Application;
import android.content.Context;

import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.dagger.modules.BasketModule;
import com.baikaleg.v3.cookingaid.data.dagger.modules.RecipesModule;
import com.baikaleg.v3.cookingaid.data.dagger.modules.StepDetailsModule;
import com.baikaleg.v3.cookingaid.data.dagger.modules.StorageModule;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.FragmentScoped;
import com.baikaleg.v3.cookingaid.ui.BaseActivity;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductDialog;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductModelFactory;
import com.baikaleg.v3.cookingaid.ui.basket.BasketActivity;
import com.baikaleg.v3.cookingaid.ui.recipes.RecipesActivity;
import com.baikaleg.v3.cookingaid.ui.recipestepsdetails.StepDetailsActivity;
import com.baikaleg.v3.cookingaid.ui.storage.StorageActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = AndroidSupportInjectionModule.class)
public interface AppModule {
    @Binds
    Context bindContext(Application application);

    @ActivityScoped
    @ContributesAndroidInjector(modules = RecipesModule.class)
    RecipesActivity recipesActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = BasketModule.class)
    BasketActivity basketActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = StorageModule.class)
    StorageActivity storageActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = StepDetailsModule.class)
    StepDetailsActivity stepDetailsActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector()
    BaseActivity baseActivityInjector();
}
