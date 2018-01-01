package com.example.noushad.mealmanager.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.noushad.mealmanager.dao.MemberDAO;
import com.example.noushad.mealmanager.model.Member;

/**
 * Created by noushad on 12/31/17.
 */
@Database(entities = {Member.class}, version = 1)
public abstract class MemberDB extends RoomDatabase {

    private static MemberDB INSTANCE;

    public static MemberDB getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), MemberDB.class, "member_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract MemberDAO memberDAO();
}
