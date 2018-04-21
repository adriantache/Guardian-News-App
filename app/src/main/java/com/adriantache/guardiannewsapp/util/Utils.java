package com.adriantache.guardiannewsapp.util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;

import com.adriantache.guardiannewsapp.MainActivity;
import com.adriantache.guardiannewsapp.customClasses.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.adriantache.guardiannewsapp.MainActivity.TAG;

public class Utils {

    public static WeakReference<MainActivity> activity;

    public static ArrayList<NewsItem> getJSON(String url) {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                return parseJSON(readJSONFromStream(inputStream));
            } else
                Log.e(MainActivity.TAG, "Incorrect HTTP response code: " +
                        httpURLConnection.getResponseCode());
        } catch (IOException e) {
            Log.e(TAG, "Cannot get JSON.", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "Cannot get JSON.", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String readJSONFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        return output.toString();
    }

    private static ArrayList<NewsItem> parseJSON(String jsonResponse) {
        ArrayList<NewsItem> arrayList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.optJSONObject("response");
            JSONArray results = response.optJSONArray("results");

            final int len = results.length();

            for (int i = 0; i < len; i++) {
                //update progress bar
                final int progress = i + 1;
                if (activity.get() != null) {
                    activity.get().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ProgressBar temp = activity.get().progressBar;
                            temp.setIndeterminate(false);
                            temp.setMax(len);
                            temp.setProgress(progress);
                        }
                    });
                }

                JSONObject post = results.optJSONObject(i);

                //get news post content
                String category = post.optString("sectionName");
                String title = post.optString("webTitle");
                String url = post.optString("webUrl");
                String date = post.optString("webPublicationDate");
                String author = post.optString("author");

                //get thumbnail image
                JSONObject fields = post.optJSONObject("fields");
                String thumbnail = fields.optString("thumbnail");

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
}