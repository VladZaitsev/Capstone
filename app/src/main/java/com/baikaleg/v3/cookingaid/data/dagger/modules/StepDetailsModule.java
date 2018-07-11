package com.baikaleg.v3.cookingaid.data.dagger.modules;

import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.ui.recipestepsdetails.StepDetailsActivity;

import dagger.Module;
import dagger.Provides;

@Module
public interface StepDetailsModule {

    @Provides
    @ActivityScoped
    static Recipe provideRecipe(StepDetailsActivity activity) {
        return activity.getIntent().getParcelableExtra(StepDetailsActivity.EXTRA_RECIPE);
    }

    @Provides
    @ActivityScoped
    static int provideStepPosition(StepDetailsActivity activity) {
        return activity.getIntent().getIntExtra(StepDetailsActivity.EXTRA_STEP_POSITION, 0);
    }
}
