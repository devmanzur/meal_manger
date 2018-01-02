package com.example.noushad.mealmanager.model;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by noushad on 1/1/18.
 */

public class BazarConverter {

    @TypeConverter
    public static List<Integer> stringToBazars(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> bazars = gson.fromJson(json, type);
        return bazars;
    }

    @TypeConverter
    public static String bazarsToString(List<Integer> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }

}
