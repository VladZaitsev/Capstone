package com.baikaleg.v3.cookingaid.ui.basket;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.callback.OnProductEntityLoadedListener;
import com.baikaleg.v3.cookingaid.data.callback.OnProductEntitySaveListener;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductList;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BasketViewModel extends AndroidViewModel {
    private final static int STATE = 2;
    private final Repository repository;
    private ProductList productList;

    private final MutableLiveData<List<ProductEntity>> data = new MutableLiveData<>();
    private final MutableLiveData<String> totalPrice = new MutableLiveData<>();
    private final MutableLiveData<String> totalWeight = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEmpty = new MutableLiveData<>();

    private OnProductEntityLoadedListener loadedListener = new OnProductEntityLoadedListener() {
        @Override
        public void onAllProductEntitiesLoaded(List<ProductEntity> list) {
            data.setValue(list);
            productList.clear();
            for (ProductEntity e : list) {
                productList.add(e);
            }
            totalPrice.setValue(String.format(Locale.getDefault(), "%.2f", productList.getTotalPrice()));
            totalWeight.setValue(String.format(Locale.getDefault(), "%.2f", productList.getTotalWeight()));

            if (list.size() != 0) {
                isEmpty.setValue(false);
            } else {
                isEmpty.setValue(true);
            }
        }

        @Override
        public void onProductEntityByIdLoaded(ProductEntity entity) {

        }
    };

    private OnProductEntitySaveListener saveListener = () -> {

    };

    public BasketViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        productList = new ProductList();
        loadData();
    }

    public MutableLiveData<List<ProductEntity>> getData() {
        return data;
    }

    public MutableLiveData<String> getTotalPrice() {
        return totalPrice;
    }

    public MutableLiveData<String> getTotalWeight() {
        return totalWeight;
    }

    public MutableLiveData<Boolean> getIsEmpty() {
        return isEmpty;
    }

    private void loadData() {
        repository.loadAllProductEntities(loadedListener, STATE);
    }

    public void remove(ProductEntity entity) {
        repository.removeProductEntity(entity);
    }

    public void bought(ProductEntity entity) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        entity.setState(3);
        entity.setPurchaseDate(calendar.getTime());
        repository.updateProductEntity(entity, saveListener);
    }

    public void onDestroy() {
        loadedListener = null;
        saveListener = null;
        repository.onDestroyed();
    }
}
