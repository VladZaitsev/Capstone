package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.Repository;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;

import java.util.List;
import java.util.Objects;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

public class AddEditProductModel extends ViewModel implements DatabaseCallback {

    private AddEditProductNavigator navigator;

    private CompositeDisposable compositeDisposable;

    private MutableLiveData<List<String>> catalogEntityNames = new MutableLiveData<>();

    private MutableLiveData<ProductEntity> productEntity = new MutableLiveData<>();

    private MutableLiveData<Boolean> isUnitMeasure = new MutableLiveData<>();

    private MutableLiveData<Float> price = new MutableLiveData<>();

    private boolean isEditable;

    private int oldCatalogId, dialogId;

    private String oldIngredient;

    private Repository repository;

    private Resources resources;

    AddEditProductModel(Context context, int dialogId, int productId) {
        this.repository = Repository.getInstance(context);
        this.resources = context.getResources();
        this.dialogId = dialogId;
        compositeDisposable = new CompositeDisposable();

        ProductEntity entity = new ProductEntity(0, resources.getString(R.string.kg_measure), null);
        entity.setState(dialogId);
        productEntity.setValue(entity);

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

    public MutableLiveData<Float> getPrice() {
        return price;
    }

    public MutableLiveData<Boolean> getIsUnitMeasure() {
        return isUnitMeasure;
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
                entity.setState(dialogId);
                entity.setCalories(data.getCalories());
                entity.setDensity(data.getDensity());
                entity.setPrice(data.getPrice());
                entity.setExpiration(data.getExpiration());
                if (!TextUtils.isEmpty(data.getUnitMeasure())) {
                    entity.setUnitMeasure(data.getUnitMeasure());
                    entity.setUnitQuantity(data.getUnitQuantity());
                    entity.setMeasure(resources.getString(R.string.unit_measure));
                }
                productEntity.setValue(entity);

                oldIngredient = data.getIngredient();
                price.postValue(data.getPrice());
            }
            oldCatalogId = data.getId();
        }
    }

    @Override
    public void onProductEntityByIdLoaded(ProductEntity entity) {
        productEntity.setValue(entity);
        price.setValue(entity.getTotalPrice());
        repository.loadCatalogEntityByName(entity.getIngredient(), this);
    }

    @Override
    public void onProductEntitySaved() {
       if(productEntity.getValue()!=null){
           productEntity.getValue().setMeasure(null);
           productEntity.getValue().setQuantity(0);
           if (isEditable) {
               if (!productEntity.getValue().getIngredient().equals(oldIngredient)) {
                   repository.saveCatalogEntity(productEntity.getValue(),this);
               } else {
                   productEntity.getValue().setId(oldCatalogId);
                   repository.updateCatalogEntity(productEntity.getValue(),this);
               }
           }else {
               productEntity.getValue().setId(oldCatalogId);
               repository.updateCatalogEntity( productEntity.getValue(),this);
           }
       }
    }

    @Override
    public void onCatalogEntitySaved() {
        navigator.onCancel();
    }

    public void onSaveBtnClicked() {
        compositeDisposable.clear();
        if (productEntity.getValue() != null) {
            productEntity.getValue().fromTotalPrice(price.getValue());
            if (isEditable) {
                productEntity.getValue().setId(0);
                repository.saveProductEntity(productEntity.getValue(),this);
            } else {
                repository.updateProductEntity(productEntity.getValue(),this);
            }
        }
    }

    public void onCancelBtnClicked() {
        navigator.onCancel();
    }

    public AdapterView.OnItemClickListener catalogSelectedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String temp = adapterView.getItemAtPosition(i).toString();
            repository.loadCatalogEntityByName(temp, AddEditProductModel.this);
        }
    };

    public OnMeasureSelected measureSelectedListener = item -> {
        if (item.equals(resources.getString(R.string.unit_measure))) {
            isUnitMeasure.setValue(true);
            Objects.requireNonNull(productEntity.getValue()).setUnitMeasure(resources.getString(R.string.kg_measure));
        } else {
            isUnitMeasure.setValue(false);
            Objects.requireNonNull(productEntity.getValue()).setUnitMeasure("");
        }
    };

    public interface OnMeasureSelected {
        void onMeasureSelected(String item);
    }
}
