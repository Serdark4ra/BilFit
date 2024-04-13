package com.serdar_kara.bilfit.get_info_activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityGoalBinding;

public class GoalActivity extends AppCompatActivity {
    private ActivityGoalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent comingIntent = getIntent();
        UserInfoHolder userInfoHolder = (UserInfoHolder) comingIntent.getSerializableExtra("userInfoHolder");

        binding.buttonNextGoal.setOnClickListener(view -> {
            RadioGroup radioGroup = binding.radioGroup;
            int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

            if(checkedRadioButtonId == -1){
                Toast.makeText(this,"Choose a goal",Toast.LENGTH_LONG).show();
            }else{
                if(checkedRadioButtonId == R.id.radioButton_buildMuscles) {
                    userInfoHolder.setPurpose("buildMuscles");
                }
                else if(checkedRadioButtonId == R.id.radioButton_loseWeight) {
                    userInfoHolder.setPurpose("loseWeight");
                }
                else if(checkedRadioButtonId == R.id.radioButton_MaintainingForm) {
                    userInfoHolder.setPurpose("maintainForm");
                }else{
                    Log.d("Error in choosing form ", String.valueOf(checkedRadioButtonId));
                    throw new IllegalStateException("Unexpected value: " + checkedRadioButtonId);
                }

                Intent intent;
                if(userInfoHolder.getPurpose().equals("loseWeight")) {
                    intent = new Intent(GoalActivity.this, BodyTypeActivity.class);
                } else {
                    intent = new Intent(GoalActivity.this, TargetMusclesActivity.class);
                }
                intent.putExtra("userInfoHolder", userInfoHolder);
                startActivity(intent);
            }
        });

        binding.buttonPrevGoal.setOnClickListener(view -> {
            Intent intentPrev = new Intent(GoalActivity.this, PreferredDaysActivity.class);
            startActivity(intentPrev);
        });

    }
}