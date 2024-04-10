package com.serdar_kara.bilfit.get_info_activities;

import com.serdar_kara.bilfit.algorithm.Exercises;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInfoHolder implements Serializable {
    private String gender;
    private boolean chest;
    private boolean back;
    private boolean arm;
    private boolean leg;

    private int age;
    private int weight;
    private int height;

    private boolean isMondayEligible;
    private boolean isTuesdayEligible;
    private boolean isWednesdayEligible;
    private boolean isThursdayEligible;
    private boolean isFridayEligible;
    private boolean isSaturdayEligible;
    private boolean isSundayEligible;

    private String purpose;
    private String bodyType;
    private int pushupCount;

    private int numberGoingGym;

    private boolean[] days;

    private double userPoint;
    private double ibm;
    private ArrayList<ArrayList<Exercises>> program;

    public UserInfoHolder(String gender, boolean chest, boolean back, boolean arm, boolean leg,
                          int age, int weight, int height, boolean isMondayEligible,
                          boolean isTuesdayEligible, boolean isWednesdayEligible,
                          boolean isThursdayEligible, boolean isFridayEligible,
                          boolean isSaturdayEligible, boolean isSundayEligible,
                          String purpose, String bodyType, int pushupCount) {
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.days = new boolean[7];
        this.purpose = purpose;
        this.bodyType = bodyType;
        this.pushupCount = pushupCount;
        this.userPoint = 2;
        this.numberGoingGym = 0;
    }

    public void calculateIbm()
    {
        this.ibm = (double) this.getWeight() / ((double) (this.getHeight() * this.getHeight()) / 10000);
    }



    public void setDays(int index, boolean isEligible)
    {
        this.days[index] = isEligible;
        if (isEligible)
        {
            this.numberGoingGym++;
        }
    }

    public String getGender() {
        return gender;
    }

    public boolean isChest() {
        return chest;
    }

    public boolean isBack() {
        return back;
    }

    public boolean isArm() {
        return arm;
    }

    public boolean isLeg() {
        return leg;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public boolean isMondayEligible() {
        return isMondayEligible;
    }

    public boolean isTuesdayEligible() {
        return isTuesdayEligible;
    }

    public boolean isWednesdayEligible() {
        return isWednesdayEligible;
    }

    public boolean isThursdayEligible() {
        return isThursdayEligible;
    }

    public boolean isFridayEligible() {
        return isFridayEligible;
    }

    public boolean isSaturdayEligible() {
        return isSaturdayEligible;
    }

    public boolean isSundayEligible() {
        return isSundayEligible;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getBodyType() {
        return bodyType;
    }

    public int getPushupCount() {
        return pushupCount;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setChest(boolean chest) {
        this.chest = chest;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public void setArm(boolean arm) {
        this.arm = arm;
    }

    public void setLeg(boolean leg) {
        this.leg = leg;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }
/*
    public void setMondayEligible(boolean mondayEligible) {
        isMondayEligible = mondayEligible;
    }

    public void setTuesdayEligible(boolean tuesdayEligible) {
        isTuesdayEligible = tuesdayEligible;
    }

    public void setWednesdayEligible(boolean wednesdayEligible) {
        isWednesdayEligible = wednesdayEligible;
    }
    public void setThursdayEligible(boolean thursdayEligible) {
        isThursdayEligible = thursdayEligible;
    }
    public void setFridayEligible(boolean fridayEligible) {
        isFridayEligible = fridayEligible;
    }

    public void setSaturdayEligible(boolean saturdayEligible) {
        isSaturdayEligible = saturdayEligible;
    }
    public void setSundayEligible(boolean sundayEligible) {
        isSundayEligible = sundayEligible;
    }*/

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public void setPushupCount(int pushupCount) {
        this.pushupCount = pushupCount;
    }
}
