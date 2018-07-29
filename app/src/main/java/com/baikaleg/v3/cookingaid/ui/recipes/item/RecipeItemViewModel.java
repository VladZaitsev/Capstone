package com.baikaleg.v3.cookingaid.ui.recipes.item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.util.Log;

import com.baikaleg.v3.cookingaid.BR;
import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductList;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;
import com.baikaleg.v3.cookingaid.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

public class RecipeItemViewModel extends BaseObservable {

    @Bindable
    public final ObservableBoolean isExpanded = new ObservableBoolean(false);

    @Bindable
    public final ObservableList<Ingredient> ingredients = new ObservableArrayList<>();

    @Bindable
    public final ObservableFloat calories = new ObservableFloat(0);

    @Bindable
    public final ObservableFloat price = new ObservableFloat(0);

    @Bindable
    public final ObservableInt servings = new ObservableInt(0);

    private Repository repository;

    private CompositeDisposable compositeDisposable;

    private ObservableField<Recipe> recipe = new ObservableField<>();

    private final ObservableFloat ratio = new ObservableFloat();

    public RecipeItemViewModel(Recipe recipe, float ratio, Repository repository) {
        this.repository = repository;
        this.compositeDisposable = new CompositeDisposable();

        if (ratio != 1) {
            isExpanded.set(true);
        }

        this.ratio.set(ratio);
        this.recipe.set(recipe);
        this.servings.set((int) (recipe.getServings() * ratio));
        calculateCalories(recipe.getIngredients());


        ingredients.clear();
        ingredients.addAll(recipe.getIngredients());
        notifyChange();
    }

    private void calculateCalories(List<Ingredient> ingredientsList) {
        for (int i = 0; i < ingredientsList.size(); i++) {
            String ingredient = ingredientsList.get(i).getIngredient();
            int finalI = i;
            compositeDisposable.add(repository.loadCatalogEntitiesByQuery(ingredient)
                    .flatMapIterable(entities -> entities)
                    .filter((entity -> ingredient.contains(entity.getIngredient())))
                    .firstElement().subscribe(catalogEntity -> {
                        catalogEntity.setMeasure(ingredientsList.get(finalI).getMeasure());
                        catalogEntity.setQuantity(ingredientsList.get(finalI).getQuantity());

                        calories.set(calories.get() + catalogEntity.getTotalCalories() * ratio.get());
                        price.set(price.get() + catalogEntity.getTotalPrice() * ratio.get());

                        notifyPropertyChanged(BR.calories);
                        notifyPropertyChanged(BR.price);
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

    public ObservableField<Recipe> getRecipe() {
        return recipe;
    }

    public ObservableFloat getRatio() {
        return ratio;
    }
}
