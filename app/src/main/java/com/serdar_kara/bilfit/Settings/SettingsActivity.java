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
import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.databinding.ActivitySettingsBinding;
import com.serdar_kara.bilfit.get_info_activities.GenderActivity;
import com.serdar_kara.bilfit.login_activities.ForgotPasswordActivity;
import com.serdar_kara.bilfit.login_activities.SignUpActivity;

public class SettingsActivity extends AppCompatActivity {
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

        binding.buttonChangeProgram.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, GenderActivity.class);
            startActivity(intent);
        });

        binding.buttonLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(SettingsActivity.this, SignUpActivity.class);
            startActivity(intent);
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


}