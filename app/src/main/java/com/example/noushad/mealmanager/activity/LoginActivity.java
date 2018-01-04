package com.example.noushad.mealmanager.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.noushad.mealmanager.R;
import com.example.noushad.mealmanager.utility.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    FirebaseAuth mAuth;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private static final int COMMAND_SIGNIN = 222;
    private static final int COMMAND_SIGNUP = 333;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();

    }

    private void initialize() {

        mProgressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button mRegisterButton = (Button) findViewById(R.id.register_button);

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(COMMAND_SIGNIN);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(COMMAND_SIGNUP);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }


    private void attemptLogin(int command) {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt

            if (command == COMMAND_SIGNIN) {
                //
                mProgressDialog.setMessage("Signing In User");
                mProgressDialog.show();
                signinUser(email, password);

            } else if (command == COMMAND_SIGNUP) {
                //
                mProgressDialog.setMessage("Registering User");
                mProgressDialog.show();
                registerUser(email, password);
            }

        }
    }

    private void signinUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.dismiss();
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    SharedPrefManager.getInstance(LoginActivity.this).setUserId(user.getUid());

                    LoginActivity.this.finish();

                } else {
                    showSnack(task.getException().getLocalizedMessage());
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.dismiss();
                if (task.isSuccessful()) {
                    showSnack("Registered Successfully");
                } else {
                    showSnack(task.getException().getLocalizedMessage());
                }

            }
        });
    }

    private void showSnack(String text) {

        Snackbar.make(this.findViewById(R.id.login_form), text, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_blue_light))
                .show();

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


}

