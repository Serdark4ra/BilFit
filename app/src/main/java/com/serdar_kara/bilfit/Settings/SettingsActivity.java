package com.serdar_kara.bilfit.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.serdar_kara.bilfit.FeedbackActivity;
import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivitySettingsBinding;
import com.serdar_kara.bilfit.get_info_activities.GenderActivity;
import com.serdar_kara.bilfit.get_info_activities.UserInfoHolder;
import com.serdar_kara.bilfit.login_activities.ForgotPasswordActivity;
import com.serdar_kara.bilfit.login_activities.SignUpActivity;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

























    ArrayList<ArrayList<Exercises>> program;

    private static final String TAG = "SettingsActivity";

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private DocumentReference documentReference;


    private ActivitySettingsBinding binding;
    private TextView username;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        username = findViewById(R.id.textView_user_name);
        updateUsernameTextView();

        loadUserProfilePhoto();




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
           // regenerateWorkoutProgram(new UserInfoHolder()); //TODO buraya firebaseden tüm bilgiler gelcek
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
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

    private void loadUserProfilePhoto() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore.getInstance().collection("Users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists() && doc.contains("profileImage")) {
                                String photoUrl = doc.getString("profileImage");
                                if (photoUrl != null && !photoUrl.isEmpty()) {
                                    Glide.with(this)
                                            .load(photoUrl)
                                            .circleCrop() // if you want to apply circle cropping to the image
                                            .into(binding.imageViewUser);
                                }
                            }
                        } else {
                            Toast.makeText(this, "Failed to load user photo.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    public void regenerateWorkoutProgram(UserInfoHolder userInfoHolder, ArrayList<ArrayList<String>> program)
    {
        userInfoHolder.regenerateWorkoutProgram();
    }
    private void updateUsernameTextView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            String userName = document.getString("name_surname");
                            // TextView'a kullanıcı adını ayarla
                            username.setText(userName);
                        }
                    }
                });
    }






}