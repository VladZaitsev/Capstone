package com.baikaleg.v3.cookingaid.ui.basket;

import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;

public interface BasketItemNavigator {

    void onItemClicked(int id);

    void onItemRemoved(ProductEntity entity);

    void onSelected(ProductEntity entity);
}
