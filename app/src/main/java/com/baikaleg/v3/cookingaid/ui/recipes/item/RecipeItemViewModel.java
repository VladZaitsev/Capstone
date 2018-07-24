package com.baikaleg.v3.cookingaid.ui.recipes.item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableList;
import android.util.Log;

import com.baikaleg.v3.cookingaid.BR;
import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductList;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;
import com.baikaleg.v3.cookingaid.data.model.Recipe;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

public class RecipeItemViewModel extends BaseObservable {

    public ObservableField<Recipe> recipe = new ObservableField<>();

    public ObservableBoolean isExpanded = new ObservableBoolean(false);

    @Bindable
    public final ObservableList<Ingredient> ingredients = new ObservableArrayList<>();

    @Bindable
    public final ObservableFloat calories = new ObservableFloat();

    @Bindable
    public final ObservableFloat price = new ObservableFloat();

    private Repository repository;

    private CompositeDisposable compositeDisposable;

    public RecipeItemViewModel(Recipe data, Repository repository, RecipeItemEventNavigator navigator) {
        this.repository = repository;
        this.compositeDisposable = new CompositeDisposable();

        recipe.set(data);
        calculateCalories(data.getIngredients());

        ingredients.clear();
        ingredients.addAll(data.getIngredients());
        notifyChange();
    }

    private void calculateCalories(List<Ingredient> ingredientsList) {
       /* for (int i = 0; i < ingredientsList.size(); i++) {
            String ingredient = ingredientsList.get(i).getIngredient();
            int finalI = i;
            compositeDisposable.add(repository.loadCatalogEntitiesByQuery(ingredient)
                    .subscribe(list -> {
                        CatalogEntity entity = null;
                        for (int j = 0; j < list.size(); j++) {
                            if (ingredient.contains(list.get(j).getIngredient())) {
                                entity = list.get(j);
                                break;
                            }
                        }
                        if (entity != null) {
                            entity.setMeasure(ingredientsList.get(finalI).getMeasure());
                            entity.setQuantity(ingredientsList.get(finalI).getQuantity());
                            calories.set(calories.get() + entity.getTotalCalories());
                            notifyPropertyChanged(BR.calories);
                        }
                    }));
        }*/

        for (int i = 0; i < ingredientsList.size(); i++) {
            String ingredient = ingredientsList.get(i).getIngredient();
            int finalI = i;
            compositeDisposable.add(repository.loadCatalogEntitiesByQuery(ingredient)
                    .flatMapIterable((Function<List<CatalogEntity>, Iterable<CatalogEntity>>) entities -> entities)
                    .filter((entity -> ingredient.contains(entity.getIngredient())))
                    .firstElement().subscribe(catalogEntity -> {
                        catalogEntity.setMeasure(ingredientsList.get(finalI).getMeasure());
                        catalogEntity.setQuantity(ingredientsList.get(finalI).getQuantity());
                        calories.set(calories.get() + catalogEntity.getTotalCalories());
                        notifyPropertyChanged(BR.calories);
                    }, throwable -> Log.i("calculateCalories", throwable.toString())));
        }
    }

    public void expandBtnClick() {
        isExpanded.set(!isExpanded.get());
        if (isExpanded.get() && recipe != null) {
            List<Ingredient> ingredientsList = recipe.get().getIngredients();
            for (int i = 0; i < ingredientsList.size(); i++) {
                String ingredient = ingredientsList.get(i).getIngredient();
                int finalI = i;
                compositeDisposable.add(repository.loadProductEntitiesByQuery(ingredient)
                        .subscribe(list -> {
                            ProductList productList = new ProductList();
                            productList.addAll(list);
                            float allQuantity = productList.getTransformedQuantity(ingredientsList.get(finalI).getMeasure());
                            if (allQuantity == 0) {
                                ingredients.get(finalI).setIngredientState(0);
                            } else if (allQuantity >= ingredientsList.get(finalI).getQuantity()) {
                                ingredients.get(finalI).setIngredientState(2);
                                notifyPropertyChanged(BR.ingredients);
                            } else if (allQuantity < ingredientsList.get(finalI).getQuantity()) {
                                ingredients.get(finalI).setIngredientState(1);
                                notifyPropertyChanged(BR.ingredients);
                            }
                        }));
            }
        } else {
            compositeDisposable.clear();
        }

    }
}
