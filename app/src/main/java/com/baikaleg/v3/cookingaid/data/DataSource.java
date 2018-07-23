package com.baikaleg.v3.cookingaid.data;

import com.baikaleg.v3.cookingaid.data.callback.OnCatalogEntityLoadedListener;
import com.baikaleg.v3.cookingaid.data.callback.OnCatalogEntitySaveListener;
import com.baikaleg.v3.cookingaid.data.callback.OnProductEntityLoadedListener;
import com.baikaleg.v3.cookingaid.data.callback.OnProductEntitySaveListener;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.data.model.Recipe;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface DataSource {

    Observable<List<Recipe>> getRecipes();

    void loadAllProductEntities(OnProductEntityLoadedListener listener, int state);

    void loadAllCatalogEntities(OnCatalogEntityLoadedListener listener);

    void loadProductEntityById(int id, OnProductEntityLoadedListener listener);

    void loadCatalogEntityByName(String name, OnCatalogEntityLoadedListener listener);

    void saveCatalogEntity(CatalogEntity entity, OnCatalogEntitySaveListener listener);

    void updateCatalogEntity(CatalogEntity entity, OnCatalogEntitySaveListener listener);

    void saveProductEntity(ProductEntity entity, OnProductEntitySaveListener listener);

    void updateProductEntity(ProductEntity entity, OnProductEntitySaveListener listener);

    void removeProductEntity(ProductEntity entity);
}
