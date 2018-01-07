package com.example.noushad.mealmanager.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @TypeConverters(MealConverter.class)
    private List<Meal> meals;
    @TypeConverters(BazarConverter.class)
    private List<Integer> bazars;
    @TypeConverters(DateConverter.class)
    private Date lastBazarDate;
    @TypeConverters(DateConverter.class)
    private Date creationDate;

    public Member() {
        creationDate = new Date();
        this.totalMeal = 0;
        this.totalMoneySpent = 0;
        meals = new ArrayList<>();
        bazars = new ArrayList<>();
    }

    public Member(String name) {
        this.name = name;
        creationDate = new Date();
        this.totalMeal = 0;
        this.totalMoneySpent = 0;
        meals = new ArrayList<>();
        bazars = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public float getTotalMeal() {
        return totalMeal;
    }

    public void setTotalMeal(float totalMeal) {
        this.totalMeal = totalMeal;
    }

    public void addMeal(float meal) {
        this.totalMeal += meal;
        meals.add(new Meal(new Date(),meal));
    }

    public void setTotalMoneySpent(int totalMoneySpent) {
        this.totalMoneySpent = totalMoneySpent;
    }

    public int getTotalMoneySpent() {
        return totalMoneySpent;
    }

    public void addTotalMoney(int money) {
        this.totalMoneySpent += money;
        bazars.add(money);
        this.lastBazarDate = new Date();
    }

    public int getId() {
        return id;
    }

    public void setMeals(List<Meal> pMeals) {
        meals = pMeals;
    }

    public void setBazars(List<Integer> pBazars) {
        bazars = pBazars;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public List<Integer> getBazars() {
        return bazars;
    }


    public void setLastBazarDate(Date pLastBazarDate) {
        this.lastBazarDate = pLastBazarDate;
    }

    public Date getLastBazarDate() {
        return lastBazarDate;
    }

    public void setName(String pName) {
        name = pName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date pCreationDate) {
        creationDate = pCreationDate;
    }
}
