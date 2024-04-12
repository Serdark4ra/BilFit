package com.serdar_kara.bilfit.algorithm;

import java.util.ArrayList;
import java.util.Random;

public class Tester {

    static Random rnd;
    static ArrayList<Exercises> exercisesList;
    static ArrayList<Exercises> cardioExercises;
    public static void programDoldurucu(ArrayList<ArrayList<Exercises>> program, double power)
    {

        /*for (int i = 0; i < days.length; i ++)
        {
            if (days[i])
            {
                for (int j = 0; j < program[i].size(); j++)
                {
                    for (int k = 0; k < exercisesList.size(); k++)
                    {
                        if (exercisesList.get(k).getClass() == program[i].get(j).getClass()
                                && exercisesList.get(k).getZorluk() < power && program[i].get(j).getZorluk() == 0
                                    && !program[i].contains(exercisesList.get(k)))
                        {
                            program[i].set(j, exercisesList.get(k));
                            if (power > 1)
                            {
                                power = power - exercisesList.get(k).getZorluk() * 0.03;
                            }
                        }
                    }
                }
            }
        }*/

        for (int i = 0; i < program.size(); i++)
        {
            for (int j = 0; j < program.get(i).size(); j++)
            {
                for (int k = 0; k < exercisesList.size(); k++)
                {
                    if (exercisesList.get(k).getClass() == program.get(i).get(j).getClass()
                        && exercisesList.get(k).getZorluk() < power && program.get(i).get(j).getZorluk() == 0
                            && !program.get(i).contains(exercisesList.get(k)))
                    {
                        program.get(i).set(j, exercisesList.get(k));
                        if (power > 1.1)
                        {
                            power = power - exercisesList.get(k).getZorluk() * 0.03;
                        }
                    }
                }
            }
        }

        hareketleriKaristir();
    }

    public static void generateCardioWorkout()
    {

    }


    public static ChestExercises getAvailableChestExercise(double guc, ArrayList<Exercises> program)
    {
        ChestExercises h = new ChestExercises(0, null);

        for (int i = 0; i < exercisesList.size(); i++)
        {
            if (exercisesList.get(i).getClass() == h.getClass() && !program.contains(exercisesList.get(i)) && guc > exercisesList.get(i).getZorluk())
            {
                return (ChestExercises) exercisesList.get(i);
            }
        }
        return null;
    }

    public static BackExercises getAvailableBackExercise(double guc, ArrayList<Exercises> program)
    {
        BackExercises h = new BackExercises(0, null);

        for (int i = 0; i < exercisesList.size(); i++)
        {
            if (exercisesList.get(i).getClass() == h.getClass() && !program.contains(exercisesList.get(i))&& guc > exercisesList.get(i).getZorluk())
            {
                return (BackExercises) exercisesList.get(i);
            }
        }

        return null;
    }
    public static BicepsExercises getAvailableBicepsExercise(double guc, ArrayList<Exercises> program) 
    {
        BicepsExercises h = new BicepsExercises(0, null);

        for (int i = 0; i < exercisesList.size(); i++)
        {
            if (exercisesList.get(i).getClass() == h.getClass() && !program.contains(exercisesList.get(i))&& guc > exercisesList.get(i).getZorluk())
            {
                return (BicepsExercises) exercisesList.get(i);
            }
        }

        return null;
    }
    
    public static ShoulderExercises getAvailableOmuzExercise(double guc, ArrayList<Exercises> program) 
    {
        ShoulderExercises h = new ShoulderExercises(0, null);

        for (int i = 0; i < exercisesList.size(); i++)
        {
            if (exercisesList.get(i).getClass() == h.getClass() && !program.contains(exercisesList.get(i))&& guc > exercisesList.get(i).getZorluk())
            {
                return (ShoulderExercises) exercisesList.get(i);
            }
        }

        return null;
    }
    
    public static TricepsExercises getAvailableTricepsExercise(double guc, ArrayList<Exercises> program) 
    {
        TricepsExercises h = new TricepsExercises(0, null);

        for (int i = 0; i < exercisesList.size(); i++)
        {
            if (exercisesList.get(i).getClass() == h.getClass() && !program.contains(exercisesList.get(i)) && guc > exercisesList.get(i).getZorluk())
            {
                return (TricepsExercises) exercisesList.get(i);
            }
        }

        return null;
    }
    
    public static LegExercises getAvailableBacakExercise(double guc, ArrayList<Exercises> program) 
    {
        LegExercises h = new LegExercises(0, null);

        for (int i = 0; i < exercisesList.size(); i++)
        {
            if (exercisesList.get(i).getClass() == h.getClass() && !program.contains(exercisesList.get(i)) && guc > exercisesList.get(i).getZorluk())
            {
                return (LegExercises) exercisesList.get(i);
            }
        }

        return null;
    }

