package com.adriantache.guardiannewsapp.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.adriantache.guardiannewsapp.customClasses.NewsItem;
import com.adriantache.guardiannewsapp.util.Utils;

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
        Utils utils = new Utils();
        ArrayList<NewsItem> newsList = (ArrayList<NewsItem>) utils.getNews(url);
        return newsList;
    }
}
