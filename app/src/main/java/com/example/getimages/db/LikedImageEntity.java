package com.example.getimages.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "liked_images")
public class LikedImageEntity {

    @PrimaryKey
    @NonNull
    private String imageUrl;
    private String description;

    public LikedImageEntity(@NonNull String imageUrl, @NonNull String description) {
        this.imageUrl = imageUrl;
        this.description = description;
    }

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    @NonNull
    public String getDescription() {
        return description;
    }
}
