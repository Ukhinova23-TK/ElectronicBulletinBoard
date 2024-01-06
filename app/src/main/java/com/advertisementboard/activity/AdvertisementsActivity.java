package com.advertisementboard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.advertisementboard.R;
import com.advertisementboard.adapter.AdvertisementsAdapter;
import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.advertisement.AdvertisementPageRequestDto;
import com.advertisementboard.data.dto.advertisement.AdvertisementPageResponseDto;
import com.advertisementboard.databinding.ActivityAdvertisementsBinding;
import com.advertisementboard.decoration.ItemDivider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertisementsActivity extends AppCompatActivity {
    private ActivityAdvertisementsBinding binding;

    private AdvertisementsAdapter advertisementsAdapter; // Адаптер для recyclerView

    private RecyclerView recyclerViewAdvertisements;

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
                                    advertisement -> {},
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



}
