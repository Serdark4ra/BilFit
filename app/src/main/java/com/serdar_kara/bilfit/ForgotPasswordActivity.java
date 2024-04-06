package com.serdar_kara.bilfit;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.serdar_kara.bilfit.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding activityForgotPasswordBinding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForgotPasswordBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(activityForgotPasswordBinding.getRoot());

        activityForgotPasswordBinding.passResetButton.setOnClickListener(view -> sendResetMail());
    }

    private void sendResetMail() {
        String email = activityForgotPasswordBinding.editTextEmailForgotPass.getText().toString();

        auth = FirebaseAuth.getInstance();
        if (!email.isEmpty()){

        }

    }
}