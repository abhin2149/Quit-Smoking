package com.example.abhinav.quitsmoking;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abhinav.quitsmoking.model.LoginCredentialsRequest;
import com.example.abhinav.quitsmoking.model.User;
import com.example.abhinav.quitsmoking.remote.APIService;
import com.example.abhinav.quitsmoking.remote.ApiUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST = 801;
    private static final int GALLERY_REQUEST = 802;

    private APIService apiService;
    private User.UserResult user;
    private SharedPreferences  preferences;

    private AutoCompleteTextView usernameAutoCompleteTextView;
    private AutoCompleteTextView firstNameAutoCompleteTextView;
    private AutoCompleteTextView lastNameAutoCompleteTextView;
    private AutoCompleteTextView emailNameAutoCompleteTextView;

    private AutoCompleteTextView smokedDayAutoCompleteTextView;
    private AutoCompleteTextView costAutoCompleteTextView;
    private AutoCompleteTextView startedSmokingNameAutoCompleteTextView;

    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText  emailEditText;
    //    private static EditText dobEditText;
//    private Button dobButton;
    private ImageView uploadedImageView;
    private Button uploadImageButton;
    private Button nextButton;

    private MultipartBody.Part body;
    private boolean signUpFormOkay;

    private FragmentActivity fragmentContext;

    private CharSequence taskChoosen;
    CharSequence[] items = {"Choose from Gallery", "Cancel"};

    private View rootView;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpFormOkay = false;
        preferences=this.getSharedPreferences("com.example.abhinav.quitsmoking",MODE_PRIVATE);
        nextButton=(Button)findViewById(R.id.nextButton);
        usernameAutoCompleteTextView = findViewById(R.id.username_textView);
        firstNameAutoCompleteTextView = findViewById(R.id.first_name_textView);
        lastNameAutoCompleteTextView = findViewById(R.id.last_name_textView);
        smokedDayAutoCompleteTextView = findViewById(R.id.smoked_day_textView);
        emailNameAutoCompleteTextView=findViewById(R.id.email_textView);
        costAutoCompleteTextView = findViewById(R.id.cost_textView);
        startedSmokingNameAutoCompleteTextView = findViewById(R.id.started_smoking_textView);
        passwordEditText = findViewById(R.id.password_editText);
        confirmPasswordEditText = findViewById(R.id.confirm_password_editText);
//        dobEditText = rootView.findViewById(R.id.date_of_birth_editText);
//        dobButton = rootView.findViewById(R.id.date_of_birth_Button);
        uploadedImageView = findViewById(R.id.uploaded_imageView);
        uploadImageButton = findViewById(R.id.upload_image_button);

        apiService = ApiUtils.getAPIService();
        user = User.UserResult.getUserResult();

