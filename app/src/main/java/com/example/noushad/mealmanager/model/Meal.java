package com.example.noushad.mealmanager.model;

import java.util.Date;

/**
 * Created by noushad on 1/1/18.
 */

public class Meal {
    private Date mDate;
    private float mMeal;

    public Meal() {
    }

    public Meal(Date pDate, float pMeal) {
        mDate = pDate;
        mMeal = pMeal;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setMeal(float meal) {
        mMeal = meal;
    }

    public Date getDate() {
        return mDate;
    }

    public float getMeal() {
        return mMeal;
    }
}
