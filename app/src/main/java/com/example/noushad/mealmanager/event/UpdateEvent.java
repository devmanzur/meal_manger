package com.example.noushad.mealmanager.event;

/**
 * Created by noushad on 12/31/17.
 */

public class UpdateEvent {
    private float mTotal;
    private String mTag;

    public UpdateEvent(float pTotal, String pTag) {

        mTotal = pTotal;
        mTag = pTag;
    }

    public float getTotal() {
        return mTotal;
    }

    public String getTag() {
        return mTag;
    }
}
