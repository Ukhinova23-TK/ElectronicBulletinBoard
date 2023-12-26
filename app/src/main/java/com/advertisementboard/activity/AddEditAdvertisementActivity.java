package com.advertisementboard.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.advertisementboard.R;
import com.advertisementboard.adapter.CategoriesAdapter;
import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.category.CategoryDto;
import com.advertisementboard.databinding.ActivityAddEditAdvertisementBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

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

    private List<CategoryDto> categoryList;

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

        coordinatorLayout = findViewById(R.id.addEditCoordinatorLayout);

        loadCategories();
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

    }

}
