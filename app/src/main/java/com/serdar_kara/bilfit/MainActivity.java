package com.serdar_kara.bilfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.serdar_kara.bilfit.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FirebaseFirestore db;
    private DocumentReference documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        setVariablesForUser();

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

    private void setVariablesForUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


    }


}