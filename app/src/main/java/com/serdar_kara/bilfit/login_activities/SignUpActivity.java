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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.databinding.ActivitySignUpBinding;
import com.serdar_kara.bilfit.get_info_activities.GenderActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding signUpBinding;
    private FirebaseAuth auth;

    private FirebaseUser currentUser;

    private FirebaseFirestore db;
    private DocumentReference documentReference;
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
        String name_surname = signUpBinding.editTextNameSurname.getText().toString();

        if (email.isEmpty() || password.isEmpty() || nameSurname.isEmpty()){
            Toast.makeText(this,"fill in the blanks",Toast.LENGTH_LONG).show();
        }else{auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            currentUser = auth.getCurrentUser();
                            Log.d("Login Page","Create account Successful");

                            db = FirebaseFirestore.getInstance();
                            documentReference = db.collection("Users").document(currentUser.getUid());

                            HashMap userInfo = new HashMap();
                            userInfo.put("name_surname",name_surname);
                            userInfo.put("email",email);
                            userInfo.put("friends", new ArrayList<String>());
                            userInfo.put("friendRequests", new ArrayList<String>());
                            userInfo.put("points",0);
                            userInfo.put("power",0.0);

                            if (currentUser != null){
                                documentReference.set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Log.d("Login Page","User Info Saved");
                                        }else{
                                            Log.d("Login Page","User Info Not Saved");
                                        }
                                    }
                                });
                            }
                            Intent intent = new Intent(SignUpActivity.this,GenderActivity.class);
                            startActivity(intent);

                    }else{
                            Toast.makeText(SignUpActivity.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Login Page","Create account failed!");
                        }
                    }
                });

        }
    }


}