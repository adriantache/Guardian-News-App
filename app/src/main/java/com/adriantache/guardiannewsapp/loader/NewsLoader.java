package com.adriantache.guardiannewsapp.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.adriantache.guardiannewsapp.NewsItem;
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
