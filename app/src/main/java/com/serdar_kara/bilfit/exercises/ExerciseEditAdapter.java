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

    private Tester t;
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

        t = new Tester();

        holder.binding.textExerciseName.setText(exerciseList.get(position).getName());
        holder.binding.textExerciseSetReps.setText("3x12");
        //photos
        if (exerciseList.get(position).getName().equalsIgnoreCase("squat") ||
                exerciseList.get(position).getName().equalsIgnoreCase("dumbell squat") ||
                exerciseList.get(position).getName().equalsIgnoreCase("bulgarian split squat") ||
                exerciseList.get(position).getName().equalsIgnoreCase("hack squat") ||
                exerciseList.get(position).getName().equalsIgnoreCase("sumo squat"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.leg_squat);
        }
        else if (exerciseList.get(position).getName().equalsIgnoreCase("leg press"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.leg);
        }
        else if (exerciseList.get(position).getName().equalsIgnoreCase("leg curl") ||
                exerciseList.get(position).getName().equalsIgnoreCase("leg extension") ||
                exerciseList.get(position).getName().equalsIgnoreCase("calf raise") ||
                exerciseList.get(position).getName().equalsIgnoreCase("lunges") ||
                exerciseList.get(position).getName().equalsIgnoreCase("good mornings"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.legmachine);
        }
        else if (Tester.isLegExercise(exerciseList.get(position).getName()))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.leg_comprehensive);
        }
        else if (exerciseList.get(position).getName().contains("Deadlift"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.training);
        }
        else if (exerciseList.get(position).getName().contains("Row"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.back__1_);
        }
        else if (exerciseList.get(position).getName().equalsIgnoreCase("Pull-up"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.pull);
        }
        else if (Tester.isBackExercise(exerciseList.get(position).getName()))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.back__2_);
        }
        else if (Tester.isBicepsExercise(exerciseList.get(position).getName()))
        {
            if (exerciseList.get(position).getName().equalsIgnoreCase("barbell curl") ||
                    exerciseList.get(position).getName().equalsIgnoreCase("dumbbell curl") ||
                    exerciseList.get(position).getName().equalsIgnoreCase("preacher curl") ||
                    exerciseList.get(position).getName().equalsIgnoreCase("concentration curl") ||
                    exerciseList.get(position).getName().equalsIgnoreCase("cable curl") ||
                    exerciseList.get(position).getName().equalsIgnoreCase("spider curl"))
            {
                holder.binding.imageExercise.setImageResource(R.drawable.bicep_curl);
            }
            else
            {
                holder.binding.imageExercise.setImageResource(R.drawable.exercise_icon_hummer_curl);
            }
        }
        else if (Tester.isTricepsExercise(exerciseList.get(position).getName()))
        {
            if (exerciseList.get(position).getName().contains("Dip"))
            {
                holder.binding.imageExercise.setImageResource(R.drawable.workout__2_);
            }
            else if (exerciseList.get(position).getName().contains("Pushdown") || exerciseList.get(position).getName().contains("Extension"))
            {
                holder.binding.imageExercise.setImageResource(R.drawable.workout__1_);
            }
            else if (exerciseList.get(position).getName().contains("Kickback"))
            {
                holder.binding.imageExercise.setImageResource(R.drawable.exercise__1_);
            }
            else
            {
                holder.binding.imageExercise.setImageResource(R.drawable.triceps);
            }
        }

        else if (exerciseList.get(position).getName().contains("Swimming"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.swimming);
        }
        else if (exerciseList.get(position).getName().contains("Jumping Rope") ||
                exerciseList.get(position).getName().contains("Box Jumps") ||
                exerciseList.get(position).getName().contains("Jumping Jacks") ||
                exerciseList.get(position).getName().contains("Plyometric"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.jumping_rope);
        }
        else if (exerciseList.get(position).getName().contains("Rowing"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.rowing);
        }
        else if (exerciseList.get(position).getName().contains("Running") ||
                exerciseList.get(position).getName().contains("HIIT") ||
                exerciseList.get(position).getName().contains("Sprinting"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.running);
        }
        else if (exerciseList.get(position).getName().contains("Climbing"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.stair_climbing);
        }
        else if (exerciseList.get(position).getName().contains("Cycling") ||
                exerciseList.get(position).getName().contains("Circuit Training"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.cycling);
        }
        else if (exerciseList.get(position).getName().contains("Elliptical"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.elliptical);
        }
        else if (exerciseList.get(position).getName().contains("Burpees"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.burpee);
        }
        else if (exerciseList.get(position).getName().contains("Climbing")||
                (exerciseList.get(position).getName().contains("Mountain Climbers")))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.stair_climbing);
        }
        else if (exerciseList.get(position).getName().contains("Military Press") ||
                exerciseList.get(position).getName().contains("Arnold Press") ||
                exerciseList.get(position).getName().contains("Lateral Raise") ||
                exerciseList.get(position).getName().contains("Front Raise") ||
                exerciseList.get(position).getName().contains("Reverse Fly") ||
                exerciseList.get(position).getName().contains("Shrugs") ||
                exerciseList.get(position).getName().contains("Upright Row") ||
                exerciseList.get(position).getName().contains("Face Pull") ||
                exerciseList.get(position).getName().contains("Lateral (In machine)") ||
                exerciseList.get(position).getName().contains("Seated Dumbbell Press") ||
                exerciseList.get(position).getName().contains("High Pulls") ||
                exerciseList.get(position).getName().contains("Push Press") ||
                exerciseList.get(position).getName().contains("Dumbbell Fly") ||
                exerciseList.get(position).getName().contains("Cable Lateral Raise") ||
                exerciseList.get(position).getName().contains("Bent Over Lateral Raise") ||
                exerciseList.get(position).getName().contains("Barbell Front Raise") ||
                exerciseList.get(position).getName().contains("Machine Shoulder Press"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.shoulder_exercise);
        }
        else if (exerciseList.get(position).getName().contains("Bench Press") ||
                exerciseList.get(position).getName().contains("Incline Bench Press") ||
                exerciseList.get(position).getName().contains("Decline Bench Press") ||
                exerciseList.get(position).getName().contains("Dumbbell Press") ||
                exerciseList.get(position).getName().contains("Incline Dumbbell Press") ||
                exerciseList.get(position).getName().contains("Decline Dumbbell Press") ||
                exerciseList.get(position).getName().contains("Cable Fly") ||
                exerciseList.get(position).getName().contains("Machine Fly") ||
                exerciseList.get(position).getName().contains("Push-ups") ||
                exerciseList.get(position).getName().contains("Dips"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.chest_exercise1);
        }
        else if (exerciseList.get(position).getName().contains("Pec Deck") ||
                exerciseList.get(position).getName().contains("Chest Press Machine") ||
                exerciseList.get(position).getName().contains("Chest Squeeze"))
        {
            holder.binding.imageExercise.setImageResource(R.drawable.chest_exercise2);
        }
        else
        {
            holder.binding.imageExercise.setImageResource(R.drawable.woman__1_);
        }
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
