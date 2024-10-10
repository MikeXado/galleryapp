package com.example.galleryapp.feature_images;

import android.net.Uri;

import java.util.Date;

public class ImageDto {
    private final Uri url;
    private final String name;
    private final Date creationDate;

    public ImageDto(Uri url, String name, Date creationDate) {
        this.url = url;
        this.name = name;
        this.creationDate = creationDate;
    }

    public Uri getUrl() {
        return this.url;
    }

    public String getName() {
        return this.name;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
