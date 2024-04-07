package com.serdar_kara.bilfit.get_info_activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityBodyTypeBinding;

public class BodyTypeActivity extends AppCompatActivity {
    private ActivityBodyTypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBodyTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent comingIntent = getIntent();
        UserInfoHolder userInfoHolder = (UserInfoHolder) comingIntent.getSerializableExtra("userInfoHolder");

        RadioGroup radioGroup = binding.radioGroup2;
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.radioButton_muscular) {
                userInfoHolder.setBodyType("muscular");
            }
            else if(checkedId == R.id.radioButton_fat) {
                userInfoHolder.setBodyType("fat");
            }
            else if(checkedId == R.id.radioButton_thin) {
                userInfoHolder.setBodyType("thin");
            }
            else if(checkedId == R.id.radioButton_normal) {
                userInfoHolder.setBodyType("normal");
            }else{
                throw new IllegalStateException("Unexpected value: " + checkedId);
            }
        });

        Intent intent = new Intent(BodyTypeActivity.this, GetPushupActivity.class);
        intent.putExtra("userInfoHolder", userInfoHolder);

        binding.buttonNextBodyType.setOnClickListener(view -> {
            startActivity(intent);
        });
        binding.buttonPrevBodyType.setOnClickListener(view -> {
            Intent intentPrev = new Intent(BodyTypeActivity.this, GoalActivity.class);
            startActivity(intentPrev);
        });



    }
}