package com.baikaleg.v3.cookingaid.ui.recipes;

import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.databinding.ActivityRecipeBinding;
import com.baikaleg.v3.cookingaid.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends BaseActivity {

    private ActivityRecipeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(getString(R.string.title_activity_recipes));
        // Inflate view and obtain an instance of the binding class.
        LayoutInflater inflater = LayoutInflater.from(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_recipe, frameLayout, true);
        //  setContentView(this, R.layout.activity_recipe);

        //Original names of recipes categories
        String[] originalNames = getResources().getStringArray(R.array.categories_original);
        //Titles of recipes categories
        String[] titles = getResources().getStringArray(R.array.categories);
        //Images of recipes categories
        TypedArray images = getResources().obtainTypedArray(R.array.category_imgs);

        createFragments(originalNames);
        binding.tabs.setupWithViewPager(binding.viewpager);

        for (int i = 0; i < titles.length; i++) {
            setTab(titles[i], images.getResourceId(i, -1), i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void createFragments(String[] names) {
        CategoryPagerAdapter adapter = new CategoryPagerAdapter(getSupportFragmentManager());
        for (String name : names) {
            adapter.addFrag(RecipesFragment.newInstance(name));
        }
        binding.viewpager.setAdapter(adapter);
    }

    private void setTab(String title, int imageId, int i) {
        TextView tab = (TextView) LayoutInflater
                .from(this)
                .inflate(R.layout.tab_recipe_item, null);
        tab.setText(title);
        tab.setPadding(0,8,0,0);
       // tab.setBackgroundColor(getResources().getColor(R.color.colorTabBackground));
        tab.setCompoundDrawablesWithIntrinsicBounds(0, imageId, 0, 0);
        binding.tabs.getTabAt(i).setCustomView(tab);
    }

    private class CategoryPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        CategoryPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFrag(Fragment fragment) {
            fragments.add(fragment);

        }
    }
}
