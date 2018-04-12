package com.adriantache.guardiannewsapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.adriantache.guardiannewsapp.NewsItem;

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

public class Utils {

    public List<NewsItem> getNews(String url) {
        String JSONResponse = "";

        //get JSON response from website
        try {
            JSONResponse = OKHTTP(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //parse JSON String to NewsItem objects and add them to the ArrayList
        ArrayList<NewsItem> newsArray = new ArrayList<>();
        if (!TextUtils.isEmpty(JSONResponse)) {
            newsArray = parseJSON(JSONResponse);
        }

        return newsArray;
    }

    private String OKHTTP(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private ArrayList<NewsItem> parseJSON(String jsonResponse) {
        ArrayList<NewsItem> arrayList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject post = results.getJSONObject(i);

                //get thumbnail image
                Bitmap thumbnail = null;
                JSONObject fields = post.getJSONObject("fields");
                try {
                    thumbnail = getImage(fields.getString("thumbnail"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //get other content
                String category = post.getString("sectionName");
                String title = post.getString("webTitle");
                String url = post.getString("webUrl");
                String date = post.getString("webPublicationDate");
                String author = post.optString("author");

                //use appropriate constructor if author is available (I expect it never is)
                if (TextUtils.isEmpty(author)) {
                    arrayList.add(new NewsItem(category, title, url, date, thumbnail));
                } else {
                    arrayList.add(new NewsItem(category, author, title, url, date, thumbnail));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

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
