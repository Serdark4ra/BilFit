package com.serdar_kara.bilfit.get_info_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityGenderBinding;

public class GenderActivity extends AppCompatActivity {
    private ActivityGenderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserInfoHolder userInfoHolder = new UserInfoHolder("", false, false, false, false, 0, 0, 0, false, false, false, false, false, false, false, null, null, 0);


        binding.buttonMale.setOnClickListener(view -> {
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userInfoHolder.setGender("male");
                    Intent intent = new Intent(GenderActivity.this, TargetMusclesActivity.class);
                    intent.putExtra("userInfoHolder", userInfoHolder);
                    startActivity(intent);
                }
            };
        });
    }


}