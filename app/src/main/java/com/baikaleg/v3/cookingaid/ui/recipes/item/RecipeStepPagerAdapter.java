package com.baikaleg.v3.cookingaid.ui.recipes.item;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.databinding.ViewStepInItemRecipeBinding;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepPagerAdapter extends PagerAdapter {
    private static final String TAG = RecipeStepPagerAdapter.class.getSimpleName();

    private List<String> titles = new ArrayList<>();

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ViewStepInItemRecipeBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.view_step_in_item_recipe, container, false);
        binding.stepShortDescription.setText(titles.get(position));
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    public void refresh(List<String> list) {
        this.titles = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return titles.size();
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
            Log.i(TAG, "failed to destroy reviewItem");
        }
    }
}