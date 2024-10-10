package com.example.galleryapp.feature_auth;

public class UserDto {
    private final int id;
    private final String username;
    private final String phone;
    private final int imagesCount;

    public UserDto(int id, String username, String phone, int imagesCount) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.imagesCount = imagesCount;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPhone() {
        return this.phone;
    }

    public int getImagesCount() {
        return this.imagesCount;
    }

    public int getId() {
        return this.id;
    }
}
