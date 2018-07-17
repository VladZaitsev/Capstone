package com.baikaleg.v3.cookingaid.data.database.entity.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductList extends CatalogEntity {
    private List<Product> children = new ArrayList<>();

    public ProductList() {
        super(0, null, null);
    }

    public void add(Product component) {
        children.add(component);
    }

    public void add(Product... components) {
        children.addAll(Arrays.asList(components));
    }

    public void remove(Product child) {
        int i = children.indexOf(child);
        children.remove(i);
    }

    public void remove(Product... components) {
        children.removeAll(Arrays.asList(components));
    }

    public void clear() {
        children.clear();
    }

    @Override
    public float getTotalCalories() {
        if (children.size() == 0) {
            return 0;
        }

        float calories = 0;
        for (Product product : children) {
            calories = calories + product.getTotalCalories();
        }
        return calories;
    }

    @Override
    public float getTotalPrice() {
        if (children.size() == 0) {
            return 0;
        }

        float price = 0;
        for (Product product : children) {
            price = price + product.getTotalPrice();
        }
        return price;
    }

    @Override
    public float getTotalWeight() {
        if (children.size() == 0) {
            return 0;
        }

        float weight = 0;
        for (Product product : children) {
            weight = weight + product.getTotalWeight();
        }
        return weight;
    }

    @Override
    public float getTransformedQuantity(String measure) {
        if (children.size() == 0) {
            return 0;
        }

        float quantity = 0;
        for (Product product : children) {
            quantity = quantity + product.getTransformedQuantity(measure);
        }
        return quantity;
    }
}
