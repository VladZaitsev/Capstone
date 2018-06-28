package com.baikaleg.v3.cookingaid.ui.recipes.adapter;

import android.support.v7.widget.RecyclerView;

import com.baikaleg.v3.cookingaid.databinding.ItemRecipeBinding;

public class RecipesViewHolder extends RecyclerView.ViewHolder {
    ItemRecipeBinding recipeItemBinding;

    RecipesViewHolder(ItemRecipeBinding binding) {
        super(binding.getRoot());
        recipeItemBinding = binding;
    }
}
