package com.adriantache.guardiannewsapp.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.adriantache.guardiannewsapp.MainActivity;
import com.adriantache.guardiannewsapp.customClasses.NewsItem;
import com.adriantache.guardiannewsapp.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {
    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public List<NewsItem> loadInBackground() {
        Log.i(MainActivity.TAG, "3. Load in background");

        ArrayList<NewsItem> newsArray = new ArrayList<>();
        try {
            newsArray = Utils.OKHTTP(url);
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Cannot get news array", e);
        }
        return newsArray;
    }


}
