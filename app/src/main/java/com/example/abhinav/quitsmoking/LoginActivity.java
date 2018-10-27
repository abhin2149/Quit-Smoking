package com.example.abhinav.quitsmoking;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhinav.quitsmoking.model.LoginCredentialsRequest;
import com.example.abhinav.quitsmoking.model.User;
import com.example.abhinav.quitsmoking.remote.APIService;
import com.example.abhinav.quitsmoking.remote.ApiUtils;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REGISTRATION_REQUEST = 501;

    private SQLiteDatabase sqLiteDatabase;
    private User.UserResult user;

    private APIService apiService;
    private SharedPreferences preferences;

    private AutoCompleteTextView usernameAutoCompleteTextView;
    private EditText passwordEditText;
    private ProgressBar progressBar;
    private ScrollView loginFormScrollView;
    private TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login Here");
        preferences=this.getSharedPreferences("com.example.abhinav.quitsmoking",MODE_PRIVATE);

        if (preferences.getAll()==null){

            preferences.edit().putInt("count",1).apply();
           // preferences.edit().putString("started",new Date().);
        }
        else{

            int count=preferences.getInt("count",1);
            preferences.edit().putInt("count",count+1).apply();
        }

        loginFormScrollView = (ScrollView) findViewById(R.id.login_form);
        progressBar = (ProgressBar) findViewById(R.id.login_progressBar);

        usernameAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.username_textView);
        passwordEditText = (EditText) findViewById(R.id.password_editText);

        sqLiteDatabase = this.openOrCreateDatabase("LMS", MODE_PRIVATE, null);
        user = User.UserResult.getUserResult();




        apiService = ApiUtils.getAPIService();


        registerTextView = (TextView) findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

            }
        });

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTRATION_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, data);
                finish();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        }
    }



    private void attemptLogin() {

        usernameAutoCompleteTextView.setError(null);
        passwordEditText.setError(null);

        String username = usernameAutoCompleteTextView.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            usernameAutoCompleteTextView.setError(getString(R.string.error_field_required));
            focusView = usernameAutoCompleteTextView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            usernameAutoCompleteTextView.setError(getString(R.string.error_invalid_username));
            focusView = usernameAutoCompleteTextView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {



                showProgress(true);
                loginUser(username, password);


        }
    }

    private boolean isUsernameValid(String username) {
        return username.length() <= 150;
    }

    private void loginUser(final String username, final String password) {
        apiService.loginUser(new LoginCredentialsRequest(username, password)).enqueue(new Callback<User.UserResult>() {
            @Override
            public void onResponse(@NonNull Call<User.UserResult> call, @NonNull Response<User.UserResult> response) {
                if (response.isSuccessful()) {
//                    Log.i(TAG, "post submitted to API." + response.body().toString());

                    user.setUserId(response.body().getUserId());
                    user.setUsername(response.body().getUsername());
                    user.setFirstName(response.body().getFirstName());
                    user.setLastName(response.body().getLastName());
                    user.setEmail(response.body().getEmail());
                    user.setPassword(password);
                    user.setToken(response.body().getToken());



                    getUser();
                } else {
                    showProgress(false);
                    Toast.makeText(getApplicationContext(), "Invalid Username or Password. Try Again", Toast.LENGTH_LONG).show();
                }
//                Log.i(TAG, String.valueOf(response.code()));
//                Log.i(TAG, response.message());
//                try {
//                    Log.i(TAG, response.errorBody().string());
//                } catch (Exception e) {
//                    Log.i(TAG, e.toString());
//                }
            }

            @Override
            public void onFailure(@NonNull Call<User.UserResult> call, @NonNull Throwable t) {
//                Log.e(TAG, "Unable to submit post to API.");
                showProgress(false);
            }
        });
    }

    private void getUser() {
        apiService.getUser(user.getUserId()).enqueue(new Callback<User.UserResult>() {
            @Override
            public void onResponse(@NonNull Call<User.UserResult> call, @NonNull Response<User.UserResult> response) {
                if (response.isSuccessful()) {
                  /*  if (!response.body().isApproved()) {
                        showProgress(false);
                        Snackbar.make(loginFormScrollView,
                                response.body().getUsername() + " has not been approved by the admin. Check later.",
                                Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    */
                    user.setUserUrl(response.body().getUserUrl());
                    user.setUserType(response.body().getUserType());
                    user.setDateOfBirth(response.body().getDateOfBirth());
                    user.setProfilePicture(response.body().getProfilePicture());
                    user.setApproved(response.body().isApproved());
                    user.setPaidSubscription(response.body().hasPaidSubscription());
                    user.setProfileTags(response.body().getProfileTags());
                    startActivity(new Intent(LoginActivity.this,MainScreenActivity.class));
                    finish();


                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected error occurred while fetching user.", Toast.LENGTH_SHORT).show();
                }
//                Log.i(TAG, String.valueOf(response.code()));
//                Log.i(TAG, response.message());
//                try {
//                    Log.i(TAG, String.valueOf(user.getUserId()));
//                    Log.i(TAG, response.errorBody().string());
//                } catch (Exception e) {
//                    Log.i(TAG, e.toString());
//                }
            }

            @Override
            public void onFailure(@NonNull Call<User.UserResult> call, @NonNull Throwable t) {
//                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginFormScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        loginFormScrollView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginFormScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        registerTextView.setVisibility(show ? View.GONE : View.VISIBLE);
        registerTextView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                registerTextView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
