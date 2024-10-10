package com.example.galleryapp.ui.photos;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.galleryapp.feature_images.ImageDto;
import com.example.galleryapp.feature_images.ImageGroup;
import com.example.galleryapp.feature_images.ImagesService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class PhotosViewModel extends ViewModel {

    private final MutableLiveData<List<ImageGroup>> photoGroups;
    private final MutableLiveData<String> title;
    public PhotosViewModel() {
        photoGroups = new MutableLiveData<>();
        title = new MutableLiveData<>();
        title.setValue("Photos");
    }

    void loadPhotos(Activity activity) {
        ImagesService imagesService = ImagesService.getInstance();
        List<ImageDto> images = imagesService.getPhotosFromGallery(activity);
        List<ImageGroup> groupedImages = imagesService.groupImagesByDate(images);
        groupedImages.sort((group1, group2) -> {
            return group2.getKey().compareTo(group1.getKey());
        });

        photoGroups.setValue(groupedImages);
    }

    public LiveData<List<ImageGroup>> getPhotoItems() {
        return photoGroups;
    }

    public LiveData<String> getTitle() {
        return title;
    }
}
