package com.example.getimages.ui.activities;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.getimages.R;
import com.example.getimages.db.LikedImageEntity;
import com.example.getimages.ui.ImagesViewModel;
import com.example.getimages.repository.ImagesRepository;
import com.example.getimages.ui.ImagesViewModelFactory;
import com.example.getimages.utils.LoadImageTask;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FullImageActivity extends AppCompatActivity {

    private static int REQUEST_CODE = 100;

    private String imageUrl, description;
    ImageView image;

    private ImagesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_image);
        getSupportActionBar().hide();

        Set<String> likedImageUrls = new HashSet<>();

        ImagesRepository newsRepository = new ImagesRepository(this);
        ImagesViewModelFactory viewModelProviderFactory = new ImagesViewModelFactory(getApplication(),newsRepository);
        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ImagesViewModel.class);

        ImageView addToLike = findViewById(R.id.like);
        ImageView download = findViewById(R.id.download);
        image = findViewById(R.id.zoomageView);
        ImageView backButton = findViewById(R.id.backButton);
        ImageView info = findViewById(R.id.info);
        ImageView share = findViewById(R.id.share);
        CardView setWallpaperButton = findViewById(R.id.setAsWallpaperBtn);

        imageUrl = getIntent().getStringExtra("image");
        description = getIntent().getStringExtra("description");

        new LoadImageTask(image).execute(imageUrl);




        // Observe the LiveData in activity
        viewModel.getAllLikedImages().observe(this, new Observer<List<LikedImageEntity>>() {
            @Override
            public void onChanged(List<LikedImageEntity> likedImages) {
                // Clear the existing set and re-populate it with the latest liked images
                likedImageUrls.clear();
                for (LikedImageEntity likedImage : likedImages) {
                    likedImageUrls.add(likedImage.getImageUrl());
                }

                if (likedImageUrls.contains(imageUrl)) {
                    addToLike.setImageResource(R.drawable.black_heart);
                } else {
                    addToLike.setImageResource(R.drawable.heart);
                }
            }
        });

        addToLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (likedImageUrls.contains(imageUrl)) {
                    // Remove the image.png URL from the list and update the button
                    LikedImageEntity imageToDelete = new LikedImageEntity(imageUrl, description);
                    viewModel.deleteLikedImage(imageToDelete);
                    addToLike.setImageResource(R.drawable.heart);
                } else {
                    // Add the image.png URL to the list and update the button
                    LikedImageEntity newLikedImage = new LikedImageEntity(imageUrl, description);
                    viewModel.insertLikedImage(newLikedImage);
                    addToLike.setImageResource(R.drawable.black_heart);
                }

            }
        });


        share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "It's an Image, click to get: \n " + imageUrl);
                Intent chooser = Intent.createChooser(intent, "Share this Image using...");
                startActivity(chooser);

            }
        });


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and configure the PopupWindow
                View popupView = getLayoutInflater().inflate(R.layout.info_dialog_layout, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                );
                // Set the background drawable to detect outside clicks
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Allow the PopupWindow to be dismissed when tapping outside
                popupWindow.setOutsideTouchable(true);

                // Calculate the xOffset and yOffset to position the popup at the top right corner
                int xOffset = 170;
                int yOffset = 120;

                // Show the popup at the specified location
                popupWindow.showAtLocation(popupView, Gravity.CENTER | Gravity.TOP, xOffset, yOffset);
                // Customize the pop-up message appearance and behavior here
                TextView messageText = popupView.findViewById(R.id.messageText); // Find your TextView

                // Set the message text dynamically (if needed)
                String originalText = description;
                String capitalizedText = originalText.substring(0, 1).toUpperCase() + originalText.substring(1);
                messageText.setText(capitalizedText);


                // Show the pop-up message at the button's location
                popupWindow.showAsDropDown(info);

                // Close the pop-up when the user taps outside of it
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call finish to close the current activity and return to the previous one
                finish();
            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String readImagePermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                        Manifest.permission.READ_MEDIA_IMAGES :
                        Manifest.permission.READ_EXTERNAL_STORAGE;

                if (ContextCompat.checkSelfPermission(FullImageActivity.this, readImagePermission) == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, so proceed to save the image.png
                    saveImageToStorage();
                } else {
                    // Permission is not granted, request it
                    askPermission();
                }
            }
        });

        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper();
            }
        });


    }



    private void saveImageToStorage() {
        Drawable drawable = image.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "GetImages_images");

            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    Toast.makeText(this, "Error creating directory", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String fileName = "your_image_" + System.currentTimeMillis() + ".jpg";
            File file = new File(directory, fileName);

            try {
                OutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();

                MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, null);
                Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving image.png", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Image could not be saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void setWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = image.getDrawable(); // Get the Drawable from the ImageView

        if (wallpaperDrawable instanceof BitmapDrawable) {
            Bitmap wallpaperBitmap = ((BitmapDrawable) wallpaperDrawable).getBitmap();

            try {
                wallpaperManager.setBitmap(wallpaperBitmap);
                Toast.makeText(this, "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to set wallpaper", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Image format not supported", Toast.LENGTH_SHORT).show();
        }
    }

    private void askPermission() {
        String writePermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                Manifest.permission.READ_MEDIA_IMAGES :
                Manifest.permission.WRITE_EXTERNAL_STORAGE;
        ActivityCompat.requestPermissions(FullImageActivity.this, new String[]{writePermission, Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            // Check if all requested permissions are granted.
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // All permissions are granted, proceed with operation.
                saveImageToStorage();
            } else {
                Toast.makeText(FullImageActivity.this, "Please provide the required permissions", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



















}
