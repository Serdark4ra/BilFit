package com.serdar_kara.bilfit.get_info_activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityTargetMusclesBinding;

public class TargetMusclesActivity extends AppCompatActivity {
    private ActivityTargetMusclesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTargetMusclesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent comingIntent = getIntent();
        UserInfoHolder userInfoHolder = (UserInfoHolder) comingIntent.getSerializableExtra("userInfoHolder");


        binding.buttonNext.setOnClickListener(view -> {
            userInfoHolder.setChest(binding.checkBoxChest.isChecked());
            userInfoHolder.setBack(binding.checkBoxBack.isChecked());
            userInfoHolder.setLeg(binding.checkBoxLegs.isChecked());
            userInfoHolder.setArm(binding.checkBoxArms.isChecked());

            Intent intent = new Intent(TargetMusclesActivity.this, BodyTypeActivity.class);
            intent.putExtra("userInfoHolder", userInfoHolder);
            startActivity(intent);
            finish();
        });
        binding.buttonPrev.setOnClickListener(view -> {
            Intent intentPrev = new Intent(TargetMusclesActivity.this, GoalActivity.class);
            startActivity(intentPrev);
            finish();
        });
    }

}