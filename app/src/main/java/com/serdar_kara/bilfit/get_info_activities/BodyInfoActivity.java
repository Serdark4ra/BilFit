package com.serdar_kara.bilfit.get_info_activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityBodyInfoBinding;

public class BodyInfoActivity extends AppCompatActivity {
    private ActivityBodyInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBodyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent comingIntent = getIntent();
        UserInfoHolder userInfoHolder = (UserInfoHolder) comingIntent.getSerializableExtra("userInfoHolder");
        userInfoHolder.setWeight(Integer.parseInt(binding.editTextNumberWeight.getText().toString()));
        userInfoHolder.setHeight(Integer.parseInt(binding.editTextNumberDecimalHeight.getText().toString()));
        userInfoHolder.setAge(Integer.parseInt(binding.editTextNumberAge.getText().toString()));

        Intent intent = new Intent(BodyInfoActivity.this, PreferredDaysActivity.class);
        intent.putExtra("userInfoHolder", userInfoHolder);

        binding.buttonNextBodyInfo.setOnClickListener(view -> {
            startActivity(intent);
        });
        binding.buttonPrevBodyInfo.setOnClickListener(view -> {
            Intent intentPrev = new Intent(BodyInfoActivity.this, TargetMusclesActivity.class);
            startActivity(intentPrev);
        });


    }
}