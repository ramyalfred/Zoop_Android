package com.example.stockup.model;

import android.net.Uri;

import java.util.Date;
import java.util.HashMap;

public class Habit {

    String mHabitName, mHabitDesc;
    int mHabitStreak;
    HashMap mHabitHits = new HashMap<Date,Boolean>();

    public Habit(String name, int streak){
        this.mHabitName = name;
        this.mHabitStreak = streak;
    }

    public int getHabitStreak() {
        return mHabitStreak;
    }

    public void setHabitStreak(int habitStreak) {
        mHabitStreak = habitStreak;
    }

    public String getHabitDesc() {
        return mHabitDesc;
    }

    public void setHabitDesc(String habitDesc) {
        mHabitDesc = habitDesc;
    }

    public String getHabitName() {
        return mHabitName;
    }

    public void setHabitName(String habitName) {
        mHabitName = habitName;
    }

    public void setHabitHit(Date date){
        mHabitHits.put(date,true);
    }

    public void clearHabitHit(Date date){
        mHabitHits.put(date,false);
    }

    public HashMap getHabitHits() {
        return mHabitHits;
    }
}
