package com.serdar_kara.bilfit.exercises;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.algorithm.Tester;
import com.serdar_kara.bilfit.databinding.ExerciseListActivityBaseModelBinding;
import com.serdar_kara.bilfit.databinding.ExerciseListBaseModelBinding;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ExerciseEditAdapter extends RecyclerView.Adapter<ExerciseEditAdapter.ExerciseViewHolder>{

    private ArrayList<ExerciseModel> exerciseList;
    private Context context;

    public ExerciseEditAdapter(ArrayList<ExerciseModel> exerciseList, Context context) {
        this.exerciseList = exerciseList;
        this.context = context;
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

        // Example list of exercises. This should be dynamically loaded based on the muscle group or other criteria
        String[] exercises = getExerciseList(position).toArray(new String[0]);

        builder.setItems(exercises, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the exercise at the given position
                exercises[position] = (exercises[which]);
                notifyItemChanged(position);
            }
        });

        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<String> getExerciseList(int position) {

        ArrayList<String> exercises = new ArrayList<>();
        exercises = Tester.returnBicepsExercises(exerciseList.get(position).getName());
        if (exercises != null) {
            return exercises;
        }

        exercises = Tester.returnBicepsExercises(exerciseList.get(position).getName());
        if (exercises != null) {
            return exercises;
        }
        exercises.add("Exercise 1");
        exercises.add("Exercise 2");
        exercises.add("Exercise 3");
        return exercises;
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
