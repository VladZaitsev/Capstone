package com.baikaleg.v3.cookingaid.data.dagger.modules;

import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.ui.recipes.RecipesViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public interface RecipesModule {
    @ActivityScoped
    @Provides
    static RecipesViewModelFactory provideRecipeListViewModelFactory(Repository repository) {
        return new RecipesViewModelFactory(repository);
    }
}
