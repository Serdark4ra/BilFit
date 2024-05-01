package com.serdar_kara.bilfit;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.serdar_kara.bilfit.databinding.ActivityProgramBinding;
import com.serdar_kara.bilfit.exercises.ExerciseAdapter;
import com.serdar_kara.bilfit.exercises.ExerciseEditAdapter;
import com.serdar_kara.bilfit.exercises.ExerciseModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProgramActivity extends AppCompatActivity {
    private ActivityProgramBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private ArrayList<ExerciseModel> exerciseList;
    private ExerciseEditAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //exerciseList = new ArrayList<>();
        //exerciseAdapter = new ExerciseEditAdapter(exerciseList);
        //binding.recyclerViewExerciseListEdit.setLayoutManager(new LinearLayoutManager(this));
        //retrieveProgramFromDatabase();
        //binding.recyclerViewExerciseListEdit.setAdapter(exerciseAdapter);

        TabLayout tabLayout = binding.tabLayoutDays;
        ViewPager2 viewPager = binding.viewPagerDaySchedule;

        List<String> daysList = getUserSpecificDays();





    }

    private List<String> getUserSpecificDays() {
        boolean[] exerciseDays = getExerciseDays();
        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();
        
        return null;
    }

    public boolean[] getExerciseDays(){
        SharedPreferences sharedPreferences = getSharedPreferences("ExerciseDays", Context.MODE_PRIVATE);
        boolean[] days = new boolean[7];
        for (int i = 0; i < 7; i++) {
            days[i] = sharedPreferences.getBoolean("day_" + i, false);
        }
        return days;
    }

    private void retrieveProgramFromDatabase() {
        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        documentReference = db.collection("Users").document(currentUser.getUid());

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> programData = (Map<String, Object>) documentSnapshot.get("program");
                    if (programData != null) {
                        // Get the program for the current day
                        ArrayList<String> program = (ArrayList<String>) programData.get("tuesday");
                        if (program != null) {
                            // Clear the exerciseList before adding new exercises
                            exerciseList.clear();
                            for (String exercise : program) {
                                exerciseList.add(new ExerciseModel(exercise));
                            }
                            // Notify the adapter that the data set has changed
                            exerciseAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Error", "No program data for the current day: " + "tuesday");
                        }
                    } else {
                        Log.d("Error", "No program data for the current user id: " + currentUser.getUid());
                    }
                } else {
                    Log.d("Error", "No such document with the current user id: " + currentUser.getUid());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error retrieving program data", e);
            }
        });
    }
}