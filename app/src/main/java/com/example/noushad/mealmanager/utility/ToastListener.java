package com.example.noushad.mealmanager.utility;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

/**
 * Created by noushad on 1/7/18.
 */

public class ToastListener extends AdListener {

    private Context mContext;
    private String mErrorReason;


    public ToastListener(Context context) {
        mContext = context;
    }

    @Override
    public void onAdClosed() {

//        Toast.makeText(mContext, "onAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdFailedToLoad(int i) {
        mErrorReason = "";

        switch (i) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                mErrorReason = "internal";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                mErrorReason = "invalid request";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                mErrorReason = "network error";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                mErrorReason = "no fill";
                break;
        }

//        Toast.makeText(mContext,String.format("onAdFailedToLoad(%s)",mErrorReason),Toast.LENGTH_SHORT).show();

    }

    public String getErrorReason() {
        return mErrorReason;
    }

    @Override
    public void onAdLeftApplication() {
//        Toast.makeText(mContext, "onAdLeftApp", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdOpened() {
//        Toast.makeText(mContext, "onAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded() {
//        Toast.makeText(mContext, "onAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdClicked() {
//        Toast.makeText(mContext, "onAdClicked    ", Toast.LENGTH_SHORT).show();
    }
}
