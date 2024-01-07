package com.advertisementboard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.advertisementboard.decoration.ItemDivider;
import com.advertisementboard.R;
import com.advertisementboard.account.DialogListener;
import com.advertisementboard.account.LoginDialogFragment;
import com.advertisementboard.account.RegistrationDialogFragment;
import com.advertisementboard.adapter.CategoriesAdapter;
import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.category.CategoryDto;
import com.advertisementboard.data.dto.user.UserDto;
import com.advertisementboard.databinding.ActivityMainBinding;
import com.advertisementboard.fragment.AddEditCategoryFragment;
import com.advertisementboard.fragment.DeleteDialogFragment;
import com.advertisementboard.util.RoleUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
    implements  DialogListener {

    private ActivityMainBinding binding;

    boolean loggedIn = false;

    private CategoriesAdapter categoriesAdapter; // Адаптер для recyclerView

    private RecyclerView recyclerViewCategories;

    private FloatingActionButton addCategoryButton;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        addCategoryButton = findViewById(R.id.addCategoryButton);
        addCategoryButton.setVisibility(View.INVISIBLE);

        addCategoryButton.setOnClickListener(view -> {
            AddEditCategoryFragment fragment = new AddEditCategoryFragment(this::createCategory);
            fragment.show(getSupportFragmentManager(), "Create category");
        });

        // recyclerView выводит элементы в вертикальном списке
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewCategories.setLayoutManager(layoutManager);

        loadCategories();

        // Присоединение ItemDecorator для вывода разделителей
        recyclerViewCategories.addItemDecoration(new ItemDivider(this));

        recyclerViewCategories.setHasFixedSize(false);
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


    private void updateButtonsMenu(Menu menu){
        MenuItem menuItemLogin = menu.findItem(R.id.action_login);
        MenuItem menuItemRegistration = menu.findItem(R.id.action_registration);
        MenuItem menuItemExit = menu.findItem(R.id.action_exit);
        menuItemLogin.setVisible(!loggedIn);
        menuItemRegistration.setVisible(!loggedIn);
        menuItemExit.setVisible(loggedIn);
        addCategoryButton.setVisibility(RoleUtil.isAdministrator(AppConfiguration.user()) ? View.VISIBLE : View.INVISIBLE);
    }

    private void checkAccount(Menu menu){
        AppConfiguration.accountClient()
                .getAccount()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                        if(response.code() == 200) {
                            Log.i("Account", "The user is logged in");
                            if(response.body() != null) {
                                updateUser(response.body());
                                loggedIn = true;
                            }
                            else {
                                updateUser(UserDto.builder().build());
                                loggedIn = false;
                            }
                        }
                        else {
                            Log.i("Account", "The user is not logged in " + response.code());
                            updateUser(UserDto.builder().build());
                            loggedIn = false;
                        }
                        updateButtonsMenu(menu);
                        categoriesAdapter.notifyDataSetChanged();
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

    private void loadCategories() {
        AppConfiguration.categoryClient().getCategories()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                        if(response.code() == 200) {
                            // создание адаптера recyclerView и слушателя щелчков на элементах
                            categoriesAdapter = new CategoriesAdapter(
                                    category -> onClickCategory(category),
                                    category -> {
                                        AddEditCategoryFragment fragment = new AddEditCategoryFragment(MainActivity.this::updateCategory, category);
                                        fragment.show(getSupportFragmentManager(), "Update category");
                                    },
                                    category -> {
                                        DeleteDialogFragment fragment = new DeleteDialogFragment(
                                                new DialogListener() {
                                                    @Override
                                                    public void onDialogPositiveClick(DialogFragment dialog) {
                                                        deleteCategory(category.getId());
                                                    }

                                                    @Override
                                                    public void onDialogNegativeClick(DialogFragment dialog) {}
                                                }
                                        );
                                        fragment.show(getSupportFragmentManager(), "Delete advertisement");
                                    },
                                    response.body()
                            );

                            recyclerViewCategories.setAdapter(categoriesAdapter); // Назначение адаптера
                        }
                        else {
                            Log.i("Categories", "Fetching categories failed with code " + response.code());
                            Snackbar.make(coordinatorLayout, R.string.categories_failed, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CategoryDto>> call, Throwable t) {
                        Log.e("Categories", "No connection");
                        Snackbar.make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void onClickCategory(CategoryDto categoryDto) {
        Intent intent = new Intent(this, AdvertisementsActivity.class);
        intent.putExtra("categoryId", categoryDto.getId());
        intent.putExtra("categoryName", categoryDto.getName());
        startActivity(intent);
    }

    private void updateUser(UserDto user) {
        UserDto configUser = AppConfiguration.user();
        configUser.setLogin(user.getLogin());
        configUser.setName(user.getName());
        configUser.setPassword(user.getPassword());
        configUser.setRole(user.getRole());
    }

    private void createCategory(CategoryDto category) {
        AppConfiguration.categoryClient().createCategory(category).enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        if(response.code() == 201) {
                            Snackbar.make(coordinatorLayout, R.string.successful_saving, Snackbar.LENGTH_SHORT).show();
                            loadCategories();
                        }
                        else {
                            Log.e("Categories", "Error during saving");
                            Snackbar.make(coordinatorLayout, R.string.error_during_saving, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {
                        Log.e("Categories", "No connection");
                        Snackbar.make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void updateCategory(CategoryDto category) {
        AppConfiguration.categoryClient().updateCategory(category).enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200) {
                            Snackbar.make(coordinatorLayout, R.string.successful_saving, Snackbar.LENGTH_SHORT).show();
                            loadCategories();
                        }
                        else {
                            Log.e("Categories", "Error during saving");
                            Snackbar.make(coordinatorLayout, R.string.error_during_saving, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Categories", "No connection");
                        Snackbar.make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void deleteCategory(Long id) {
        AppConfiguration.categoryClient().deleteCategory(id).enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200) {
                            Snackbar.make(coordinatorLayout, R.string.successful_deleting, Snackbar.LENGTH_SHORT).show();
                            loadCategories();
                        }
                        else {
                            Log.e("Categories", "Error during deleting");
                            Snackbar.make(coordinatorLayout, R.string.error_during_deleting, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Categories", "No connection");
                        Snackbar.make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );
    }
}