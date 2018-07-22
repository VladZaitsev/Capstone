package com.baikaleg.v3.cookingaid.ui.storage;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.databinding.ItemStorageBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class StorageViewAdapter extends RecyclerView.Adapter<StorageViewAdapter.StorageViewHolder> {
    private List<ProductEntity> products = new ArrayList<>();


    public Context context;
    private StorageItemNavigator navigator;

    @Inject
    public StorageViewAdapter(Context context, StorageItemNavigator navigator) {
        this.context = context;
        this.navigator = navigator;
    }

    @NonNull
    @Override
    public StorageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStorageBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_storage,
                        parent, false);
        return new StorageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StorageViewHolder holder, int position) {
        ProductEntity entity = products.get(position);
        holder.binding.setProduct(entity);
        holder.binding.setDetails(String.valueOf(entity.getExpiration()));
        holder.binding.getRoot().setOnClickListener(view -> navigator.onItemClicked(entity.getId()));
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

    class StorageViewHolder extends RecyclerView.ViewHolder {

        ItemStorageBinding binding;

        StorageViewHolder(ItemStorageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
