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
import com.baikaleg.v3.cookingaid.data.callback.OnCatalogEntityLoadedListener;
import com.baikaleg.v3.cookingaid.data.callback.OnCatalogEntitySaveListener;
import com.baikaleg.v3.cookingaid.data.callback.OnProductEntityLoadedListener;
import com.baikaleg.v3.cookingaid.data.callback.OnProductEntitySaveListener;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AddEditProductModel extends ViewModel {
    public static final int DIALOG_BASKET_ID = 2;
    public static final int DIALOG_STORAGE_ID = 3;

    private AddEditProductNavigator navigator;

    private MutableLiveData<List<String>> catalogEntityNames = new MutableLiveData<>();

    private MutableLiveData<ProductEntity> productEntity = new MutableLiveData<>();

    private MutableLiveData<Boolean> isUnitMeasure = new MutableLiveData<>();

    private MutableLiveData<Boolean> isQuantitySet = new MutableLiveData<>();

    private boolean isEditable;

    private int oldCatalogId, dialogId;

    private String oldIngredient;

    private Repository repository;

    private Resources resources;

    private OnCatalogEntitySaveListener catalogSaveListener = this::onDestroy;

    private OnProductEntitySaveListener productSaveListener = new OnProductEntitySaveListener() {
        @Override
        public void onProductEntitySaved() {
            if (productEntity.getValue() != null) {
                productEntity.getValue().setMeasure(null);
                productEntity.getValue().setQuantity(0);
                if (isEditable) {
                    if (!productEntity.getValue().getIngredient().equals(oldIngredient)) {
                        repository.saveCatalogEntity(productEntity.getValue(), catalogSaveListener);
                    } else {
                        productEntity.getValue().setId(oldCatalogId);
                        repository.updateCatalogEntity(productEntity.getValue(), catalogSaveListener);
                    }
                } else {
                    productEntity.getValue().setId(oldCatalogId);
                    repository.updateCatalogEntity(productEntity.getValue(), catalogSaveListener);
                }
            }
        }
    };

    private OnCatalogEntityLoadedListener catalogLoadedListener = new OnCatalogEntityLoadedListener() {

        @Override
        public void onAllCatalogIngredientsLoaded(List<String> list) {
            catalogEntityNames.postValue(list);
        }

        @Override
        public void onCatalogEntityByNameLoaded(CatalogEntity data) {
            if (productEntity.getValue() != null) {
                if (isEditable) {
                    ProductEntity entity = new ProductEntity(0, resources.getString(R.string.kg_measure), data.getIngredient());
                    entity.setProductState(dialogId);
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
                }
                oldCatalogId = data.getId();
            }
        }
    };

    private OnProductEntityLoadedListener productLoadedListener = new OnProductEntityLoadedListener() {
        @Override
        public void onAllProductEntitiesLoaded(List<ProductEntity> list) {

        }

        @Override
        public void onProductEntityByIdLoaded(ProductEntity entity) {
            productEntity.setValue(entity);
            repository.loadCatalogEntityByName(entity.getIngredient(), catalogLoadedListener);
        }
    };

    AddEditProductModel(Context context, int dialogId, int productId) {
        this.repository = Repository.getInstance(context);
        this.resources = context.getResources();
        this.dialogId = dialogId;

        ProductEntity entity = new ProductEntity(0, resources.getString(R.string.kg_measure), null);
        entity.setProductState(dialogId);
        productEntity.setValue(entity);

        if (productId == 0) {
            repository.loadAllCatalogIngredients(catalogLoadedListener);
            isEditable = true;
        } else {
            repository.loadProductEntityById(productId, productLoadedListener);
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

    public MutableLiveData<Boolean> getIsQuantitySet() {
        return isQuantitySet;
    }

    public MutableLiveData<Boolean> getIsUnitMeasure() {
        return isUnitMeasure;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void onSaveBtnClicked() {
        if (productEntity.getValue() != null) {
            if (TextUtils.isEmpty(productEntity.getValue().getIngredient())) {
                navigator.showNoIngredientMsg();
            } else if (productEntity.getValue().getQuantity() == 0) {
                navigator.showNoQuantityMsg();
            } else {
                if (isEditable) {
                    if (dialogId == DIALOG_STORAGE_ID) {
                        Calendar calendar = Calendar.getInstance();
                        productEntity.getValue().setPurchaseDate(calendar.getTime());
                    }
                    productEntity.getValue().setId(0);
                    repository.saveProductEntity(productEntity.getValue(), productSaveListener);
                } else {
                    repository.updateProductEntity(productEntity.getValue(), productSaveListener);
                }
            }
        }
    }

    public void onCancelBtnClicked() {
        onDestroy();
    }

    private void onDestroy() {
        navigator.onCancel();
    }

    public AdapterView.OnItemClickListener catalogSelectedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String temp = adapterView.getItemAtPosition(i).toString();
            repository.loadCatalogEntityByName(temp, catalogLoadedListener);
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
