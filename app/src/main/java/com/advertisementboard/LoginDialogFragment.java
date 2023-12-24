package com.advertisementboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.authentication.AuthenticationRequestDto;
import com.advertisementboard.data.dto.authentication.AuthenticationResponseDto;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDialogFragment extends DialogFragment {

    private TextInputLayout loginTextInputLayout;
    private TextInputLayout passwordTextInputLayout;

    private CoordinatorLayout coordinatorLayout;

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        View loginDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_login, null);
        builder.setView(loginDialogView);

        loginTextInputLayout = loginDialogView.findViewById(R.id.loginTextInputLayout);
        passwordTextInputLayout = loginDialogView.findViewById(R.id.passwordTextInputLayout);

        coordinatorLayout = getActivity().findViewById(R.id.coordinatorLayout);

        builder.setMessage(R.string.action_login);
        builder.setPositiveButton(
                R.string.button_login,
                (DialogInterface.OnClickListener) (dialog, id) -> login()
        );
        builder.setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    private void login() {
        AppConfiguration.accountClient()
                .authenticate(
                        AuthenticationRequestDto.builder()
                                .login(loginTextInputLayout.getEditText().getText().toString())
                                .password(passwordTextInputLayout.getEditText().getText().toString())
                                .build()
                )
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<AuthenticationResponseDto> call, Response<AuthenticationResponseDto> response) {
                        if(response.code() == 200) {
                            AppConfiguration.token().setToken(response.body().getToken());
                            Snackbar.make(coordinatorLayout, "Auth + ", Snackbar.LENGTH_SHORT).show(); //TODO сообщение в ресурсы
                        }
                        else {
                            Snackbar.make(coordinatorLayout, "Auth - ", Snackbar.LENGTH_SHORT).show(); //TODO сообщение в ресурсы
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthenticationResponseDto> call, Throwable t) {
                        Log.i("Login", "Failed");
                        Snackbar.make(coordinatorLayout, "Auth - ", Snackbar.LENGTH_SHORT).show(); //TODO сообщение в ресурсы


                    }
                });
    }
}