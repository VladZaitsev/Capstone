package com.baikaleg.v3.cookingaid.data;

import android.content.Context;

import com.baikaleg.v3.cookingaid.data.database.AppDatabase;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.data.model.Recipe;
import com.baikaleg.v3.cookingaid.data.network.RecipeApi;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.DatabaseCallback;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("WeakerAccess")

public class Repository implements DataSource {

    private final RecipeApi recipeApi;
    private AppDatabase db;
    private CompositeDisposable compositeDisposable;

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
                        .doOnNext(recipe -> recipe.setCategory("dessert"))
                        .toList()
                        .toObservable());
    }

    @Override
    public Flowable<List<ProductEntity>> loadAllStorageEntities() {
        return db.productDao().loadAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void loadAllCatalogEntities(DatabaseCallback callback) {
        compositeDisposable.add(db.catalogDao()
                .loadAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onAllCatalogEntitiesLoaded));
    }

    @Override
    public void loadCatalogEntityByName(String name, DatabaseCallback callback) {
        compositeDisposable.add(db.catalogDao()
                .loadProductByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onCatalogEntityByNameLoaded));
    }

    @Override
    public void loadProductEntityById(int id, DatabaseCallback callback) {
        compositeDisposable.add(db.productDao().loadProductById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onProductEntityByIdLoaded));
    }

    @Override
    public void saveProductEntity(ProductEntity entity, DatabaseCallback callback) {
        Completable.fromAction(() -> db.productDao().insertProduct(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        callback.onProductEntitySaved();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void updateProductEntity(ProductEntity entity, DatabaseCallback callback) {
        Completable.fromAction(() -> db.productDao().updateProduct(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        callback.onProductEntitySaved();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void saveCatalogEntity(CatalogEntity entity, DatabaseCallback callback) {
        Completable.fromAction(() -> db.catalogDao().insertProduct(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        callback.onCatalogEntitySaved();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void updateCatalogEntity(CatalogEntity entity, DatabaseCallback callback) {
        Completable.fromAction(() -> db.catalogDao().updateProduct(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        callback.onCatalogEntitySaved();
                    }

                    @Override
                    public void onError(Throwable e) {

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

                    }
                });
    }

    public void onDestroyed() {
        db.close();
        compositeDisposable.clear();
    }
}
