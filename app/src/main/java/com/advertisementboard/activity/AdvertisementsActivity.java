package com.advertisementboard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.advertisementboard.R;
import com.advertisementboard.account.DialogListener;
import com.advertisementboard.adapter.AdvertisementsAdapter;
import com.advertisementboard.adapter.CategoriesAdapter;
import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.advertisement.AdvertisementPageRequestDto;
import com.advertisementboard.data.dto.advertisement.AdvertisementPageResponseDto;
import com.advertisementboard.data.dto.category.CategoryDto;
import com.advertisementboard.databinding.ActivityAdvertisementsBinding;
import com.advertisementboard.decoration.ItemDivider;
import com.advertisementboard.fragment.AdvertisementsFragment;
import com.advertisementboard.fragment.DeleteDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertisementsActivity extends AppCompatActivity {
    private ActivityAdvertisementsBinding binding;

    private CategoriesAdapter categoriesAdapter; // Адаптер для recyclerView

    private AdvertisementsAdapter advertisementsAdapter; // Адаптер для recyclerView

    private RecyclerView recyclerViewAdvertisements;

    private RecyclerView recyclerViewCategories;

    private CoordinatorLayout coordinatorLayout;

    private FloatingActionButton addAdvertisementButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdvertisementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.advertisementToolbar.setTitle(getIntent().getExtras().get("categoryName").toString());
        setSupportActionBar(binding.advertisementToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = findViewById(R.id.advertisementCoordinatorLayout);
        if(savedInstanceState == null &&
                findViewById(R.id.fragmentAdvertisements) != null) {
            recyclerViewCategories = findViewById(R.id.recyclerViewCategories);

            // recyclerView выводит элементы в вертикальном списке
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerViewCategories.setLayoutManager(layoutManager);

            loadCategories();

            // Присоединение ItemDecorator для вывода разделителей
            recyclerViewCategories.addItemDecoration(new ItemDivider(this));

            recyclerViewCategories.setHasFixedSize(false);
        }
        else{
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerViewAdvertisements = findViewById(R.id.recyclerViewAdvertisements);

        addAdvertisementButton = findViewById(R.id.addAdvertisementButton);
        addAdvertisementButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddEditAdvertisementActivity.class);
            startActivity(intent);
        });
        if(AppConfiguration.user().getLogin() == null) {
            addAdvertisementButton.setVisibility(View.INVISIBLE);
        }

        // recyclerView выводит элементы в вертикальном списке

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewAdvertisements.setLayoutManager(layoutManager);

        loadAdvertisements((Long)getIntent().getExtras().get("categoryId"));

        // Присоединение ItemDecorator для вывода разделителей
        recyclerViewAdvertisements.addItemDecoration(new ItemDivider(this));

        recyclerViewAdvertisements.setHasFixedSize(false);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    private void loadAdvertisements(Long id) {
        AppConfiguration
                .advertisementClient()
                .getAdvertisementsByFilter(
                        AdvertisementPageRequestDto.builder()
                                .page(0)
                                .pageSize(100)
                                .categoryFilter(id)
                                .build()
                )
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<AdvertisementPageResponseDto> call, Response<AdvertisementPageResponseDto> response) {
                        if(response.code() == 200) {
                            // создание адаптера recyclerView и слушателя щелчков на элементах
                            advertisementsAdapter = new AdvertisementsAdapter(
                                    advertisement -> {
                                        Intent intent = new Intent(getBaseContext(), ViewActivity.class);
                                        intent.putExtra("advertisement", advertisement);
                                        startActivity(intent);
                                    },
                                    advertisement -> {
                                        Intent intent = new Intent(getBaseContext(), AddEditAdvertisementActivity.class);
                                        intent.putExtra("advertisement", advertisement);
                                        startActivity(intent);
                                    },
                                    advertisement -> {
                                        DeleteDialogFragment fragment = new DeleteDialogFragment(
                                                new DialogListener() {
                                                    @Override
                                                    public void onDialogPositiveClick(DialogFragment dialog) {
                                                        delete(advertisement.getId());
                                                    }

                                                    @Override
                                                    public void onDialogNegativeClick(DialogFragment dialog) {}
                                                }
                                        );
                                        fragment.show(getSupportFragmentManager(), "Delete advertisement");
                                    },
                                    response.body().getAdvertisements(),
                                    getBaseContext()
                            );

                            recyclerViewAdvertisements.setAdapter(advertisementsAdapter); // Назначение адаптера
                        }
                        else {
                            Log.i("Categories", "Fetching categories failed with code " + response.code());
                            Snackbar.make(coordinatorLayout, R.string.categories_failed, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AdvertisementPageResponseDto> call, Throwable t) {
                        Log.e("Advertisements", "No connection");
                        Snackbar.make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
                    }
                });
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
                                    response.body()
                            );

                            recyclerViewCategories.setAdapter(categoriesAdapter); // Назначение адаптера
                        }
                        else {
                            Log.i("Categories", "Fetching categories failed with code " + response.code());
                            Snackbar.make(coordinatorLayout, R.string.categories_failed, Snackbar.LENGTH_SHORT).show();
                        }
                    }
    private void delete(Long id) {
        AppConfiguration.advertisementClient().deleteAdvertisement(id).enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200) {
                            Snackbar.make(coordinatorLayout, R.string.successful_deleting, Snackbar.LENGTH_SHORT).show();
                            loadAdvertisements((Long)getIntent().getExtras().get("categoryId"));
                        }
                        else {
                            Log.e("Advertisements", "Error during deleting");
                            Snackbar.make(coordinatorLayout, R.string.error_during_deleting, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Advertisements", "No connection");
                        Snackbar.make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );
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

}