//        dobEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment dialogFragment = new DatePickerFragment();
//                dialogFragment.show(fragmentContext.getSupportFragmentManager(), "datePicker");
//            }
//        });
//
//        dobButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment dialogFragment = new DatePickerFragment();
//                dialogFragment.show(fragmentContext.getSupportFragmentManager(), "datePicker");
//            }
//        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });
    }




    public boolean isSignUpFormOkay() {
        return signUpFormOkay;
    }

    public void attemptSignUp() {
        usernameAutoCompleteTextView.setError(null);
        firstNameAutoCompleteTextView.setError(null);
        lastNameAutoCompleteTextView.setError(null);
        passwordEditText.setError(null);
        confirmPasswordEditText.setError(null);

        user.setUserType(1);
//        dobEditText.setError(null);

        String username = usernameAutoCompleteTextView.getText().toString().trim();
        String firstName = firstNameAutoCompleteTextView.getText().toString().trim();
        String lastName = lastNameAutoCompleteTextView.getText().toString().trim();
        String email=emailNameAutoCompleteTextView.getText().toString().trim();
        String password1 = passwordEditText.getText().toString().trim();
        String password2 = confirmPasswordEditText.getText().toString().trim();


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
        if (TextUtils.isEmpty(username)) {
            usernameAutoCompleteTextView.setError(getString(R.string.error_field_required));
            focusView = usernameAutoCompleteTextView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            usernameAutoCompleteTextView.setError(getString(R.string.error_invalid_username));
            focusView = usernameAutoCompleteTextView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password1)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        } else if (!isPasswordValid(password1, password2)) {
            confirmPasswordEditText.setError(getString(R.string.error_invalid_password));
            focusView = confirmPasswordEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(email)){
            emailNameAutoCompleteTextView.setError(getString(R.string.error_field_required));
            focusView=confirmPasswordEditText;
            cancel=true;

        }
        else if(!isEmailValid(email)){

            emailNameAutoCompleteTextView.setError(getString(R.string.error_invalid_email));
            focusView=confirmPasswordEditText;
            cancel=true;
        }

        if (cancel) {
            focusView.requestFocus();
            signUpFormOkay = false;
        } else {
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setEmail(email);
            user.setLastName(lastName);
            user.setPassword(password1);
            preferences.edit().putString("smoked_day",smokedDayAutoCompleteTextView.getText().toString()).apply();
            preferences.edit().putString("cost",costAutoCompleteTextView.getText().toString()).apply();
            preferences.edit().putString("started",startedSmokingNameAutoCompleteTextView.getText().toString()).apply();


            signUpFormOkay = true;
            signUpUser();
        }
    }

    private void signUpUser() {
        apiService.createUser(user.getUsername(),user.getEmail(), user.getFirstName(), user.getLastName(), user.getPassword(), user.getUserType(), user.getBio(),user.getReferralCode())
                .enqueue(new Callback<User.UserResult>() {
                    @Override
                    public void onResponse(@NonNull Call<User.UserResult> call, @NonNull Response<User.UserResult> response) {
                        if (response.isSuccessful()) {
//                    Log.i(TAG, "post submitted to API." + response.body().toString());
                            user.setUserId(response.body().getUserId());
                            user.setUserUrl(response.body().getUserUrl());
                            user.setProfilePicture(response.body().getProfilePicture());
                            user.setProfileTags(response.body().getProfileTags());
                            if(response.body().getCode()!=null) {
                                user.setCode(response.body().getCode());
                                Log.d("Code generated",response.body().getCode());
                            }
                            loginUser();
                        } else {
                            Toast.makeText(getApplicationContext(), "Some error occurred.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "Some error occurred. Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginUser() {
        apiService.loginUser(new LoginCredentialsRequest(user.getUsername(), user.getPassword())).enqueue(new Callback<User.UserResult>() {
            @Override
            public void onResponse(@NonNull Call<User.UserResult> call, @NonNull Response<User.UserResult> response) {
                if (response.isSuccessful()) {
//                    Log.i(TAG, "post submitted to API." + response.body().toString());
                    user.setToken(response.body().getToken());
                    user.setCode(response.body().getCode());
                    user.setReferralCount(response.body().getReferralCount());
                    user.setReferralCode(response.body().getReferralCode());
                    if (body != null)
                        uploadUserProfilePicture();
                    else {
                        startActivity(new Intent(MainActivity.this, MainScreenActivity.class));
                        finish();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred. Try Again", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Some error occurred. Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadUserProfilePicture() {
        apiService.uploadProfilePicture(user.getUserId(), body).enqueue(new Callback<User.UserResult>() {
            @Override
            public void onResponse(@NonNull Call<User.UserResult> call, @NonNull Response<User.UserResult> response) {
                if (response.isSuccessful()) {
                    user.setProfilePicture(response.body().getProfilePicture());
                    startActivity(new Intent(MainActivity.this,MainScreenActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred. Try Again", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(activity, "Some error occurred while uploading picture. Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean isUsernameValid(String username) {
        return username.length() <= 150;
    }


    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password1, String password2) {
        return !password1.matches("[0-9]+") && password1.length() >= 8 && password1.equals(password2);
    }

    private boolean isBioValid(String bio) {
        return bio.length() > 100;
    }

//    private boolean checkPermissions() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            int check1 = ContextCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.CAMERA);
//            int check2 = ContextCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
//            int check3 = ContextCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            if (check1 != PackageManager.PERMISSION_GRANTED && check2 != PackageManager.PERMISSION_GRANTED && check3 != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(activity, new String[]{
//                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.CAMERA
//                }, PERMISSIONS_REQUEST);
//                return false;
//            }
//        }
//        return true;
//    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                if (taskChoosen.equals(items[1])) {
                    galleryIntent();
                } else {
                    taskChoosen = "";
                }
            } else {
           //     checkPermissions();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = null;
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (requestCode == GALLERY_REQUEST) {
                file = onSelectFromGalleryResult(data);
            }
            if (file != null && uri != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("profile_picture", String.format("pic_%s.png", user.getUsername()), requestFile);
            }
        }
    }

    private File onSelectFromGalleryResult(Intent data) {
        if (data != null && data.getData() != null) {
            Uri uri = data.getData();
            String filePath = getRealPathFromURIPath(uri, activity);
            File file = new File(filePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
                uploadedImageView.setImageBitmap(bitmap);
                uploadedImageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
//                Log.e(TAG, e.getMessage());
                uploadedImageView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Something went wrong. Try again.", Toast.LENGTH_SHORT).show();
            }
            return file;
        }
        return null;
    }

    private String getRealPathFromURIPath(Uri contentUri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        String path;
        if (cursor == null) {
            path = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path = cursor.getString(index);
            cursor.close();
        }
        return path;
    }

//
}
