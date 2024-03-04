package com.example.getimages.ui.activities;

import static com.example.getimages.utils.Constants.GRID_COUNT;
import static com.example.getimages.utils.Constants.IMAGES_PER_PAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;

import android.widget.EditText;
import android.widget.Toast;

import com.example.getimages.R;
import com.example.getimages.adapter.ImageAdapter;
import com.example.getimages.ui.ImagesViewModel;
import com.example.getimages.models.ImageModel;
import com.example.getimages.models.SearchModel;
import com.example.getimages.repository.ImagesRepository;
import com.example.getimages.ui.ImagesViewModelFactory;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ImageModel> list;
    private GridLayoutManager manager;
    private ImageAdapter adapter;

    private ProgressDialog dialog;
    private ImagesViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();


        ImagesRepository newsRepository = new ImagesRepository(this);
        ImagesViewModelFactory viewModelProviderFactory = new ImagesViewModelFactory(getApplication(),newsRepository);
        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ImagesViewModel.class);

        recyclerView = findViewById(R.id.SearchRecyclerView);
        list = new ArrayList<>();
        adapter = new ImageAdapter(this, list);
        manager = new GridLayoutManager(this, GRID_COUNT);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.setCancelable(true);
        dialog.create();


        SearchView searchView = findViewById(R.id.searchView);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        // Set the hint text color
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.show();
                searchData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }



    private void searchData(String query) {

        // Observe LiveData from ViewModel
        viewModel.searchImages(query, IMAGES_PER_PAGE).observe(this, resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    SearchModel searchModel = resource.getData();
                    if (searchModel != null) {
                        list.clear();
                        list.addAll(searchModel.getResults());
                        adapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                    break;
                case ERROR:
                    String errorMessage = resource.getMessage();
                    Toast.makeText(SearchActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    break;
            }
        });
    }



}