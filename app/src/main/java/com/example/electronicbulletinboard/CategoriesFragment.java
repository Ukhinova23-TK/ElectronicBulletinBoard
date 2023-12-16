package com.example.electronicbulletinboard;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicbulletinboard.databinding.FragmentCategoriesBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    private FragmentCategoriesBinding binding;

    private CategoriesFragmentListener listener; // Сообщает MainActivity о выборе категории

    private CategoriesAdapter categoriesAdapter; // Адаптер для recyclerView

    private List<String> categories;
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
        RecyclerView recyclerView =
                (RecyclerView) view.findViewById(R.id.recyclerViewCategories);

        // recyclerView выводит элементы в вертикальном списке
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity().getBaseContext()));

        categories = new ArrayList<>();
        categories.add("Недвижимость");
        categories.add("Товары");
        categories.add("Животные");

        // создание адаптера recyclerView и слушателя щелчков на элементах
        categoriesAdapter = new CategoriesAdapter(
                new CategoriesAdapter.CategoriesClickListener() {
                    @Override
                    public void onClick(String category){
                        listener.onCategorySelected(category);
                    }
                }, categories);
        recyclerView.setAdapter(categoriesAdapter); // Назначение адаптера

        // Присоединение ItemDecorator для вывода разделителей
        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        // Улучшает быстродействие, если размер макета RecyclerView не изменяется
        recyclerView.setHasFixedSize(true);

        List<String> categories = new ArrayList<>();
        categories.add("Нежвижимость");
        categories.add("Животные");
        categories.add("Товары");
        categoriesAdapter.swapList(categories);


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

}