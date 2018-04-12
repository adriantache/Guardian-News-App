package com.adriantache.guardiannewsapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adriantache.guardiannewsapp.adapter.NewsAdapter;
import com.adriantache.guardiannewsapp.customClasses.NewsItem;
import com.adriantache.guardiannewsapp.loader.NewsLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

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

        //start Loader to fetch news and populate ListView
        getSupportLoaderManager().initLoader(0, null, this);

        //set list view onClick
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem newsItem = (NewsItem) parent.getItemAtPosition(position);

                //open linked website on click
                Uri webPage = Uri.parse(newsItem.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
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

    private void hideProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    //bind custom adapter to list using results from loader
    private void setAdapter(List<NewsItem> newsList) {
        hideProgressBar();

        NewsAdapter newsAdapter = new NewsAdapter(this, newsList);
        listView.setAdapter(newsAdapter);
    }

    @NonNull
    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(this, GUARDIAN_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsItem>> loader, List<NewsItem> data) {
        if (data != null) setAdapter(data);
        else {
            TextView textView = findViewById(R.id.errorText);
            textView.setText(R.string.no_news);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsItem>> loader) {
        setAdapter(new ArrayList<NewsItem>());
    }
}
