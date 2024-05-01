package com.serdar_kara.bilfit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class DaysPagerAdapter extends FragmentStateAdapter {
    private final List<String> daysList;
    private final Map<String, List<Exercise>> exercisesByDay;

    public DaysPagerAdapter(Fragment fragment, List<String> daysList, Map<String, List<Exercise>> exercisesByDay) {
        super(fragment);
        this.daysList = daysList;
        this.exercisesByDay = exercisesByDay;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        List<Exercise> exercises = exercisesByDay.getOrDefault(daysList.get(position), new ArrayList<>());
        return DayFragment.newInstance(exercises);
    }

    @Override
    public int getItemCount() {
        return daysList.size();
    }
}
