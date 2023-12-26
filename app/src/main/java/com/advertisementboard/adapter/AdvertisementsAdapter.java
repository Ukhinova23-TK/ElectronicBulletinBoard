package com.advertisementboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advertisementboard.R;
import com.advertisementboard.data.dto.advertisement.AdvertisementDto;
import com.advertisementboard.data.enumeration.AdvertisementStatus;

import java.util.List;

public class AdvertisementsAdapter extends RecyclerView.Adapter<AdvertisementsAdapter.AdvertisementHolder> {

    // Переменные экземпляров ContactsAdapter
    private final AdvertisementsAdapter.AdvertisementsClickListener clickListener;

    // Временное хранилище категорий
    private List<AdvertisementDto> advertisementList;

    private Context context;

    public interface AdvertisementsClickListener {
        void onClick(AdvertisementDto advertisement);
    }

    // Вложенный субкласс RecyclerView.ViewHolder используется
    // для реализации паттерна View–Holder в контексте RecyclerView
    public class AdvertisementHolder extends RecyclerView.ViewHolder {

        final TextView advertisementName;

        final ImageView statusImageView;

        AdvertisementDto advertisement;

        // Настройка объекта ViewHolder элемента RecyclerView
        public AdvertisementHolder(View itemView) {
            super(itemView);

            advertisementName = itemView.findViewById(R.id.advertisementName);
            statusImageView = itemView.findViewById(R.id.statusImageView);

            itemView.setOnClickListener(view -> clickListener.onClick(advertisement));

        }
    }

    // Конструктор
    public AdvertisementsAdapter(
            AdvertisementsAdapter.AdvertisementsClickListener clickListener,
            List<AdvertisementDto> advertisementList,
            Context context
    ) {
        this.clickListener = clickListener;
        this.advertisementList = advertisementList;
        this.context = context;
    }

    // Подготовка нового элемента списка и его объекта ViewHolder
    @NonNull
    @Override
    public AdvertisementsAdapter.AdvertisementHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Заполнение макета android.R.layout.simple_list_item_1
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.advertisement_list_item, parent, false);
        // ViewHolder текущего элемента
        return new AdvertisementsAdapter.AdvertisementHolder(view);
    }

    // Назначает текст элемента списка
    @Override
    public void onBindViewHolder(AdvertisementsAdapter.AdvertisementHolder holder, int position) {
        AdvertisementDto advertisement = advertisementList.get(position);
        holder.advertisementName.setText(advertisement.getHeading());
        holder.advertisement = advertisement;
        if(advertisement.getStatus() == AdvertisementStatus.CONFIRMED) {
            holder.statusImageView.setColorFilter(context.getResources().getColor(R.color.primaryColor));
        }
        else if(advertisement.getStatus() == AdvertisementStatus.REJECTED) {
            holder.statusImageView.setColorFilter(context.getResources().getColor(R.color.redColor));
        }
    }

    // Возвращает количество элементов, предоставляемых адаптером
    @Override
    public int getItemCount() {
        return advertisementList.size();
    }

}

