package com.serdar_kara.bilfit.get_info_activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityLoadingInfoSessionSactivityBinding;

public class LoadingInfoSessionSActivity extends AppCompatActivity {

    private ActivityLoadingInfoSessionSactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingInfoSessionSactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        ProgressBar progressBar = binding.progressBar;
        TextView loadingPercentage = binding.textViewLoadingPercentage;

        // Calculate the completion percentage (assuming completionPercentage is between 0 and 100)
        int completionPercentage = 56;
        progressBar.setProgress(completionPercentage);
        loadingPercentage.setText(completionPercentage + "%");


        //Intent intent1 = new Intent(LoadingInfoSessionSActivity.this, MainActivity.class);
        //startActivity(intent1);
    }
}