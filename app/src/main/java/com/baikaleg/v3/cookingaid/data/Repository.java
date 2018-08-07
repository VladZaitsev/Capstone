package com.baikaleg.v3.cookingaid.data;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.content.Context;
import android.os.AsyncTask;

import com.baikaleg.v3.cookingaid.data.callback.OnCatalogEntityLoadedListener;
import com.baikaleg.v3.cookingaid.data.callback.OnCatalogEntitySaveListener;
import com.baikaleg.v3.cookingaid.data.callback.OnProductEntityLoadedListener;
import com.baikaleg.v3.cookingaid.data.callback.OnProductEntitySaveListener;
import com.baikaleg.v3.cookingaid.data.database.AppDatabase;
import com.baikaleg.v3.cookingaid.data.database.entity.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.ProductEntity;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.data.network.RecipeApi;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductModel;
import com.baikaleg.v3.cookingaid.util.AppUtils;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Repository implements DataSource {
    private final static int UPDATE_TYPE = 0;
    private final static int SAVE_TYPE = 1;

    private final RecipeApi recipeApi;
    private final AppDatabase db;
    private final CompositeDisposable compositeDisposable;

    private static Repository instance;

    public static Repository getInstance(Context context) {
        if (instance == null) {
            instance = new Repository(context);
        }
        return instance;
    }

    private Repository(Context context) {
        recipeApi = new RecipeApi(context);
        db = AppDatabase.getInstance(context);
        compositeDisposable = new CompositeDisposable();
    }

    //TODO Replace category inserting after source changing
    @Override
    public Observable<List<Recipe>> getRecipes() {
        return recipeApi.createService().getRecipes()
                .flatMap(recipes -> Observable.fromIterable(recipes)
                        .doOnNext(recipe -> {
                            recipe.setCategory("dessert");
                            // recipe.setImage("https://vk.com/doc2131185_450915954?hash=d858773117387e2a2f&dl=73d3832f40b8a68d3f");
                        })
                        .toList()
                        .toObservable());
    }

    @Override
    public void loadAllProductEntities(OnProductEntityLoadedListener listener, int state) {
        compositeDisposable.add(db.productDao()
                .loadAllProducts()
                .flatMap(productEntities -> Flowable.fromIterable(productEntities)
                        .filter(productEntity -> productEntity.getProductState() == state)
                        .toList()
                        .toFlowable())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onAllProductEntitiesLoaded, throwable -> Crashlytics.log(throwable.getMessage())));
    }

    @Override
    public Flowable<ArrayList<String>> loadShoppingList() {
        return db.productDao().loadAllProducts()
                .flatMap(productEntities -> Flowable.fromIterable(productEntities)
                        .filter(productEntity -> productEntity.getProductState() == AddEditProductModel.DIALOG_BASKET_ID)
                        .map(Ingredient::getIngredient)
                        .toList()
                        .toFlowable()
                        .map(ArrayList::new));
    }

    @Override
    public void loadAllCatalogIngredients(OnCatalogEntityLoadedListener listener) {
        compositeDisposable.add(db.catalogDao()
                .loadAllProducts()
                .flatMap(catalogEntities -> Flowable.fromIterable(catalogEntities)
                        .map(Ingredient::getIngredient)
                        .toList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onAllCatalogIngredientsLoaded, throwable -> Crashlytics.log(throwable.getMessage())));
    }

    @Override
    public Flowable<List<CatalogEntity>> loadCatalogEntitiesByQuery(String ingredient) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                AppUtils.productLoadQuery("catalog", "ingredient", ingredient));
        return db.catalogDao()
                .loadProductsByQuery(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void loadCatalogEntityByName(String name, OnCatalogEntityLoadedListener listener) {
        compositeDisposable.add(db.catalogDao()
                .loadProductByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onCatalogEntityByNameLoaded, throwable -> Crashlytics.log(throwable.getMessage())));
    }

    @Override
    public Single<List<ProductEntity>> loadProductEntitiesByQuery(String ingredient) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                AppUtils.productLoadQuery("product", "ingredient", ingredient));
        return db.productDao()
                .loadProductsByQuery(query)
                .flatMap(productEntities -> Flowable.fromIterable(productEntities)
                        .filter(productEntity -> productEntity.getProductState() == 3)
                        .toList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void loadProductEntityById(int id, OnProductEntityLoadedListener listener) {
        compositeDisposable.add(db.productDao().loadProductById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onProductEntityByIdLoaded, throwable -> Crashlytics.log(throwable.getMessage())));
    }

    @Override
    public void saveProductEntity(ProductEntity entity, OnProductEntitySaveListener listener) {
        new SaveProductEntityTask(entity, listener, db, SAVE_TYPE).execute();
    }

    @Override
    public void updateProductEntity(ProductEntity entity, OnProductEntitySaveListener listener) {
        new SaveProductEntityTask(entity, listener, db, UPDATE_TYPE).execute();
    }

    @Override
    public void saveCatalogEntity(CatalogEntity entity, OnCatalogEntitySaveListener listener) {
       new SaveCatalogEntityTask(entity, listener, db, SAVE_TYPE).execute();
    }

    @Override
    public void updateCatalogEntity(CatalogEntity entity, OnCatalogEntitySaveListener listener) {
        new SaveCatalogEntityTask(entity, listener, db, UPDATE_TYPE).execute();
    }

    @Override
    public void removeProductEntity(ProductEntity entity) {
        new RemoveProductEntityTask(db, entity).execute();
    }

    public void onDestroyed() {
        compositeDisposable.clear();
    }

    private static class SaveProductEntityTask extends AsyncTask<Void, Void, Void> {
        private ProductEntity entity;
        private OnProductEntitySaveListener listener;
        private AppDatabase database;
        private int type;

        SaveProductEntityTask(ProductEntity entity, OnProductEntitySaveListener listener, AppDatabase database, int type) {
            this.entity = entity;
            this.listener = listener;
            this.database = database;
            this.type = type;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (type == SAVE_TYPE) {
                database.productDao().insertProduct(entity);
            } else if (type == UPDATE_TYPE) {
                database.productDao().updateProduct(entity);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (listener != null) {
                listener.onProductEntitySaved();
            }
        }
    }

    private static class SaveCatalogEntityTask extends AsyncTask<Void, Void, Void> {
        private CatalogEntity entity;
        private OnCatalogEntitySaveListener listener;
        private AppDatabase database;
        private int type;

        SaveCatalogEntityTask(CatalogEntity entity, OnCatalogEntitySaveListener listener, AppDatabase database, int type) {
            this.entity = entity;
            this.listener = listener;
            this.database = database;
            this.type = type;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (type == SAVE_TYPE) {
                database.catalogDao().insertProduct(entity);
            } else if (type == UPDATE_TYPE) {
                database.catalogDao().updateProduct(entity);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (listener != null) {
                listener.onCatalogEntitySaved();
            }
        }
    }

    private static class RemoveProductEntityTask extends AsyncTask<Void, Void, Void> {
        private AppDatabase database;
        private ProductEntity entity;

        RemoveProductEntityTask(AppDatabase database, ProductEntity entity) {
            this.database = database;
            this.entity = entity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            database.productDao().deleteProduct(entity);
            return null;
        }
    }
}
