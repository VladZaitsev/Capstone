package com.baikaleg.v3.cookingaid.ui.recipes.item;

import com.baikaleg.v3.cookingaid.data.model.Recipe;

/**
 * Defines the navigation actions that can be called from a list item in the recipe list.
 */
public interface RecipeItemEventNavigator {

    void onClickRecountBtn(int position);

    void onClickSendBtn(Recipe recipe, float ratio);
}
