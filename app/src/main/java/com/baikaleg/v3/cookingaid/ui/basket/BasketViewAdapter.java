package com.baikaleg.v3.cookingaid.ui.basket;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.database.entity.ProductEntity;
import com.baikaleg.v3.cookingaid.databinding.ItemBasketBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BasketViewAdapter extends RecyclerView.Adapter<BasketViewAdapter.BasketViewHolder> {

    private List<ProductEntity> products = new ArrayList<>();

    public Context context;
    private BasketItemNavigator navigator;

    BasketViewAdapter(Context context, BasketItemNavigator navigator) {
        this.context = context;
        this.navigator = navigator;
    }

    @NonNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBasketBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_basket,
                        parent, false);
        return new BasketViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder holder, int position) {
        ProductEntity entity = products.get(position);
        holder.binding.check.setChecked(false);
        holder.binding.setProduct(entity);
        holder.binding.setDetails(String.format(Locale.getDefault(), "%.2f", entity.getTotalPrice()));
        holder.binding.getRoot().setOnClickListener(view -> navigator.onItemClicked(entity.getId()));
        holder.binding.check.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                navigator.onItemSelected(products.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    void refresh(@NonNull List<ProductEntity> list) {
        this.products = new ArrayList<>();
        this.products.addAll(list);
        notifyDataSetChanged();
    }

    void remove(int position) {
        navigator.onItemRemoved(products.get(position));
        products.remove(position);
    }

    class BasketViewHolder extends RecyclerView.ViewHolder {

        ItemBasketBinding binding;

        BasketViewHolder(ItemBasketBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
