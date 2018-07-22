package com.baikaleg.v3.cookingaid.ui.storage;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.NonNull;
import android.util.Log;

import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class StorageViewModel extends AndroidViewModel implements Observable {

    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();

    private final Repository repository;

    public final MutableLiveData<List<ProductEntity>> data = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable;

    StorageViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        this.compositeDisposable = new CompositeDisposable();
        loadData();
    }

    private void loadData() {
        compositeDisposable.add(repository.loadAllStorageEntities()
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

    public void remove(ProductEntity entity) {
        repository.removeProductEntity(entity);
    }

    public void onDestroy() {
        compositeDisposable.clear();
        repository.onDestroyed();
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
