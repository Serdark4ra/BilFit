package com.serdar_kara.bilfit.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.databinding.ActivitySignUpBinding;
import com.serdar_kara.bilfit.get_info_activities.GenderActivity;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding signUpBinding;
    private FirebaseAuth auth;

    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = signUpBinding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null){
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        
        signUpBinding.signupButton.setOnClickListener(view1 -> createUser());
        signUpBinding.textViewSignIn.setOnClickListener(view1 -> goSignin());
    }

    private void goSignin() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    private void createUser() {
        String nameSurname = signUpBinding.editTextNameSurname.getText().toString();
        String email = signUpBinding.editTextEmail.getText().toString();
        String password = signUpBinding.editTextPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty() || nameSurname.isEmpty()){
            Toast.makeText(this,"fill in the blanks",Toast.LENGTH_LONG).show();
        }else{auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            currentUser = auth.getCurrentUser();
                            Log.d("Login Page","Create account Successful");
                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(intent);

                    }else{
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Login Page","Create account failed!");
                        }
                    }
                });

        }
        Intent intent = new Intent(this, GenderActivity.class);
        startActivity(intent);
    }


}