    public static void hareketleriKaristir()
    {
        rnd = new Random();
        for (int i = 0; i < 10000; i++)
        {
            int a = rnd.nextInt(exercisesList.size());
            int b = rnd.nextInt(exercisesList.size());

            //System.out.println("a: " + a + " , b: " + b);

            if (exercisesList.get(a).getClass() == exercisesList.get(b).getClass() &&
                exercisesList.get(a).getZorluk() == exercisesList.get(b).getZorluk())
            {
                //System.out.println("bunlarin yeri degistirildi");
                Exercises temp = exercisesList.get(a);
                exercisesList.set(a, exercisesList.get(b));
                exercisesList.set(b, temp);
            }
        }
    }

    public static void main(String[] args) {

        exercisesList = new ArrayList<Exercises>();
        cardioExercises = new ArrayList<Exercises>();

        //cardio exercises
        cardioExercises.add(new CardioExercises(4, "Running"));
        cardioExercises.add(new CardioExercises(3, "Cycling"));
        cardioExercises.add(new CardioExercises(5, "Jump Rope"));
        cardioExercises.add(new CardioExercises(3, "Swimming"));
        cardioExercises.add(new CardioExercises(5, "Breast Stroke Swimming"));
        cardioExercises.add(new CardioExercises(1, "Rowing Machine"));
        cardioExercises.add(new CardioExercises(1, "Elliptical Trainer"));
        cardioExercises.add(new CardioExercises(3, "Stair Climbing"));
        cardioExercises.add(new CardioExercises(5, "HIIT (High-Intensity Interval Training)"));
        cardioExercises.add(new CardioExercises(4, "Jumping Jacks"));
        cardioExercises.add(new CardioExercises(2, "Burpees"));
        cardioExercises.add(new CardioExercises(5, "Sprinting"));
        cardioExercises.add(new CardioExercises(3, "Mountain Climbers"));
        cardioExercises.add(new CardioExercises(4, "Jumping Rope"));
        cardioExercises.add(new CardioExercises(5, "Jump Squats"));
        cardioExercises.add(new CardioExercises(1, "Circuit Training"));
        cardioExercises.add(new CardioExercises(3, "Plyometric Exercises"));
        cardioExercises.add(new CardioExercises(4, "Trail Running"));
        cardioExercises.add(new CardioExercises(2, "Box Jumps"));

        //bacak hareketleri
        exercisesList.add(new LegExercises(5, "Squat"));
        exercisesList.add(new LegExercises(5, "Dumbell Squat"));
        exercisesList.add(new LegExercises(4, "Leg press"));
        exercisesList.add(new LegExercises(3, "Leg curl"));
        exercisesList.add(new LegExercises(2, "Leg Extension"));
        exercisesList.add(new LegExercises(1, "Dumbell Step up"));
        exercisesList.add(new LegExercises(3, "Yogun tempo kosu"));
        exercisesList.add(new LegExercises(1, "Hafif tempo kosu"));
        exercisesList.add(new LegExercises(4, "Lunges"));
        exercisesList.add(new LegExercises(3, "Bulgarian Split Squat"));
        exercisesList.add(new LegExercises(4, "Hack Squat"));
        exercisesList.add(new LegExercises(2, "Calf Raise"));
        exercisesList.add(new LegExercises(3, "Sumo Squat"));
        exercisesList.add(new LegExercises(1, "Good Mornings"));

        // Sırt Hareketleri
        exercisesList.add(new BackExercises(5, "Geleneksel Deadlift"));
        exercisesList.add(new BackExercises(5, "Pull-up"));
        exercisesList.add(new BackExercises(4, "Barbell Row"));
        exercisesList.add(new BackExercises(3, "Lat Pulldown"));
        exercisesList.add(new BackExercises(2, "Dumbbell Row"));
        exercisesList.add(new BackExercises(4, "T-Bar Row"));
        exercisesList.add(new BackExercises(1, "Cable Row"));
        exercisesList.add(new BackExercises(3, "Single Arm Dumbbell Row"));
        exercisesList.add(new BackExercises(2, "Seated Cable Row"));
        exercisesList.add(new BackExercises(3, "Bent Over Barbell Row"));
        exercisesList.add(new BackExercises(1, "Face Pulls"));
        exercisesList.add(new BackExercises(3, "Machine Row"));
        exercisesList.add(new BackExercises(4, "Romanian Deadlift"));
        exercisesList.add(new BackExercises(1, "Inverted Row"));
    
        // Biceps Hareketleri
        exercisesList.add(new BicepsExercises(5, "Barbell Curl"));
        exercisesList.add(new BicepsExercises(1, "Dumbbell Curl"));
        exercisesList.add(new BicepsExercises(3, "Preacher Curl"));
        exercisesList.add(new BicepsExercises(3, "Hammer Curl"));
        exercisesList.add(new BicepsExercises(5, "Concentration Curl"));
        exercisesList.add(new BicepsExercises(4, "Reverse Curl"));
        exercisesList.add(new BicepsExercises(3, "Cable Curl"));
        exercisesList.add(new BicepsExercises(2, "Spider Curl"));
        exercisesList.add(new BicepsExercises(2, "Alternating Hammer Curl"));
        exercisesList.add(new BicepsExercises(1, "Cable Hammer Curl"));
        exercisesList.add(new BicepsExercises(4, "Zottman Curl"));

        // Triceps Hareketleri
        exercisesList.add(new TricepsExercises(5, "Tricep Dip"));
        exercisesList.add(new TricepsExercises(4, "Skull Crusher"));
        exercisesList.add(new TricepsExercises(3, "Tricep Pushdown"));
        exercisesList.add(new TricepsExercises(5, "Overhead Tricep Extension"));
        exercisesList.add(new TricepsExercises(1, "Tricep Kickback"));
        exercisesList.add(new TricepsExercises(1, "Tricep Rope Pushdown"));
        exercisesList.add(new TricepsExercises(2, "Tricep Bench Dip"));
        exercisesList.add(new TricepsExercises(3, "Overhead Dumbbell Tricep Press"));
        exercisesList.add(new TricepsExercises(2, "Triceps Cable Kickback"));
        exercisesList.add(new TricepsExercises(5, "Dumbbell Triceps Extension"));
        exercisesList.add(new TricepsExercises(4, "Triceps Rope Overhead Extension"));
        
        // Omuz Hareketleri
        exercisesList.add(new ShoulderExercises(5, "Military Press"));
        exercisesList.add(new ShoulderExercises(5, "Arnold Press"));
        exercisesList.add(new ShoulderExercises(1, "Lateral Raise"));
        exercisesList.add(new ShoulderExercises(3, "Front Raise"));
        exercisesList.add(new ShoulderExercises(4, "Reverse Fly"));
        exercisesList.add(new ShoulderExercises(3, "Shrugs"));
        exercisesList.add(new ShoulderExercises(4, "Upright Row"));
        exercisesList.add(new ShoulderExercises(2, "Face Pull"));
        exercisesList.add(new ShoulderExercises(1, "Makinede Lateral"));
        exercisesList.add(new ShoulderExercises(3, "Seated Dumbbell Press"));
        exercisesList.add(new ShoulderExercises(4, "High Pulls"));
        exercisesList.add(new ShoulderExercises(5, "Push Press"));
        exercisesList.add(new ShoulderExercises(1, "Cable Lateral Raise"));
        exercisesList.add(new ShoulderExercises(2, "Bent Over Lateral Raise"));
        exercisesList.add(new ShoulderExercises(4, "Barbell Front Raise"));
        exercisesList.add(new ShoulderExercises(3, "Machine Shoulder Press"));

        // Göğüs Hareketleri
        exercisesList.add(new ChestExercises(5, "Bench Press"));
        exercisesList.add(new ChestExercises(5, "Dumbbell Fly"));
        exercisesList.add(new ChestExercises(5, "Incline Bench Press"));
        exercisesList.add(new ChestExercises(2, "Cable Crossover"));
        exercisesList.add(new ChestExercises(3, "Push-up"));
        exercisesList.add(new ChestExercises(2, "Incline Push-up"));
        exercisesList.add(new ChestExercises(4, "Decline Bench Press"));
        exercisesList.add(new ChestExercises(1, "Machine Chest Press"));
        exercisesList.add(new ChestExercises(5, "Incline Dumbbell Press"));
        exercisesList.add(new ChestExercises(5, "Makinede Fly"));
        exercisesList.add(new ChestExercises(4, "Dumbbell Bench Press"));
        exercisesList.add(new ChestExercises(3, "Barbell Pullover"));
        exercisesList.add(new ChestExercises(4, "Dumbbell Pullover"));
        exercisesList.add(new ChestExercises(4, "Chest Dips"));
        exercisesList.add(new ChestExercises(1, "Smith Machine Bench Press"));

        exercisesList.sort(null);
        hareketleriKaristir();

    }
}