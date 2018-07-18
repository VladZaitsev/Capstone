package com.baikaleg.v3.cookingaid.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.baikaleg.v3.cookingaid.data.database.AppDatabase;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.data.network.RecipeApi;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("WeakerAccess")
@Singleton
public class Repository implements DataSource {

    private final RecipeApi recipeApi;
    private AppDatabase db;

    @Inject
    public Repository(Context context) {
        recipeApi = new RecipeApi(context);
        db = AppDatabase.getInstance(context);
    }

    //TODO Replace category inserting after source changing
    @Override
    public Observable<List<Recipe>> getRecipes() {
        return recipeApi.createService().getRecipes()
                .flatMap(recipes -> Observable.fromIterable(recipes)
                        .doOnNext(recipe -> {
                            recipe.setCategory("dessert");
                        })
                        .toList()
                        .toObservable());
    }


    public Observable<List<CatalogEntity>> loadAllCatalogEntities() {
        return Observable
                .fromIterable(db.catalogDao().loadAllProducts())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .toObservable();
    }

    public CatalogEntity loadProductByName(String name) {
        return db.catalogDao().loadProductByName(name);
    }

}
