package com.serdar_kara.bilfit.exercises;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.algorithm.Tester;
import com.serdar_kara.bilfit.databinding.ExerciseListBaseModelBinding;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>{

    private ArrayList<ExerciseModel> exerciseList;
    private Tester t;

    public ExerciseAdapter(ArrayList<ExerciseModel> exerciseList) {
        this.exerciseList = exerciseList;

    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExerciseListBaseModelBinding binding = ExerciseListBaseModelBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ExerciseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        t = new Tester();
        holder.binding.textExerciseName.setText(exerciseList.get(position).getName());
        holder.binding.textExerciseSetReps.setText("3x12");

        if (exerciseList.get(position).getName().equalsIgnoreCase("squat") ||
                exerciseList.get(position).getName().equalsIgnoreCase("dumbbell squat") ||
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
            
        }


        /*if(Tester.isLegExercise(exerciseList.get(position).getName()))
        {

            System.out.println("9 MAYISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            holder.binding.imageExercise.setImageResource(R.drawable.leg_comprehensive);
        }
        else {
            System.out.println("eksersisin adi : " + exerciseList.get(position).getName());
            System.out.println("9 MAYISA GIRMEDI. .. .. . ");
            holder.binding.imageExercise.setImageResource(R.drawable.exercise_icon_push_up);
        }*/
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{
        private ExerciseListBaseModelBinding binding;
        public ExerciseViewHolder(ExerciseListBaseModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
