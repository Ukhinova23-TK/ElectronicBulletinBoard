package com.advertisementboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.advertisementboard.account.LoginDialogFragment;
import com.advertisementboard.account.RegistrationDialogFragment;
import com.advertisementboard.categories.CategoriesFragment;
import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.authentication.AuthenticationRequestDto;
import com.advertisementboard.data.dto.authentication.AuthenticationResponseDto;
import com.advertisementboard.data.dto.user.UserDto;
import com.advertisementboard.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
    implements CategoriesFragment.CategoriesFragmentListener, LoginDialogFragment.LoginDialogListener{

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    boolean loggedIn = false;

    private CategoriesFragment categoriesFragment; // Вывод категорий

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        categoriesFragment = new CategoriesFragment();
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, categoriesFragment);
        transaction.commit();

        /*binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        checkAccount(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            LoginDialogFragment fragment = new LoginDialogFragment();
            fragment.show(getSupportFragmentManager(), "Login dialog");
            return true;
        }
        if(id == R.id.action_registration) {
            RegistrationDialogFragment fragment = new RegistrationDialogFragment();
            fragment.show(getSupportFragmentManager(), "Registration dialog");
            return true;
        }
        if(id == R.id.action_exit) {
            AppConfiguration.token().setToken(null);
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onCategorySelected(String category) {

    }

    private void updateButtonsMenu(Menu menu){
        MenuItem menuItemLogin = menu.findItem(R.id.action_login);
        MenuItem menuItemRegistration = menu.findItem(R.id.action_registration);
        MenuItem menuItemExit = menu.findItem(R.id.action_exit);
        if(loggedIn){
            menuItemLogin.setVisible(false);
            menuItemRegistration.setVisible(false);
            menuItemExit.setVisible(true);
        }
        else{
            menuItemLogin.setVisible(true);
            menuItemRegistration.setVisible(true);
            menuItemExit.setVisible(false);
        }
    }

    private void checkAccount(Menu menu){
        AppConfiguration.accountClient()
                .getAccount()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                        if(response.code() == 200) {
                            Log.i("Account", "The user is logged in");
                            loggedIn = true;
                        }
                        else {
                            Log.i("Account", "The user is not logged in " + response.code());
                            loggedIn = false;
                        }
                        updateButtonsMenu(menu);
                    }

                    @Override
                    public void onFailure(Call<UserDto> call, Throwable t) {
                        Log.e("Account", "No connection");
                        loggedIn = false;
                    }
                });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        invalidateOptionsMenu();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        invalidateOptionsMenu();
    }
}