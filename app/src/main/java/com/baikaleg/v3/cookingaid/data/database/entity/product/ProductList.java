package com.baikaleg.v3.cookingaid.data.database.entity.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductList extends CatalogEntity {
    protected List<Product> children = new ArrayList<>();

    public ProductList(Product... components) {
        super(0, null, null);
        add(components);
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

}
