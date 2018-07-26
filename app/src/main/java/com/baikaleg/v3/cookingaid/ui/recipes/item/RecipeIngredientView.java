package com.baikaleg.v3.cookingaid.ui.recipes.item;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;


public class RecipeIngredientView extends RelativeLayout {
    private TextView ingredientTxt, quantityTxt, measureTxt;
    private ImageButton img;

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
        img = findViewById(R.id.available_img);
    }

    public void setIngredient(Ingredient ingredient, float ratio) {
        ingredientTxt.setText(ingredient.getIngredient());
        quantityTxt.setText(String.valueOf(ingredient.getQuantity() * ratio));
        measureTxt.setText(ingredient.getMeasure());

        switch (ingredient.getIngredientState()) {
            case 0:
                img.setBackground(getResources().getDrawable(R.drawable.ic_close));
                break;
            case 1:
                img.setBackground(getResources().getDrawable(R.drawable.ic_warning));
                break;
            case 2:
                img.setBackground(getResources().getDrawable(R.drawable.ic_check));
                break;

        }
    }
}