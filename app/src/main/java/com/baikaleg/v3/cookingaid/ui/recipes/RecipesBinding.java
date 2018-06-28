package com.baikaleg.v3.cookingaid.ui.recipes;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.ui.recipes.adapter.RecipesViewAdapter;

import java.util.List;

public class RecipesBinding {

    /**
     * Prevent instantiation
     */
    private RecipesBinding() {
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:recipes")
    public static void setRecipes(RecyclerView recyclerView, List<Recipe> recipes) {
        RecipesViewAdapter adapter = (RecipesViewAdapter ) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.refresh(recipes);
        }
    }

}
