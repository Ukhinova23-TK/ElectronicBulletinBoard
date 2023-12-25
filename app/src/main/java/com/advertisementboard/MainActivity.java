package com.advertisementboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.advertisementboard.account.LoginDialogFragment;
import com.advertisementboard.account.RegistrationDialogFragment;
import com.advertisementboard.categories.CategoriesFragment;
import com.advertisementboard.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
    implements CategoriesFragment.CategoriesFragmentListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onCategorySelected(String category) {

    }
}