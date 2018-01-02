package com.example.noushad.mealmanager.model;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by noushad on 1/1/18.
 */

public class MealConverter {
    @TypeConverter
    public static List<Meal> stringToMeals(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Meal>>() {}.getType();
        List<Meal> meals = gson.fromJson(json, type);
        return meals;
    }

    @TypeConverter
    public static String mealsToString(List<Meal> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Meal>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}
