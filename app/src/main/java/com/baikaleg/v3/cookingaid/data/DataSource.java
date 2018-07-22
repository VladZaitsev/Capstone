package com.baikaleg.v3.cookingaid.data;

import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.DatabaseCallback;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface DataSource {

    Observable<List<Recipe>> getRecipes();

    Flowable<List<ProductEntity>> loadAllStorageEntities();

    void loadAllCatalogEntities(DatabaseCallback callback);

    void loadCatalogEntityByName(String name, DatabaseCallback callback);

    void loadProductEntityById(int id, DatabaseCallback callback);

    void saveCatalogEntity(CatalogEntity entity, DatabaseCallback callback);

    void updateCatalogEntity(CatalogEntity entity, DatabaseCallback callback);

    void saveProductEntity(ProductEntity entity, DatabaseCallback callback);

    void updateProductEntity(ProductEntity entity, DatabaseCallback callback);
}
