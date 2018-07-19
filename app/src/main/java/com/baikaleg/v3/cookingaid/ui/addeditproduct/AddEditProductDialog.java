package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.databinding.DialogAddEditProductBinding;

import java.util.Timer;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerAppCompatDialogFragment;

/**
 * @author Vlad Zaytcev
 */

@ActivityScoped
public class AddEditProductDialog extends DaggerAppCompatDialogFragment implements AddEditProductNavigator {

    @Inject
    @Named("dialogId")
    public int dialogId;
    @Inject
    @Named("productId")
    public int productId;

    private AddEditProductModel viewModel;

    private DialogAddEditProductBinding binding;

    @Inject
    public AddEditProductModelFactory viewModelFactory;

    @Inject
    public AddEditProductDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEditProductModel.class);
        viewModel.setNavigator(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() != null) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_edit_product, null, false);
            binding.setLifecycleOwner(this);
            binding.setModel(viewModel);

            dialog.setView(binding.getRoot());

            initializeScreen();
            return dialog.create();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    private void initializeScreen() {

        binding.cancelBtn.setOnClickListener(view -> getDialog().cancel());
        binding.saveBtn.setOnClickListener(saveBtnClickListener);
    }

    private View.OnClickListener saveBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TextUtils.isEmpty(binding.title.getText().toString()) ||
                    TextUtils.isEmpty(binding.quantity.getText().toString()) ||
                    TextUtils.isEmpty(binding.priceField.getText().toString()) ||
                    TextUtils.isEmpty(binding.expirationField.getText().toString()) ||
                    TextUtils.isEmpty(binding.caloriesField.getText().toString()) ) {
                Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            //TODO Change density adding
            //TODO Send realization to ViewModel
            float density = 1f;
            String ingredient = binding.title.getText().toString();
            float quantity = Float.valueOf(binding.quantity.getText().toString());
            float price = Float.valueOf(binding.priceField.getText().toString());
            int expiration = Integer.valueOf(binding.expirationField.getText().toString());
            float calories = Float.valueOf(binding.caloriesField.getText().toString());
            float unitQuantity = Float.valueOf(binding.singleUnitField.getText().toString());
            if (price == 0 || expiration == 0 || calories == 0 || quantity == 0) {
                Toast.makeText(getActivity(), "Fields cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.onSaveBtnClicked(ingredient, quantity, unitQuantity, price, calories, expiration, density);
        }
    };

    @Override
    public void onCancel() {
        getDialog().cancel();
    }
}
