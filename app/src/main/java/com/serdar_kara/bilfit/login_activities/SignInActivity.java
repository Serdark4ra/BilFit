package com.serdar_kara.bilfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.serdar_kara.bilfit.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding activitySignInBinding;
    private FirebaseAuth auth;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(activitySignInBinding.getRoot());


        auth = FirebaseAuth.getInstance();


        activitySignInBinding.loginButton.setOnClickListener(view -> login());

        activitySignInBinding.textViewForgetPass.setOnClickListener(view -> goForgotPass());
    }

    private void goForgotPass() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void login() {
        String email = activitySignInBinding.editTextEmailSignIn.getText().toString();
        String password = activitySignInBinding.editTextPasswordSignIn.getText().toString();

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"fill in the blanks",Toast.LENGTH_LONG).show();
        }else{auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            currentUser = auth.getCurrentUser();
                            Log.d("Login Page","login  Successful");
                            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Login Page","login failed!");
                        }
                    }
                });

        }
    }
}