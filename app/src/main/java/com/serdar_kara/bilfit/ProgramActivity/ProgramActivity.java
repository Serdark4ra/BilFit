package com.serdar_kara.bilfit.ProgramActivity;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.serdar_kara.bilfit.databinding.ActivityProgramBinding;
import com.serdar_kara.bilfit.exercises.ExerciseEditAdapter;
import com.serdar_kara.bilfit.exercises.ExerciseModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramActivity extends AppCompatActivity {
    private ActivityProgramBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FirebaseFirestore db;
    private DocumentReference documentReference;

    private DaysPagerAdapter daysPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            fetchExercisesAndSetupUI();
        }
    }

    private void fetchExercisesAndSetupUI() {
        List<String> daysList = getUserSpecificDays();
        Map<String, List<ExerciseModel>> exercisesByDay = new HashMap<>();

        if (daysList.isEmpty()) {
            Log.d(TAG, "No exercise days set in SharedPreferences.");
            return; // Exit if no days are selected
        }

        documentReference = db.collection("Users").document(currentUser.getUid());

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Map<String, Object> program = (Map<String, Object>) documentSnapshot.get("program");
                if (program != null) {
                    for (String day : daysList) {
                        List<String> exercises = (List<String>) program.get(day);
                        ArrayList<ExerciseModel> exerciseModels = new ArrayList<>();
                        if (exercises != null) {
                            for (String exercise : exercises) {
                                exerciseModels.add(new ExerciseModel(exercise));
                            }
                        }
                        exercisesByDay.put(day, exerciseModels);
                    }
                    setupViewPagerAndTabs(daysList, exercisesByDay);
                } else {
                    Log.d(TAG, "Program data is missing in user document");
                }
            } else {
                Log.d(TAG, "No user document exists for UID: " + currentUser.getUid());
            }
        }).addOnFailureListener(e -> Log.w(TAG, "Error retrieving user document", e));
    }

    private void setupViewPagerAndTabs(List<String> daysList, Map<String, List<ExerciseModel>> exercisesByDay) {
        runOnUiThread(() -> {
            daysPagerAdapter = new DaysPagerAdapter(getSupportFragmentManager(), getLifecycle(), daysList, exercisesByDay);
            binding.viewPagerDaySchedule.setAdapter(daysPagerAdapter);

            new TabLayoutMediator(binding.tabLayoutDays, binding.viewPagerDaySchedule,
                    (tab, position) -> tab.setText(daysList.get(position))
            ).attach();
        });
    }

    private ArrayList<String> getUserSpecificDays() {
        SharedPreferences sharedPreferences = getSharedPreferences("ExerciseDays", Context.MODE_PRIVATE);
        ArrayList<String> daysList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (sharedPreferences.getBoolean("day_" + i, false)) {
                daysList.add(getDayName(i));
            }
        }
        return daysList;
    }

    private String getDayName(int dayIndex) {
        switch (dayIndex) {
            case 0: return "Monday";
            case 1: return "Tuesday";
            case 2: return "Wednesday";
            case 3: return "Thursday";
            case 4: return "Friday";
            case 5: return "Saturday";
            case 6: return "Sunday";
            default: return "";
        }
    }
}
