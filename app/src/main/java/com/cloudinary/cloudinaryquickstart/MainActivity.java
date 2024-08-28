package com.cloudinary.cloudinaryquickstart;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import androidx.navigation.ui.AppBarConfiguration;

import com.cloudinary.cloudinaryquickstart.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String cloudName = "<your_cloud_name>";
    private String url;
    private String publicId = "<your_public_id>";

    private String uploadPreset = "<your_upload_preset>";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle("Cloudinary Quickstart");
        setSupportActionBar(binding.toolbar);

        initCloudinary();
        generateUrl();
        uploadImage();
    }

    private void initCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        MediaManager.init(this, config);
    }

    private void generateUrl() {
        url = MediaManager.get().url().transformation(new Transformation().effect("sepia")).generate(publicId);
        Glide.with(this).load(url).into(binding.mainContant.generatedImageview);
    }

    private void uploadImage() {
        Uri uri = Uri.parse("android.resource://com.cloudinary.cloudinaryquickstart/drawable/cloudinary_logo");
        MediaManager.get().upload(uri).unsigned(uploadPreset).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                Log.d("Cloudinary Quickstart", "Upload start");
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Log.d("Cloudinary Quickstart", "Upload progress");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                Log.d("Cloudinary Quickstart", "Upload success");
                String url = (String) resultData.get("secure_url");
                Glide.with(getApplicationContext()).load(url).into(binding.mainContant.uploadedImageview);
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.d("Cloudinary Quickstart", "Upload failed");
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        }).dispatch();
    }
}