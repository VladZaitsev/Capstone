package com.baikaleg.v3.cookingaid.ui.storage;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.databinding.ActivityStorageBinding;
import com.baikaleg.v3.cookingaid.ui.BaseActivity;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductDialog;
import com.baikaleg.v3.cookingaid.ui.SwipeToDeleteCallback;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductModel;

import java.util.List;

public class StorageActivity extends BaseActivity implements StorageItemNavigator {
    private StorageViewModel viewModel;

    public AddEditProductDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_storage));
        }

        viewModel = ViewModelProviders.of(this).get(StorageViewModel.class);

        ActivityStorageBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.activity_storage, frameLayout, true);
        binding.setLifecycleOwner(this);
        binding.setModel(viewModel);

        StorageViewAdapter adapter = new StorageViewAdapter(this, this);
        binding.productsContent.setAdapter(adapter);
        binding.productsContent.setLayoutManager(new LinearLayoutManager(this));
        SwipeToDeleteCallback swipe = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.remove(viewHolder.getLayoutPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe);
        itemTouchHelper.attachToRecyclerView(binding.productsContent);


        binding.fab.setOnClickListener(view -> {
            dialog = AddEditProductDialog.newInstance(AddEditProductModel.DIALOG_STORAGE_ID, 0);
            dialog.show(getSupportFragmentManager(), "dialog");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }

    @Override
    public void onItemClicked(int id) {
        dialog = AddEditProductDialog.newInstance(AddEditProductModel.DIALOG_STORAGE_ID, id);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onItemRemoved(ProductEntity entity) {
        viewModel.remove(entity);
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
