package com.example.getimages.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {LikedImageEntity.class}, version = 1, exportSchema = false)
public abstract class LikedImageDatabase extends RoomDatabase {

    public abstract LikedImageDao likedImageDao();

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile LikedImageDatabase INSTANCE;

    public static synchronized LikedImageDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LikedImageDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    LikedImageDatabase.class, "liked_image_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
