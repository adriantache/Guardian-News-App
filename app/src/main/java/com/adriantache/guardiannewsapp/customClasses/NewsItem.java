package com.adriantache.guardiannewsapp.customClasses;

import android.graphics.Bitmap;

public class NewsItem {
    private String category;
    private String title;
    private String url;
    private String date;
    private Bitmap thumbnail;

    public NewsItem(String category, String title, String url, String date, Bitmap thumbnail) {
        this.category = category;
        this.title = title;
        this.url = url;
        this.date = cleanUpDateString(date);
        this.thumbnail = thumbnail;
    }

    public NewsItem(String category, String author, String title, String url, String date, Bitmap thumbnail) {
        this.category = category + " | by " + author;
        this.title = title;
        this.url = url;
        this.date = cleanUpDateString(date);
        this.thumbnail = thumbnail;
    }

    //todo remove this constructor after fixing bug
    public NewsItem(String category, String title, String url, String date) {
        this.category = category;
        this.title = title;
        this.url = url;
        this.date = cleanUpDateString(date);
    }

    private String cleanUpDateString(String date) {
        return date.replaceAll("T", "\n").replaceAll("Z", "");
    }

    public Bitmap getThumbnail() {
        return thumbnail;
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
