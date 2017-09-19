package com.example.admin.makingrestcalls;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by admin on 9/15/2017.
 */

/*NEEDS 3 Things:
* 1. URL Connection
* 2. Input Stream
* 3. Scanner */

public class HttpNativeThread extends Thread {

    private static final String TAG = "TAG";
    public String BASE_URL = null;
    HttpURLConnection urlConnection;

    public HttpNativeThread(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    @Override
    public void run() {
        super.run();

        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream =
                    new BufferedInputStream(urlConnection.getInputStream());

            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNext()) {
                Log.d(TAG, "run: " + scanner.nextLine());
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
