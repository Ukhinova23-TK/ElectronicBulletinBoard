package com.advertisementboard.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.advertisementboard.R;
import com.advertisementboard.account.DialogListener;

public class DeleteDialogFragment extends DialogFragment {

    public DialogListener listener;

    public DeleteDialogFragment(DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        View loginDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_delete, null);
        builder.setView(loginDialogView);


        builder.setMessage(R.string.action_delete);
        builder.setPositiveButton(
                R.string.button_delete,
                (dialog, id) -> listener.onDialogPositiveClick(DeleteDialogFragment.this)
        );

        builder.setNegativeButton(R.string.button_cancel, (dialog, id) -> {
            // Send the negative button event back to the host activity.
            listener.onDialogNegativeClick(DeleteDialogFragment.this);
        });
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
        return inflater.inflate(R.layout.fragment_delete, container, false);
    }

}
