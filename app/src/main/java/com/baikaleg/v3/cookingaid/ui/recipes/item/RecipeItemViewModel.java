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
import com.baikaleg.v3.cookingaid.data.database.entity.ProductList;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;
import com.baikaleg.v3.cookingaid.data.model.Recipe;

import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;

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
    public final ObservableInt width = new ObservableInt(0);

    @Bindable
    public final ObservableInt height = new ObservableInt(0);

    @Bindable
    public final ObservableInt servings = new ObservableInt(0);

    private final Repository repository;

    private final CompositeDisposable compositeDisposable;

    private final ObservableField<Recipe> recipe = new ObservableField<>();

    private final ObservableFloat ratio = new ObservableFloat();

    private final OnExpandButtonClickListener listener;


    public RecipeItemViewModel(Recipe recipe, float ratio, Repository repository, OnExpandButtonClickListener listener) {
        this.repository = repository;
        this.compositeDisposable = new CompositeDisposable();
        this.listener = listener;

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
        boolean b=!isExpanded.get();
        isExpanded.set(b);
        listener.onExpandButtonClickListener(b);
        if (b && recipe != null) {
            List<Ingredient> ingredientsList = Objects.requireNonNull(recipe.get()).getIngredients();
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

    public void setImageSize(int width, int height) {
        this.width.set(width);
        this.height.set(height);
    }

    public void setIsExpanded(boolean b) {
        isExpanded.set(b);
    }

    public interface OnExpandButtonClickListener {
        void onExpandButtonClickListener(boolean b);
    }
}
