package com.advertisementboard.account;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;

import com.advertisementboard.R;
import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.authentication.AuthenticationResponseDto;
import com.advertisementboard.data.dto.authentication.RegistrationRequestDto;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationDialogFragment extends DialogFragment {

    private TextInputLayout loginTextInputLayout;
    private TextInputLayout nameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout doublePasswordTextInputLayout;

    private TextView check;

    private CoordinatorLayout coordinatorLayout;

    private final TextWatcher passwordEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkPassword();
        }
    };

    private final TextWatcher doublePasswordEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkPassword();
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        View registrationDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_registration, null);
        builder.setView(registrationDialogView);

        loginTextInputLayout = registrationDialogView.findViewById(R.id.loginTextInputLayout);
        nameTextInputLayout = registrationDialogView.findViewById(R.id.nameTextInputLayout);
        passwordTextInputLayout = registrationDialogView.findViewById(R.id.passwordTextInputLayout);
        doublePasswordTextInputLayout = registrationDialogView.findViewById(R.id.doublePasswordTextInputLayout);

        check = registrationDialogView.findViewById(R.id.checkTextView);
        check.setVisibility(View.INVISIBLE);

        passwordTextInputLayout.getEditText().addTextChangedListener(passwordEditTextWatcher);
        doublePasswordTextInputLayout.getEditText().addTextChangedListener(doublePasswordEditTextWatcher);

        coordinatorLayout = getActivity().findViewById(R.id.coordinatorLayout);

        builder.setMessage(R.string.action_registration);
        builder.setPositiveButton(
                R.string.button_registration,
                (DialogInterface.OnClickListener) (dialog, id) -> registration()
        );

        builder.setNegativeButton(R.string.button_cancel, null);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(R.color.darkPrimaryColor));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(R.color.darkPrimaryColor));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    private boolean checkEmpty(){
        if(passwordTextInputLayout.getEditText().getText().toString().equals("")
                || doublePasswordTextInputLayout.getEditText().getText().toString().equals("")
                || nameTextInputLayout.getEditText().getText().toString().equals("")
                || loginTextInputLayout.getEditText().getText().toString().equals("")){
            check.setText(getResources().getString(R.string.message_empty_input));
            check.setVisibility(View.VISIBLE);
            return false;

        }
        check.setVisibility(View.INVISIBLE);
        return true;
    }

    private boolean checkPassword(){
        if(passwordTextInputLayout.getEditText().getText().toString()
                .equals(doublePasswordTextInputLayout.getEditText().getText().toString())){
            check.setVisibility(View.INVISIBLE);
            return true;
        }
        else{
            check.setText(getResources().getString(R.string.message_match_passwords));
            check.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private void registration(){
        if(checkEmpty() && checkPassword()) {
            AppConfiguration.accountClient()
                    .register(
                            RegistrationRequestDto.builder()
                                    .login(loginTextInputLayout.getEditText().getText().toString())
                                    .name(nameTextInputLayout.getEditText().getText().toString())
                                    .password(passwordTextInputLayout.getEditText().getText().toString())
                                    .build()
                    )
                    .enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<AuthenticationResponseDto> call, Response<AuthenticationResponseDto> response) {
                            if (response.code() == 200) {
                                AppConfiguration.token().setToken(response.body().getToken());
                                Log.i("Registration", "Registration completed");
                                Snackbar.make(coordinatorLayout, R.string.registration_success, Snackbar.LENGTH_SHORT).show();
                            } else {
                                Log.i("Registration", "Registration failed with code " + response.code());
                                Snackbar.make(coordinatorLayout, R.string.registration_failed, Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthenticationResponseDto> call, Throwable t) {
                            Log.e("Registration", "No connection");
                            Snackbar.make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
