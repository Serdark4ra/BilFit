package com.serdar_kara.bilfit;

import static android.content.ContentValues.TAG;
import static android.content.ContentValues.TAG;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;


import java.net.DatagramPacket;
import java.util.ArrayList;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.serdar_kara.bilfit.ProgramActivity.ProgramActivity;
import com.serdar_kara.bilfit.Settings.SettingsActivity;

import com.serdar_kara.bilfit.databinding.ActivityMainBinding;
import com.serdar_kara.bilfit.databinding.ActivityReportBinding;
import com.serdar_kara.bilfit.databinding.ActivitySettingsBinding;

import com.serdar_kara.bilfit.exercises.ExerciseAdapter;
import com.serdar_kara.bilfit.exercises.ExerciseModel;
import com.serdar_kara.bilfit.friends.FriendsActivity;
import com.serdar_kara.bilfit.get_info_activities.UserInfoHolder;
import com.serdar_kara.bilfit.get_info_activities.UserInfoManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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
import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityMainBinding;
import com.serdar_kara.bilfit.exercises.ExerciseAdapter;
import com.serdar_kara.bilfit.exercises.ExerciseModel;

import java.util.ArrayList;


public class ReportActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private int thisWeekPoints = 0;
    private int lastWeekPoints = 0;
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private ArrayList<ExerciseModel> exerciseList;
    private int goalpoints = 10000;


    private ActivityReportBinding binding;

    private ExerciseAdapter exerciseAdapter;
    TextView caloriesText;
    TextView cancerText;
    TextView heartAttackText;

    TextView daysToGoalText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarReport.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ReportActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        daysToGoalText = findViewById(R.id.textView15);
        caloriesText = binding.caloriesText;
        cancerText = binding.cancerText;
        heartAttackText = binding.heartAttackText;
        findCurrentUserPlace();
        setTexts(currentUser.getUid());
    }

    private void setTexts(String userId) {
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Users").document(userId);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve and display name
                System.out.println("MERHABA REPORT ACTIVITY");
                // Retrieve and display points
                Number pointsNumber = documentSnapshot.getLong("points");
                if (pointsNumber != null) {  // Check if the points data exists
                    int points = pointsNumber.intValue();
                    double n1 = (double) points / 30000.0;
                    double n2 = (double) points / 25000.0;


                    String formattedN1 = String.format("%.6f", n1);
                    String formattedN2 = String.format("%.6f", n2);

                    caloriesText.setText("Calories have burned so far: " + points * 3);
                    cancerText.setText("You've reduced your risk of getting cancer by this much: %" + formattedN1);
                    heartAttackText.setText("You've reduced your risk of having a heart attack by this much: %" + formattedN2);

                } else {
                    caloriesText.setText("Calories have burned so far: " + 0);; // Default to 0 if no points
                    cancerText.setText("You've reduced your risk of getting cancer by this much: " + 0);
                    heartAttackText.setText("You've reduced your risk of having a heart attack by this much: " + 0);
                }
            } else {
                Log.d("Error", "No such document with the current user id: " + userId);
            }
        }).addOnFailureListener(e -> Log.d(TAG, "Error fetching document", e));
    }
    private void findCurrentUserPlace() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("Users")
                    .orderBy("points", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            int currentUserRank = 0;
                            int totalUsers = documents.size();
                            for (int i = 0; i < documents.size(); i++) {
                                if (documents.get(i).getId().equals(userId)) {
                                    currentUserRank = i + 1; // Rank starts from 1
                                    break;
                                }
                            }
                            double percentage = 100.0 - ((double) currentUserRank / totalUsers * 100);
                            String message = "Congratulations! You have exercised more than " + String.format(Locale.getDefault(), "%.2f", percentage) + "% of Bilfit users.";
                            // Şimdi bu mesajı bir TextView'e yazdıralım. Örneğin:
                            daysToGoalText.setText(message);
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        }
    }

}