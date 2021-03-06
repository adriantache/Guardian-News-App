package com.adriantache.guardiannewsapp.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.adriantache.guardiannewsapp.customClasses.NewsItem;
import com.adriantache.guardiannewsapp.util.Utils;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {
    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public List<NewsItem> loadInBackground() {
        return Utils.getJSON(url);
    }
}