package com.advertisementboard.account;

import androidx.fragment.app.DialogFragment;

public interface DialogListener {
    void onDialogPositiveClick(DialogFragment dialog);
    void onDialogNegativeClick(DialogFragment dialog);
}
