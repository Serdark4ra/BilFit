package com.serdar_kara.bilfit.algorithm;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class Tester {

    static Random rnd;
    static ArrayList<Exercises> hareketler;
    
    public static void programDoldurucu(ArrayList<ArrayList<Exercises>> program, double guc)
    {
        for (int i = 0; i < program.size(); i++)
        {
            for (int j = 0; j < program.get(i).size(); j++)
            {
                for (int k = 0; k < hareketler.size(); k++)
                {
                    if (hareketler.get(k).getClass() == program.get(i).get(j).getClass()
                        && hareketler.get(k).getZorluk() < guc && program.get(i).get(j).getZorluk() == 0
                            && !program.get(i).contains(hareketler.get(k)))
                    {
                        program.get(i).set(j, hareketler.get(k));
                        guc = guc - hareketler.get(k).getZorluk() * 0.03;
                    }
                }
            }
        }

        hareketleriKaristir();
    }


    public static ChestExercises getAvailableChestExercise(double guc, ArrayList<Exercises> program)
    {
        ChestExercises h = new ChestExercises(0, null);

        for (int i = 0; i < hareketler.size(); i++)
        {
            if (hareketler.get(i).getClass() == h.getClass() && !program.contains(hareketler.get(i)) && guc > hareketler.get(i).getZorluk())
            {
                return (ChestExercises) hareketler.get(i);
            }
        }
        return null;
    }

    public static BackExercises getAvailableBackExercise(double guc, ArrayList<Exercises> program)
    {
        BackExercises h = new BackExercises(0, null);

        for (int i = 0; i < hareketler.size(); i++)
        {
            if (hareketler.get(i).getClass() == h.getClass() && !program.contains(hareketler.get(i))&& guc > hareketler.get(i).getZorluk())
            {
                return (BackExercises) hareketler.get(i);
            }
        }

        return null;
    }
    public static BicepsExercises getAvailableBicepsExercise(double guc, ArrayList<Exercises> program) 
    {
        BicepsExercises h = new BicepsExercises(0, null);

        for (int i = 0; i < hareketler.size(); i++)
        {
            if (hareketler.get(i).getClass() == h.getClass() && !program.contains(hareketler.get(i))&& guc > hareketler.get(i).getZorluk())
            {
                return (BicepsExercises) hareketler.get(i);
            }
        }

        return null;
    }
    
    public static ShoulderExercises getAvailableOmuzExercise(double guc, ArrayList<Exercises> program) 
    {
        ShoulderExercises h = new ShoulderExercises(0, null);

        for (int i = 0; i < hareketler.size(); i++)
        {
            if (hareketler.get(i).getClass() == h.getClass() && !program.contains(hareketler.get(i))&& guc > hareketler.get(i).getZorluk())
            {
                return (ShoulderExercises) hareketler.get(i);
            }
        }

        return null;
    }
    
    public static TricepsExercises getAvailableTricepsExercise(double guc, ArrayList<Exercises> program) 
    {
        TricepsExercises h = new TricepsExercises(0, null);

        for (int i = 0; i < hareketler.size(); i++)
        {
            if (hareketler.get(i).getClass() == h.getClass() && !program.contains(hareketler.get(i)) && guc > hareketler.get(i).getZorluk())
            {
                return (TricepsExercises) hareketler.get(i);
            }
        }

        return null;
    }
    
    public static LegExercises getAvailableBacakExercise(double guc, ArrayList<Exercises> program) 
    {
        LegExercises h = new LegExercises(0, null);

        for (int i = 0; i < hareketler.size(); i++)
        {
            if (hareketler.get(i).getClass() == h.getClass() && !program.contains(hareketler.get(i)) && guc > hareketler.get(i).getZorluk())
            {
                return (LegExercises) hareketler.get(i);
            }
        }

        return null;
    }

    public static void hareketleriKaristir()
    {
        rnd = new Random();
        for (int i = 0; i < 10000; i++)
        {
            int a = rnd.nextInt(hareketler.size());
            int b = rnd.nextInt(hareketler.size());

            //System.out.println("a: " + a + " , b: " + b);

            if (hareketler.get(a).getClass() == hareketler.get(b).getClass() && 
                hareketler.get(a).getZorluk() == hareketler.get(b).getZorluk())
            {
                //System.out.println("bunlarin yeri degistirildi");
                Exercises temp = hareketler.get(a);
                hareketler.set(a, hareketler.get(b));
                hareketler.set(b, temp);
            }
        }
    }

    public static void main(String[] args) {

        hareketler = new ArrayList<Exercises>();

        //bacak hareketleri
        hareketler.add(new LegExercises(5, "Squat"));
        hareketler.add(new LegExercises(5, "Dumbell Squat"));
        hareketler.add(new LegExercises(4, "Leg press"));
        hareketler.add(new LegExercises(3, "Leg curl"));
        hareketler.add(new LegExercises(2, "Leg Extension"));
        hareketler.add(new LegExercises(1, "Dumbell Step up"));
        hareketler.add(new LegExercises(3, "Yogun tempo kosu"));
        hareketler.add(new LegExercises(1, "Hafif tempo kosu"));
        hareketler.add(new LegExercises(4, "Lunges"));
        hareketler.add(new LegExercises(3, "Bulgarian Split Squat"));
        hareketler.add(new LegExercises(4, "Hack Squat"));
        hareketler.add(new LegExercises(2, "Calf Raise"));
        hareketler.add(new LegExercises(3, "Sumo Squat"));
        hareketler.add(new LegExercises(1, "Good Mornings"));

        // Sırt Hareketleri
        hareketler.add(new BackExercises(5, "Geleneksel Deadlift"));
        hareketler.add(new BackExercises(5, "Elleri ters tutmali Deadlift"));
        hareketler.add(new BackExercises(5, "Pull-up"));
        hareketler.add(new BackExercises(4, "Barbell Row"));
        hareketler.add(new BackExercises(3, "Lat Pulldown"));
        hareketler.add(new BackExercises(2, "Dumbbell Row"));
        hareketler.add(new BackExercises(4, "T-Bar Row"));
        hareketler.add(new BackExercises(1, "Cable Row"));
        hareketler.add(new BackExercises(3, "Single Arm Dumbbell Row"));
        hareketler.add(new BackExercises(2, "Seated Cable Row"));
        hareketler.add(new BackExercises(3, "Bent Over Barbell Row"));
        hareketler.add(new BackExercises(1, "Face Pulls"));
        hareketler.add(new BackExercises(3, "Machine Row"));
        hareketler.add(new BackExercises(5, "Romanian Deadlift"));
        hareketler.add(new BackExercises(4, "Inverted Row"));
    
        // Biceps Hareketleri
        hareketler.add(new BicepsExercises(5, "Barbell Curl"));
        hareketler.add(new BicepsExercises(1, "Dumbbell Curl"));
        hareketler.add(new BicepsExercises(3, "Preacher Curl"));
        hareketler.add(new BicepsExercises(3, "Hammer Curl"));
        hareketler.add(new BicepsExercises(5, "Concentration Curl"));
        hareketler.add(new BicepsExercises(4, "Reverse Curl"));
        hareketler.add(new BicepsExercises(3, "Cable Curl"));
        hareketler.add(new BicepsExercises(2, "Spider Curl"));
        hareketler.add(new BicepsExercises(2, "Alternating Hammer Curl"));
        hareketler.add(new BicepsExercises(1, "Cable Hammer Curl"));
        hareketler.add(new BicepsExercises(4, "Zottman Curl"));

        // Triceps Hareketleri
        hareketler.add(new TricepsExercises(5, "Tricep Dip"));
        hareketler.add(new TricepsExercises(4, "Skull Crusher"));
        hareketler.add(new TricepsExercises(3, "Tricep Pushdown"));
        hareketler.add(new TricepsExercises(5, "Overhead Tricep Extension"));
        hareketler.add(new TricepsExercises(1, "Tricep Kickback"));
        hareketler.add(new TricepsExercises(1, "Tricep Rope Pushdown"));
        hareketler.add(new TricepsExercises(2, "Tricep Bench Dip"));
        hareketler.add(new TricepsExercises(3, "Overhead Dumbbell Tricep Press"));
        hareketler.add(new TricepsExercises(2, "Triceps Cable Kickback"));
        hareketler.add(new TricepsExercises(5, "Dumbbell Triceps Extension"));
        hareketler.add(new TricepsExercises(4, "Triceps Rope Overhead Extension"));
        
        // Omuz Hareketleri
        hareketler.add(new ShoulderExercises(5, "Military Press"));
        hareketler.add(new ShoulderExercises(5, "Arnold Press"));
        hareketler.add(new ShoulderExercises(1, "Lateral Raise"));
        hareketler.add(new ShoulderExercises(3, "Front Raise"));
        hareketler.add(new ShoulderExercises(4, "Reverse Fly"));
        hareketler.add(new ShoulderExercises(3, "Shrugs"));
        hareketler.add(new ShoulderExercises(4, "Upright Row"));
        hareketler.add(new ShoulderExercises(2, "Face Pull"));
        hareketler.add(new ShoulderExercises(1, "Makinede Lateral"));
        hareketler.add(new ShoulderExercises(3, "Seated Dumbbell Press"));
        hareketler.add(new ShoulderExercises(4, "High Pulls"));
        hareketler.add(new ShoulderExercises(5, "Push Press"));
        hareketler.add(new ShoulderExercises(1, "Cable Lateral Raise"));
        hareketler.add(new ShoulderExercises(2, "Bent Over Lateral Raise"));
        hareketler.add(new ShoulderExercises(4, "Barbell Front Raise"));
        hareketler.add(new ShoulderExercises(3, "Machine Shoulder Press"));

        // Göğüs Hareketleri
        hareketler.add(new ChestExercises(5, "Bench Press"));
        hareketler.add(new ChestExercises(5, "Dumbbell Fly"));
        hareketler.add(new ChestExercises(5, "Incline Bench Press"));
        hareketler.add(new ChestExercises(2, "Cable Crossover"));
        hareketler.add(new ChestExercises(3, "Push-up"));
        hareketler.add(new ChestExercises(2, "Incline Push-up"));
        hareketler.add(new ChestExercises(4, "Decline Bench Press"));
        hareketler.add(new ChestExercises(1, "Machine Chest Press"));
        hareketler.add(new ChestExercises(5, "Incline Dumbbell Press"));
        hareketler.add(new ChestExercises(5, "Makinede Fly"));
        hareketler.add(new ChestExercises(4, "Dumbbell Bench Press"));
        hareketler.add(new ChestExercises(3, "Barbell Pullover"));
        hareketler.add(new ChestExercises(4, "Dumbbell Pullover"));
        hareketler.add(new ChestExercises(4, "Chest Dips"));
        hareketler.add(new ChestExercises(1, "Smith Machine Bench Press"));

        hareketler.sort(null);
        hareketleriKaristir();

        //programDoldurucu(program, 6);
        User sporcu = new User();
        JFrame frame = new KullaniciArayuzu(sporcu);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //System.out.println(program); 
        /* for (int i = 0; i < program.size(); i++)
        {
            System.out.println("--------- " + (i + 1) + ". Day ---------");
            for (int j = 0; j < program.get(i).size(); j++)
            {
                System.out.println(program.get(i).get(j));
            }
        } */
    }
}