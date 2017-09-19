package com.example.admin.makingrestcalls;

import com.example.admin.makingrestcalls.Model.github.GithubRepo;
import com.example.admin.makingrestcalls.Model.github.Owner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by admin on 9/18/2017.
 */

public class RetrofitHelper {
    private static final String BASE_URL = "https://api.github.com/";

    public static Retrofit create(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return  retrofit;

    }

    //create a static method to ues the interface verbs
    public static Call<List<GithubRepo>> createCall(String user){
        Retrofit retrofit = create();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.getGithubRepos(user);

    }

    //create an interface to have all the paths and verbs for the REST api to use
    interface ApiService{

        @GET("users/{user}/repos")
        Call<List<GithubRepo>> getGithubRepos(@Path("user") String user);

        @GET("users/{user}")
        Call<List<Owner>> getGithubProfile(@Path("user") String user);
    }

}
