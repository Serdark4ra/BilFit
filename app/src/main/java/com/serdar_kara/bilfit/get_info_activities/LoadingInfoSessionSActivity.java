package com.serdar_kara.bilfit.get_info_activities;

import static android.content.ContentValues.TAG;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.algorithm.Exercises;
import com.serdar_kara.bilfit.databinding.ActivityLoadingInfoSessionSactivityBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoadingInfoSessionSActivity extends AppCompatActivity {

    private ActivityLoadingInfoSessionSactivityBinding binding;
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingInfoSessionSactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent comingIntent = getIntent();
        UserInfoHolder userInfoHolder = (UserInfoHolder) comingIntent.getSerializableExtra("userInfoHolder");
        UserInfoManager.getInstance().setUserInfo(userInfoHolder);

        ProgressBar progressBar = binding.progressBar;
        TextView loadingPercentage = binding.textViewLoadingPercentage;

        System.out.println("BURAYA GELDIIIIIIIIIIIIIIIIIIIII");
        userInfoHolder.setPower();
        ArrayList<ArrayList<Exercises>> program = userInfoHolder.getProgram();

        boolean[] days = userInfoHolder.getDays();
        putProgramToDatabase(program,days);
        userInfoHolder.saveExerciseDaysToThePhone(this, days);
        saveCompletedExercisesToThePhone(this);
        putPowerToDatabase(userInfoHolder);

        int completionPercentage = 20;
        progressBar.setProgress(completionPercentage);
        loadingPercentage.setText(completionPercentage + "%");

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        completionPercentage = 56;
        progressBar.setProgress(completionPercentage);
        loadingPercentage.setText(completionPercentage + "%");

        try {
            sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        Intent intent1 = new Intent(LoadingInfoSessionSActivity.this, MainActivity.class);
        startActivity(intent1);
    }

    private void putPowerToDatabase(UserInfoHolder userInfoHolder) {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            documentReference = db.collection("Users").document(currentUser.getUid());
            double power = userInfoHolder.getPower();

            documentReference.update("power", power)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Power updated successfully"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating power", e));
        } else {
            Log.w(TAG, "Current user is null");
        }
    }



    public void saveCompletedExercisesToThePhone(Context context) {
        String PREF_NAME = "CompletedExerciseDays";
        String KEY_PREFIX = "day_";
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < 7; i++) {
            editor.putBoolean(KEY_PREFIX + i, false);
        }
        editor.apply();
    }

    private void putProgramToDatabase(ArrayList<ArrayList<Exercises>> program, boolean[] days) {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        documentReference = db.collection("Users").document(currentUser.getUid());

        // Convert the program ArrayList<ArrayList<Exercises>> to a format Firestore understands
        // For example, you can use a HashMap to represent the program
        // Here, assuming you have a method to convert the program to a HashMap
        HashMap<String, Object> programData = convertProgramToHashMap(program, days);

        // Update the document with the program data
        documentReference.update("program", programData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Program data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding program data", e);
                    }
                });
    }

    // Method to convert the program ArrayList<ArrayList<Exercises>> to a HashMap
    private HashMap<String, Object> convertProgramToHashMap(ArrayList<ArrayList<Exercises>> program, boolean[] days) {
        HashMap<String, Object> programData = new HashMap<>();
        String[] gunler = {"Monday" , "Tuesday", "Wednesday", "Thursday" ,"Friday" , "Saturday", "Sunday"};
        int k = 0;
        for (int i = 0; i < program.size(); i++) {
            boolean gunBulundu = true;
            String currentDay = "";
            for (;gunBulundu && k < 7; k++)
            {
                if (days[k])
                {
                    currentDay = gunler[k];
                    gunBulundu = false;
                }
            }
            ArrayList<Exercises> dayProgram = program.get(i);
            ArrayList<String> exerciseNames = new ArrayList<>();
            for (Exercises exercise : dayProgram) {
                exerciseNames.add(exercise.getName());
            }
            // Add the list of exercise names for this day to the programData
            programData.put(currentDay, exerciseNames);
        }
        return programData;
    }


}