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

        userInfoHolder.setDays(0, binding.checkBoxMon.isChecked());
        userInfoHolder.setDays(1, binding.checkBoxTue.isChecked());
        userInfoHolder.setDays(2, binding.checkBoxWed.isChecked());
        userInfoHolder.setDays(3, binding.checkBoxThu.isChecked());
        userInfoHolder.setDays(4, binding.checkBoxFri.isChecked());
        userInfoHolder.setDays(5, binding.checkBoxSat.isChecked());
        userInfoHolder.setDays(6, binding.checkBoxSun.isChecked());

        userInfoHolder.printPrefDays();

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