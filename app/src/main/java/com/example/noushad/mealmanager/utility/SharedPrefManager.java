package com.example.noushad.mealmanager.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.noushad.mealmanager.event.UpdateEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by noushad on 12/31/17.
 */

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context sContext;


    private static final String SHARED_PREF_NAME = "com.example.noushad.mealmanager";
    private static final String KEY_TOTAL_EXPENSE = "com.example.noushad.mealmanager.total_expense";
    private static final String KEY_TOTAL_MEALS = "com.example.noushad.mealmanager.total_meals";
    private static final String KEY_CURRENT_MEAL_PRICE = "com.example.noushad.mealmanager.meal_price";
    private static final String KEY_USER_ID = "keyuserid";
    private static final String KEY_LAST_CHANGED = "keylastchanged";


    private SharedPrefManager(Context context) {
        sContext = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean setTotalExpense(int amount, int code) {
        int total;

        if (code == 101) {
            total = amount;
        } else {
            total = getTotalExpense() + amount;
        }
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_TOTAL_EXPENSE, total);
        editor.apply();
        EventBus.getDefault().post(new UpdateEvent(total, "Expense"));
        float meals = getTotalMeals();
        if (meals > 0) {
            float mealPrice = total / meals;
            setMealPrice(mealPrice);
        }
        return true;
    }

    public boolean setTotalMeals(float amount, int code) {
        float total;
        if (code == 101) {
            total = amount;
        } else {
            total = getTotalMeals() + amount;
        }
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(KEY_TOTAL_MEALS, total);
        editor.apply();
        EventBus.getDefault().post(new UpdateEvent(total, "Meal"));
        if (total > 0) {
            float mealPrice = getTotalExpense() / total;
            setMealPrice(mealPrice);
        }
        return true;
    }

    public boolean setMealPrice(float amount) {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(KEY_CURRENT_MEAL_PRICE, amount);
        editor.apply();
        EventBus.getDefault().post(new UpdateEvent(amount, "Price"));
        return true;
    }

    public int getTotalExpense() {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_TOTAL_EXPENSE, 0);
    }

    public float getTotalMeals() {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(KEY_TOTAL_MEALS, 0);
    }

    public float getCurrentMealPrice() {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(KEY_CURRENT_MEAL_PRICE, 0);
    }

    public boolean clear() {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        editor.commit();
        return true;

    }

    public void setUserId(String uid) {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, uid);
        editor.apply();
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(KEY_USER_ID, null);
        return userId;
    }

    public boolean isLoggedIn() {
        return getUserId() != null;
    }

    public void logOutUser() {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_ID);
        editor.apply();
    }
}
