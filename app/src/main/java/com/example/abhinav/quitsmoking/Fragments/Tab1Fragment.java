package com.example.abhinav.quitsmoking.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abhinav.quitsmoking.R;
import com.example.abhinav.quitsmoking.model.Quotes;
import com.example.abhinav.quitsmoking.remote.APIService;
import com.example.abhinav.quitsmoking.remote.ApiUtils;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ashu on 26-10-2018.
 */

public class Tab1Fragment extends Fragment {
    private SharedPreferences preferences;
    private ImageView garden;
    private Quotes.Result quote;
    private APIService apiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService= ApiUtils.getAPIService();
        getQuote();






        garden=view.findViewById(R.id.gardenImageView);

        preferences=getActivity().getSharedPreferences("com.example.abhinav.quitsmoking", Context.MODE_PRIVATE);
        int count=preferences.getInt("count",1);
        switch (count){
            case 1:  garden.setImageResource(R.drawable.bestcube1);break;
            case 2: garden.setImageResource(R.drawable.bestcube2); break;
            case 3: garden.setImageResource(R.drawable.bestcube3); break;
            case 4: garden.setImageResource(R.drawable.bestcube4); break;
            case 5: garden.setImageResource(R.drawable.bestcube5); break;
            case 6: garden.setImageResource(R.drawable.bestcube6); break;

            default: garden.setImageResource(R.drawable.bestcube4);





        }
    }

    public void getQuote(){

        Random random=new Random();
        int id= random.nextInt(20)+1;
        apiService.getQuote(id).enqueue(new Callback<Quotes.Result>() {
            @Override
            public void onResponse(Call<Quotes.Result> call, Response<Quotes.Result> response) {
                if(response.isSuccessful()){

                    quote=response.body();
                    new AlertDialog.Builder(getActivity()).
                            setIcon(android.R.drawable.sym_def_app_icon).
                            setTitle("Motivational Quote").
                            setMessage(quote.getQuote()).
                            setPositiveButton("GREAT!",null).show();
                }
            }

            @Override
            public void onFailure(Call<Quotes.Result> call, Throwable t) {

            }
        });


    }
}
