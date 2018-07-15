package com.baikaleg.v3.cookingaid.data.database.entity.product;

public interface Product {

    float getPrice();

    float getCalories();

    float getWeight();

    float getQuantity(String measure);
}
