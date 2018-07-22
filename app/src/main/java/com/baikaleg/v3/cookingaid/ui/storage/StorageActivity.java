package com.baikaleg.v3.cookingaid.ui.storage;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.databinding.ActivityStorageBinding;
import com.baikaleg.v3.cookingaid.ui.BaseActivity;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductDialog;
import com.baikaleg.v3.cookingaid.ui.recipes.RecipesViewModel;

import javax.inject.Inject;

public class StorageActivity extends BaseActivity implements StorageItemNavigator {

    private int id;

    private StorageViewModel viewModel;

    @Inject
    public StorageViewModelFactory viewModelFactory;

    @Inject
    public AddEditProductDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_storage));
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StorageViewModel.class);

        ActivityStorageBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.activity_storage, frameLayout, true);
        binding.setModel(viewModel);

        StorageViewAdapter adapter = new StorageViewAdapter(this, this);
        binding.productsContent.setAdapter(adapter);
        binding.productsContent.setLayoutManager(new LinearLayoutManager(this));

        binding.fab.setOnClickListener(view -> dialog.show(getSupportFragmentManager(), "dialog"));
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
        this.id = id;
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public int getSelectedID() {
        return id;
    }
}
