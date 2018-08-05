package com.baikaleg.v3.cookingaid.data;

import com.baikaleg.v3.cookingaid.data.callback.OnCatalogEntityLoadedListener;
import com.baikaleg.v3.cookingaid.data.callback.OnCatalogEntitySaveListener;
import com.baikaleg.v3.cookingaid.data.callback.OnProductEntityLoadedListener;
import com.baikaleg.v3.cookingaid.data.callback.OnProductEntitySaveListener;
import com.baikaleg.v3.cookingaid.data.database.entity.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.ProductEntity;
import com.baikaleg.v3.cookingaid.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface DataSource {

    Observable<List<Recipe>> getRecipes();

    void loadAllCatalogIngredients(OnCatalogEntityLoadedListener listener);

    Flowable<List<CatalogEntity>> loadCatalogEntitiesByQuery(String ingredient);

    void loadCatalogEntityByName(String name, OnCatalogEntityLoadedListener listener);

    void loadAllProductEntities(OnProductEntityLoadedListener listener, int state);

    Flowable<ArrayList<String>> loadExpiryProductsNames();

    Single<List<ProductEntity>> loadProductEntitiesByQuery(String ingredient);

    void loadProductEntityById(int id, OnProductEntityLoadedListener listener);

    void saveCatalogEntity(CatalogEntity entity, OnCatalogEntitySaveListener listener);

    void updateCatalogEntity(CatalogEntity entity, OnCatalogEntitySaveListener listener);

    void saveProductEntity(ProductEntity entity, OnProductEntitySaveListener listener);

    void updateProductEntity(ProductEntity entity, OnProductEntitySaveListener listener);

    void removeProductEntity(ProductEntity entity);
}
