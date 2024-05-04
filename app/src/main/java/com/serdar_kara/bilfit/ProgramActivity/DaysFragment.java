package com.serdar_kara.bilfit.ProgramActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.exercises.ExerciseAdapter;
import com.serdar_kara.bilfit.exercises.ExerciseEditAdapter;
import com.serdar_kara.bilfit.exercises.ExerciseModel;

import java.util.ArrayList;
import java.util.List;

public class DaysFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ExerciseModel> exercises;

    public static DaysFragment newInstance(List<ExerciseModel> exercises, String day) {
        DaysFragment fragment = new DaysFragment();
        Bundle args = new Bundle();
        args.putSerializable("exercises", new ArrayList<>(exercises));
        args.putString("day", day);
        fragment.setArguments(args);
        return fragment;
    }

    private String currentDay;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exercises = (ArrayList<ExerciseModel>) getArguments().getSerializable("exercises");
            currentDay = getArguments().getString("day", ""); // Default to an empty string if not found
        } else {
            Log.d("DaysFragment", "No exercises or day found");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_days, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_exercise);
        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ExerciseEditAdapter(exercises, getActivity(), currentDay));
    }
}
