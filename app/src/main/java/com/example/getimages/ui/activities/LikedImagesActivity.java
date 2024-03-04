package com.example.getimages.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.getimages.R;
import com.example.getimages.adapter.LikedImagesAdapter;
import com.example.getimages.db.LikedImageEntity;
import com.example.getimages.ui.ImagesViewModel;
import com.example.getimages.repository.ImagesRepository;
import com.example.getimages.ui.ImagesViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class LikedImagesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<LikedImageEntity> ImageUrlList = new ArrayList<>();
    private GridLayoutManager manager;
    private LikedImagesAdapter adapter;
    private ImagesViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_images);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.likedImagesRecyclerView);
        ImageUrlList = new ArrayList<>();
        adapter = new LikedImagesAdapter(this, ImageUrlList);
        manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        ImagesRepository newsRepository = new ImagesRepository(this);
        ImagesViewModelFactory viewModelProviderFactory = new ImagesViewModelFactory(getApplication(),newsRepository);
        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ImagesViewModel.class);
        // Observe the LiveData in activity
        viewModel.getAllLikedImages().observe(this, new Observer<List<LikedImageEntity>>() {
            @Override
            public void onChanged(List<LikedImageEntity> likedImages) {
                Collections.reverse(likedImages);
                // Update UI with the new list of liked images
                ImageUrlList.clear(); // Clear the existing list
                ImageUrlList.addAll(likedImages);
                adapter.notifyDataSetChanged();

                if(ImageUrlList.isEmpty()){
                    TextView message = findViewById(R.id.notice);
                    message.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}