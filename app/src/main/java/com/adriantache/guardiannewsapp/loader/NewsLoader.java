package com.adriantache.guardiannewsapp.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.adriantache.guardiannewsapp.MainActivity;
import com.adriantache.guardiannewsapp.util.Utils;

import java.io.IOException;

public class NewsLoader extends AsyncTaskLoader<String> {
    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public String loadInBackground() {
        Log.i(MainActivity.TAG, "3. Load in background");

        String JSONResponse = "";
        try {
            JSONResponse = Utils.OKHTTP(url);
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Cannot fetch JSON", e);
        }
        return JSONResponse;
    }
}
