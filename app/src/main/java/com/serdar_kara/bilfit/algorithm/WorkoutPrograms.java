package com.serdar_kara.bilfit.algorithm;

import java.util.ArrayList;

public class WorkoutPrograms {

    ArrayList<ArrayList<Exercises>>[] buildMusclePrograms;
    ArrayList<ArrayList<Exercises>> twoDaysMuscle;
    ArrayList<ArrayList<Exercises>> threeDaysMuscle;
    ArrayList<ArrayList<Exercises>> fourDaysMuscle;
    ArrayList<ArrayList<Exercises>> fiveDaysMuscle;

    ArrayList<ArrayList<Exercises>>[] cardioPrograms;
    ArrayList<ArrayList<Exercises>> twoDaysCardio;
    ArrayList<ArrayList<Exercises>> threeDaysCardio;
    ArrayList<ArrayList<Exercises>> fourDaysCardio;
    ArrayList<ArrayList<Exercises>> fiveDaysCardio;

    ArrayList<ArrayList<Exercises>>[] mixedPrograms;
    ArrayList<ArrayList<Exercises>> twoDaysMixed;
    ArrayList<ArrayList<Exercises>> threeDaysMixed;
    ArrayList<ArrayList<Exercises>> fourDaysMixed;
    ArrayList<ArrayList<Exercises>> fiveDaysMixed;

    public WorkoutPrograms()
    {
        twoDaysBuildMuscleProgramGenerator();
        threeDaysBuildMuscleProgramGenerator();
        fourDaysBuildMuscleProgramGenerator();
        fiveDaysBuildMuscleProgramGenerator();

        buildMusclePrograms = new ArrayList[4];
        buildMusclePrograms[0] = twoDaysMuscle;
        buildMusclePrograms[1] = threeDaysMuscle;
        buildMusclePrograms[2] = fourDaysMuscle;
        buildMusclePrograms[3] = fiveDaysMuscle;

        twoDaysCardioGenerator();
        threeDaysCardioGenerator();
        fourDaysCardioGenerator();
        fiveDaysCardioGenerator();

        cardioPrograms = new ArrayList[4];
        cardioPrograms[0] = twoDaysCardio;
        cardioPrograms[1] = threeDaysCardio;
        cardioPrograms[2] = fourDaysCardio;
        cardioPrograms[3] = fiveDaysCardio;

        //buraya da mixed program olusturan metodlar gelecek...

        mixedPrograms = new ArrayList[4];
        mixedPrograms[0] = twoDaysMixed;
        mixedPrograms[1] = threeDaysMixed;
        mixedPrograms[2] = fourDaysMixed;
        mixedPrograms[3] = fiveDaysMixed;
    }

    private void twoDaysBuildMuscleProgramGenerator()
    {
        twoDaysMuscle = new ArrayList<>();
        twoDaysMuscle.add(new ArrayList<>());

        twoDaysMuscle.get(0).add(new BackExercises(0, null));
        twoDaysMuscle.get(0).add(new BicepsExercises(0, null));
        twoDaysMuscle.get(0).add(new ShoulderExercises(0, null));
        twoDaysMuscle.get(0).add(new LegExercises(0, null));

        twoDaysMuscle.add(new ArrayList<>());
        twoDaysMuscle.get(1).add(new ChestExercises(0, null));
        twoDaysMuscle.get(1).add(new TricepsExercises(0, null));
        twoDaysMuscle.get(1).add(new BackExercises(0, null));
        twoDaysMuscle.get(1).add(new LegExercises(0, null));
    }

    private void threeDaysBuildMuscleProgramGenerator()
    {
        threeDaysMuscle = new ArrayList<>();
        threeDaysMuscle.add(new ArrayList<>());
        threeDaysMuscle.get(0).add(new LegExercises(0, null));
        threeDaysMuscle.get(0).add(new ChestExercises(0, null));
        threeDaysMuscle.get(0).add(new BackExercises(0, null));

        threeDaysMuscle.add(new ArrayList<>());
        threeDaysMuscle.get(1).add(new LegExercises(0, null));
        threeDaysMuscle.get(1).add(new ShoulderExercises(0, null));
        threeDaysMuscle.get(1).add(new BackExercises(0, null));
        threeDaysMuscle.get(1).add(new TricepsExercises(0, null));

        threeDaysMuscle.add(new ArrayList<>());
        threeDaysMuscle.get(2).add(new LegExercises(0, null));
        threeDaysMuscle.get(2).add(new ChestExercises(0, null));
        threeDaysMuscle.get(2).add(new BackExercises(0, null));
        threeDaysMuscle.get(2).add(new BicepsExercises(0, null));
    }

