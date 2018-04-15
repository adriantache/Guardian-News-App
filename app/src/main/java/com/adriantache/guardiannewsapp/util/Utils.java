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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.adriantache.guardiannewsapp.MainActivity.TAG;

public class Utils {
    public static ArrayList<NewsItem> OKHTTP(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return parseJSON(response.body().string());
    }

    private static ArrayList<NewsItem> parseJSON(String jsonResponse) {
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
                    //todo decide if this makes sense
                    Log.i(TAG, "Get image for post");
                    thumbnail = getImage(fields.getString("thumbnail"));
                } catch (IOException e) {
                    Log.e(TAG, "Cannot get thumbnail.", e);
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
            Log.e(TAG, "Cannot parse JSON.", e);
        }

        return arrayList;
    }

    private static Bitmap getImage(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        InputStream inputStream = response.body().byteStream();
        return BitmapFactory.decodeStream(inputStream);
    }
}
