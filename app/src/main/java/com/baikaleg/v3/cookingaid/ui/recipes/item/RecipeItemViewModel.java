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

    @Nullable
    private WeakReference<RecipeItemEventNavigator> navigator;

    @Bindable
    public ObservableField<Integer> stepPosition = new ObservableField<>();

    @Bindable
    public ObservableField<Recipe> recipe = new ObservableField<>();
    @Bindable
    public ObservableField<String> name = new ObservableField<>();

    @Bindable
    public ObservableBoolean isExpanded = new ObservableBoolean(false);

    public final ObservableList<Ingredient> ingredients = new ObservableArrayList<>();
    public final ObservableList<String> stepsShortDescriptions = new ObservableArrayList<>();

    public RecipeItemViewModel(Recipe data, RecipeItemEventNavigator navigator) {
        recipe.set(data);
        name.set(data.getName());

        ingredients.clear();
        ingredients.addAll(data.getIngredients());

        Observable.fromArray(data)
                .map(Recipe::getSteps)
                .flatMap(steps -> Observable.fromIterable(steps)
                        .map(Step::getShortDescription))
                .toList()
                .toObservable()
                .subscribe(list -> stepsShortDescriptions.addAll(list));

        notifyChange();
    }

    public void expandBtnClick() {
        isExpanded.set(!isExpanded.get());
    }

    public void setNavigator(@Nullable RecipeItemEventNavigator navigator) {
        this.navigator = new WeakReference<>(navigator);
    }

    public void stepClicked() {
        Recipe data = recipe.get();
        if (data == null) {
            return;
        }
        if (navigator != null && stepPosition != null) {
            int position=stepPosition.get();
            navigator.get().onStepClickListener(position);
        }
    }

    public void setStepPosition(int position) {
        stepPosition.set(position);
    }
}
