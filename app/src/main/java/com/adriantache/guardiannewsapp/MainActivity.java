package com.adriantache.guardiannewsapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adriantache.guardiannewsapp.adapter.NewsAdapter;
import com.adriantache.guardiannewsapp.customClasses.NewsItem;
import com.adriantache.guardiannewsapp.loader.NewsLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public static final String TAG = "DEBUG-TAG";
    private ListView listView;
    private TextView errorText;
    private String GUARDIAN_URL;

    //todo remove this string
    private static final String testJSON = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":1665,\"startIndex\":1,\"pageSize\":10,\"currentPage\":1,\"pages\":167,\"orderBy\":\"newest\",\"results\":[{\"id\":\"technology/2018/apr/13/android-phone-makers-skip-security-updates-users-smartphone-software-google\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-04-13T10:35:12Z\",\"webTitle\":\"Android phone makers skip Google security updates without telling users – study\",\"webUrl\":\"https://www.theguardian.com/technology/2018/apr/13/android-phone-makers-skip-security-updates-users-smartphone-software-google\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/apr/13/android-phone-makers-skip-security-updates-users-smartphone-software-google\",\"fields\":{\"thumbnail\":\"https://media.guim.co.uk/91219407473ade0330533bbe738ae0e5f3b3d466/0_393_5812_3487/500.jpg\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2018/mar/27/huawei-p20-pro-smartphone-three-cameras-full-body-screen\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-03-27T13:30:18Z\",\"webTitle\":\"Huawei says three cameras are better than one with P20 Pro smartphone\",\"webUrl\":\"https://www.theguardian.com/technology/2018/mar/27/huawei-p20-pro-smartphone-three-cameras-full-body-screen\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/mar/27/huawei-p20-pro-smartphone-three-cameras-full-body-screen\",\"fields\":{\"thumbnail\":\"https://media.guim.co.uk/060b2dcf3bf70c1373c67677ba7f5c36c24130e4/0_0_4400_2640/500.jpg\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2018/mar/08/samsung-galaxy-s9-review-big-screen-phone-camera-top-end-smartphone-battery-life\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-03-08T16:13:57Z\",\"webTitle\":\"Samsung Galaxy S9+ review: the best big-screen smartphone by miles\",\"webUrl\":\"https://www.theguardian.com/technology/2018/mar/08/samsung-galaxy-s9-review-big-screen-phone-camera-top-end-smartphone-battery-life\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/mar/08/samsung-galaxy-s9-review-big-screen-phone-camera-top-end-smartphone-battery-life\",\"fields\":{\"thumbnail\":\"https://media.guim.co.uk/d1b74fcb89db241fbac53b7d3fbcad3e402e39be/0_0_4035_2421/500.jpg\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2018/feb/25/samsung-galaxy-s9-s9-launched-dual-aperture-camera\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-02-25T17:00:15Z\",\"webTitle\":\"Samsung Galaxy S9 and S9+ launched with first dual-aperture camera\",\"webUrl\":\"https://www.theguardian.com/technology/2018/feb/25/samsung-galaxy-s9-s9-launched-dual-aperture-camera\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/feb/25/samsung-galaxy-s9-s9-launched-dual-aperture-camera\",\"fields\":{\"thumbnail\":\"https://media.guim.co.uk/919783e0006a704d5955a2768098103f6a177157/0_0_4238_2543/500.jpg\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2018/feb/19/samsung-galaxy-s9-new-flagship-smartphones-full-screen-design-camera\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-02-19T12:20:59Z\",\"webTitle\":\"Samsung Galaxy S9: everything we think we know about the new smartphones\",\"webUrl\":\"https://www.theguardian.com/technology/2018/feb/19/samsung-galaxy-s9-new-flagship-smartphones-full-screen-design-camera\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/feb/19/samsung-galaxy-s9-new-flagship-smartphones-full-screen-design-camera\",\"fields\":{\"thumbnail\":\"https://media.guim.co.uk/f6348f74445ee74d45ed5888f6d696110c4f4b21/1_0_4345_2608/500.jpg\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2018/feb/09/honor-10-view-review-price--top-smartphone-with-two-day-battery-life\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-02-09T07:00:49Z\",\"webTitle\":\"Honor 10 View review: cut-price top smartphone with two-day battery life\",\"webUrl\":\"https://www.theguardian.com/technology/2018/feb/09/honor-10-view-review-price--top-smartphone-with-two-day-battery-life\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/feb/09/honor-10-view-review-price--top-smartphone-with-two-day-battery-life\",\"fields\":{\"thumbnail\":\"https://media.guim.co.uk/d65798c94af310d8bbb2b8f0aaf041a0e0001cdf/0_0_3990_2394/500.jpg\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2018/jan/13/how-to-quit-your-tech-phone-digital-detox\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-01-13T08:00:00Z\",\"webTitle\":\"How to quit your tech: a beginner's guide to divorcing your phone\",\"webUrl\":\"https://www.theguardian.com/technology/2018/jan/13/how-to-quit-your-tech-phone-digital-detox\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/jan/13/how-to-quit-your-tech-phone-digital-detox\",\"fields\":{\"thumbnail\":\"https://media.guim.co.uk/d66b2e888a0ac24aa8936447db2eefe3c3749749/0_614_3840_2303/500.jpg\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2017/dec/23/smartphones-all-screen-ai\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2017-12-23T08:00:16Z\",\"webTitle\":\"2017: the year smartphones went all-screen and came with baked-in AI\",\"webUrl\":\"https://www.theguardian.com/technology/2017/dec/23/smartphones-all-screen-ai\",\"apiUrl\":\"https://content.guardianapis.com/technology/2017/dec/23/smartphones-all-screen-ai\",\"fields\":{\"thumbnail\":\"https://media.guim.co.uk/7d419fd3f11fea781eec7388f0065fd28f3cd532/154_88_3611_2167/500.jpg\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2017/dec/07/google-pixelbook-review-chromebook-computer-hardware\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2017-12-07T07:00:04Z\",\"webTitle\":\"Google Pixelbook review: the king of Chromebooks is pricey but first rate\",\"webUrl\":\"https://www.theguardian.com/technology/2017/dec/07/google-pixelbook-review-chromebook-computer-hardware\",\"apiUrl\":\"https://content.guardianapis.com/technology/2017/dec/07/google-pixelbook-review-chromebook-computer-hardware\",\"fields\":{\"thumbnail\":\"https://media.guim.co.uk/4ad9229ad54e9aab7d8909dc56af5b4e2971a4fd/3_0_4923_2956/500.jpg\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2017/dec/04/google-pixel-buds-review-bluetooth-earbuds-headphone-apple-airpods-translation\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2017-12-04T07:00:03Z\",\"webTitle\":\"Google Pixel Buds review: Bluetooth earbuds are a missed opportunity\",\"webUrl\":\"https://www.theguardian.com/technology/2017/dec/04/google-pixel-buds-review-bluetooth-earbuds-headphone-apple-airpods-translation\",\"apiUrl\":\"https://content.guardianapis.com/technology/2017/dec/04/google-pixel-buds-review-bluetooth-earbuds-headphone-apple-airpods-translation\",\"fields\":{\"thumbnail\":\"https://media.guim.co.uk/4e1fbb813dd773dd2635f6698ad047714bec38bf/0_0_1185_711/500.jpg\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"}]}}";

    //todo refactor app to move processing code out of loader

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Read Guardian API key from file so we don't just post it to GitHub.
        //If file is not present just use `test` API key.
        String GUARDIAN_API_KEY = "test";
        try {
            if (Arrays.asList(getResources().getAssets().list("")).contains("GuardianAPI.txt")) {
                String temp = readAPIKey();
                if (!TextUtils.isEmpty(temp)) GUARDIAN_API_KEY = temp;
            }
        } catch (IOException e) {
            Log.e(TAG, "Cannot open API key file.", e);
        }
        //then create Guardian URL
        //todo increase page size after fixing the bug
        GUARDIAN_URL =
                "http://content.guardianapis.com/search?order-by=newest&tag=technology%2Fandroid" +
                        "&section=technology&page-size=10&show-fields=thumbnail&api-key=" +
                        GUARDIAN_API_KEY;

        //find views
        listView = findViewById(R.id.listView);
        errorText = findViewById(R.id.errorText);

        listView.setEmptyView(errorText);

        //test network connectivity
        ConnectivityManager cm = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {
            //start Loader to fetch news and populate ListView
            Log.i(TAG, "1. Init loader");

            setAdapter(parseJSON(testJSON));

            //todo reenable loader
            //getSupportLoaderManager().initLoader(0, null, this);
        } else {
            hideProgressBar();
            errorText.setText(R.string.no_internet);
            return;
        }

        //set list view onClick
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem newsItem = (NewsItem) parent.getItemAtPosition(position);

                //open linked website on click
                Uri webPage = Uri.parse(newsItem.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    private String readAPIKey() {
        AssetManager am = getApplicationContext().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open("GuardianAPI.txt");
        } catch (IOException e) {
            Log.e(TAG, "Cannot read API key from file.", e);
        }

        if (inputStream != null) {
            int ch;
            StringBuilder sb = new StringBuilder();
            try {
                while ((ch = inputStream.read()) != -1) {
                    sb.append((char) ch);
                }
            } catch (IOException e) {
                Log.e(TAG, "Cannot read API key InputStream.", e);
            }

            return sb.toString();
        }

        return null;
    }

    private void hideProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    //bind custom adapter to list using results from loader
    private void setAdapter(List<NewsItem> newsList) {
        hideProgressBar();

        NewsAdapter newsAdapter = new NewsAdapter(this, newsList);
        listView.setAdapter(newsAdapter);
        Log.i(MainActivity.TAG, "10. Display data");
    }

    private ArrayList<NewsItem> parseJSON(String jsonResponse) {
        ArrayList<NewsItem> arrayList = new ArrayList<>();
        Log.i(MainActivity.TAG, "xx. Start JSON parsing");
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject post = results.getJSONObject(i);

                //get thumbnail image
//                Bitmap thumbnail = null;
//                JSONObject fields = post.getJSONObject("fields");
//                try {
//                    thumbnail = getImage(fields.getString("thumbnail"));
//                    Log.i(MainActivity.TAG, "7. Get image for post");
//                } catch (IOException e) {
//                    Log.e(TAG, "Cannot get thumbnail.", e);
//                }

                //get other content
                String category = post.getString("sectionName");
                String title = post.getString("webTitle");
                String url = post.getString("webUrl");
                String date = post.getString("webPublicationDate");
//                String author = post.optString("author");
//
//                //use appropriate constructor if author is available (I expect it never is)
//                if (TextUtils.isEmpty(author)) {
//                    arrayList.add(new NewsItem(category, title, url, date, thumbnail));
//                } else {
//                    arrayList.add(new NewsItem(category, author, title, url, date, thumbnail));
//                }

                arrayList.add(new NewsItem(category, title, url, date));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Cannot parse JSON.", e);
        }

        Log.i(MainActivity.TAG, "8. Finish JSON parsing");
        return arrayList;
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(TAG, "2. Start loader");
        return new NewsLoader(this, GUARDIAN_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String JSONResponse) {
        Log.i(MainActivity.TAG, "9a. Send list to ArrayAdapter for display");
        if (!TextUtils.isEmpty(JSONResponse)) setAdapter(parseJSON(JSONResponse));
        else {
            errorText.setText(R.string.no_news);
        }
        Log.i(MainActivity.TAG, "9b. Send list to ArrayAdapter for display");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        setAdapter(new ArrayList<NewsItem>());
    }
}
