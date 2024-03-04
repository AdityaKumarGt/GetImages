package com.example.getimages.repository;

import static com.example.getimages.utils.Constants.API_KEY;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.getimages.api.ApiInterface;
import com.example.getimages.api.ApiUtilities;
import com.example.getimages.db.LikedImageDao;
import com.example.getimages.db.LikedImageDatabase;
import com.example.getimages.db.LikedImageEntity;
import com.example.getimages.models.ImageModel;
import com.example.getimages.models.SearchModel;
import com.example.getimages.utils.Resource;
import java.util.List;

public class ImagesRepository {

    private LikedImageDao likedImageDao;
    private final ApiInterface apiInterface;

    public ImagesRepository(Context context) {
        LikedImageDatabase database = LikedImageDatabase.getInstance(context);
        this.likedImageDao = database.likedImageDao();
        this.apiInterface =  ApiUtilities.getApiInterface();

    }


    public LiveData<List<LikedImageEntity>> getAllLikedImages() {
        return likedImageDao.getAllLikedImages();
    }

    public void insertLikedImage(LikedImageEntity likedImage) {
        LikedImageDatabase.databaseWriteExecutor.execute(() -> {
            likedImageDao.insert(likedImage);
        });
    }

    public void deleteLikedImage(LikedImageEntity likedImage) {
        LikedImageDatabase.databaseWriteExecutor.execute(() -> {
            likedImageDao.delete(likedImage);
        });
    }

    public MutableLiveData<Resource<List<ImageModel>>> getImageData(int page, int perPage) {
        MutableLiveData<Resource<List<ImageModel>>> imageData = new MutableLiveData<>();
        String authorization = "Authorization: Client-ID " + API_KEY;

        apiInterface.getImages(authorization, page, perPage)
                .enqueue(new Callback<List<ImageModel>>() {
                    @Override
                    public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                        if (response.isSuccessful()) {
                            imageData.postValue(Resource.success(response.body()));
                        } else {
                            // Handle non-successful response
                            imageData.postValue(Resource.error("Failed to fetch data", null));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                        imageData.postValue(Resource.error("Network request failed", null));
                    }
                });

        return imageData;
    }

    public MutableLiveData<Resource<SearchModel>> searchImages(String query, int perPage) {
        MutableLiveData<Resource<SearchModel>> imageData = new MutableLiveData<>();
        String authorization = "Authorization: Client-ID " + API_KEY;

        apiInterface.searchImages(authorization, query, perPage)
                .enqueue(new Callback<SearchModel>(){
                    @Override
                    public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                        if (response.isSuccessful()) {
                            imageData.postValue(Resource.success(response.body()));
                        } else {
                            imageData.postValue(Resource.error("Failed to fetch data", null));
                        }
                    }
                    @Override
                    public void onFailure(Call<SearchModel> call, Throwable t) {
                        imageData.postValue(Resource.error("Network request failed", null));
                    }
                });

        return imageData;
    }

}




