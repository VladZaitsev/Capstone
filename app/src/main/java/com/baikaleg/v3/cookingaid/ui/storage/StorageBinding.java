package com.baikaleg.v3.cookingaid.ui.storage;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;

import java.util.List;

public class StorageBinding {

    private StorageBinding() {
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:storageProducts")
    public static void setStorageProducts(RecyclerView recyclerView, List<ProductEntity> entities) {
        StorageViewAdapter adapter = (StorageViewAdapter) recyclerView.getAdapter();
        if (adapter != null && entities != null) {
            adapter.refresh(entities);
        }
    }
}
