package com.serdar_kara.bilfit.exercises;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ExerciseListActivityBaseModelBinding;
import com.serdar_kara.bilfit.databinding.ExerciseListBaseModelBinding;

import java.util.ArrayList;

public class ExerciseEditAdapter extends RecyclerView.Adapter<ExerciseEditAdapter.ExerciseViewHolder>{

    private ArrayList<ExerciseModel> exerciseList;

    public ExerciseEditAdapter(ArrayList<ExerciseModel> exerciseList) {
        this.exerciseList = exerciseList;
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
