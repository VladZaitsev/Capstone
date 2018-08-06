package com.baikaleg.v3.cookingaid.ui.storage;

import com.baikaleg.v3.cookingaid.data.database.entity.ProductEntity;

interface StorageItemNavigator {

    void onItemClicked(int id);

    void onItemRemoved(ProductEntity entity);
}
