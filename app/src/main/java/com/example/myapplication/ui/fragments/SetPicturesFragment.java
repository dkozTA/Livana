package com.example.myapplication.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SetPicturesFragment extends Fragment {

    private static final int REQUEST_PERMISSION_CODE = 101;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<Intent> pickMultipleImagesLauncher;

    private Bitmap mainBM;
    private List<Bitmap> subList = new ArrayList<Bitmap>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_pictures, container, false);

        // Khởi tạo launcher để xử lý kết quả chọn ảnh (một ảnh)
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                // Xử lý ảnh được chọn (một ảnh)
                Log.d("Image URI", imageUri.toString());
            }
        });

        // Khởi tạo launcher để xử lý kết quả chọn nhiều ảnh
        pickMultipleImagesLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                ClipData clipData = result.getData().getClipData();
                if (clipData != null) {
                    // Nếu chọn nhiều ảnh
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        // Xử lý từng ảnh
                        Log.d("Multiple Image URI", imageUri.toString());

                        Bitmap newSubPhoto = getBitmapFromUri(imageUri);
                        subList.add(newSubPhoto);
                        updateDisplay();
                    }
                } else {
                    // Nếu chỉ chọn một ảnh
                    Uri imageUri = result.getData().getData();
                    Log.d("Image URI", imageUri.toString());

                    mainBM = getBitmapFromUri(imageUri);
                    updateDisplay();
                }
            }
        });

        // Button chọn một ảnh
        ImageButton pickImageButton = view.findViewById(R.id.pickMainPhoto);
        pickImageButton.setOnClickListener(v -> {
            // Kiểm tra và yêu cầu quyền trước khi mở gallery
            if (checkAndRequestPermissions()) {
                // Mở gallery chọn một ảnh
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(intent);
            }
        });

        // Button chọn nhiều ảnh
        ImageButton pickMultipleImagesButton = view.findViewById(R.id.pickSubPhoto);
        pickMultipleImagesButton.setOnClickListener(v -> {
            // Kiểm tra và yêu cầu quyền trước khi mở gallery
            if (checkAndRequestPermissions()) {
                // Mở gallery chọn nhiều ảnh
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                pickMultipleImagesLauncher.launch(intent);
            }
        });

        return view;
    }

    public Bitmap getBitmapFromUri(Uri uri){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Kiểm tra và yêu cầu quyền truy cập
    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_CODE);
                return false;
            }

            return true;
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Nếu chưa có quyền, yêu cầu quyền
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                return false;
            }

            return true;
        }
    }

    public void updateDisplay() {

    }
}
