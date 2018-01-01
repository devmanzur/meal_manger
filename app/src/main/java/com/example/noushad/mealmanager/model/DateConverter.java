package com.example.noushad.mealmanager.model;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by noushad on 12/22/17.
 */

public class DateConverter {
    @TypeConverter
    public static Long toTimeStamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date toDate(Long time) {
        return time == null ? null : new Date(time);
    }
}
