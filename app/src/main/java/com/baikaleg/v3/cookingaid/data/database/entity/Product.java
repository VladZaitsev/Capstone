package com.baikaleg.v3.cookingaid.data.database.entity;

public interface Product {

    float getTotalPrice();

    float getTotalCalories();

    float getTotalWeight();

    float getTransformedQuantity(String measure);

    void setTotalPrice(float total);
}
