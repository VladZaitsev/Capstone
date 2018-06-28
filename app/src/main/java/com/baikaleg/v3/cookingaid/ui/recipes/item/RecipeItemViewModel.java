package com.baikaleg.v3.cookingaid.ui.recipes.item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.baikaleg.v3.cookingaid.data.model.Recipe;

public class RecipeItemViewModel extends BaseObservable {

    @Bindable
    public ObservableField<String> name = new ObservableField<>();

    @Bindable
    public ObservableBoolean isExpanded = new ObservableBoolean(false);

    public RecipeItemViewModel(Recipe recipe, RecipeEventNavigator navigator) {
        name.set(recipe.getName());
        notifyChange();
    }

    public void expandBtnClick() {
        isExpanded.set(!isExpanded.get());
    }
}
