package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.databinding.DialogAddEditProductBinding;

/**
 * @author Vlad Zaytcev
 */

public class AddEditProductDialog extends DialogFragment implements AddEditProductNavigator {
    private static final String dialog_arg = "dialog";
    private static final String product_arg = "product";

    private AddEditProductModel viewModel;

    public static AddEditProductDialog newInstance(int dialogId, int productId) {
        AddEditProductDialog frag = new AddEditProductDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(dialog_arg, dialogId);
        bundle.putInt(product_arg, productId);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            AddEditProductModelFactory factory = new AddEditProductModelFactory(
                    getActivity(),
                    getArguments().getInt(dialog_arg),
                    getArguments().getInt(product_arg));
            viewModel = ViewModelProviders.of(this, factory).get(AddEditProductModel.class);
            viewModel.setNavigator(this);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}
