package com.adriantache.guardiannewsapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.adriantache.guardiannewsapp.MainActivity;
import com.adriantache.guardiannewsapp.customClasses.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.adriantache.guardiannewsapp.MainActivity.TAG;

public class Utils {

   public String OKHTTP(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    //todo figure out if this works and if we keep it
    private Bitmap getImage(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        InputStream inputStream = response.body().byteStream();
        return BitmapFactory.decodeStream(inputStream);
    }
}
