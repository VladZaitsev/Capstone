package com.baikaleg.v3.cookingaid.ui.basket;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.databinding.ItemBasketBinding;

import java.util.ArrayList;
import java.util.List;

public class BasketViewAdapter extends RecyclerView.Adapter<BasketViewAdapter.BasketViewHolder> {

    private List<ProductEntity> products = new ArrayList<>();

    public Context context;
    private BasketItemNavigator navigator;

    public BasketViewAdapter(Context context, BasketItemNavigator navigator) {
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
        holder.binding.setProduct(entity);
        holder.binding.setDetails(String.valueOf(entity.getTotalPrice()));
        holder.binding.getRoot().setOnClickListener(view -> navigator.onItemClicked(entity.getId()));
        holder.binding.check.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                navigator.onItemSelected(products.get(position));
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void refresh(@NonNull List<ProductEntity> list) {
        this.products.clear();
        this.products.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        navigator.onItemRemoved(products.get(position));
        notifyItemRemoved(position);
    }

    class BasketViewHolder extends RecyclerView.ViewHolder {

        ItemBasketBinding binding;

        BasketViewHolder(ItemBasketBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
