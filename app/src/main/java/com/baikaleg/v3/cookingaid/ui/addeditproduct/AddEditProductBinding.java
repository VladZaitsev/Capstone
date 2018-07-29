package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.adapters.ListenerUtil;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.baikaleg.v3.cookingaid.R;

import java.util.List;

public class AddEditProductBinding {

    private AddEditProductBinding() {
    }

    @BindingAdapter(value = {"catalogValue", "catalogList", "onCatalogEntitySelected", "isEditable", "catalogAttrChanged"}, requireAll = false)
    public static void bindCatalogEntities(
            AutoCompleteTextView view,
            String value,
            List<String> list,
            AdapterView.OnItemClickListener selectedListener,
            boolean isEditable,
            final InverseBindingListener newTextAttrChanged) {

        view.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_dropdown_item_1line, list));

        TextWatcher newValue = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    newTextAttrChanged.onChange();
                }
            }
        };
        view.addTextChangedListener(newValue);
        view.setOnItemClickListener(selectedListener);
        if (!isEditable) view.setText(value);
    }

    @InverseBindingAdapter(attribute = "catalogValue", event = "catalogAttrChanged")
    public static String captureCatalogEntities(AutoCompleteTextView view) {
        return view.getText().toString();
    }

    @BindingAdapter(value = {"selectedValue", "onMeasureSelected", "selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(Spinner spinner, String newSelectedValue, AddEditProductModel.OnMeasureSelected listener, final InverseBindingListener newTextAttrChanged) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onMeasureSelected(parent.getSelectedItem().toString());
                }
                newTextAttrChanged.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (newSelectedValue != null) {
            int pos = ((ArrayAdapter<String>) spinner.getAdapter()).getPosition(newSelectedValue);
            spinner.setSelection(pos, true);
        }
    }

    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    public static String captureSelectedValue(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }


    @BindingAdapter(value = {"quantityIsSet", "quantitySetAttrChanged"}, requireAll = false)
    public static void bindQuantityField(
            EditText view,
            boolean isQuantitySet,
            final InverseBindingListener newTextAttrChanged) {

        TextWatcher newValue = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                newTextAttrChanged.onChange();
            }
        };
        view.addTextChangedListener(newValue);
    }

    @InverseBindingAdapter(attribute = "quantityIsSet", event = "quantitySetAttrChanged")
    public static Boolean captureQuantityField(EditText view) {
        String text = view.getText().toString();
        return !TextUtils.isEmpty(text) && !(Float.parseFloat(text) == 0);
    }

}
