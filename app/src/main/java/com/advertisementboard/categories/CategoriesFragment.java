package com.advertisementboard.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.advertisementboard.ItemDivider;
import com.advertisementboard.R;
import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.category.CategoryDto;
import com.advertisementboard.databinding.FragmentCategoriesBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesFragment extends Fragment {

    private FragmentCategoriesBinding binding;

    private CategoriesFragmentListener listener; // Сообщает MainActivity о выборе категории

    private CategoriesAdapter categoriesAdapter; // Адаптер для recyclerView

    private RecyclerView recyclerViewCategories;

    private CoordinatorLayout coordinatorLayout;

    private static final int CATEGORIES_LOADER = 0;

    // Метод обратного вызова, реализуемый MainActivity
    public interface CategoriesFragmentListener {

        // Вызывается при выборе записи расписания
        void onCategorySelected(String category);

        // Вызывается при нажатии кнопки добавления
        //void onAddSchedule();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

        // Заполнение GUI и получение ссылки на RecyclerView
        View view = inflater.inflate(
                R.layout.fragment_categories, container, false);

        coordinatorLayout = getActivity().findViewById(R.id.coordinatorLayout);

        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getActivity()));

        // recyclerView выводит элементы в вертикальном списке
        recyclerViewCategories.setLayoutManager(
                new LinearLayoutManager(getActivity().getBaseContext())
        );

        loadCategories();

        // Присоединение ItemDecorator для вывода разделителей
        recyclerViewCategories.addItemDecoration(new ItemDivider(getContext()));

        // Улучшает быстродействие, если размер макета RecyclerView не изменяется
        recyclerViewCategories.setHasFixedSize(true);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadCategories() {
        AppConfiguration.categoryClient().getCategories()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                        if(response.code() == 200) {
                            // создание адаптера recyclerView и слушателя щелчков на элементах
                            categoriesAdapter = new CategoriesAdapter(
                                    category -> listener.onCategorySelected(category),
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

}