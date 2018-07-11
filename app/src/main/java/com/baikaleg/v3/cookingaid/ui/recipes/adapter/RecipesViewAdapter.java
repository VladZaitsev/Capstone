package com.baikaleg.v3.cookingaid.ui.recipes.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.data.model.Step;
import com.baikaleg.v3.cookingaid.databinding.ItemRecipeBinding;
import com.baikaleg.v3.cookingaid.databinding.ViewStepInItemRecipeBinding;
import com.baikaleg.v3.cookingaid.ui.recipes.item.RecipeItemEventNavigator;
import com.baikaleg.v3.cookingaid.ui.recipes.item.RecipeItemViewModel;
import com.baikaleg.v3.cookingaid.ui.recipestepsdetails.StepDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipesViewAdapter extends RecyclerView.Adapter<RecipesViewHolder> {
    private static final String TAG = RecipesViewAdapter.class.getSimpleName();
    private List<Recipe> recipesList = new ArrayList<>();

    public Context context;

    @Inject
    public RecipesViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecipeBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_recipe,
                        parent, false);
        return new RecipesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        final Recipe recipe = recipesList.get(position);

        RecipeItemViewModel viewModel = new RecipeItemViewModel(recipe, null);
       /* viewModel.setNavigator(new RecipeItemEventNavigator() {
            @Override
            public void onStepClickListener(int position) {
                Intent intent = new Intent(context, StepDetailsActivity.class);
                intent.putExtra(StepDetailsActivity.EXTRA_STEP_POSITION, position);
                intent.putExtra(StepDetailsActivity.EXTRA_RECIPE, recipe);
                context.startActivity(intent);
            }
        });*/
        holder.recipeItemBinding.setViewmodel(viewModel);

        RecipesStepsPagerAdapter pagerAdapter = new RecipesStepsPagerAdapter();
        holder.recipeItemBinding.dots.setupWithViewPager(holder.recipeItemBinding.stepsContent, true);
        holder.recipeItemBinding.stepsContent.setAdapter(pagerAdapter);
    }

    @Override
    public int getItemCount() {
        return this.recipesList.size();
    }

    public void refresh(@NonNull List<Recipe> list) {
        this.recipesList.clear();
        this.recipesList.addAll(list);
        notifyDataSetChanged();
    }

    public class RecipesStepsPagerAdapter extends PagerAdapter {
        private Recipe recipe;
        private List<Step> steps = new ArrayList<>();

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            ViewStepInItemRecipeBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.view_step_in_item_recipe, container, false);
            binding.stepShortDescription.setText("Step " + position + ": " + steps.get(position).getShortDescription());
            binding.stepDescription.setText(steps.get(position).getDescription());
            container.addView(binding.getRoot());

            View page = binding.getRoot();
            page.setOnClickListener(view -> {
                Intent intent = new Intent(context, StepDetailsActivity.class);
                intent.putExtra(StepDetailsActivity.EXTRA_STEP_POSITION, position);
                intent.putExtra(StepDetailsActivity.EXTRA_RECIPE, recipe);
                context.startActivity(intent);
            });

            return binding.getRoot();
        }

        public void refresh(Recipe recipe) {
            this.recipe = recipe;
            steps = recipe.getSteps();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return steps.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            try {
                container.removeView((View) object);
            } catch (Exception e) {
                Log.i(TAG, "failed to destroy step in recipe item");
            }
        }
    }
}
