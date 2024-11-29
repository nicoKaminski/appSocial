package com.murek.appsocial.view;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.murek.appsocial.R;
import com.murek.appsocial.adapters.ImageAdapter;
import com.murek.appsocial.databinding.ActivityPostBinding;
import com.murek.appsocial.model.Post;
import com.murek.appsocial.util.Validaciones;
import com.murek.appsocial.viewModel.PostViewModel;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;
    private PostViewModel postViewModel;
    private static final int REQUEST_IMAGE = 1;
    private List<String> imagenesUrl = new ArrayList<>();
    private String categoria;
    private ImageAdapter adapter;
    private ActivityResultLauncher<Intent> galleryLauncher;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setupRecyclerView() {

    }

    private void setupViewModel() {

    }

    private void setupCategorySpinner() {

    }

    private void setupGalleryLauncher() {

    }

    private void publicarPost() {

    }

    private void updateRecyclerViewVisibility() {

    }

    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
