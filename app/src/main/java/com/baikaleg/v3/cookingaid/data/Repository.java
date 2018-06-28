package com.baikaleg.v3.cookingaid.data;

import android.content.Context;

import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.data.network.RecipeApi;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@SuppressWarnings("WeakerAccess")
@Singleton
public class Repository implements DataSource {

    private final RecipeApi recipeApi;

    @Inject
    public Repository(Context context) {
        recipeApi = new RecipeApi(context);
    }

    @Override
    public Observable<List<Recipe>> getRecipes() {
        return recipeApi.createService().getRecipes();
    }
}
