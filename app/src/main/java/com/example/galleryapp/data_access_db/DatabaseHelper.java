package com.example.galleryapp.data_access_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.galleryapp.feature_auth.UserDto;

import org.mindrot.jbcrypt.BCrypt;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gallery_app.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";

    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_IMAGES_COUNT = "images_count";
    private static final String COLUMN_PHONE = "phone";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_PHONE + " TEXT, " // New column for phone number
                + COLUMN_IMAGES_COUNT + " INTEGER" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(String username, String password, String phone, int imagesCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, hashedPassword);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_IMAGES_COUNT, imagesCount);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }


    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_PASSWORD},
                COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);

            if (passwordIndex >= 0) {
                String hashedPassword = cursor.getString(passwordIndex);

                cursor.close();

                return BCrypt.checkpw(password, hashedPassword);
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return false;
    }


    public UserDto getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_PHONE, COLUMN_IMAGES_COUNT},
                COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
            int imagesCountIndex = cursor.getColumnIndex(COLUMN_IMAGES_COUNT);

            if (idIndex >= 0 && phoneIndex >= 0 && imagesCountIndex >= 0) {
                int id = cursor.getInt(idIndex);
                String phone = cursor.getString(phoneIndex);
                int imagesCount = cursor.getInt(imagesCountIndex);
                cursor.close();
                return new UserDto(id, username, phone, imagesCount);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

}