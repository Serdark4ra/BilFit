package com.serdar_kara.bilfit.get_info_activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityPreferredDaysBinding;

public class PreferredDaysActivity extends AppCompatActivity {

    private ActivityPreferredDaysBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreferredDaysBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent comingIntent = getIntent();
        UserInfoHolder userInfoHolder = (UserInfoHolder) comingIntent.getSerializableExtra("userInfoHolder");

        userInfoHolder.setMondayEligible(binding.checkBoxMon.isChecked());
        userInfoHolder.setTuesdayEligible(binding.checkBoxTue.isChecked());
        userInfoHolder.setWednesdayEligible(binding.checkBoxWed.isChecked());
        userInfoHolder.setThursdayEligible(binding.checkBoxThu.isChecked());
        userInfoHolder.setFridayEligible(binding.checkBoxFri.isChecked());
        userInfoHolder.setSaturdayEligible(binding.checkBoxSat.isChecked());
        userInfoHolder.setSundayEligible(binding.checkBoxSun.isChecked());
        
        Intent intent = new Intent(PreferredDaysActivity.this, GoalActivity.class);
        intent.putExtra("userInfoHolder", userInfoHolder);
        
        binding.buttonNextPrefDays.setOnClickListener(view -> {
            startActivity(intent);
        });
        binding.buttonPrevPrefDays.setOnClickListener(view -> {
            Intent intentPrev = new Intent(PreferredDaysActivity.this, BodyInfoActivity.class);
            startActivity(intentPrev);
        });




    }
}