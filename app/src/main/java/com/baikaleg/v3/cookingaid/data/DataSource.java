package com.baikaleg.v3.cookingaid.data;

import com.baikaleg.v3.cookingaid.data.model.Recipe;

import java.util.List;

import io.reactivex.Observable;

public interface DataSource {
    Observable<List<Recipe>> getRecipes();
}
