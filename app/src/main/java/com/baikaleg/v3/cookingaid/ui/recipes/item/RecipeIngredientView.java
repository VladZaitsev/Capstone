package com.baikaleg.v3.cookingaid.ui.recipes.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;


public class RecipeIngredientView extends RelativeLayout {
    private TextView ingredientTxt, quantityTxt, measureTxt;

    public RecipeIngredientView(Context context) {
        super(context);
        init();
    }

    public RecipeIngredientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        inflate(getContext(), R.layout.view_ingredient_in_item_recipe, this);
        ingredientTxt = findViewById(R.id.ingredient_txt);
        quantityTxt = findViewById(R.id.quantity_txt);
        measureTxt = findViewById(R.id.measure_txt);
    }

    public void setIngredient(Ingredient ingredient) {
        ingredientTxt.setText(ingredient.getIngredient());
        quantityTxt.setText(String.valueOf(ingredient.getQuantity()));
        measureTxt.setText(ingredient.getMeasure());
    }
}