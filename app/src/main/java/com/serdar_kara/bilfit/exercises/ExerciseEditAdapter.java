package com.serdar_kara.bilfit.exercises;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.algorithm.Tester;
import com.serdar_kara.bilfit.databinding.ExerciseListActivityBaseModelBinding;
import com.serdar_kara.bilfit.databinding.ExerciseListBaseModelBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ExerciseEditAdapter extends RecyclerView.Adapter<ExerciseEditAdapter.ExerciseViewHolder>{

    private ArrayList<ExerciseModel> exerciseList;
    private Context context;
    private String day;

    public ExerciseEditAdapter(ArrayList<ExerciseModel> exerciseList, Context context, String day) {
        this.exerciseList = exerciseList;
        this.context = context;
        this.day = day;
    }

    @NonNull
    @Override
    public ExerciseEditAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExerciseListActivityBaseModelBinding binding = ExerciseListActivityBaseModelBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ExerciseEditAdapter.ExerciseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.binding.textExerciseName.setText(exerciseList.get(position).getName());
        holder.binding.textExerciseSetReps.setText("3x12");
        holder.binding.imageExercise.setImageResource(R.drawable.exercise_icon_hummer_curl);
        holder.binding.imageEdit.setOnClickListener(view -> {
            showEditDialog(position);
        });
    }

    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select an Exercise");

        Tester tester = new Tester();
        // Example list of exercises. This should be dynamically loaded based on the muscle group or other criteria
        String[] exercises = Tester.returnAssociatedList(exerciseList.get(position).getName()).toArray(new String[0]);

        builder.setItems(exercises, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the exercise at the given position
                String selectedExercise = exercises[which];
                exerciseList.get(position).setName(selectedExercise);
                notifyItemChanged(position);

                // Update the exercise in the database
                updateExerciseInArray(day, position, selectedExercise);

            }
        });

        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private ArrayList<String> getExerciseList(int position) {

        ArrayList<String> exercises = new ArrayList<>();

        exercises.add("Exercise 1");
        exercises.add("Exercise 2");
        exercises.add("Exercise 3");
        return exercises;
    }

    public void updateExerciseInArray(String day, int exerciseIndex, String newExerciseName) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Log.d(TAG, "No user logged in");
            return; // Early return if the user is not logged in
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
                        List<String> exercises = new ArrayList<>((List<String>) program.get(day));
                        if (exerciseIndex >= 0 && exerciseIndex < exercises.size()) {
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







    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{
        private ExerciseListActivityBaseModelBinding binding;
        public ExerciseViewHolder(ExerciseListActivityBaseModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
