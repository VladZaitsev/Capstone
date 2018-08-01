package com.baikaleg.v3.cookingaid.ui.recipes.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;


public class RecipeIngredientView extends RelativeLayout {
    private TextView ingredientTxt, detailsTxt;
    private View root;

    public RecipeIngredientView(Context context) {
        super(context);
        init();
    }

    public RecipeIngredientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        root = inflate(getContext(), R.layout.view_ingredient_in_item_recipe, this);
        ingredientTxt = findViewById(R.id.ingredient_txt);
        detailsTxt = findViewById(R.id.details_txt);
    }

    public void setIngredient(Ingredient ingredient, float ratio) {
        ingredientTxt.setText(ingredient.getIngredient());
        detailsTxt.setText(String.valueOf(ingredient.getQuantity() * ratio) + " " + ingredient.getMeasure());

        switch (ingredient.getIngredientState()) {
            case 0:
                root.setBackgroundColor(getResources().getColor(R.color.ingredient_is_not_available));
                break;
            case 1:
                root.setBackgroundColor(getResources().getColor(R.color.ingredient_not_enough));
                break;
            case 2:
                root.setBackgroundColor(getResources().getColor(R.color.ingredient_is_available));
                break;
        }
    }
}