package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

class AddEditProductModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private final int dialogID;
    private final int productID;

    AddEditProductModelFactory(Context context, int dialogID, int productID) {
        this.context = context;
        this.dialogID = dialogID;
        this.productID = productID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddEditProductModel.class)) {
            return (T) new AddEditProductModel(context,dialogID,productID);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
