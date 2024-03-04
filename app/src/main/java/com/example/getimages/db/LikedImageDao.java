package com.example.getimages.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LikedImageDao {
    @Query("SELECT * FROM liked_images")
    LiveData<List<LikedImageEntity>> getAllLikedImages();

    @Insert
    void insert(LikedImageEntity likedImage);

    @Delete
    void delete(LikedImageEntity likedImage);
}

