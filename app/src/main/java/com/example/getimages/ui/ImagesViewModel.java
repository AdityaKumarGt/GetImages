package com.example.getimages.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.getimages.db.LikedImageEntity;
import com.example.getimages.models.ImageModel;
import com.example.getimages.models.SearchModel;
import com.example.getimages.repository.ImagesRepository;
import com.example.getimages.utils.Resource;
import java.util.List;

public class ImagesViewModel extends AndroidViewModel {

    private final ImagesRepository imagesRepository;
    public ImagesViewModel(Application app, ImagesRepository imagesRepository) {
        super(app);
        this.imagesRepository = imagesRepository;
    }

    public LiveData<List<LikedImageEntity>> getAllLikedImages() {
        return imagesRepository.getAllLikedImages();
    }

    public void insertLikedImage(LikedImageEntity likedImage) {
        imagesRepository.insertLikedImage(likedImage);
    }

    public void deleteLikedImage(LikedImageEntity likedImage) {
        imagesRepository.deleteLikedImage(likedImage);
    }

    public LiveData<Resource<List<ImageModel>>> getImageData(int page, int perPage) {
        return imagesRepository.getImageData(page, perPage);
    }

    public LiveData<Resource<SearchModel>> searchImages(String query, int perPage) {
        return imagesRepository.searchImages(query, perPage);
    }
}


