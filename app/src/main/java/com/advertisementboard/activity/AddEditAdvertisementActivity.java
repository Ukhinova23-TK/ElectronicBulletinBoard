package com.advertisementboard.activity;

import static java.util.Objects.nonNull;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NavUtils;

import com.advertisementboard.R;
import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.advertisement.AdvertisementDto;
import com.advertisementboard.data.dto.advertisement.AdvertisementRequestDto;
import com.advertisementboard.data.dto.category.CategoryDto;
import com.advertisementboard.databinding.ActivityAddEditAdvertisementBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditAdvertisementActivity extends AppCompatActivity {
    private ActivityAddEditAdvertisementBinding binding;

    private CoordinatorLayout coordinatorLayout;

    private TextInputLayout titleTextInputLayout;

    private TextInputLayout descriptionTextInputLayout;

    private TextInputLayout urlTextInputLayout;

    private TextInputLayout contactsTextInputLayout;

    private Spinner categorySpinner;

    private Button saveButton;

    private List<CategoryDto> categoryList;

    private AdvertisementDto advertisement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddEditAdvertisementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.addEditToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleTextInputLayout = findViewById(R.id.titleTextInputLayout);
        descriptionTextInputLayout = findViewById(R.id.descriptionTextInputLayout);
        urlTextInputLayout = findViewById(R.id.urlTextInputLayout);
        contactsTextInputLayout = findViewById(R.id.contactsTextInputLayout);
        categorySpinner = findViewById(R.id.categorySpinner);
        saveButton = findViewById(R.id.saveButton);

        coordinatorLayout = findViewById(R.id.addEditCoordinatorLayout);

        loadCategories();

        if(nonNull(getIntent().getExtras())
                && getIntent().getExtras().containsKey("advertisement")) {
            Object advertisementObject = getIntent().getExtras().get("advertisement");
            if(advertisementObject instanceof AdvertisementDto) {
                advertisement = (AdvertisementDto) advertisementObject;
                titleTextInputLayout.getEditText().setText(advertisement.getHeading());
                descriptionTextInputLayout.getEditText().setText(advertisement.getText());
                urlTextInputLayout.getEditText().setText(advertisement.getUrl());
                contactsTextInputLayout.getEditText().setText(advertisement.getContacts());
            }
        }

        saveButton.setOnClickListener(view -> {
            if (advertisement == null) {
                save();
            } else {
                update();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    private void loadCategories() {
        AppConfiguration.categoryClient().getCategories()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                        if(response.code() == 200) {
                            categoryList = response.body();
                            ArrayAdapter<CategoryDto> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner, categoryList);
                            categorySpinner.setAdapter(adapter);
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

    private void save() {
        AppConfiguration.advertisementClient()
                .createAdvertisement(
                        AdvertisementRequestDto.builder()
                                .heading(titleTextInputLayout.getEditText().getText().toString())
                                .text(descriptionTextInputLayout.getEditText().getText().toString())
                                .url(urlTextInputLayout.getEditText().getText().toString())
                                .contacts(contactsTextInputLayout.getEditText().getText().toString())
                                .categoryId(categoryList.get(categorySpinner.getSelectedItemPosition()).getId())
                                .build()
                )
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        if(response.code() == 201) {
                            Log.e("Advertisement", "Successful saving");
                            Snackbar.make(coordinatorLayout, R.string.successful_saving, Snackbar.LENGTH_SHORT).show();
                            NavUtils.navigateUpFromSameTask(getParent());
                        }
                        else {
                            Log.e("Advertisement", "Error during saving");
                            Snackbar.make(coordinatorLayout, R.string.error_during_saving, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {
                        Log.e("Advertisement", "No connection");
                        Snackbar.make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void update() {
        AppConfiguration.advertisementClient()
                .updateAdvertisement(
                        AdvertisementRequestDto.builder()
                                .id(advertisement.getId())
                                .heading(titleTextInputLayout.getEditText().getText().toString())
                                .text(descriptionTextInputLayout.getEditText().getText().toString())
                                .url(urlTextInputLayout.getEditText().getText().toString())
                                .contacts(contactsTextInputLayout.getEditText().getText().toString())
                                .categoryId(categoryList.get(categorySpinner.getSelectedItemPosition()).getId())
                                .build()
                )
                .enqueue(new Callback<>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200) {
                            Log.e("Advertisement", "Successful saving");
                            Snackbar.make(coordinatorLayout, R.string.successful_saving, Snackbar.LENGTH_SHORT).show();
                            NavUtils.navigateUpFromSameTask(getParent());
                        }
                        else {
                            Log.e("Advertisement", "Error during saving");
                            Snackbar.make(coordinatorLayout, R.string.error_during_saving, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Advertisement", "No connection");
                        Snackbar.make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

}
