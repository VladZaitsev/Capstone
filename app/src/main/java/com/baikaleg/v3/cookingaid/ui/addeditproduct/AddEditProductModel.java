package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.AdapterView;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.DatabaseCallback;
import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddEditProductModel extends ViewModel implements DatabaseCallback {

    private AddEditProductNavigator navigator;

    private CompositeDisposable compositeDisposable;

    private MutableLiveData<List<String>> catalogEntityNames = new MutableLiveData<>();

    private MutableLiveData<ProductEntity> productEntity = new MutableLiveData<>();

    private MutableLiveData<String> measure = new MutableLiveData<>();

    private MutableLiveData<Float> price = new MutableLiveData<>();

    private boolean isEditable;

    private int oldId;

    private String oldIngredient;

    private Repository repository;

    private Resources resources;

    AddEditProductModel(Context context, int dialogId, int productId) {
        this.repository = Repository.getInstance(context);
        this.resources = context.getResources();
        compositeDisposable = new CompositeDisposable();

        ProductEntity entity = new ProductEntity(0, resources.getString(R.string.kg_measure), null);
        entity.setState(dialogId);
        productEntity.setValue(entity);
        measure.setValue(resources.getString(R.string.kg_measure));

        if (productId == 0) {
            repository.loadAllCatalogEntities(this);
            isEditable = true;
        } else {
            repository.loadProductEntityById(productId, this);
            isEditable = false;
        }
    }

    public void setNavigator(AddEditProductNavigator navigator) {
        this.navigator = navigator;
    }

    public MutableLiveData<List<String>> getCatalogEntityNames() {
        return catalogEntityNames;
    }

    public MutableLiveData<ProductEntity> getProductEntity() {
        return productEntity;
    }

    public MutableLiveData<String> getMeasure() {
        return measure;
    }

    public MutableLiveData<Float> getPrice() {
        return price;
    }

    public boolean isEditable() {
        return isEditable;
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
    public void onCatalogEntityByNameLoaded(CatalogEntity data) {
        if (productEntity.getValue() != null) {
            if (isEditable) {
                ProductEntity entity = new ProductEntity(0, resources.getString(R.string.kg_measure), data.getIngredient());
                entity.setCalories(data.getCalories());
                entity.setDensity(data.getDensity());
                entity.setPrice(data.getPrice());
                entity.setExpiration(data.getExpiration());
                if (!TextUtils.isEmpty(data.getUnitMeasure())) {
                    entity.setUnitMeasure(data.getUnitMeasure());
                    entity.setUnitQuantity(data.getUnitQuantity());
                    entity.setMeasure(resources.getString(R.string.unit_measure));
                    measure.setValue(resources.getString(R.string.unit_measure));
                } else {
                    measure.setValue(resources.getString(R.string.kg_measure));
                }
                productEntity.setValue(entity);
            }
            oldId = data.getId();
            oldIngredient = data.getIngredient();
            price.postValue(data.getPrice());
        }
    }

    @Override
    public void onProductEntityByIdLoaded(ProductEntity entity) {
        productEntity.setValue(entity);
        measure.setValue(entity.getMeasure());
        repository.loadCatalogEntityByName(entity.getIngredient(), this);
    }

    public void onSaveBtnClicked() {
        compositeDisposable.clear();
        if (productEntity.getValue() != null) {
            productEntity.getValue().fromTotalPrice(price.getValue());
            productEntity.getValue().setMeasure(measure.getValue());
            CatalogEntity temp = productEntity.getValue();
            temp.setMeasure(null);
            temp.setQuantity(0);

            if (isEditable) {
                productEntity.getValue().setId(0);
                repository.saveProductEntity(productEntity.getValue());
                if (!temp.getIngredient().equals(oldIngredient)) {
                    repository.saveCatalogEntity(temp);
                } else {
                    temp.setId(oldId);
                    repository.updateCatalogEntity(temp);
                }
            } else {
                temp.setId(oldId);
                repository.updateCatalogEntity(temp);
                repository.updateProductEntity(productEntity.getValue());
            }
            navigator.onCancel();
        }
    }

    public void onCancelBtnClicked() {
        navigator.onCancel();
    }

    public void ingredientTextChangedListener(Editable s) {
        compositeDisposable.add(Observable.just(s)
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(editable -> {
                    Objects.requireNonNull(productEntity.getValue()).setIngredient(editable.toString());
                }));
    }

    public AdapterView.OnItemClickListener ingredientItemClickListener = (parent, view, position, l) -> {
        String ingredient = parent.getItemAtPosition(position).toString();
        repository.loadCatalogEntityByName(ingredient, this);
    };
}
