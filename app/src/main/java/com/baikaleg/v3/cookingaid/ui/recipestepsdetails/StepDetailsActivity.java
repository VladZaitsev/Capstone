package com.baikaleg.v3.cookingaid.ui.recipestepsdetails;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.databinding.ActivityStepDetailsBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

public class StepDetailsActivity extends DaggerAppCompatActivity {
    public static final String EXTRA_RECIPE = "RECIPE";
    public static final String EXTRA_STEP_POSITION = "POSITION";

    @Inject
    Recipe recipe;

    @Inject
    Integer currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        ActivityStepDetailsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_step_details);

        StepDetailsPagerAdapter adapter = new StepDetailsPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < recipe.getSteps().size(); i++) {
            adapter.addFrag(StepDetailsFragment.newInstance(recipe.getSteps().get(i)));
        }
        binding.pagerSteps.setAdapter(adapter);
        binding.pagerSteps.setCurrentItem(currentPosition);

        binding.pagerSteps.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                adapter.initializePlayer(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_SETTLING) {
                    adapter.releasePlayer(currentPosition);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class StepDetailsPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        StepDetailsPagerAdapter(FragmentManager fm) {
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

        void initializePlayer(int position) {
            ((StepDetailsFragment) fragments.get(position)).initializePlayer();
        }

        void releasePlayer(int position) {
            ((StepDetailsFragment) fragments.get(position)).releasePlayer();
        }
    }
}
