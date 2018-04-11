package com.adriantache.guardiannewsapp;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private String GUARDIAN_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readAPIKey();
    }

    private void readAPIKey() {
        AssetManager am = getApplicationContext().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open("GuardianAPI.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputStream != null) {
            int ch;
            StringBuilder sb = new StringBuilder();
            try {
                while ((ch = inputStream.read()) != -1)
                    sb.append((char) ch);
            } catch (IOException e) {
                e.printStackTrace();
            }

            GUARDIAN_API_KEY = sb.toString();
        }

        Log.i("XXX", "readAPIKey: " + GUARDIAN_API_KEY);
    }
}
