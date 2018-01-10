package com.example.noushad.mealmanager.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.noushad.mealmanager.R;
import com.example.noushad.mealmanager.activity.LoginActivity;
import com.example.noushad.mealmanager.utility.SharedPrefManager;
import com.example.noushad.mealmanager.utility.TagManager;
import com.example.noushad.mealmanager.utility.ToastListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class DashboardFragment extends Fragment {
    private static final int SHOW_LOGIN = 111;
    // TODO: Rename parameter arguments, choose names that match
    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;
    private boolean loggedIn;
    private Button signInButton;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    public DashboardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        loggedIn = SharedPrefManager.getInstance(getContext()).isLoggedIn();
        setButtonText();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View v) {

        mAdView = v.findViewById(R.id.banner_ad_2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new ToastListener(getContext()));

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.InterstitialAd));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        FloatingActionButton fabUpload = v.findViewById(R.id.fab_upload_button);
        FloatingActionButton fabDownload = v.findViewById(R.id.fab_download_button);
        FloatingActionButton fabCheck = v.findViewById(R.id.fab_check_button);
        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(TagManager.UPLOAD_TASK);
            }
        });
        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(TagManager.DOWNLOAD_TASK);
            }
        });

        fabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(TagManager.CHECK_TASK);
            }
        });
        signInButton = v.findViewById(R.id.sign_in_button);
        setButtonText();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loggedIn) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("You are about to sign out")
                            .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    signOutUser();
                                    signInButton.setText("SIGN IN");
                                }
                            })
                            .setNegativeButton("CANCEL", null)
                            .show();
                } else {
                    showInterstitialAd(SHOW_LOGIN);
                    showLoginScreen();
                }
            }
        });


    }

    private void showInterstitialAd(int command) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            if (command == SHOW_LOGIN) {
                showLoginScreen();
            }else{

            }
        }
    }

    private void setButtonText() {
        if (loggedIn) {
            signInButton.setText("SIGN OUT");
        } else {
            signInButton.setText("SIGN IN");
        }
    }

    private void signOutUser() {
        SharedPrefManager.getInstance(getContext()).logOutUser();
        loggedIn = false;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(final int taskmode) {

        if (loggedIn) {
            new AlertDialog.Builder(getContext())
                    .setMessage("You are connecting to server")
                    .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mListener != null) {
                                showInterstitialAd(0);
                                mListener.onDashboardInteraction(taskmode);
                            }
                        }
                    })
                    .setNegativeButton("CANCEL", null)
                    .show();

        } else {
            showLoginScreen();
        }
    }

    private void showLoginScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        Toast.makeText(getContext(), "You Need To Login", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDashboardInteraction(int command);
    }
}
