package com.example.galleryapp.feature_images;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ImagesService {


    public static class SingletonHolder {
        public static final ImagesService INSTANCE = new ImagesService();
    }

    public static ImagesService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void requestStoragePermission(Activity activity) {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {

            // If permission is not granted, request the permission
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    100
            );
        }
    }

    // Optional: Check if permission is granted
    public boolean isStoragePermissionGranted(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES)
                == PackageManager.PERMISSION_GRANTED;
    }


    public List<ImageDto> getPhotosFromGallery(Activity activity) {
        List<ImageDto> images = new ArrayList<>();
        if (isStoragePermissionGranted(activity)) {
            ContentResolver contentResolver = activity.getContentResolver();

            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.DATE_MODIFIED
            };

            Cursor cursor = contentResolver.query(imageUri, projection, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
                    long dateTakenMillis = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
                    long dateModifiedMillis = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED));
                    LocalDateTime localDateTime;
                    if (dateTakenMillis > 0) {
                        localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTakenMillis), ZoneId.systemDefault());
                    } else {
                        localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateModifiedMillis * 1000), ZoneId.systemDefault());
                    }


                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    // Use the image URI as needed (e.g., display in an ImageView)
                    Log.d("ImagesService", "Image URI: " + uri.toString() + ", Name: " + name);
                    images.add(new ImageDto(uri, name, date));
                }
                cursor.close();

            }
        } else {
            // If permission is not granted, request it
            requestStoragePermission(activity);
        }

        return images;
    }

    public List<ImageGroup> groupImagesByDate(List<ImageDto> photos) {
        Map<String, List<ImageDto>> groupedPhotos = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (ImageDto photo : photos) {
            Date date = photo.getCreationDate();
            String key = dateFormat.format(date);
            if (!groupedPhotos.containsKey(key)) {
                groupedPhotos.put(key, new ArrayList<>());
            }
            groupedPhotos.get(key).add(photo);
        }

        List<ImageGroup> photoGroups = new ArrayList<>();
        for (Map.Entry<String, List<ImageDto>> entry : groupedPhotos.entrySet()) {
            photoGroups.add(new ImageGroup(entry.getKey(), entry.getValue()));
        }


        return photoGroups;
    }
}
