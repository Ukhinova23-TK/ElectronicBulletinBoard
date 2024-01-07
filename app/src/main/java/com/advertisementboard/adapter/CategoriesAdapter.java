package com.advertisementboard.adapter;

import static java.util.Objects.nonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advertisementboard.R;
import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.category.CategoryDto;
import com.advertisementboard.data.dto.user.UserDto;
import com.advertisementboard.data.enumeration.UserRole;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryHolder> {

    private final CategoriesClickListener clickListener;

    private final CategoriesClickListener editClickListener;

    private final CategoriesClickListener deleteClickListener;

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

        final ImageView updateCategoryImageView;

        final ImageView deleteCategoryImageView;

        CategoryDto category;

        // Настройка объекта ViewHolder элемента RecyclerView
        public CategoryHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.categoryName);
            descriptionTextView = itemView.findViewById(R.id.categoryDescription);
            updateCategoryImageView = itemView.findViewById(R.id.updateCategoryImageView);
            deleteCategoryImageView = itemView.findViewById(R.id.deleteCategoryImageView);

            itemView.setOnClickListener(view -> clickListener.onClick(category));
            updateCategoryImageView.setOnClickListener(view -> editClickListener.onClick(category));
            deleteCategoryImageView.setOnClickListener(view -> deleteClickListener.onClick(category));

        }
    }

    // Конструктор
    public CategoriesAdapter(CategoriesClickListener clickListener,
                             CategoriesClickListener editClickListener,
                             CategoriesClickListener deleteClickListener,
                             List<CategoryDto> categoryList) {
        this.clickListener = clickListener;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
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
        UserDto user = AppConfiguration.user();
        holder.nameTextView.setText(category.getName());
        holder.descriptionTextView.setText(category.getDescription());
        holder.category = category;
        boolean isAdministrator = nonNull(user.getRole())
                && nonNull(user.getRole().getName())
                && user.getRole().getName().equals(UserRole.ADMINISTRATOR.name());
        holder.updateCategoryImageView.setVisibility(isAdministrator ? View.VISIBLE : View.INVISIBLE);
        holder.deleteCategoryImageView.setVisibility(isAdministrator ? View.VISIBLE : View.INVISIBLE);
    }

    // Возвращает количество элементов, предоставляемых адаптером
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

}
