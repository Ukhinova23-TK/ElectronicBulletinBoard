package com.advertisementboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advertisementboard.R;
import com.advertisementboard.data.dto.category.CategoryDto;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryHolder> {

    // Переменные экземпляров ContactsAdapter
    private final CategoriesClickListener clickListener;

    // Временное хранилище категорий
    private List<CategoryDto> categoryList;

    public interface CategoriesClickListener {
        void onClick(CategoryDto category);
    }

    // Вложенный субкласс RecyclerView.ViewHolder используется
    // для реализации паттерна View–Holder в контексте RecyclerView
    public class CategoryHolder extends RecyclerView.ViewHolder {

        final TextView nameTextView;

        final TextView descriptionTextView;

        CategoryDto category;

        // Настройка объекта ViewHolder элемента RecyclerView
        public CategoryHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.categoryName);
            descriptionTextView = itemView.findViewById(R.id.categoryDescription);

            itemView.setOnClickListener(view -> clickListener.onClick(category));

        }
    }

    // Конструктор
    public CategoriesAdapter(CategoriesClickListener clickListener, List<CategoryDto> categoryList) {
        this.clickListener = clickListener;
        this.categoryList = categoryList;
    }

    // Подготовка нового элемента списка и его объекта ViewHolder
    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Заполнение макета android.R.layout.simple_list_item_1
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.category_list_item, parent, false);
        // ViewHolder текущего элемента
        return new CategoryHolder(view);
    }

    // Назначает текст элемента списка
    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        CategoryDto category = categoryList.get(position);
        holder.nameTextView.setText(category.getName());
        holder.descriptionTextView.setText(category.getDescription());
        holder.category = category;
    }

    // Возвращает количество элементов, предоставляемых адаптером
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

}
