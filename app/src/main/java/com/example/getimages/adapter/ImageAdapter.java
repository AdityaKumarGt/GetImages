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
import com.example.getimages.ui.activities.FullImageActivity;
import com.example.getimages.R;
import com.example.getimages.models.ImageModel;
import com.example.getimages.utils.LoadImageTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private ArrayList<ImageModel> list;
    public ImageAdapter(Context context, ArrayList<ImageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.image_item_layout, parent, false);
        return new ImageViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {

        String imageUrl = list.get(position).getUrls().getRegular();
        new LoadImageTask(holder.imageView).execute(imageUrl);


        holder.imageView.setOnClickListener(v->{
            Intent intent = new Intent(context, FullImageActivity.class);
            intent.putExtra("image", list.get(position).getUrls().getRegular());
            intent.putExtra("description", list.get(position).getAlt_description());

            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return list.size();

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }








}




