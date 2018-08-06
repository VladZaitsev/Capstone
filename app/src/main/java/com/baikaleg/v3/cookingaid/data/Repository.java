package com.baikaleg.v3.cookingaid.data;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.content.Context;

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

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Repository implements DataSource {

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
    public Flowable<ArrayList<String>> loadExpiryProductsNames() {
        return db.productDao().loadAllProducts()
                .flatMap(productEntities -> Flowable.fromIterable(productEntities)
                        .filter(productEntity -> productEntity.getProductState() == AddEditProductModel.DIALOG_STORAGE_ID)
                        .filter(productEntity -> AppUtils.timeDiff(productEntity) < 0)
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
        //TODO Select state
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
        Completable.fromAction(() -> db.productDao().insertProduct(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        if (listener != null) {
                            listener.onProductEntitySaved();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.log(e.getMessage());
                    }
                });
    }

    @Override
    public void updateProductEntity(ProductEntity entity, OnProductEntitySaveListener listener) {
        Completable.fromAction(() -> db.productDao().updateProduct(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        if (listener != null) {
                            listener.onProductEntitySaved();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.log(e.getMessage());
                    }
                });
    }

    @Override
    public void saveCatalogEntity(CatalogEntity entity, OnCatalogEntitySaveListener listener) {
        Completable.fromAction(() -> db.catalogDao().insertProduct(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        listener.onCatalogEntitySaved();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.log(e.getMessage());
                    }
                });
    }

    @Override
    public void updateCatalogEntity(CatalogEntity entity, OnCatalogEntitySaveListener listener) {
        Completable.fromAction(() -> db.catalogDao().updateProduct(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        listener.onCatalogEntitySaved();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.log(e.getMessage());
                    }
                });
    }

    @Override
    public void removeProductEntity(ProductEntity entity) {
        Completable.fromAction(() -> db.productDao().deleteProduct(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.log(e.getMessage());
                    }
                });
    }

    public void onDestroyed() {
        db.close();
        compositeDisposable.clear();
    }
}
