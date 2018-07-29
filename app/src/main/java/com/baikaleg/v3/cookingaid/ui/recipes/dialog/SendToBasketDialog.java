package com.baikaleg.v3.cookingaid.ui.recipes.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.model.Recipe;

public class SendToBasketDialog extends DialogFragment {
    public static final String RECIPE_INTENT_KEY = "recipe";
    public static final String RATIO_INTENT_KEY = "ratio";

    private static final String arg_recipe = "recipe";
    private static final String arg_ratio = "ratio";

    private Recipe recipe;
    private float ratio;

    public static SendToBasketDialog newInstance(Recipe recipe, float ratio) {
        Bundle args = new Bundle();
        args.putParcelable(arg_recipe, recipe);
        args.putFloat(arg_ratio, ratio);

        SendToBasketDialog fragment = new SendToBasketDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(arg_recipe);
            ratio = getArguments().getFloat(arg_ratio);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Do you really want to send all ingredients for recipe '" + recipe.getName() + "' to shopping list?");
        dialog.setPositiveButton(getString(R.string.save), (dialogInterface, i) -> {
            Intent intent = new Intent();
            intent.putExtra(RECIPE_INTENT_KEY, recipe);
            intent.putExtra(RATIO_INTENT_KEY, ratio);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            dismiss();
        });
        dialog.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
            dismiss();
        });

        return dialog.create();
    }
}
