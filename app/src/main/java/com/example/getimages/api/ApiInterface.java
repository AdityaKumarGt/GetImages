package com.example.getimages.api;

import com.example.getimages.models.ImageModel;
import com.example.getimages.models.SearchModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/photos")
    Call<List<ImageModel>> getImages(
            @Header("authorization")String authorization,
            @Query("page") int page,
            @Query("per_page") int perPage
    );


    @GET("/search/photos")
    Call<SearchModel> searchImages(
            @Header("authorization")String authorization,
            @Query("query") String query,
            @Query("per_page") int perPage
    );
}
