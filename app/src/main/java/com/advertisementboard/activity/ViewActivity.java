package com.advertisementboard.activity;

import static java.util.Objects.nonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.advertisementboard.R;
import com.advertisementboard.account.DialogListener;
import com.advertisementboard.adapter.CategoriesAdapter;
import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.advertisement.AdvertisementDto;
import com.advertisementboard.data.dto.category.CategoryDto;
import com.advertisementboard.data.enumeration.AdvertisementStatus;
import com.advertisementboard.databinding.ActivityViewBinding;
import com.advertisementboard.decoration.ItemDivider;
import com.advertisementboard.fragment.AddEditCategoryFragment;
import com.advertisementboard.fragment.DeleteDialogFragment;
import com.advertisementboard.util.RoleUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewActivity extends AppCompatActivity {

    private ActivityViewBinding binding;

    private TextView titleLabelTextView;

    private TextView descriptionLabelTextView;

    private TextView urlLabelTextView;

    private TextView contactsLabelTextView;

    private Button confirmButton;

    private Button rejectButton;

    private CoordinatorLayout coordinatorLayout;

    private AdvertisementDto advertisement;

    private RecyclerView recyclerViewCategories;

    private CategoriesAdapter categoriesAdapter;

    private FloatingActionButton addCategoryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.viewToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null &&
            findViewById(R.id.fragmentCategories) != null){
            recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
            addCategoryButton = findViewById(R.id.addCategoryButton);
            addCategoryButton.setVisibility(RoleUtil.isAdministrator(AppConfiguration.user()) ? View.VISIBLE : View.INVISIBLE);

            // recyclerView выводит элементы в вертикальном списке
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerViewCategories.setLayoutManager(layoutManager);

            loadCategories();

            // Присоединение ItemDecorator для вывода разделителей
            recyclerViewCategories.addItemDecoration(new ItemDivider(this));

            recyclerViewCategories.setHasFixedSize(false);
        }

        titleLabelTextView = findViewById(R.id.titleLabelTextView);
        descriptionLabelTextView = findViewById(R.id.descriptionLabelTextView);
        urlLabelTextView = findViewById(R.id.urlLabelTextView);
        contactsLabelTextView = findViewById(R.id.contactsLabelTextView);
        confirmButton = findViewById(R.id.confirmButton);
        rejectButton = findViewById(R.id.rejectButton);
        coordinatorLayout = findViewById(R.id.viewCoordinatorLayout);

        confirmButton.setOnClickListener(view -> confirm());
        rejectButton.setOnClickListener(view -> reject());

        if(nonNull(getIntent().getExtras())
                && getIntent().getExtras().containsKey("advertisement")) {
            Object advertisementObject = getIntent().getExtras().get("advertisement");
            if(advertisementObject instanceof AdvertisementDto) {
                advertisement = (AdvertisementDto) advertisementObject;
                titleLabelTextView.setText(advertisement.getHeading());
                descriptionLabelTextView.setText(advertisement.getText());
                urlLabelTextView.setText(advertisement.getUrl());
                contactsLabelTextView.setText(advertisement.getContacts());
                binding.viewToolbar.setTitle(advertisement.getCategory().getName());
            }
        }

        processButtons();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getOnBackPressedDispatcher().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void processButtons() {

        boolean isModerator = RoleUtil.isModerator(AppConfiguration.user());

        confirmButton.setVisibility(isModerator
                && advertisement.getStatus() != AdvertisementStatus.CONFIRMED
                ? View.VISIBLE
                : View.INVISIBLE);
        rejectButton.setVisibility(isModerator
                && advertisement.getStatus() != AdvertisementStatus.REJECTED
                ? View.VISIBLE
                : View.INVISIBLE);

    }

    private void confirm() {
        AppConfiguration.advertisementClient().confirmAdvertisement(advertisement.getId()).enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            Log.i("Advertisement", "Advertisement confirmed");
                            Snackbar.make(coordinatorLayout, R.string.confirmed, Snackbar.LENGTH_SHORT).show();
                            advertisement.setStatus(AdvertisementStatus.CONFIRMED);
                            processButtons();
                        } else {
                            Log.e("Advertisement", "Error confirming the advertisement: " + response.code());
                            Snackbar.make(coordinatorLayout, R.string.confirm_error, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Advertisement", "No connection");
                        Snackbar.make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void reject() {
        AppConfiguration.advertisementClient().rejectAdvertisement(advertisement.getId()).enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            Log.i("Advertisement", "Advertisement rejected");
                            Snackbar.make(coordinatorLayout, R.string.rejected, Snackbar.LENGTH_SHORT).show();
                            advertisement.setStatus(AdvertisementStatus.REJECTED);
                            processButtons();
                        } else {
                            Log.e("Advertisement", "Error rejecting the advertisement: " + response.code());
                            Snackbar.make(coordinatorLayout, R.string.reject_error, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Advertisement", "No connection");
                        Snackbar.make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void loadCategories() {
        AppConfiguration.categoryClient().getCategories()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                        if (response.code() == 200) {
                            // создание адаптера recyclerView и слушателя щелчков на элементах
                            categoriesAdapter = new CategoriesAdapter(
                                    category -> onClickCategory(category),
                                    category -> {
                                        AddEditCategoryFragment fragment = new AddEditCategoryFragment(ViewActivity.this::updateCategory, category);
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
                        } else {
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
                        if (response.code() == 200) {
                            Snackbar.make(coordinatorLayout, R.string.successful_deleting, Snackbar.LENGTH_SHORT).show();
                            loadCategories();
                        } else {
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
