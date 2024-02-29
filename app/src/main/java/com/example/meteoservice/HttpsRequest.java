package com.example.meteoservice;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class HttpsRequest implements Runnable {
    static final String KEY = "2ff11e3fcaad4bad99261444231612";
    static final String api_base = "http://api.weatherapi.com/v1/current.json";
    URL url;
    Handler handler;

    public HttpsRequest(Handler handler, String city) {
        this.handler = handler;
        try {
            if (Objects.equals(city, "")) {
                city = "Moscow";
            }
            url = new URL(api_base + "?" + "q=" + city + "&" + "key=" + KEY);
            Log.d("RESULT", url.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Scanner in = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (in.hasNext()) {
                response.append(in.nextLine());
            }
            in.close();
            connection.disconnect();

            Message msg = Message.obtain();
            msg.obj = response.toString();
            handler.sendMessage(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
