package com.example.admin.makingrestcalls;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.makingrestcalls.Model.MyResponse;
import com.example.admin.makingrestcalls.Model.github.GithubRepo;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://www.mocky.io/v2/59bbf4130f0000c003ff87e7";
    public static final String TAG = "TAG";
    TextView tvResult;
    OkHttpClient client;
    ImageView imageview;

    private String image_url = "https://static1.squarespace.com/static/54e8ba93e4b07c3f655b452e/t/56c2a04520c64707756f4267/1493764650017/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = (TextView) findViewById(R.id.tvResult);
        imageview = (ImageView) findViewById(R.id.ivTest);

        Glide.with(this).load(image_url).into(imageview);
        //create an OkHttpClient

    }

    public void makingRestfulCalls(View view) {
        client = new OkHttpClient();

        switch (view.getId()) {

            case R.id.btnNativeHttp:
                HttpNativeThread httpNativeThread = new HttpNativeThread(BASE_URL);
                httpNativeThread.start();
                break;

            case R.id.btnOKHttpSync:

                final Request request = new Request.Builder()
                        .url(BASE_URL).build();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String response = client.newCall(request).execute().body().string();
                            Log.d(TAG, "run: " + response);

                            Gson gson = new Gson();
                            MyResponse myResponse = gson.fromJson(response, MyResponse.class);

                            Log.d(TAG, "run: " + myResponse.getTitle());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(this, "Check your logs", Toast.LENGTH_SHORT).show();


                break;

            case R.id.btnOKHttpAsync:

                Request request2 = new Request.Builder()
                        .url(BASE_URL).build();

                client.newCall(request2).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String myString = response.body().string();

                        Log.d(TAG, "run: " + myString);

                        Gson gson = new Gson();
                        MyResponse myResponse = gson.fromJson(myString, MyResponse.class);

                        Log.d(TAG, "onResponse: " + Thread.currentThread());
                        Log.d(TAG, "onResponse: " + myResponse.getProperties().getAge().getDescription());

                    }
                });

                break;

            case R.id.btnRetrofitSync:

                final retrofit2.Call<List<GithubRepo>> callRepos
                        = RetrofitHelper.createCall("sdsearle");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String repoName = callRepos.execute().body().get(0).getName();
                            Log.d(TAG, "run: " + repoName);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvResult.setText(repoName);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;

            case R.id.btnRetrofitAsync:

                break;

        }
    }
}
