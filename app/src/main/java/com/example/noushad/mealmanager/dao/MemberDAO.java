package com.example.noushad.mealmanager.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;
import com.example.noushad.mealmanager.model.DateConverter;
import com.example.noushad.mealmanager.model.Member;
import java.util.List;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by noushad on 12/31/17.
 */
@Dao
@TypeConverters(DateConverter.class)
public interface MemberDAO {

    @Query("select * from Member")
    LiveData<List<Member>> getAllMembers();

    @Query("select * from Member where id = :id")
    Member getMemberById(String id);

    @Insert(onConflict = REPLACE)
    void addMember(Member Member);

    @Delete
    void deleteMember(Member Member);

    @Update
    void updateMember(Member Member);

    @Query("delete from Member")
    void clearAllMembers();

}
