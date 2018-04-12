package com.adriantache.guardiannewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ListView;

import com.adriantache.guardiannewsapp.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private String GUARDIAN_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Read Guardian API key from file so we don't just post it to GitHub.
        //If file is not present just use `test` API key.
        String GUARDIAN_API_KEY = "test";
        try {
            if (Arrays.asList(getResources().getAssets().list("")).contains("myFile")) {
                String temp = readAPIKey();
                if (!TextUtils.isEmpty(temp)) GUARDIAN_API_KEY = temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //then create Guardian URL
        GUARDIAN_URL =
                "http://content.guardianapis.com/search?order-by=newest&tag=technology%2Fandroid" +
                        "&section=technology&page-size=100&show-fields=thumbnail&api-key=" +
                        GUARDIAN_API_KEY;

        //find views
        listView = findViewById(R.id.listView);

        //todo start Loader to fetch news and populate ListView

    }

    private String readAPIKey() {
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
                while ((ch = inputStream.read()) != -1) {
                    sb.append((char) ch);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        return null;
    }

    //todo write code to bind custom adapter to list using results from loader
    private void setAdapter(List<NewsItem> newsList){

    }

}
