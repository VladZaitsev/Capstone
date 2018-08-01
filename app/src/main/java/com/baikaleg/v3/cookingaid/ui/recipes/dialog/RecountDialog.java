package com.baikaleg.v3.cookingaid.ui.recipes.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.databinding.DialogRecountRecipeBinding;

public class RecountDialog extends DialogFragment {
    private static final String arg_pos = "position";
    public static final String POSITION_INTENT_KEY = "position_recipe";
    public static final String PERSONS_INTENT_KEY = "persons_recipe";
    private int position;

    public static RecountDialog newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(arg_pos, position);
        RecountDialog fragment = new RecountDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(arg_pos);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        DialogRecountRecipeBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_recount_recipe, null, false);
        dialog.setTitle(getString(R.string.msg_how_many_servings));

        dialog.setPositiveButton(getString(R.string.save), (dialogInterface, i) -> {
            if (!TextUtils.isEmpty(binding.personsField.getText().toString())) {
                int persons = Integer.parseInt(binding.personsField.getText().toString());
                if (persons != 0) {
                    Intent intent = new Intent();
                    intent.putExtra(POSITION_INTENT_KEY, position);
                    intent.putExtra(PERSONS_INTENT_KEY, persons);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.msg_cannot_be_null), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.msg_fill_field), Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
            dismiss();
        });

        dialog.setView(binding.getRoot());
        return dialog.create();
    }
}
