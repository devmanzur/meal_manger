package com.example.noushad.mealmanager.utility;

import android.os.Build;
import android.support.annotation.NonNull;

import com.example.noushad.mealmanager.event.DataUploadedEvent;
import com.example.noushad.mealmanager.event.ErrorEvent;
import com.example.noushad.mealmanager.model.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

/**
 * Created by noushad on 1/4/18.
 */

public class FirebaseService {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void uploadLocalDB(String id, int expense, float meals, float currentPrice, List<Member> membersList) {
        mDatabase.child("users").child(id).child("last_updated").setValue(new Date());
        mDatabase.child("users").child(id).child("device_name").setValue(Build.MODEL);
        mDatabase.child("users").child(id).child("expense").setValue(expense);
        mDatabase.child("users").child(id).child("meals").setValue(meals);
        mDatabase.child("users").child(id).child("current_price").setValue(currentPrice);

        mDatabase.child("users").child(id).child("members").setValue(membersList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> pTask) {
                if (pTask.isSuccessful()) {
                    EventBus.getDefault().post(new DataUploadedEvent());
                } else {
                    EventBus.getDefault().post(new ErrorEvent("Data Upload Failed"));
                }
            }
        });

    }
}
