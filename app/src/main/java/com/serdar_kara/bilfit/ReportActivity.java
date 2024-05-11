package com.serdar_kara.bilfit;

import static android.content.ContentValues.TAG;
import static android.content.ContentValues.TAG;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private ArrayList<ExerciseModel> exerciseList;
    private int index = 1;
    private ExerciseAdapter exerciseAdapter;
    TextView caloriesText;
    TextView cancerText;
    TextView heartAttackText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Button backToMainMenuButton = findViewById(R.id.button_back_to_menu);

        backToMainMenuButton.setOnClickListener(view -> {
            Intent intent = new Intent(ReportActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {


            caloriesText = findViewById(R.id.caloriesText);
            cancerText = findViewById(R.id.cancerText);
            heartAttackText = findViewById(R.id.heartAttackText);

            setTexts(currentUser.getUid());

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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

}