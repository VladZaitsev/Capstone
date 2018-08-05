package com.baikaleg.v3.cookingaid.data.callback;

import com.baikaleg.v3.cookingaid.data.database.entity.ProductEntity;

import java.util.List;

public interface OnProductEntityLoadedListener {
    void onAllProductEntitiesLoaded(List<ProductEntity> list);

    void onProductEntityByIdLoaded(ProductEntity entity);
}
