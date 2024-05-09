package com.serdar_kara.bilfit.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.serdar_kara.bilfit.FeedbackActivity;
import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.algorithm.Exercises;
import com.serdar_kara.bilfit.databinding.ActivitySettingsBinding;
import com.serdar_kara.bilfit.get_info_activities.GenderActivity;
import com.serdar_kara.bilfit.get_info_activities.UserInfoHolder;
import com.serdar_kara.bilfit.login_activities.ForgotPasswordActivity;
import com.serdar_kara.bilfit.login_activities.SignUpActivity;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
import com.serdar_kara.bilfit.get_info_activities.UserInfoHolder;
import com.serdar_kara.bilfit.get_info_activities.UserInfoManager;
public class SettingsActivity extends AppCompatActivity {



























    private static final String TAG = "SettingsActivity";

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private DocumentReference documentReference;


    private ActivitySettingsBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        binding.buttonChangePassword.setOnClickListener(v -> {
            sendResetMail();
            Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        binding.buttonChangePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ChangePhotoActivity.class);
            startActivity(intent);
        });

        binding.buttonRegenerate.setOnClickListener(v -> {

                // Fetch data from Firebase
                db = FirebaseFirestore.getInstance();
                auth = FirebaseAuth.getInstance();
                currentUser = auth.getCurrentUser();

                if (currentUser != null) {
                    DocumentReference userDocRef = db.collection("Users").document(currentUser.getUid());

                    userDocRef.get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    // Retrieve data from document snapshot
                                    String gender = documentSnapshot.getString("gender");
                                    boolean chest = documentSnapshot.getBoolean("chest");
                                    boolean back = documentSnapshot.getBoolean("back");
                                    boolean arm = documentSnapshot.getBoolean("arm");
                                    boolean leg = documentSnapshot.getBoolean("leg");
                                    int age = documentSnapshot.getLong("age").intValue();
                                    int weight = documentSnapshot.getLong("weight").intValue();
                                    int height = documentSnapshot.getLong("height").intValue();
                                    boolean isMondayEligible = documentSnapshot.getBoolean("isMondayEligible");
                                    boolean isTuesdayEligible = documentSnapshot.getBoolean("isTuesdayEligible");
                                    boolean isWednesdayEligible = documentSnapshot.getBoolean("isWednesdayEligible");
                                    boolean isThursdayEligible = documentSnapshot.getBoolean("isThursdayEligible");
                                    boolean isFridayEligible = documentSnapshot.getBoolean("isFridayEligible");
                                    boolean isSaturdayEligible = documentSnapshot.getBoolean("isSaturdayEligible");
                                    boolean isSundayEligible = documentSnapshot.getBoolean("isSundayEligible");
                                    String purpose = documentSnapshot.getString("purpose");
                                    String bodyType = documentSnapshot.getString("bodyType");
                                    int pushupCount = documentSnapshot.getLong("pushupCount").intValue();

                                    // Create UserInfoHolder with retrieved data
                                    UserInfoHolder userInfoHolder = new UserInfoHolder(gender,
                                            chest,
                                            back,
                                            arm,
                                            leg,
                                            age,
                                            weight,
                                            height,
                                            isMondayEligible,
                                            isTuesdayEligible,
                                            isWednesdayEligible,
                                            isThursdayEligible,
                                            isFridayEligible,
                                            isSaturdayEligible,
                                            isSundayEligible,
                                            purpose,
                                            bodyType,
                                            pushupCount);
                                    // Add other variables to constructor similarly

                                    // Call regenerateWorkoutProgram with the created UserInfoHolder instance
                                    regenerateWorkoutProgram(userInfoHolder);
                                } else {
                                    Log.d(TAG, "Document does not exist");
                                }
                            }) //test altan bedri
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error fetching document", e);
                                // Handle failure
                            });

                    private void putProgramToDatabase(ArrayList<ArrayList<Exercises>> program, boolean[] days) {
                        db = FirebaseFirestore.getInstance();
                        auth = FirebaseAuth.getInstance();
                        currentUser = auth.getCurrentUser();
                        documentReference = db.collection("Users").document(currentUser.getUid());

                        // Convert the program ArrayList<ArrayList<Exercises>> to a format Firestore understands
                        // For example, you can use a HashMap to represent the program
                        HashMap<String, Object> programData = convertProgramToHashMap(program, days);

                        // Update the document with the program data
                        documentReference.update("program", programData)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Program data added successfully"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error adding program data", e));
                    }
                } else {
                    Log.w(TAG, "Current user is null");
                }

        });

        binding.buttonChangeProgram.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, GenderActivity.class);
            startActivity(intent);
        });

        binding.buttonRegenerate.setOnClickListener(v -> {
            Toast.makeText(this, "Your program is rearranged according to your feedbakcs", Toast.LENGTH_LONG).show();
        });

        binding.buttonLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(SettingsActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        binding.toolbarSettings.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void sendResetMail() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();
        if (email == null) {
            Toast.makeText(this, "fill in the blanks", Toast.LENGTH_LONG).show();
            return;
        }else{
            mAuth.sendPasswordResetEmail(email);
        }
    }

    public void regenerateWorkoutProgram(UserInfoHolder userInfoHolder)
    {
        userInfoHolder.regenerateWorkoutProgram();
    }

    private HashMap<String, Object> convertProgramToHashMap(ArrayList<ArrayList<Exercises>> program, boolean[] days) {
        HashMap<String, Object> programData = new HashMap<>();
        String[] gunler = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        int k = 0;
        for (int i = 0; i < program.size(); i++) {
            boolean gunBulundu = true;
            String currentDay = "";
            for (; gunBulundu && k < 7; k++) {
                if (days[k]) {
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