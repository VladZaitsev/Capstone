package com.baikaleg.v3.cookingaid.ui.storage;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.database.entity.ProductEntity;
import com.baikaleg.v3.cookingaid.databinding.ItemStorageBinding;
import com.baikaleg.v3.cookingaid.util.AppUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StorageViewAdapter extends RecyclerView.Adapter<StorageViewAdapter.StorageViewHolder> {
    private List<ProductEntity> products = new ArrayList<>();

    public Context context;
    private StorageItemNavigator navigator;
    private DateFormat dateFormat;

    StorageViewAdapter(Context context, StorageItemNavigator navigator) {
        this.context = context;
        this.navigator = navigator;
        dateFormat = new SimpleDateFormat(context.getString(R.string.format_of_date), Locale.getDefault());
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
        String details;
        long diff = AppUtils.timeDiff(entity);
        if (diff < 0) {
            details = context.getString(R.string.msg_product_has_expired);
        } else {
            if (diff < 5) {
                details = context.getString(R.string.msg_days_to_expire, diff);
            } else {
                details = context.getString(R.string.msg_bought_date, dateFormat.format(entity.getPurchaseDate()));
            }
        }
        holder.binding.setProduct(entity);
        holder.binding.setDetails(details);
        holder.binding.getRoot().setOnClickListener(view -> navigator.onItemClicked(entity.getId()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    void refresh(@NonNull List<ProductEntity> list) {
        this.products.clear();
        this.products.addAll(list);
        notifyDataSetChanged();
    }

    void remove(int position) {
        navigator.onItemRemoved(products.get(position));
    }

    class StorageViewHolder extends RecyclerView.ViewHolder {

        ItemStorageBinding binding;

        StorageViewHolder(ItemStorageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
