package com.advertisementboard.adapter;

import static java.util.Objects.nonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advertisementboard.R;
import com.advertisementboard.config.AppConfiguration;
import com.advertisementboard.data.dto.advertisement.AdvertisementDto;
import com.advertisementboard.data.dto.user.UserDto;
import com.advertisementboard.data.enumeration.AdvertisementStatus;
import com.advertisementboard.data.enumeration.UserRole;

import java.util.List;

public class AdvertisementsAdapter extends RecyclerView.Adapter<AdvertisementsAdapter.AdvertisementHolder> {

    private final AdvertisementsAdapter.AdvertisementsClickListener clickListener;

    private final AdvertisementsAdapter.AdvertisementsClickListener editClickListener;

    private final AdvertisementsAdapter.AdvertisementsClickListener deleteClickListener;

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

        final ImageView updateImageView;

        final ImageView deleteImageView;

        AdvertisementDto advertisement;

        // Настройка объекта ViewHolder элемента RecyclerView
        public AdvertisementHolder(View itemView) {
            super(itemView);

            advertisementName = itemView.findViewById(R.id.advertisementName);
            statusImageView = itemView.findViewById(R.id.statusImageView);
            updateImageView = itemView.findViewById(R.id.updateImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);

            itemView.setOnClickListener(view -> clickListener.onClick(advertisement));
            updateImageView.setOnClickListener(view -> editClickListener.onClick(advertisement));
            deleteImageView.setOnClickListener(view -> deleteClickListener.onClick(advertisement));
        }
    }

    // Конструктор
    public AdvertisementsAdapter(
            AdvertisementsAdapter.AdvertisementsClickListener clickListener,
            AdvertisementsAdapter.AdvertisementsClickListener editClickListener,
            AdvertisementsAdapter.AdvertisementsClickListener deleteClickListener,
            List<AdvertisementDto> advertisementList,
            Context context
    ) {
        this.clickListener = clickListener;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
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
        if(nonNull(advertisement)) {

            UserDto currentUser = AppConfiguration.user();

            boolean isOperationsVisible = nonNull(currentUser.getRole())
                    && nonNull(currentUser.getRole().getName())
                    && (currentUser.getRole().getName().equals(UserRole.ADMINISTRATOR.name())
                    || advertisement.getUser().getLogin().equals(currentUser.getLogin()));

            holder.updateImageView.setVisibility(isOperationsVisible ? View.VISIBLE : View.INVISIBLE);
            holder.deleteImageView.setVisibility(isOperationsVisible ? View.VISIBLE : View.INVISIBLE);

        }
        else {
            holder.updateImageView.setVisibility(View.INVISIBLE);
            holder.deleteImageView.setVisibility(View.INVISIBLE);
        }
    }

    // Возвращает количество элементов, предоставляемых адаптером
    @Override
    public int getItemCount() {
        return advertisementList.size();
    }

}

