package com.baikaleg.v3.cookingaid.ui.recipes.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.databinding.ItemRecipeBinding;
import com.baikaleg.v3.cookingaid.ui.recipes.item.RecipeItemViewModel;
import com.baikaleg.v3.cookingaid.ui.recipes.item.RecipeStepPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecipesViewAdapter extends RecyclerView.Adapter<RecipesViewHolder> {

    private List<Recipe> recipesList = new ArrayList<>();
    private ItemRecipeBinding binding;

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_recipe,
                        parent, false);
        return new RecipesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        final Recipe recipe = recipesList.get(position);

        RecipeItemViewModel viewModel=new RecipeItemViewModel(recipe,null);
        holder.recipeItemBinding.setViewmodel(viewModel);

        RecipeStepPagerAdapter pagerAdapter=new RecipeStepPagerAdapter();
        holder.recipeItemBinding.stepsContent.setAdapter(pagerAdapter);
    }

    @Override
    public int getItemCount() {
        return this.recipesList.size();
    }

    public void refresh(@NonNull List<Recipe> list) {
        if(list!=null){
            this.recipesList.clear();
            this.recipesList.addAll(list);
            notifyDataSetChanged();
        }
    }
}
