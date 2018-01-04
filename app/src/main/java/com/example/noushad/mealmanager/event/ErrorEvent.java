package com.example.noushad.mealmanager.event;

/**
 * Created by noushad on 1/4/18.
 */

public class ErrorEvent {
    private String mS;

    public ErrorEvent(String s) {
        mS = s;
    }

    public String getErrorMessage() {
        return mS;
    }
}
