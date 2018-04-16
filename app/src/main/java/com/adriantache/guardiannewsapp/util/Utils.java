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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.adriantache.guardiannewsapp.MainActivity.TAG;

public class Utils {
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
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject post = results.getJSONObject(i);

                //get thumbnail image
                Bitmap thumbnail;
                JSONObject fields = post.getJSONObject("fields");

                //todo decide whether to keep log message here
                Log.i(TAG, "Get image for post " + (i + 1));
                thumbnail = getImage(fields.getString("thumbnail"));

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

    private static Bitmap getImage(String url) {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            } else
                Log.e(MainActivity.TAG, "Incorrect HTTP response code: " +
                        httpURLConnection.getResponseCode());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Cannot parse image URL.", e);
        } catch (IOException e) {
            Log.e(TAG, "Cannot open connection to fetch image.", e);
        } finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "Cannot close input stream.", e);
            }
        }

        return null;
    }
}