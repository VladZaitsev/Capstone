package com.baikaleg.v3.cookingaid.ui.recipes;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.databinding.FragmentRecipeBinding;
import com.baikaleg.v3.cookingaid.ui.recipes.dialog.RecountDialog;
import com.baikaleg.v3.cookingaid.ui.recipes.dialog.SendToBasketDialog;
import com.baikaleg.v3.cookingaid.ui.recipes.item.RecipeItemEventNavigator;

public class RecipesFragment extends Fragment implements RecipeItemEventNavigator {
    private static final String arg_cat = "argCategory";
    private static final int RECIPES_RECOUNT_REQUEST_CODE = 101;
    private static final int RECIPES_TO_BASKET_REQUEST_CODE = 102;
    private RecipesViewModel recipesViewModel;
    private Repository repository;
    private RecipesViewAdapter adapter;
    private boolean isLand, isTablet;

    public static RecipesFragment newInstance(String category) {
        RecipesFragment fragment = new RecipesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(arg_cat, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RECIPES_RECOUNT_REQUEST_CODE) {
                int persons = data.getIntExtra(RecountDialog.PERSONS_INTENT_KEY, 0);
                int position = data.getIntExtra(RecountDialog.POSITION_INTENT_KEY, 0);
                adapter.recount(position, persons);
            } else if (requestCode == RECIPES_TO_BASKET_REQUEST_CODE) {
                Recipe recipe = data.getParcelableExtra(SendToBasketDialog.RECIPE_INTENT_KEY);
                float ratio = data.getFloatExtra(SendToBasketDialog.RATIO_INTENT_KEY, 0);
                recipesViewModel.sendToBasket(recipe, ratio);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            repository = Repository.getInstance(getActivity());

            String category = getArguments().getString(arg_cat);

            recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
            recipesViewModel.setCategory(category);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRecipeBinding binding = FragmentRecipeBinding.inflate(inflater, container, false);

        isLand = getResources().getBoolean(R.bool.isLand);
        isTablet = getResources().getBoolean(R.bool.isTablet);

        adapter = new RecipesViewAdapter(getActivity(), repository, this);

        binding.setViewmodel(recipesViewModel);
        binding.setLifecycleOwner(this);
        setViewParameters();

        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recipesViewModel.onDestroyed();
        repository.onDestroyed();
    }

    @Override
    public void onClickRecountBtn(int position) {
        RecountDialog recountDialog = RecountDialog.newInstance(position);
        recountDialog.setTargetFragment(this, RECIPES_RECOUNT_REQUEST_CODE);
        recountDialog.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onClickSendBtn(Recipe recipe, float ratio) {
        SendToBasketDialog basketDialog = SendToBasketDialog.newInstance(recipe, ratio);
        basketDialog.setTargetFragment(this, RECIPES_TO_BASKET_REQUEST_CODE);
        basketDialog.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    private void setViewParameters() {
        int imageWidth, imageHeight;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        if (!isTablet) {
            if (!isLand) {
                imageWidth = screenWidth;
                imageHeight = (imageWidth * 2) / 3;
            } else {
                imageWidth = screenWidth ;
                imageHeight = imageWidth /3;
            }
        } else {
            imageWidth = screenWidth;
            imageHeight = imageWidth /3 ;
        }
        adapter.setImageSize(imageWidth, imageHeight);
    }
}
