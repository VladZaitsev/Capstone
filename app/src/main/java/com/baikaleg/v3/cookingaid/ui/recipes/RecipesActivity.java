package com.baikaleg.v3.cookingaid.ui.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.databinding.ActivityRecipeBinding;
import com.baikaleg.v3.cookingaid.ui.recipes.adapter.RecipesViewAdapter;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

@ActivityScoped
public class RecipesActivity extends DaggerAppCompatActivity {

    private ActivityRecipeBinding binding;
    private RecipesViewAdapter adapter;

    @Inject
    public RecipesViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);
        // Obtain the ViewModel component.
        RecipesViewModel recipesViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipesViewModel.class);
        // Assign the component to a property in the binding class.
        binding.setViewmodel(recipesViewModel);

        createAdapter();
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private RecipesViewAdapter createAdapter() {
        adapter = new RecipesViewAdapter();
        return adapter;
    }
}
