package com.serdar_kara.bilfit;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private TextView firstUserTextView;
    private TextView secondUserTextView;
    private TextView thirdUserTextView;
    private TextView fourthUserTextView;
    private TextView fifthUserTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        // Initialize TextViews
        firstUserTextView = findViewById(R.id.textView10);
        secondUserTextView = findViewById(R.id.textView11);
        thirdUserTextView = findViewById(R.id.textView12);
        fourthUserTextView = findViewById(R.id.textView13);
        fifthUserTextView = findViewById(R.id.textView14);

        // Get current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Call the method to update user points and display leaderboard
        getUsersWithHighPoints();
        Button back = findViewById(R.id.button_back_to_menu);
        back.setOnClickListener(v -> {
            finish();
        });
    }

    private void getUsersWithHighPoints() {
        if (currentUser == null) {
            Log.d(TAG, "User is not logged in.");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // First fetch all users' documents ordered by points
        db.collection("Users")
                .orderBy("points", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> userList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userName = document.getString("name_surname");
                            userList.add(userName);
                        }
                        // Update the TextViews with top 5 users
                        updateLeaderboardTextViews(userList);
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void updateLeaderboardTextViews(List<String> userList) {
        // Update the TextViews with top 5 users
        for (int i = 0; i < 5; i++) {
            if (i < userList.size()) {
                switch (i) {
                    case 0:
                        firstUserTextView.setText(userList.get(i));
                        break;
                    case 1:
                        secondUserTextView.setText(userList.get(i));
                        break;
                    case 2:
                        thirdUserTextView.setText(userList.get(i));
                        break;
                    case 3:
                        fourthUserTextView.setText(userList.get(i));
                        break;
                    case 4:
                        fifthUserTextView.setText(userList.get(i));
                        break;
                }
            }
        }
    }
}