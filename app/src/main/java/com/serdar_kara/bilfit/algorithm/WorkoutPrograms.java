package com.serdar_kara.bilfit.algorithm;

import java.util.ArrayList;

public class WorkoutPrograms {
    
    ArrayList<ArrayList<Exercises>> ikiGunluk;
    ArrayList<ArrayList<Exercises>> ucGunluk;
    ArrayList<ArrayList<Exercises>> dortGunluk;
    ArrayList<ArrayList<Exercises>> besGunluk;

    public WorkoutPrograms()
    {
        ikiGunlukOlusturucu();
        ucGunlukOlusturucu();
        dortGunlukOlusturucu();
        besGunlukOlusturucu();
    }

    private void ikiGunlukOlusturucu()
    {
        ikiGunluk = new ArrayList<>();
        ikiGunluk.add(new ArrayList<>());

        ikiGunluk.get(0).add(new BackExercises(0, null));
        ikiGunluk.get(0).add(new BicepsExercises(0, null));
        ikiGunluk.get(0).add(new ShoulderExercises(0, null));
        ikiGunluk.get(0).add(new LegExercises(0, null));

        ikiGunluk.add(new ArrayList<>());
        ikiGunluk.get(1).add(new ChestExercises(0, null));
        ikiGunluk.get(1).add(new TricepsExercises(0, null));
        ikiGunluk.get(1).add(new BackExercises(0, null));
        ikiGunluk.get(1).add(new LegExercises(0, null));
    }

    private void ucGunlukOlusturucu()
    {
        ucGunluk = new ArrayList<>();
        ucGunluk.add(new ArrayList<>()); 
        ucGunluk.get(0).add(new LegExercises(0, null));
        ucGunluk.get(0).add(new ChestExercises(0, null));
        ucGunluk.get(0).add(new BackExercises(0, null));

        ucGunluk.add(new ArrayList<>()); 
        ucGunluk.get(1).add(new LegExercises(0, null));
        ucGunluk.get(1).add(new ShoulderExercises(0, null));
        ucGunluk.get(1).add(new BackExercises(0, null));
        ucGunluk.get(1).add(new TricepsExercises(0, null));

        ucGunluk.add(new ArrayList<>());
        ucGunluk.get(2).add(new LegExercises(0, null));
        ucGunluk.get(2).add(new ChestExercises(0, null));
        ucGunluk.get(2).add(new BackExercises(0, null));
        ucGunluk.get(2).add(new BicepsExercises(0, null));
    }

    private void dortGunlukOlusturucu()
    {
        dortGunluk = new ArrayList<>();
        dortGunluk.add(new ArrayList<>());
        dortGunluk.get(0).add(new BackExercises(0, null));
        dortGunluk.get(0).add(new BicepsExercises(0, null));
        dortGunluk.get(0).add(new ShoulderExercises(0, null));
        dortGunluk.get(0).add(new LegExercises(0, null));

        dortGunluk.add(new ArrayList<>());
        dortGunluk.get(1).add(new ChestExercises(0, null));
        dortGunluk.get(1).add(new TricepsExercises(0, null));
        dortGunluk.get(1).add(new BackExercises(0, null));
        dortGunluk.get(1).add(new LegExercises(0, null));
    
        dortGunluk.add(new ArrayList<>());
        dortGunluk.get(2).add(new BackExercises(0, null));
        dortGunluk.get(2).add(new BicepsExercises(0, null));
        dortGunluk.get(2).add(new ShoulderExercises(0, null));
        dortGunluk.get(2).add(new LegExercises(0, null));

        dortGunluk.add(new ArrayList<>()); 
        dortGunluk.get(3).add(new ChestExercises(0, null));
        dortGunluk.get(3).add(new TricepsExercises(0, null));
        dortGunluk.get(3).add(new BackExercises(0, null));
        dortGunluk.get(3).add(new LegExercises(0, null));
    }

    private void besGunlukOlusturucu()
    {
        besGunluk = new ArrayList<>();
        besGunluk.add(new ArrayList<>());
        besGunluk.get(0).add(new BackExercises(0, null));
        besGunluk.get(0).add(new BackExercises(0, null));
        besGunluk.get(0).add(new BicepsExercises(0, null));
        besGunluk.get(0).add(new BicepsExercises(0, null));

        besGunluk.add(new ArrayList<>());
        besGunluk.get(1).add(new ChestExercises(0, null));
        besGunluk.get(1).add(new ChestExercises(0, null));
        besGunluk.get(1).add(new TricepsExercises(0, null));
        besGunluk.get(1).add(new ShoulderExercises(0, null));
    
        besGunluk.add(new ArrayList<>());
        besGunluk.get(2).add(new LegExercises(0, null));
        besGunluk.get(2).add(new LegExercises(0, null));
        besGunluk.get(2).add(new BicepsExercises(0, null));

        besGunluk.add(new ArrayList<>()); 
        besGunluk.get(3).add(new BackExercises(0, null));
        besGunluk.get(3).add(new ShoulderExercises(0, null));
        besGunluk.get(3).add(new ChestExercises(0, null));

        besGunluk.add(new ArrayList<>());
        besGunluk.get(4).add(new LegExercises(0, null));
        besGunluk.get(4).add(new LegExercises(0, null));
        besGunluk.get(4).add(new TricepsExercises(0, null));
    }

    public ArrayList<ArrayList<Exercises>> getIkiGunluk() {
        return this.ikiGunluk;
    }

    public ArrayList<ArrayList<Exercises>> getUcGunluk() {
        return this.ucGunluk;
    }

    public ArrayList<ArrayList<Exercises>> getDortGunluk() {
        return this.dortGunluk;
    }

    public ArrayList<ArrayList<Exercises>> getBesGunluk() {
        return this.besGunluk;
    }
}
