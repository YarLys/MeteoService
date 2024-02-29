package com.example.meteoservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    String city = "";
    //static final String API_REQUEST = "https://api.weatherapi.com/v1/current.json?key=2ff11e3fcaad4bad99261444231612&q=Rybinsk&aqi=no";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.B1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView = findViewById(R.id.temp);
                EditText editText = findViewById(R.id.ET1);
                city = String.valueOf(editText.getText());
                Log.d("RESULT", city);

                registerReceiver(receiver, new IntentFilter("MeteoService"), RECEIVER_EXPORTED);

                Intent intent = new Intent(MainActivity.this, MeteoService.class);
                intent.putExtra("CITY", city);
                startService(intent);
            }
        });
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("RESULT", intent.getStringExtra("INFO"));
            String str = intent.getStringExtra("INFO");
            try {
                JSONObject start = new JSONObject(str);
                JSONObject current = start.getJSONObject("current");
                double temp = current.getDouble("temp_c");

                textView.setText(""+temp+"C");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, MeteoService.class);
        stopService(intent);
        unregisterReceiver(receiver);
    }
}