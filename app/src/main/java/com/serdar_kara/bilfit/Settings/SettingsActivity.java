package com.serdar_kara.bilfit.Settings;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.serdar_kara.bilfit.FeedbackActivity;
import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.ProgramActivity.DaysFragment;
import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.algorithm.Exercises;
import com.serdar_kara.bilfit.algorithm.Tester;
import com.serdar_kara.bilfit.databinding.ActivitySettingsBinding;
import com.serdar_kara.bilfit.exercises.ExerciseModel;
import com.serdar_kara.bilfit.get_info_activities.GenderActivity;
import com.serdar_kara.bilfit.get_info_activities.UserInfoHolder;
import com.serdar_kara.bilfit.login_activities.ForgotPasswordActivity;
import com.serdar_kara.bilfit.login_activities.SignUpActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import org.checkerframework.checker.units.qual.A;

public class SettingsActivity extends AppCompatActivity {
    ArrayList<ArrayList<Exercises>> program;

    private ArrayList<String> exerciseList;

    private static final String TAG = "SettingsActivity";

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private DocumentReference documentReference;


    private ActivitySettingsBinding binding;
    private TextView username;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private Tester t;
    private String currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        t = new Tester();

        DaysFragment d = new DaysFragment();
        System.out.println("REGENERATE ICIN current DAY: " + currentDay);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        username = findViewById(R.id.textView_user_name);
        updateUsernameTextView();

        /*retrieveProgramFromDatabase(currentUser.getUid(), "Monday");
        retrieveProgramFromDatabase(currentUser.getUid(), "Tuesday");
        retrieveProgramFromDatabase(currentUser.getUid(), "Wednesday");
        retrieveProgramFromDatabase(currentUser.getUid(), "Thursday");*/

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



