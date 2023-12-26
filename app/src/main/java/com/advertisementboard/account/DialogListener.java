package com.advertisementboard.account;

import androidx.fragment.app.DialogFragment;

public interface DialogListener {
    public void onDialogPositiveClick(DialogFragment dialog);
    public void onDialogNegativeClick(DialogFragment dialog);
}
