package com.example.noushad.mealmanager.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noushad.mealmanager.R;
import com.example.noushad.mealmanager.activity.LoginActivity;
import com.example.noushad.mealmanager.utility.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingsFragment extends Fragment {

    private onSettingsInteractionListener mListener;
    private ProgressDialog mProgressDialog;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        FloatingActionButton fabPassword = view.findViewById(R.id.fab_change_pass);
        FloatingActionButton fabErase = view.findViewById(R.id.fab_erase_data);
        mProgressDialog = new ProgressDialog(getContext());
        fabPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(getContext()).isLoggedIn())
                    showPasswordUpdateDialog();
                else
                    startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        fabErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });

        return view;
    }

    private void showPasswordUpdateDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Password");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.password_update_layout, null, false);
        final EditText email = (EditText) viewInflated.findViewById(R.id.email_input);
        final EditText password = (EditText) viewInflated.findViewById(R.id.password_input);
        final EditText newPassword = (EditText) viewInflated.findViewById(R.id.new_password_input);
        final EditText newPassword2 = (EditText) viewInflated.findViewById(R.id.new_password_input2);

        newPassword2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String pass1 = newPassword.getText().toString();
                String pass2 = newPassword2.getText().toString();

                if (pass1.equals(pass2)) {
                    mProgressDialog.setMessage("Updating Password");
                    mProgressDialog.show();
                    updatePassword(email, password, newPassword);
                } else {
                    String error = "Passwords Do not match";
                    newPassword.setError(error);
                    newPassword2.setError(error);
                }
                return true;
            }
        });

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    mProgressDialog.setMessage("Updating Password");
                    mProgressDialog.show();
                    updatePassword(email, password, newPassword);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updatePassword(EditText email, EditText password, final EditText newPassword) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(email.getText().toString(), password.getText().toString());

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mProgressDialog.setMessage("Password Updated Successfully");
                                        mProgressDialog.dismiss();
                                        Toast.makeText(getContext(), "Password Updated Successfully", Toast.LENGTH_LONG).show();
                                    } else {
                                        mProgressDialog.setMessage("Password Update Failed");
                                        mProgressDialog.dismiss();
                                        Toast.makeText(getContext(), "Password Update Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mProgressDialog.setMessage(e.getLocalizedMessage());
                                    mProgressDialog.dismiss();
                                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            mProgressDialog.setMessage("Authentication Failed");
                            mProgressDialog.dismiss();
                            Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onSettingsInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onSettingsInteractionListener) {
            mListener = (onSettingsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onSettingsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface onSettingsInteractionListener {
        // TODO: Update argument type and name
        void onSettingsInteraction();
    }
}
