package com.example.mybooks;

import android.graphics.drawable.Drawable;

import java.net.URL;

public class Book {
    private String mUrl;
    private String mTitle;
    private String mAuthor;
    private Drawable mImage;

    public Book(String title, String author, Drawable image, String url) {
        mTitle = title;
        mAuthor = author;
        mImage = image;
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public Drawable getImage() {
        return mImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
