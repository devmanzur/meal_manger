package com.example.noushad.mealmanager.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

/**
 * Created by noushad on 12/30/17.
 */


@Entity
public class Member {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String name;
    private float totalMeal;
    private int totalMoneySpent;
//    private List<Float> meals;
//    private List<Integer> bazars;
    @TypeConverters(DateConverter.class)
    private Date lastBazarDate;


    public Member(String name) {
        this.name = name;
        this.totalMeal = 0;
        this.totalMoneySpent = 0;
//        meals = new ArrayList<>();
//        bazars = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public float getTotalMeal() {
        return totalMeal;
    }

    public void setTotalMeal(float meal) {
        this.totalMeal += meal;
//        meals.add(meal);
    }

    public int getTotalMoneySpent() {
        return totalMoneySpent;
    }

    public void setTotalMoneySpent(int money) {
        this.totalMoneySpent += money;
//        bazars.add(money);
        this.lastBazarDate = new Date();
    }

    public int getId() {
        return id;
    }

//    public List<Float> getMeals() {
//        return meals;
//    }
//
//    public List<Integer> getBazars() {
//        return bazars;
//    }


    public void setLastBazarDate(Date pLastBazarDate) {
        this.lastBazarDate = pLastBazarDate;
    }

    public Date getLastBazarDate() {
        return lastBazarDate;
    }
}
