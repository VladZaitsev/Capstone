package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.Bindable;

import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddEditProductModel extends ViewModel {

    private static final String TAG = AddEditProductModel.class.getSimpleName();
    private final Repository repository;
    private AddEditProductEventNavigator navigator;

    private CompositeDisposable compositeDisposable;


    MutableLiveData<List<String>> catalogEntityNames = new MutableLiveData<>();

    MutableLiveData<CatalogEntity> entity = new MutableLiveData<>();

    AddEditProductModel(Repository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        loadCatalogProduct();
    }

    private void loadCatalogProduct() {
        compositeDisposable.add(repository.loadAllCatalogEntities()
                .flatMap(Observable::fromIterable)
                .map(Ingredient::getIngredient)
                .toList()
                .toObservable()
                .subscribe(list -> catalogEntityNames.postValue(list)));
    }
    public void updateCatalogName(String name) {
        entity.setValue(repository.loadProductByName(name));
    }

    void onDestroyed() {
        compositeDisposable.clear();
    }
}
