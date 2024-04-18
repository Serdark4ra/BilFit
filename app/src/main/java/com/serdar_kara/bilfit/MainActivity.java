package com.serdar_kara.bilfit;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.serdar_kara.bilfit.databinding.ActivityMainBinding;
import com.serdar_kara.bilfit.exercises.ExerciseAdapter;
import com.serdar_kara.bilfit.exercises.ExerciseModel;
import com.serdar_kara.bilfit.get_info_activities.LoadingInfoSessionSActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private ArrayList<ExerciseModel> exerciseList;
    private ExerciseAdapter exerciseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button completedButton = activityMainBinding.buttonCompletedExercise;

        setVariablesForUser();

        exerciseList = new ArrayList<>();
        exerciseAdapter = new ExerciseAdapter(exerciseList);



        activityMainBinding.recyclerViewExerciseList.setLayoutManager(new LinearLayoutManager(this));

        retrieveProgramFromDatabase();
        activityMainBinding.recyclerViewExerciseList.setAdapter(exerciseAdapter);



        SharedPreferences sharedPreferences = getSharedPreferences("CompletedExerciseDays", Context.MODE_PRIVATE);
        Boolean isCompleted = sharedPreferences.getBoolean("day_" + (LocalDate.now().getDayOfWeek().getValue() - 1), false);
        SharedPreferences sharedPreferences1 = getSharedPreferences("ExerciseDays", Context.MODE_PRIVATE);
        Boolean isCompleted1 = sharedPreferences1.getBoolean("day_" + (LocalDate.now().getDayOfWeek().getValue() - 1), false);
        if (!isCompleted && isCompleted1){
            completedButton.setVisibility(Button.VISIBLE);
        }else{
            completedButton.setVisibility(Button.INVISIBLE);
        }

        completedButton.setOnClickListener(v -> {
            LocalDate today = LocalDate.now();
            DayOfWeek dayOfWeek = today.getDayOfWeek();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("day_" + (dayOfWeek.getValue() - 1), true);
            editor.apply();
            completedButton.setVisibility(Button.INVISIBLE);
        });

        activityMainBinding.settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        activityMainBinding.Navi.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_leaderBoard) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_program) {
                Intent intent = new Intent(MainActivity.this, ProgramActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_friends) {
                Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_report) {
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
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
                        ArrayList<String> program = (ArrayList<String>) programData.get(determineDayToShowProgram());
                        if (program != null) {
                            // Clear the exerciseList before adding new exercises
                            exerciseList.clear();
                            for (String exercise : program) {
                                exerciseList.add(new ExerciseModel(exercise));
                            }
                            // Notify the adapter that the data set has changed
                            exerciseAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Error", "No program data for the current day: " + determineDayToShowProgram());
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


    private void setVariablesForUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Users").document(currentUser.getUid());

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name_surname = documentSnapshot.getString("name_surname");
                activityMainBinding.textViewUserName.setText(name_surname);
            }else{
                Log.d("Error", "No such document with the current user id: " + currentUser.getUid());
            }
        });
    }

    public boolean[] getExerciseDays(){
        SharedPreferences sharedPreferences = getSharedPreferences("ExerciseDays", Context.MODE_PRIVATE);
        boolean[] days = new boolean[7];
        for (int i = 0; i < 7; i++) {
            days[i] = sharedPreferences.getBoolean("day_" + i, false);
        }
        return days;
    }

    public String determineDayToShowProgram(){
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        boolean[] exerciseDays = getExerciseDays();

        int currentDay = dayOfWeek.getValue();
        for (; currentDay < 7 ; currentDay++) {
            if (exerciseDays[currentDay- 1]){
                break;
            }
        }
        String day = "";
        switch (currentDay){
            case 1:
                day = "monday";
                break;
            case 2:
                day = "tuesday";
                break;
            case 3:
                day = "wednesday";
                break;
            case 4:
                day = "thursday";
                break;
            case 5:
                day = "friday";
                break;
            case 6:
                day = "saturday";
                break;
            case 7:
                day = "sunday";
                break;
        }
        return day;
    }


}