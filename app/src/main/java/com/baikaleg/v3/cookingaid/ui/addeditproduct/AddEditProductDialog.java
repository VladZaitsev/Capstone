package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.databinding.DialogAddEditProductBinding;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatDialogFragment;

/**
 * @author Vlad Zaytcev
 */

@ActivityScoped
public class AddEditProductDialog extends DaggerAppCompatDialogFragment implements AddEditProductNavigator {

    private AddEditProductModel viewModel;

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
            DialogAddEditProductBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_edit_product, null, false);
            binding.setLifecycleOwner(this);
            binding.setModel(viewModel);

            dialog.setView(binding.getRoot());
            return dialog.create();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCancel() {
        getDialog().cancel();
    }
}
