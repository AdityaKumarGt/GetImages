package com.example.getimages.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.getimages.R;
import com.example.getimages.ui.activities.FullImageActivity;
import com.example.getimages.db.LikedImageEntity;
import com.example.getimages.utils.LoadImageTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class LikedImagesAdapter extends RecyclerView.Adapter<LikedImagesAdapter.LikedImagesViewHolder> {

    private Context context;
    private ArrayList<LikedImageEntity> list;
    public LikedImagesAdapter(Context context, ArrayList<LikedImageEntity> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public LikedImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.image_item_layout, parent, false);
        return new LikedImagesAdapter.LikedImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedImagesViewHolder holder, int position) {

        String imageUrl = list.get(position).getImageUrl();
        new LoadImageTask(holder.imageView).execute(imageUrl);


        holder.imageView.setOnClickListener(v->{
            Intent intent = new Intent(context, FullImageActivity.class);
            intent.putExtra("image", list.get(position).getImageUrl());
            intent.putExtra("description", list.get(position).getDescription());


            context.startActivity(intent);
        });

    }



    @Override
    public int getItemCount() {
        return list.size();

    }

    public class LikedImagesViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public LikedImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }

}
