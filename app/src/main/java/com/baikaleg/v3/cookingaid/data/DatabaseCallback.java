package com.baikaleg.v3.cookingaid.data;

import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;

import java.util.List;

public interface DatabaseCallback {
    void onAllCatalogEntitiesLoaded(List<CatalogEntity> list);

    void onCatalogEntityByNameLoaded(CatalogEntity entity);

    void onProductEntityByIdLoaded(ProductEntity entity);
}
