package com.example.noushad.mealmanager.model;

import java.util.Date;

/**
 * Created by noushad on 1/1/18.
 */

public class Meal {
    private Date mDate;
    private float mMeal;

    public Meal(Date pDate, float pMeal) {
        mDate = pDate;
        mMeal = pMeal;
    }

    public Date getDate() {
        return mDate;
    }

    public float getMeal() {
        return mMeal;
    }
}
