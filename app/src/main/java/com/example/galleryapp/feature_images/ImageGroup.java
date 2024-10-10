package com.example.galleryapp.feature_images;

import java.util.Date;
import java.util.List;

public class ImageGroup {
    private final String key;
    private final List<ImageDto> images;

    public ImageGroup(String date, List<ImageDto> images) {
        this.key = date;
        this.images = images;
    }

    public String getKey() {
        return key;
    }

    public List<ImageDto> getImages() {
        return images;
    }
}
