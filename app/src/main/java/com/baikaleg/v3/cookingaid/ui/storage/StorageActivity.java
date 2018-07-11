package com.baikaleg.v3.cookingaid.ui.storage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.databinding.ActivityStorageBinding;
import com.baikaleg.v3.cookingaid.ui.BaseActivity;
import com.baikaleg.v3.cookingaid.ui.addeditproduct.AddEditProductDialog;

import javax.inject.Inject;

public class StorageActivity extends BaseActivity {

    private ActivityStorageBinding binding;

    public String getSelectedUUID() {
        return "dsdds";
    }

    @Inject
    public AddEditProductDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_storage));
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_storage, frameLayout, true);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(2).setChecked(true);
    }
}
