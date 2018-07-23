package com.baikaleg.v3.cookingaid.data.callback;

import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;

import java.util.List;

public interface OnCatalogEntityLoadedListener {

    void onAllCatalogIngredientsLoaded(List<String> list);

    void onCatalogEntityByNameLoaded(CatalogEntity entity);
}
