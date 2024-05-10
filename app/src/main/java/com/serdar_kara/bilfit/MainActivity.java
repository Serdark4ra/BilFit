package com.serdar_kara.bilfit;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.serdar_kara.bilfit.ProgramActivity.ProgramActivity;
import com.serdar_kara.bilfit.Settings.SettingsActivity;
import com.serdar_kara.bilfit.databinding.ActivityMainBinding;
import com.serdar_kara.bilfit.exercises.ExerciseAdapter;
import com.serdar_kara.bilfit.exercises.ExerciseModel;
import com.serdar_kara.bilfit.friends.FriendsActivity;
import com.serdar_kara.bilfit.get_info_activities.UserInfoHolder;
import com.serdar_kara.bilfit.get_info_activities.UserInfoManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
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

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button completedButton = activityMainBinding.buttonCompletedExercise;

        setVariablesForUser(currentUser.getUid());

        exerciseList = new ArrayList<>();
        exerciseAdapter = new ExerciseAdapter(exerciseList);



        activityMainBinding.recyclerViewExerciseList.setLayoutManager(new LinearLayoutManager(this));

        retrieveProgramFromDatabase(currentUser.getUid());
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
            Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
            startActivity(intent);
            completedButton.setVisibility(Button.INVISIBLE);
            updateUserPoints();

        });

        activityMainBinding.settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        activityMainBinding.Navi.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_leaderBoard) {
                Intent intent = new Intent(MainActivity.this, LeaderBoardActivity.class);
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

    private void updateUserPoints() {
        if (currentUser == null) {
            Log.d(TAG, "User is not logged in.");
            return;
        }

        String userId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("Users").document(userId);

        // First fetch the user document
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Number pointsNumber = documentSnapshot.getLong("points");
                long points = pointsNumber != null ? pointsNumber.longValue() : 0;

                // Fetch the power value
                Double power = documentSnapshot.getDouble("power");
                if (power == null) {
                    Log.e("power", "Power value is null.");
                    power = 0.0; // default to 0 if not found
                }

                // Calculate new points
                long newPoints = points + (long) (10 * power);

                // Update the points in the database
                userDocRef.update("points", newPoints)
                        .addOnSuccessListener(aVoid -> Log.d("power", "Points updated successfully."))
                        .addOnFailureListener(e -> Log.e("power", "Error updating points.", e));
            } else {
                Log.e("power", "No user document found.");
            }
        }).addOnFailureListener(e -> Log.e("power", "Error fetching user document.", e));
    }



    private void retrieveProgramFromDatabase(String userId) {
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Users").document(userId);

        // Adding a snapshot listener to listen for real-time updates
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Map<String, Object> programData = (Map<String, Object>) documentSnapshot.get("program");
                    if (programData != null) {
                        // Get the program for the current day
                        ArrayList<String> program = (ArrayList<String>) programData.get(determineDayToShowProgramInUpcoming());
                        if (program != null) {
                            // Clear the exerciseList before adding new exercises
                            exerciseList.clear();
                            for (String exercise : program) {
                                exerciseList.add(new ExerciseModel(exercise));
                            }
                            // Notify the adapter that the data set has changed
                            exerciseAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Error", "No program data for the current day: " + determineDayToShowProgramInUpcoming());
                        }
                    } else {
                        Log.d("Error", "No program data for the current user id: " + userId);
                    }
                } else {
                    Log.d("Error", "No such document with the current user id: " + userId);
                }
            }
        });
    }



    private void setVariablesForUser(String userId) {
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Users").document(userId);

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve and display name
                String name_surname = documentSnapshot.getString("name_surname");
                activityMainBinding.textViewUserName.setText(name_surname);

                // Retrieve and display points
                Number pointsNumber = documentSnapshot.getLong("points");
                if (pointsNumber != null) {  // Check if the points data exists
                    int points = pointsNumber.intValue();
                    activityMainBinding.textViewCenter.setText(String.valueOf(points));
                } else {
                    activityMainBinding.textViewCenter.setText("0"); // Default to 0 if no points
                }
            } else {
                Log.d("Error", "No such document with the current user id: " + userId);
                activityMainBinding.textViewUserName.setText("User not found");
                activityMainBinding.textViewCenter.setText("0");
            }
        }).addOnFailureListener(e -> Log.d(TAG, "Error fetching document", e));
    }


    public boolean[] getExerciseDays(){
        SharedPreferences sharedPreferences = getSharedPreferences("ExerciseDays", Context.MODE_PRIVATE);
        boolean[] days = new boolean[7];
        for (int i = 0; i < 7; i++) {
            days[i] = sharedPreferences.getBoolean("day_" + i, false);
        }
        return days;
    }

    public String determineDayToShowProgramInUpcoming() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        boolean[] exerciseDays = getExerciseDays();

        int currentDayIndex = dayOfWeek.getValue(); // Day of week as 1 (Monday) to 7 (Sunday)

        for (int i = 0; i < 7; i++) {
            if (exerciseDays[(currentDayIndex + i) % 7]) {
                return dayOfWeekFromIndex(currentDayIndex + i);
            }
        }
        return dayOfWeekFromIndex(currentDayIndex);
    }

    private String dayOfWeekFromIndex(int index) {
        switch (index % 7 + 1) { // Modulo 7 and shift by 1 to match DayOfWeek values
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            case 7:
                return "Sunday";
            default:
                return "";
        }
    }


}