    private void fourDaysBuildMuscleProgramGenerator()
    {
        fourDaysMuscle = new ArrayList<>();
        fourDaysMuscle.add(new ArrayList<>());
        fourDaysMuscle.get(0).add(new BackExercises(0, null));
        fourDaysMuscle.get(0).add(new BicepsExercises(0, null));
        fourDaysMuscle.get(0).add(new ShoulderExercises(0, null));
        fourDaysMuscle.get(0).add(new LegExercises(0, null));

        fourDaysMuscle.add(new ArrayList<>());
        fourDaysMuscle.get(1).add(new ChestExercises(0, null));
        fourDaysMuscle.get(1).add(new TricepsExercises(0, null));
        fourDaysMuscle.get(1).add(new BackExercises(0, null));
        fourDaysMuscle.get(1).add(new LegExercises(0, null));
    
        fourDaysMuscle.add(new ArrayList<>());
        fourDaysMuscle.get(2).add(new BackExercises(0, null));
        fourDaysMuscle.get(2).add(new BicepsExercises(0, null));
        fourDaysMuscle.get(2).add(new ShoulderExercises(0, null));
        fourDaysMuscle.get(2).add(new LegExercises(0, null));

        fourDaysMuscle.add(new ArrayList<>());
        fourDaysMuscle.get(3).add(new ChestExercises(0, null));
        fourDaysMuscle.get(3).add(new TricepsExercises(0, null));
        fourDaysMuscle.get(3).add(new BackExercises(0, null));
        fourDaysMuscle.get(3).add(new LegExercises(0, null));
    }

    private void fiveDaysBuildMuscleProgramGenerator()
    {
        fiveDaysMuscle = new ArrayList<>();
        fiveDaysMuscle.add(new ArrayList<>());
        fiveDaysMuscle.get(0).add(new BackExercises(0, null));
        fiveDaysMuscle.get(0).add(new BackExercises(0, null));
        fiveDaysMuscle.get(0).add(new BicepsExercises(0, null));
        fiveDaysMuscle.get(0).add(new BicepsExercises(0, null));

        fiveDaysMuscle.add(new ArrayList<>());
        fiveDaysMuscle.get(1).add(new ChestExercises(0, null));
        fiveDaysMuscle.get(1).add(new ChestExercises(0, null));
        fiveDaysMuscle.get(1).add(new TricepsExercises(0, null));
        fiveDaysMuscle.get(1).add(new ShoulderExercises(0, null));
    
        fiveDaysMuscle.add(new ArrayList<>());
        fiveDaysMuscle.get(2).add(new LegExercises(0, null));
        fiveDaysMuscle.get(2).add(new LegExercises(0, null));
        fiveDaysMuscle.get(2).add(new BicepsExercises(0, null));

        fiveDaysMuscle.add(new ArrayList<>());
        fiveDaysMuscle.get(3).add(new BackExercises(0, null));
        fiveDaysMuscle.get(3).add(new ShoulderExercises(0, null));
        fiveDaysMuscle.get(3).add(new ChestExercises(0, null));

        fiveDaysMuscle.add(new ArrayList<>());
        fiveDaysMuscle.get(4).add(new LegExercises(0, null));
        fiveDaysMuscle.get(4).add(new LegExercises(0, null));
        fiveDaysMuscle.get(4).add(new TricepsExercises(0, null));
    }

    private void twoDaysCardioGenerator()
    {
        twoDaysCardio = new ArrayList<>();

        twoDaysCardio.add(new ArrayList<>());
        twoDaysCardio.add(new ArrayList<>());

        cardioHelper(twoDaysCardio);
    }

    private void threeDaysCardioGenerator()
    {
        threeDaysCardio = new ArrayList<>();

        threeDaysCardio.add(new ArrayList<>());
        threeDaysCardio.add(new ArrayList<>());
        threeDaysCardio.add(new ArrayList<>());

        cardioHelper(threeDaysCardio);
    }

    private void fourDaysCardioGenerator()
    {
        fourDaysCardio = new ArrayList<>();

        fourDaysCardio.add(new ArrayList<>());
        fourDaysCardio.add(new ArrayList<>());
        fourDaysCardio.add(new ArrayList<>());
        fourDaysCardio.add(new ArrayList<>());

        cardioHelper(fourDaysCardio);
    }

    private void fiveDaysCardioGenerator()
    {
        fiveDaysCardio = new ArrayList<>();

        fiveDaysCardio.add(new ArrayList<>());
        fiveDaysCardio.add(new ArrayList<>());
        fiveDaysCardio.add(new ArrayList<>());
        fiveDaysCardio.add(new ArrayList<>());
        fiveDaysCardio.add(new ArrayList<>());

        cardioHelper(fiveDaysCardio);
    }

    private void cardioHelper(ArrayList<ArrayList<Exercises>> exerciseList)
    {
        for (int i = 0; i < exerciseList.size(); i++)
        {
            exerciseList.get(i).add(new CardioExercises(0, null));
            exerciseList.get(i).add(new CardioExercises(0, null));
            exerciseList.get(i).add(new CardioExercises(0, null));
        }
    }

    public ArrayList<ArrayList<Exercises>>[] getBuildMusclePrograms()
    {
        return buildMusclePrograms;
    }

    public ArrayList<ArrayList<Exercises>>[] getCardioPrograms()
    {
        return  this.cardioPrograms;
    }

    public ArrayList<ArrayList<Exercises>>[] getMixedPrograms()
    {
        return this.mixedPrograms;
    }

    public ArrayList<ArrayList<Exercises>> getTwoDaysMuscle() {
        return this.twoDaysMuscle;
    }

    public ArrayList<ArrayList<Exercises>> getThreeDaysMuscle() {
        return this.threeDaysMuscle;
    }

    public ArrayList<ArrayList<Exercises>> getFourDaysMuscle() {
        return this.fourDaysMuscle;
    }

    public ArrayList<ArrayList<Exercises>> getFiveDaysMuscle() {
        return this.fiveDaysMuscle;
    }
}
