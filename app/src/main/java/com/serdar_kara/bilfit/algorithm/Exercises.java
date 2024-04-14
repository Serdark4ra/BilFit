package com.serdar_kara.bilfit.algorithm;

import java.io.Serializable;

public abstract class Exercises implements Comparable<Exercises>, Serializable{
    
    private double zorluk;
    private String isim;

    public Exercises(double zorluk, String isim) {
        this.zorluk = zorluk;
        this.isim = isim;
    }
  
    public double getZorluk() {
        return this.zorluk;
    }

    public String getIsim() {
        return this.isim;
    }

    public String toString()
    {
        return this.getClass().toString() + " isim: " + this.isim + " zorluk: " + this.zorluk + "\n";
    }

    @Override
    public int compareTo(Exercises other) {
        
        if(this.zorluk > other.zorluk)
        {
            return -1;
        }
        else if(this.zorluk < other.zorluk)
        {
            return +1;
        }
        else 
        {
            return 0;
        }
    }
}
