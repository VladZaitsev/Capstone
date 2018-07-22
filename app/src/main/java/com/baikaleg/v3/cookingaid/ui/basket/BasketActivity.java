package com.baikaleg.v3.cookingaid.ui.basket;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.databinding.ActivityBasketBinding;
import com.baikaleg.v3.cookingaid.ui.BaseActivity;

public class BasketActivity extends BaseActivity {

    private ActivityBasketBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_basket));
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_basket, frameLayout, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(1).setChecked(true);
    }
}
