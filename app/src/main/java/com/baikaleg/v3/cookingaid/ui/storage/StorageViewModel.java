package com.baikaleg.v3.cookingaid.ui.storage;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.NonNull;
import android.util.Log;

import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

@ActivityScoped
public class StorageViewModel extends ViewModel implements Observable {

    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();

    private final Repository repository;

    public final MutableLiveData<List<ProductEntity>> data = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable;

    @Inject
    public StorageViewModel(Repository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();

        loadData();
    }

    private void loadData() {
        compositeDisposable.add(repository.loadAllStorageEntities(3)
                /*.flatMap(Flowable::fromIterable)
                .filter(productEntity -> productEntity.getState()==3)
                .toList()
                .toFlowable()*/
                .subscribe(productEntities -> {
                            data.setValue(productEntities);
                            notifyChange();
                        }
                        , throwable -> Log.i("ds", throwable.getMessage())));
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }

    private void notifyChange() {
        callbacks.notifyCallbacks(this, 0, null);
    }
}
