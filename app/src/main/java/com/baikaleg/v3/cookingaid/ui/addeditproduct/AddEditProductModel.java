package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.baikaleg.v3.cookingaid.data.DatabaseCallback;
import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class AddEditProductModel extends ViewModel implements DatabaseCallback {

    private AddEditProductNavigator navigator;

    private CompositeDisposable compositeDisposable;

    private MutableLiveData<List<String>> catalogEntityNames = new MutableLiveData<>();

    private MutableLiveData<CatalogEntity> catalogEntity = new MutableLiveData<>();

    private MutableLiveData<ProductEntity> productEntity = new MutableLiveData<>();

    private MutableLiveData<Float> price = new MutableLiveData<>();

    private MutableLiveData<Boolean> isEditable = new MutableLiveData<>();

    private MutableLiveData<Boolean> isUnitMeasure = new MutableLiveData<>();

    private String measure, unitMeasure;

    @Inject
    public Repository repository;

    @Inject
    @Named("dialogId")
    public int dialogId;

    @Inject
    AddEditProductModel(Repository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();

        if (dialogId == 0) {
            repository.loadAllCatalogEntities(this);
            isEditable.setValue(true);
        } else {
            repository.loadProductEntityById(dialogId, this);
            isEditable.setValue(false);
        }
    }

    public void setNavigator(AddEditProductNavigator navigator) {
        this.navigator = navigator;
    }

    public MutableLiveData<List<String>> getCatalogEntityNames() {
        return catalogEntityNames;
    }

    public MutableLiveData<CatalogEntity> getCatalogEntity() {
        return catalogEntity;
    }

    public MutableLiveData<ProductEntity> getProductEntity() {
        return productEntity;
    }

    public MutableLiveData<Boolean> getIsEditable() {
        return isEditable;
    }

    public MutableLiveData<Boolean> getIsUnitMeasure() {
        return isUnitMeasure;
    }

    @Override
    public void onAllCatalogEntitiesLoaded(List<CatalogEntity> catalogEntities) {
        compositeDisposable.add(Flowable.fromIterable(catalogEntities)
                .flatMap(Flowable::fromArray)
                .map(Ingredient::getIngredient)
                .toList()
                .subscribe(list -> catalogEntityNames.postValue(list)));
    }

    @Override
    public void onCatalogEntityByNameLoaded(CatalogEntity entity) {
        catalogEntity.setValue(entity);
    }

    @Override
    public void onProductEntityByIdLoaded(ProductEntity entity) {
        productEntity.setValue(entity);
        repository.loadCatalogEntityByName(entity.getIngredient(), this);
    }

    @Override
    public void onEntitiesSaved() {
        navigator.onCancel();
    }

    public void onMeasureSelectItem(AdapterView<?> parent, View view, int position, long l) {
        measure = parent.getItemAtPosition(position).toString();
        if (measure.equals("UNIT")) {
            isUnitMeasure.setValue(true);
        } else {
            isUnitMeasure.setValue(false);
        }
    }

    public void onUnitMeasureSelectItem(AdapterView<?> parent, View view, int position, long l) {
        unitMeasure = parent.getItemAtPosition(position).toString();
    }

    public void onSaveBtnClicked(String ingredient, float quantity, float unitQuantity, float price, float calories, int expiration, float density) {
        compositeDisposable.clear();
        if (isEditable.getValue() != null && catalogEntity.getValue() != null) {
            //ProductEntity product;
            boolean isNew = false;
            CatalogEntity catalog = catalogEntity.getValue();
            if (isEditable.getValue()) {
                if (!catalogEntity.getValue().getIngredient().equals(ingredient)) {
                    catalog = new CatalogEntity(0, null, ingredient);
                    isNew = true;
                }
            }
            catalog.setPrice(price);
            catalog.setCalories(calories);
            catalog.setExpiration(expiration);
            catalog.setDensity(density);
            if (measure.equals("UNIT")) {
                catalog.setUnitQuantity(unitQuantity);
                catalog.setUnitMeasure(unitMeasure);
            }

            if (isNew) {
                repository.saveCatalogEntity(catalog, this);
            } else {
                repository.updateCatalogEntity(catalog, this);
            }

        }


        /* if (catalogEntity != null) {

            }

            if (productEntity != null) {
                productEntity.setQuantity(quantity);
                productEntity.setMeasure(measure);
            } else {
                productEntity = new ProductEntity(quantity, measure, ingredient);
            }
            productEntity.setPrice(price);
            productEntity.setCalories(calories);
            productEntity.setExpiration(expiration);
            productEntity.setDensity(density);
            productEntity.setUnitQuantity(unitQuantity);
            productEntity.setUnitMeasure(unitMeasure);*/
    }

    public AdapterView.OnItemClickListener listener = (parent, view, position, l) -> {
        String ingredient = parent.getItemAtPosition(position).toString();
        repository.loadCatalogEntityByName(ingredient, this);
    };
}
