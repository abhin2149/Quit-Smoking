package com.example.abhinav.quitsmoking.remote;

import com.example.abhinav.quitsmoking.model.LoginCredentialsRequest;
import com.example.abhinav.quitsmoking.model.User;
import com.example.abhinav.quitsmoking.model.UserIdAuthTokenRequest;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface APIService {

    @POST("/api/login/s9lqsQTEJRpaUleqox5s/")
    Call<User.UserResult> loginUser(@Body LoginCredentialsRequest body);

    @POST("/api/logout/s9lqsQTEJRpaUleqox5s/")
    Call<User.UserResult> logoutUser(@Body UserIdAuthTokenRequest body);

    @POST("/api/s9lqsQTEJRpaUleqox5s/users/")
    @FormUrlEncoded
    Call<User.UserResult> createUser(@Field("username") String username,
                                     @Field("email") String email,
                                     @Field("first_name") String firstName,
                                     @Field("last_name") String lastName,
                                     @Field("password") String password,
                                     @Field("user_type") int userType,
                                     @Field("bio") String bio,
                                     @Field("referral_code") String ReferralCode);

    @PATCH("/api/s9lqsQTEJRpaUleqox5s/users/{id}/")
    @Multipart
    Call<User.UserResult> uploadProfilePicture(@Path("id") int id, @Part MultipartBody.Part file);

    @PATCH("/api/s9lqsQTEJRpaUleqox5s/users/{id}/")
    @FormUrlEncoded
    Call<User.UserResult> updateUser(@Path("id") int id,
                                     @Field("first_name") String firstName,
                                     @Field("last_name") String lastName,
                                     @Field("password") String password,
                                     @Field("bio") String bio,
                                     @Field("date_of_birth") String dateOfBirth);

    @GET("/api/s9lqsQTEJRpaUleqox5s/users/{id}/")
    Call<User.UserResult> getUser(@Path("id") int id);


}
