package com.baikaleg.v3.cookingaid.ui.recipes.item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.baikaleg.v3.cookingaid.BR;
import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductList;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;
import com.baikaleg.v3.cookingaid.data.model.Recipe;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class RecipeItemViewModel extends BaseObservable {

    public ObservableField<Recipe> recipe = new ObservableField<>();

    public ObservableBoolean isExpanded = new ObservableBoolean(false);

    @Bindable
    public final ObservableList<Ingredient> ingredients = new ObservableArrayList<>();

    private Repository repository;

    private CompositeDisposable compositeDisposable;

    public RecipeItemViewModel(Recipe data, Repository repository, RecipeItemEventNavigator navigator) {
        this.repository = repository;
        this.compositeDisposable = new CompositeDisposable();

        recipe.set(data);

        ingredients.clear();
        ingredients.addAll(data.getIngredients());
        notifyChange();
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
