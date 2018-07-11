package com.baikaleg.v3.cookingaid.ui.recipes.item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.Nullable;

import com.baikaleg.v3.cookingaid.data.model.Ingredient;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.data.model.Step;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;

public class RecipeItemViewModel extends BaseObservable {

    @Bindable
    public ObservableField<Recipe> recipe = new ObservableField<>();
    @Bindable
    public ObservableField<String> name = new ObservableField<>();

    @Bindable
    public ObservableBoolean isExpanded = new ObservableBoolean(false);

    public final ObservableList<Ingredient> ingredients = new ObservableArrayList<>();

    public RecipeItemViewModel(Recipe data, RecipeItemEventNavigator navigator) {
        recipe.set(data);
        name.set(data.getName());

        ingredients.clear();
        ingredients.addAll(data.getIngredients());

        notifyChange();
    }

    public void expandBtnClick() {
        isExpanded.set(!isExpanded.get());
    }
}
