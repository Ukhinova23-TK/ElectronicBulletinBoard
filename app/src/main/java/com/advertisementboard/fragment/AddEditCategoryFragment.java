package com.advertisementboard.fragment;

import static java.util.Objects.isNull;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.advertisementboard.R;
import com.advertisementboard.data.dto.category.CategoryDto;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditCategoryFragment  extends DialogFragment {

    @FunctionalInterface
    public interface CategoryDialogListener {
        void save(CategoryDto category);
    }

    public CategoryDialogListener listener;

    private CategoryDto category;

    private TextInputLayout titleCategoryTextInputLayout;

    private TextInputLayout descriptionCategoryTextInputLayout;

    public AddEditCategoryFragment(CategoryDialogListener listener) {
        this.listener = listener;
    }

    public AddEditCategoryFragment(CategoryDialogListener listener, CategoryDto category) {
        this.listener = listener;
        this.category = category;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        View addEditDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_add_edit_category, null);
        builder.setView(addEditDialogView);

        titleCategoryTextInputLayout = addEditDialogView.findViewById(R.id.titleCategoryTextInputLayout);
        descriptionCategoryTextInputLayout = addEditDialogView.findViewById(R.id.descriptionCategoryTextInputLayout);

        if(isNull(category)) {
            builder.setMessage(R.string.create_category);
        }
        else {
            builder.setMessage(R.string.update_category);
            titleCategoryTextInputLayout.getEditText().setText(category.getName());
            descriptionCategoryTextInputLayout.getEditText().setText(category.getDescription());
        }

        builder.setPositiveButton(
                R.string.buttonSave,
                (dialog, id) -> {
                    if(isNull(category)) {
                        category = new CategoryDto();
                    }

                    category.setName(titleCategoryTextInputLayout.getEditText().getText().toString());
                    category.setDescription(descriptionCategoryTextInputLayout.getEditText().getText().toString());
                    listener.save(category);
                }
        );

        builder.setNegativeButton(
                R.string.button_cancel,
                (dialog, id) -> {}
        );

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.darkPrimaryColor));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.darkPrimaryColor));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_category, container, false);
    }

}
