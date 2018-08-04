package com.baikaleg.v3.cookingaid.ui.recipes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.MutableInt;

import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RecipesViewModel extends AndroidViewModel {

    private final Repository repository;

    private String recipeCategory;

    @NonNull
    private final CompositeDisposable compositeDisposable;

    public final MutableLiveData<List<Recipe>> data = new MutableLiveData<>();

    public final MutableLiveData<Boolean> isError = new MutableLiveData<>();

    public final MutableLiveData<Boolean> isEmpty = new MutableLiveData<>();

    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public RecipesViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        this.compositeDisposable = new CompositeDisposable();

        load();
    }

    public void load() {
        isLoading.setValue(true);
        compositeDisposable.clear();
        Disposable disposable = repository.getRecipes()
                .flatMap(io.reactivex.Observable::fromIterable)
                .filter(recipe -> recipe.getCategory().equals(recipeCategory))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipes -> showData(recipes, false), throwable -> {
                    Log.i("Recipes download error", throwable.getMessage());
                    showData(null, true);
                });
        compositeDisposable.add(disposable);
    }

    private void showData(List<Recipe> recipes, boolean error) {
        isLoading.setValue(false);
        isError.setValue(error);

        if (recipes != null) {
            data.setValue(recipes);
            isEmpty.setValue(recipes.size() == 0 && !error);
        }
    }

    void sendToBasket(Recipe recipe, float ratio) {
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            compositeDisposable.add(repository.loadCatalogEntitiesByQuery(ingredient.getIngredient())
                    .flatMapIterable(entities -> entities)
                    .filter((entity -> ingredient.getIngredient().contains(entity.getIngredient())))
                    .firstElement().subscribe(catalogEntity -> {
                        ProductEntity productEntity = new ProductEntity(ingredient.getQuantity() * ratio, ingredient.getMeasure(), catalogEntity.getIngredient());
                        productEntity.setProductState(AddEditProductModel.DIALOG_BASKET_ID);
                        productEntity.setPrice(catalogEntity.getPrice());
                        productEntity.setDensity(catalogEntity.getDensity());
                        productEntity.setCalories(catalogEntity.getCalories());
                        productEntity.setUnitMeasure(catalogEntity.getUnitMeasure());
                        productEntity.setUnitQuantity(catalogEntity.getUnitQuantity());
                        productEntity.setExpiration(catalogEntity.getExpiration());
                        repository.saveProductEntity(productEntity, null);
                    }, throwable -> Log.i("sendToBasket", throwable.toString())));

        }
    }

    void onDestroyed() {
        compositeDisposable.clear();
        repository.onDestroyed();
    }

        void setCategory(String category) {
        recipeCategory = category;
    }
}
