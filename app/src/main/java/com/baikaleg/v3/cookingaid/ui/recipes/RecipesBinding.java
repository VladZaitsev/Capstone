package com.baikaleg.v3.cookingaid.ui.recipes;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.baikaleg.v3.cookingaid.data.model.Ingredient;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.ui.recipes.adapter.RecipesViewAdapter;
import com.baikaleg.v3.cookingaid.ui.recipes.item.RecipeIngredientView;
import com.baikaleg.v3.cookingaid.ui.recipes.item.RecipeStepPagerAdapter;

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
        RecipesViewAdapter adapter = (RecipesViewAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.refresh(recipes);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:recipesRefresh")
    public static void setRecipesRefreshListener(SwipeRefreshLayout view, final RecipesViewModel viewModel) {
        view.setOnRefreshListener(viewModel::load);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:ingredients")
    public static void setIngredients(LinearLayout layout, List<Ingredient> ingredients) {
        for (int i = 0; i < ingredients.size(); i++) {
            RecipeIngredientView ingredientView = new RecipeIngredientView(layout.getContext());
            ingredientView.setIngredient(ingredients.get(i));
            layout.addView(ingredientView);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:stepTitles")
    public static void setStepTitles(ViewPager pager, List<String> list) {
        RecipeStepPagerAdapter adapter = (RecipeStepPagerAdapter) pager.getAdapter();
        if (adapter != null) {
            adapter.refresh(list);
        }
    }
}