        binding.buttonChangeProgram.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, GenderActivity.class);
            startActivity(intent);
        });

        binding.buttonRegenerate.setOnClickListener(v -> {
            Toast.makeText(this, "Your program is rearranged according to your feedbacks", Toast.LENGTH_LONG).show();
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = currentUser.getUid();

            DocumentReference userDocRef = db.collection("Users").document(userId);
            final double[] currentPower = {0};
            // Get the current power value without updating it
            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    currentPower[0] = documentSnapshot.getDouble("power");
                } else {
                    System.out.println("there is an error in regenerate method");
                }
                System.out.println("current power degeri: ");
                System.out.println(currentPower[0]);

                for (int j = 0; j < 7; j++)
                {
                    //ArrayList<String> exercises = retrieveProgramFromDatabase(currentUser.getUid(), "Thursday");
                    int finalJ = j;
                    retrieveProgramFromDatabase(currentUser.getUid(), days[j], exerciseList -> {
                        // Do something with the retrieved exercise list
                        ArrayList<String> exercises = exerciseList;

                        System.out.println("BAKAlIM GETEXERCISE NE DONDURMUS...");
                        for (String s : exercises)
                        {
                            System.out.println(s);
                        }

                        for (int k = 0; k < 1; k++)
                        {
                            String exerciseName = Tester.getSuitibleExercisesAccoringToPower(exercises.get(k), currentPower[0]);
                            System.out.println("BAKAlIM ICERDEKI FORDA EXERCISENAME NE IMIS?????...");
                            System.out.println(exerciseName);
                            updateExerciseInArray(days[finalJ], k, exerciseName);
                        }
                    });

                }

            });
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


    public void updateExerciseInArray(String day, int exerciseIndex, String newExerciseName) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        System.out.println("REGENERATE ICIN");

        if (currentUser == null) {
            Log.d(TAG, "No user logged in");
            System.out.println("REGENERATE ICIN 1111");
            return; // Early return if the user is not logged in
        }

        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(currentUser.getUid());

        // Fetch the current array
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            System.out.println("REGENERATE ICIN 2222");
            if (documentSnapshot.exists()) {
                Map<String, Object> data = (Map<String, Object>) documentSnapshot.getData();
                System.out.println("REGENERATE ICIN 33333");
                if (data != null && data.containsKey("program")) {
                    System.out.println("REGENERATE ICIN 4444");
                    Map<String, Object> program = (Map<String, Object>) data.get("program");
                    if (program.containsKey(day)) {
                        System.out.println("REGENERATE ICIN 5555555");
                        List<String> exercises = new ArrayList<>((List<String>) program.get(day));
                        if (exerciseIndex >= 0 && exerciseIndex < exercises.size()) {
                            System.out.println("REGENERATE ICIN 6666666");
                            exercises.set(exerciseIndex, newExerciseName);
                            // Update the entire array for the day
                            docRef.update("program." + day, exercises)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Exercise updated successfully in " + day + " at index " + exerciseIndex))
                                    .addOnFailureListener(e -> Log.w(TAG, "Error updating exercise at " + day + " index " + exerciseIndex, e));
                        } else {
                            Log.d(TAG, "Invalid exercise index");
                        }
                    } else {
                        Log.d(TAG, day + " does not exist in the program");
                    }
                } else {
                    Log.d(TAG, "Program data is missing in user document");
                }
            } else {
                Log.d(TAG, "No user document exists for UID: " + currentUser.getUid());
            }
        }).addOnFailureListener(e -> Log.w(TAG, "Error retrieving user document", e));
    }

    private void retrieveProgramFromDatabase(String userId, String day, OnExerciseListFetchListener listener) {
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Users").document(userId);

        // Adding a snapshot listener to listen for real-time updates
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Map<String, Object> programData = (Map<String, Object>) documentSnapshot.get("program");
                    if (programData != null) {
                        ArrayList<String> program = (ArrayList<String>) programData.get(day);
                        if (program != null) {
                            ArrayList<String> exerciseList = new ArrayList<>();
                            for (String exercise : program) {
                                exerciseList.add(exercise);
                            }
                            listener.onExerciseListFetch(exerciseList); // Pass the exercise list to the listener
                        }
                    }
                } else {
                    Log.d("Error", "No such document with the current user id: " + userId);
                }
            }
        });
    }

    // Define an interface for the callback
    interface OnExerciseListFetchListener {
        void onExerciseListFetch(ArrayList<String> exerciseList);
    }

    /*private ArrayList<String> retrieveProgramFromDatabase(String userId, String day) {
        System.out.println("buraya da gir da");
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Users").document(userId);

        ArrayList<String> exerciseList = new ArrayList<String>();

        // Adding a snapshot listener to listen for real-time updates
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    System.out.println("ilk ife girdi...");
                    Map<String, Object> programData = (Map<String, Object>) documentSnapshot.get("program");

                    if (programData != null) {

                        System.out.println("ikinci ife girdi...");
                        // Get the program for the current day
                        ArrayList<String> program = (ArrayList<String>) programData.get(day);
                        if (program != null) {

                            System.out.println("ucuncu ife girdi...");
                            // Clear the exerciseList before adding new exercises
                            exerciseList.clear();
                            for (String exercise : program) {
                                exerciseList.add(exercise);

                                System.out.println("retrievedata metodunda exerciseList denen seye ekliyor ama ne ekliyor asagi satirda..");
                                System.out.println(exercise + " bunu eklemis acaba ne");
                            }

                            // Notify the adapter that the data set has changed
                            //exerciseAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Log.d("Error", "No such document with the current user id: " + userId);
                }
            }

        });

        for (String exercise : exerciseList)
        {
            System.out.println("dondurdugu sey: " + exercise);
        }
        return  exerciseList;

    }*/

/*    public List<String> getExerciseInArray(String day) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        final List<String>[] exercises = new List[1];
        if (currentUser == null) {
            Log.d(TAG, "No user logged in");
            return null; // Early return if the user is not logged in
        }

        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(currentUser.getUid());

        // Fetch the current array
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Map<String, Object> data = (Map<String, Object>) documentSnapshot.getData();
                if (data != null && data.containsKey("program")) {
                    Map<String, Object> program = (Map<String, Object>) data.get("program");
                    if (program.containsKey(day)) {
                        exercises[0] = new ArrayList<>((List<String>) program.get(day));

                    } else {
                        Log.d(TAG, day + " does not exist in the program");
                    }
                } else {
                    Log.d(TAG, "Program data is missing in user document");
                }
            } else {
                Log.d(TAG, "No user document exists for UID: " + currentUser.getUid());
            }
        }).addOnFailureListener(e -> Log.w(TAG, "Error retrieving user document", e));

        return exercises[0];
    }*/



}