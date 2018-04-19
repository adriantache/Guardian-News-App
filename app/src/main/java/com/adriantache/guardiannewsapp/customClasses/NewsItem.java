package com.adriantache.guardiannewsapp.customClasses;

import android.graphics.Bitmap;

public class NewsItem {
    private String category;
    private String title;
    private String url;
    private String date;
    private String thumbnailURL;

    public NewsItem(String category, String title, String url, String date, String thumbnailURL) {
        this.category = category;
        this.title = title;
        this.url = url;
        this.date = cleanUpDateString(date);
        this.thumbnailURL = thumbnailURL;
    }

    public NewsItem(String category, String author, String title, String url, String date, String thumbnailURL) {
        this.category = category + " | by " + author;
        this.title = title;
        this.url = url;
        this.date = cleanUpDateString(date);
        this.thumbnailURL = thumbnailURL;
    }

    private String cleanUpDateString(String date) {
        return date.replaceAll("T", "\n").replaceAll("Z", "");
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
