package com.serdar_kara.bilfit.get_info_activities;

import android.content.Context;
import android.content.SharedPreferences;

import com.serdar_kara.bilfit.algorithm.Exercises;
import com.serdar_kara.bilfit.algorithm.Tester;
import com.serdar_kara.bilfit.algorithm.WorkoutPrograms;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInfoHolder implements Serializable {

    private double power;

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

    //We will not use this program "for now".
    /*private ArrayList<Exercises>[] program;*/

    private ArrayList<ArrayList<Exercises>> program;

    public UserInfoHolder(String gender, boolean chest, boolean back, boolean arm, boolean leg,
                          int age, int weight, int height, boolean isMondayEligible,
                          boolean isTuesdayEligible, boolean isWednesdayEligible,
                          boolean isThursdayEligible, boolean isFridayEligible,
                          boolean isSaturdayEligible, boolean isSundayEligible,
                          String purpose, String bodyType, int pushupCount) {
        Tester t = new Tester();
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.days = new boolean[7];
        this.purpose = purpose;
        this.bodyType = bodyType;
        this.pushupCount = pushupCount;
        this.userPoint = 2;
        this.numberGoingGym = 0;
        this.power = 2;
        /*this.program = new ArrayList[7];*/
    }

    public void calculateIbm()
    {
        System.out.println("sirayla kilo boy: *************** : " + this.weight + " , " + this.height);
        this.ibm = (double) this.getWeight() / ((double) (this.getHeight() * this.getHeight()) / 10000);
        System.out.println("ibm: *************** : " + this.ibm);
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

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public void setPushupCount(int pushupCount) {
        this.pushupCount = pushupCount;
    }

    public void printPrefDays(){
        System.out.println(numberGoingGym);
    }

    /**
     * returns the days in which the user goes to gym
     * if days[0] is true it means the user goes to gym on monday.     *
     * @return the days of a week as a boolean arraylist
     */
    public boolean[] getDays()
    {
        return this.days;
    }

    public void saveExerciseDaysToThePhone(Context context, boolean[] days) {
        String PREF_NAME = "ExerciseDays";
        String KEY_PREFIX = "day_";
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < days.length; i++) {
            editor.putBoolean(KEY_PREFIX + i, days[i]);
        }
        editor.apply();
    }

    /**
     * We won't use this when first issuing the program, it can be used in feedback later.
     * @param n increases the user's power by n
     */
    public void increasePower(double n)
    {
        this.power += n;
    }

    /**
     * It evaluates the power of the user according to their body information.
     */
    public void setPower()
    {
        //increases the power according to bmi.
        //if bmi is equal to 23.5 increment will be max.
        this.power = this.power + ((5 - Math.abs(ibm - 23.5)) / 5);

        System.out.println("*********IBMDEN SONRA " + this.power);

        //increases the power according to body type.
        if (this.bodyType.equals("muscular"))
        {
            this.power += 1.7;
        }
        else if(this.bodyType.equals("normal"))
        {
            this.power += 0.7;
        }
        else if (this.bodyType.equals("thin"))
        {
            this.power += 0.3;
        }
        else if (this.bodyType.equals("fat"))
        {
            this.power += 0.5;
        }

        System.out.println("*********BODITYPETAN SONRA " + this.power);

        //increases the power according to pushup count.
        this.power += this.pushupCount / 9.0;

        System.out.println("*********PUSHUPTANSONRA " + this.power);

        if (this.gender.equals("male"))
        {
            this.power += 0.4;
        }
        System.out.println("*********FINAL " + this.power);
        this.generateProgram();
    }

    private void generateProgram()
    {
        WorkoutPrograms w = new WorkoutPrograms();

        if (this.purpose.equals("buildMuscles"))
        {
            this.program = w.getBuildMusclePrograms()[numberGoingGym - 2];
            Tester.generateMuscleProgram(this.program, this.power, false);
        }
        else if (this.purpose.equals("loseWeight"))
        {
            this.program = w.getCardioPrograms()[numberGoingGym - 2];
            Tester.generateCardioWorkoutProgram(this.program, this.power, false);
        }
        else if (this.purpose.equals("maintainForm"))
        {
            this.program = w.getMixedPrograms()[numberGoingGym - 2];
            Tester.generateMuscleProgram(this.program, this.power, true);
            Tester.generateCardioWorkoutProgram(this.program, this.power, true);
        }

        if(chest)
        {
            Tester.addChestTargetExercises(this.program, this.power);
        }
        if(back)
        {
            Tester.addBackTargetExercises(this.program, this.power);
        }
        if(arm)
        {

        }
        if(leg)
        {

        }


        this.programiYazdir();
    }

    //sadece test amaçlı bir kod daha sonra sileriz...
    public void programiYazdir()
    {
        int k = 0;
        String[] gunler = {"monday" , "tuesday", "wedn", "thurs" ,"friday" , "saturday", "sunday"};
        for (int i = 0; i < program.size(); i++)
        {
            System.out.print("--------- " + (i + 1) + ". Day ---------" + "Gunlerden ");
            boolean gunBulundu = true;
            for (;gunBulundu && k < 7; k++)
            {
                if (this.days[k])
                {
                    System.out.print(gunler[k] + "-------------------------\n");
                    gunBulundu = false;
                }
            }
            for (int j = 0; j < program.get(i).size(); j++)
            {
                System.out.println(program.get(i).get(j));
            }
        }
    }

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
    }

    public ArrayList<ArrayList<Exercises>> getProgram() {
        return program;
    }

    public void regenerateWorkoutProgram() // when the user clicks "regenerate program" in settings
    {
        this.program.clear();
        generateProgram();
    }

}





