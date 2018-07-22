package com.baikaleg.v3.cookingaid.ui.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baikaleg.v3.cookingaid.databinding.FragmentRecipeBinding;
import com.baikaleg.v3.cookingaid.ui.recipes.adapter.RecipesViewAdapter;

public class RecipesFragment extends Fragment {
    private static final String arg_cat="argCategory";

    private RecipesViewModel recipesViewModel;

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

            recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
            recipesViewModel.setCategory(category);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRecipeBinding binding = FragmentRecipeBinding.inflate(inflater, container, false);

        binding.setViewmodel(recipesViewModel);

        RecipesViewAdapter adapter = new RecipesViewAdapter(getActivity());
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
