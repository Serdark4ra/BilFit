package com.serdar_kara.bilfit.algorithm;

import java.util.ArrayList;

public class User {
    
    private double guc;
    private int gunSayisi = 0;
    private WorkoutPrograms s = new WorkoutPrograms();
    private ArrayList<ArrayList<Exercises>> program;

    public void programiResetle()
    {
        this.program = null;
    }

    public User()
    {
        this.guc = 1;
    }

    public void gucuArttir(double artis)
    {
        this.guc = this.guc + artis;
    }

    public void setGunSayisi(int n)
    {
        this.gunSayisi = n;
    }

    public int getGunSayisi()
    { 
        return this.gunSayisi;
    }

    public double getGuc()
    {
        return this.guc;
    }

    public ArrayList<ArrayList<Exercises>> programiDondur()
    {
        return this.program;
    }

    /*public void programiOlustur()
    {
        if (this.gunSayisi == 2)
        {
            program = s.getIkiGunluk();
        }
        else if (this.gunSayisi == 3) {
            program = s.getUcGunluk();
        }
        else if (this.gunSayisi == 4) {
            program = s.getDortGunluk();
        }
        else if (this.gunSayisi == 5) {
            program = s.getBesGunluk();
        }

        Tester.programDoldurucu(program, guc);
    }*/

    public void programiYazdir()
    {
        for (int i = 0; i < program.size(); i++)
        {
            System.out.println("--------- " + (i + 1) + ". Day ---------");
            for (int j = 0; j < program.get(i).size(); j++)
            {
                System.out.println(program.get(i).get(j));
            }
        }
    }
/*
    public void gogseOdak()
    {
        ChestExercises h = new ChestExercises(0, null);

        for (int i = 0; i < program.size(); i++)
        {
            boolean eklenebilir = false;
            for (int j = 0; j < program.get(i).size(); j++)
            {
                if (program.get(i).get(j).getClass() == h.getClass())
                {
                    eklenebilir = true;
                }
            }
            if (eklenebilir)
            {
                this.program.get(i).add(Tester.getAvailableChestExercise(guc, program.get(i)));
            }
        }
    }

    public void sirtaOdak()
    {
        BackExercises h = new BackExercises(0, null);

        for (int i = 0; i < program.size(); i++)
        {
            boolean eklenebilir = false;
            for (int j = 0; j < program.get(i).size(); j++)
            {
                if (program.get(i).get(j).getClass() == h.getClass())
                {
                    eklenebilir = true;
                }
            }
            if (eklenebilir)
            {
                this.program.get(i).add(Tester.getAvailableBackExercise(guc, program.get(i)));
            }
        }
    }

    public void bacagaOdak()
    {
        LegExercises h = new LegExercises(0, null);

        for (int i = 0; i < program.size(); i++)
        {
            boolean eklenebilir = false;
            for (int j = 0; j < program.get(i).size(); j++)
            {
                if (program.get(i).get(j).getClass() == h.getClass())
                {
                    eklenebilir = true;
                }
            }
            if (eklenebilir)
            {
                this.program.get(i).add(Tester.getAvailableBacakExercise(guc, program.get(i)));
            }
        }
    }

    public void kolaOdak()
    {
        GogusHareketi h = new GogusHareketi(0, null);

        for (int i = 0; i < program.size(); i++)
        {
            boolean eklenebilir = false;
            for (int j = 0; j < program.get(i).size(); j++)
            {
                if (program.get(i).get(j).getClass() == h.getClass())
                {
                    eklenebilir = true;
                }
            }
            if (eklenebilir)
            {
                Tester.getAvailableChestExercise(guc, program.get(i));
            }
        }

    }*/
}
