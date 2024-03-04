package com.example.getimages.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.getimages.repository.ImagesRepository;
import com.example.getimages.ui.ImagesViewModel;

public class ImagesViewModelFactory implements ViewModelProvider.Factory {

    private final ImagesRepository imagesRepository;
    Application app;

    public ImagesViewModelFactory(Application app, ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ImagesViewModel.class)) {
            return (T) new ImagesViewModel(app, imagesRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
