package com.example.abhinav.quitsmoking.remote;

public class ApiUtils {

    private ApiUtils() {}
    private static final String BASE_URL = "http://192.168.43.96:8000/";
   // private static final String BASE_URL = "http://127.0.0.1:7000";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
