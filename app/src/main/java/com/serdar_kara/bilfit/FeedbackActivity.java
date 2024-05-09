package com.serdar_kara.bilfit;

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

public class FeedbackActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private DocumentReference documentReference;

    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            Toast.makeText(FeedbackActivity.this, "Something went wrong. Please try again",
                    Toast.LENGTH_SHORT).show();
            return "0000";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button submitButton = findViewById(R.id.submitButton);

        
        submitButton.setOnClickListener(view -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = currentUser.getUid();
            DocumentReference userDocRef = db.collection("Users").document(userId);
            RatingBar ratingBar1 = findViewById(R.id.ratingBar1);

            float rating = ratingBar1.getRating();
            double deltaPower = (rating - 3.0) / 100.0 * (-1.0);

            // Start a transaction to update the power value
            db.runTransaction((Transaction.Function<Void>) transaction -> {
                DocumentSnapshot snapshot = transaction.get(userDocRef);
                Double currentPower = snapshot.getDouble("power");
                if (currentPower == null) currentPower = 0.0; // default power if not set
                double newPower = currentPower + deltaPower;
                transaction.update(userDocRef, "power", newPower);
                return null; // This transaction does not return any result
            }).addOnSuccessListener(aVoid -> {
                Log.d("power", "Points updated successfully.");
                // Return to the main menu here
                Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the current activity
            }).addOnFailureListener(e -> {
                Log.e("power", "Error updating points.", e);
            });
        });

    }


}
