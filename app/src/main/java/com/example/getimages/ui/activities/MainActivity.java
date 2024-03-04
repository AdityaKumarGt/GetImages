package com.example.getimages.ui.activities;

import static com.example.getimages.utils.Constants.GRID_COUNT;
import static com.example.getimages.utils.Constants.IMAGES_PER_PAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.getimages.R;
import com.example.getimages.adapter.ImageAdapter;
import com.example.getimages.ui.ImagesViewModel;
import com.example.getimages.models.ImageModel;
import com.example.getimages.repository.ImagesRepository;
import com.example.getimages.ui.ImagesViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ImageModel> list;
    private GridLayoutManager manager;
    private ImageAdapter adapter;
    private int page = 1;
    private boolean isLoading;
    private boolean isLastPage;
    private ImagesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImagesRepository newsRepository = new ImagesRepository(this);
        ImagesViewModelFactory viewModelProviderFactory = new ImagesViewModelFactory(getApplication(),newsRepository);
        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ImagesViewModel.class);


        recyclerView = findViewById(R.id.recyclerview);
        list = new ArrayList<>();
        adapter = new ImageAdapter(this, list);
        manager = new GridLayoutManager(this, GRID_COUNT);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        getData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItem = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItem + firstVisibleItemPosition >= totalItem) && firstVisibleItemPosition >= 0 && totalItem >= IMAGES_PER_PAGE) {
                        page++;
                        getData();
                    }
                }
            }
        });


    }


    private void getData() {
        isLoading = true;

        // Observe LiveData from ViewModel
        viewModel.getImageData(page, IMAGES_PER_PAGE).observe(this, resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    List<ImageModel> imageModels = resource.getData();
                    if (imageModels != null) {
                        list.addAll(imageModels);
                        adapter.notifyDataSetChanged();
                    }

                    isLoading = false;
                    if (list.size() > 0) {
                        isLastPage = list.size() < IMAGES_PER_PAGE;
                    } else
                        isLastPage = true;

                    break;
                case ERROR:
                    String errorMessage = resource.getMessage();
                    Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    break;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        MenuItem search = menu.findItem(R.id.search);


        search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item 'searchView' click here
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            }
        });


        MenuItem liked = menu.findItem(R.id.liked);
        liked.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the liked item click here
                Intent intent = new Intent(MainActivity.this, LikedImagesActivity.class);
                startActivity(intent);
                return true;
            }
        });

        return true;
    }


}