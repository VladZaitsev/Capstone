package com.baikaleg.v3.cookingaid.ui.storage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.databinding.ActivityStorageBinding;
import com.baikaleg.v3.cookingaid.ui.BaseActivity;

public class StorageActivity extends BaseActivity {

    private ActivityStorageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(getString(R.string.title_activity_storage));
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_storage, frameLayout, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(2).setChecked(true);
    }
}
