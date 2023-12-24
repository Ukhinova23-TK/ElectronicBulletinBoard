package com.advertisementboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    // Переменные экземпляров ContactsAdapter
    //private Cursor cursor = null;
    private final CategoriesClickListener clickListener;

    // Временное хранилище категорий
    private List<String> categoryList;

    public interface CategoriesClickListener {
        void onClick(String category);
    }

    // Вложенный субкласс RecyclerView.ViewHolder используется
    // для реализации паттерна View–Holder в контексте RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        private int position;

        // Настройка объекта ViewHolder элемента RecyclerView
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);

            // Присоединение слушателя к itemView
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        // Выполняется при щелчке на контакте в ViewHolder
                        @Override
                        public void onClick(View view) {
                            clickListener.onClick(categoryList.get(position));
                        }
                    }
            );
        }
    }

    // Конструктор
    public CategoriesAdapter(CategoriesClickListener clickListener, List<String> categoryList) {
        this.clickListener = clickListener;
        this.categoryList = categoryList;
    }

    // Подготовка нового элемента списка и его объекта ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Заполнение макета android.R.layout.simple_list_item_1
        View view = LayoutInflater.from(parent.getContext()).inflate(
                android.R.layout.simple_list_item_1, parent, false);
        // ViewHolder текущего элемента
        return new ViewHolder(view);
    }

    // Назначает текст элемента списка
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String category = categoryList.get(position);
        holder.textView.setText(category);
    }

    // Возвращает количество элементов, предоставляемых адаптером
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // Текущий объект Cursor адаптера заменяется новым
    public void swapList(List<String> categories) {
        //this.cursor = cursor;
        notifyDataSetChanged();
    }
}
