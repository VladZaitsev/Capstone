package com.baikaleg.v3.cookingaid.data;

import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.data.model.Recipe;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface DataSource {

    Observable<List<Recipe>> getRecipes();

    void loadAllCatalogEntities(DatabaseCallback callback);

    void loadCatalogEntityByName(String name, DatabaseCallback callback);

    void loadProductEntityById(int id, DatabaseCallback callback);

    void saveCatalogEntity(CatalogEntity entity);

    void updateCatalogEntity(CatalogEntity entity);

    void saveProductEntity(ProductEntity entity);

    void updateProductEntity(ProductEntity entity);
}
