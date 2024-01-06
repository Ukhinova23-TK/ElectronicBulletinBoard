package com.advertisementboard.activity;

import static java.util.Objects.nonNull;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.advertisementboard.R;
import com.advertisementboard.data.dto.advertisement.AdvertisementDto;
import com.advertisementboard.databinding.ActivityViewBinding;

public class ViewActivity extends AppCompatActivity {

    private ActivityViewBinding binding;

    private TextView titleLabelTextView;

    private TextView descriptionLabelTextView;

    private TextView urlLabelTextView;

    private TextView contactsLabelTextView;


    private AdvertisementDto advertisement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.viewToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleLabelTextView = findViewById(R.id.titleLabelTextView);
        descriptionLabelTextView = findViewById(R.id.descriptionLabelTextView);
        urlLabelTextView = findViewById(R.id.urlLabelTextView);
        contactsLabelTextView = findViewById(R.id.contactsLabelTextView);


        if(nonNull(getIntent().getExtras())
                && getIntent().getExtras().containsKey("advertisement")) {
            Object advertisementObject = getIntent().getExtras().get("advertisement");
            if(advertisementObject instanceof AdvertisementDto) {
                advertisement = (AdvertisementDto) advertisementObject;
                titleLabelTextView.setText(advertisement.getHeading());
                descriptionLabelTextView.setText(advertisement.getText());
                urlLabelTextView.setText(advertisement.getUrl());
                contactsLabelTextView.setText(advertisement.getContacts());
            }
        }

    }

}
