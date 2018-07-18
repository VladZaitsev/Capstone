package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.cookingaid.databinding.DialogAddEditProductBinding;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerAppCompatDialogFragment;

/**
 * @author Vlad Zaytcev
 */

@ActivityScoped
public class AddEditProductDialog extends DaggerAppCompatDialogFragment implements AddEditProductEventNavigator {
    private static final String TAG = AddEditProductDialog.class.getSimpleName();

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
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() != null) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_edit_product, null, false);
            dialog.setView(binding.getRoot());

            initializeScreen();
            return dialog.create();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    private void initializeScreen() {
        viewModel.catalogEntityNames.observe(this, list -> {
            if (list != null) {
                if (getActivity() != null) {
                    binding.title.setAdapter(new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, list));
                }
            }
        });
        viewModel.entity.observe(this, entity -> {
            binding.setEntity(entity);
        });
        binding.title.setOnItemClickListener((parent, view, i, l) ->
                viewModel.updateCatalogName(parent.getItemAtPosition(i).toString()));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroyed();
    }
}
