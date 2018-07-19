package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.databinding.BindingAdapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.List;

public class AddEditProductBinding {

    private AddEditProductBinding() {
    }

    @BindingAdapter({"app:entitiesNames", "app:entitiesNamesSelected"})
    public static void setEntitiesNames(AutoCompleteTextView view, List<String> list, AdapterView.OnItemClickListener listener) {
        view.setAdapter(new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, list));
        view.setOnItemClickListener(listener);
    }

}
