package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

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


    @BindingAdapter(value = {"selectedValue", "selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(Spinner spinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
    public static String captureSelectedValue(Spinner pAppCompatSpinner) {
        return (String) pAppCompatSpinner.getSelectedItem();
    }

    @BindingAdapter(value = "priceValueAttrChanged")
    public static void setListener(EditText editText, final InverseBindingListener listener) {
        if (listener != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    listener.onChange();
                }
            });
        }
    }

    @BindingAdapter("priceValue")
    public static void setRealValue(EditText view, float value) {
        boolean setValue = view.getText().length() == 0;
        try {
            if (!setValue) {
                setValue = Float.parseFloat(view.getText().toString()) != value;
            }
        } catch (NumberFormatException e) {
        }
        if (setValue) {
            view.setText(String.valueOf(value));
        }
    }

    @InverseBindingAdapter(attribute = "priceValue")
    public static float getRealValue(EditText view) {
        try {
            return Float.parseFloat(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
