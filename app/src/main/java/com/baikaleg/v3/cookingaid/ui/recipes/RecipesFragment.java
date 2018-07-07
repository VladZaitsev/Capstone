package com.baikaleg.v3.cookingaid.ui.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.databinding.FragmentRecipeBinding;
import com.baikaleg.v3.cookingaid.ui.recipes.adapter.RecipesViewAdapter;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class RecipesFragment extends DaggerFragment {
    private static final String arg_cat="argCategory";

    private RecipesViewModel recipesViewModel;

    @Inject
    public RecipesViewModelFactory viewModelFactory;

    @Inject
    public RecipesFragment() {
    }

    public static RecipesFragment newInstance(String category){
        RecipesFragment fragment = new RecipesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(arg_cat, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            String category=getArguments().getString(arg_cat);

            recipesViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipesViewModel.class);
            recipesViewModel.setCategory(category);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRecipeBinding binding = FragmentRecipeBinding.inflate(inflater, container, false);

        binding.setViewmodel(recipesViewModel);

        RecipesViewAdapter adapter = new RecipesViewAdapter();
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recipesViewModel.onDestroyed();
    }
}
