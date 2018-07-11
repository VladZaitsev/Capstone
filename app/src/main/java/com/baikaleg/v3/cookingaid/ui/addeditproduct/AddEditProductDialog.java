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
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.FragmentScoped;
import com.baikaleg.v3.cookingaid.databinding.DialogAddEditProductBinding;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatDialogFragment;

/**
 * @author Vlad Zaytcev
 */

@ActivityScoped
public class AddEditProductDialog extends DaggerAppCompatDialogFragment implements AddEditProductEventNavigator {
    private static final String TAG = AddEditProductDialog.class.getSimpleName();
    private static final String paramDialogId = "dialog_id";
    private static final String paramProductUuid = "product_uuid";

    @Inject
    public int dialogId;
    @Inject
    public String productUuid;

    private AddEditProductModel viewModel;

    private DialogAddEditProductBinding binding;

    @Inject
    public AddEditProductModelFactory viewModelFactory;

    @Inject
    public AddEditProductDialog() {
    }

    public static AddEditProductDialog newInstance(int dialogId, String productUuid) {
        AddEditProductDialog fragment = new AddEditProductDialog();
        Bundle args = new Bundle();
        args.putInt(paramDialogId, dialogId);
        args.putString(paramProductUuid, productUuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEditProductModel.class);

        /* if (getArguments() != null) {
            if (getArguments().getString(paramProductUuid) != null) {
                viewModel.loadProduct(getArguments().getString(paramProductUuid));
            }
            dialogId = getArguments().getInt(paramDialogId);
        }*/
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() != null) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_edit_product, null, false);
            dialog.setView(binding.getRoot());
            return dialog.create();
        }
        return super.onCreateDialog(savedInstanceState);
    }
}
