package com.example.galleryapp.feature_auth;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.galleryapp.data_access_db.DatabaseHelper;
import com.example.galleryapp.feature_images.ImagesService;

public class AuthService {
    private final DatabaseHelper dbHelper;

    public AuthService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean registerUser(String username, String password, String phone, int imagesCount) {
        if (dbHelper.checkUser(username, password)) {
            return false; // User already exists
        }
        dbHelper.addUser(username, password, phone, imagesCount);
        return true;
    }

    public boolean loginUser(String username, String password) {
        return dbHelper.checkUser(username, password);
    }

    public UserDto getUser(String username) {
        return dbHelper.getUserByUsername(username);
    }

    public void saveUserPreferences(String username, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }
}
