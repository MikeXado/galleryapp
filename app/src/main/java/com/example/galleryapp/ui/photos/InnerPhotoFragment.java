package com.example.galleryapp.ui.photos;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.galleryapp.R;
import com.example.galleryapp.databinding.FragmentInnerPhotoBinding;
import com.example.galleryapp.permission_utils.PermissionUtils;

public class InnerPhotoFragment extends Fragment {
    private FragmentInnerPhotoBinding binding;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInnerPhotoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView imageView = root.findViewById(R.id.full_image_view);

        if (getArguments() != null) {
            imageUri = requireArguments().getParcelable("imageUri", Uri.class);

            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(imageView);
            }
        }

        ImageView backButton = root.findViewById(R.id.back_button);
        NavController navController = NavHostFragment.findNavController(InnerPhotoFragment.this);
        backButton.setOnClickListener(v -> navController.navigateUp());

        Button deleteButton = root.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> {
            if (imageUri != null) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if(!Environment.isExternalStorageManager()) {
                            PermissionUtils.requestManageExternalStoragePermissionNewDevices(requireActivity(), 1001);
                        }else {
                            boolean deleted = deleteImageFromMediaStore(imageUri);
                            if (deleted) {
                                Toast.makeText(getContext(), "Image deleted", Toast.LENGTH_SHORT).show();
                                navController.navigate(R.id.action_innerPhotoFragment_to_navigationPhotos);
                            } else {
                                Toast.makeText(getContext(), "Failed to delete image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        PermissionUtils.requestManageExternalStoragePermissionOldDevices(requireActivity(), 1001);
                    }
            }
        });

        return root;
    }

    // Delete image from MediaStore
    private boolean deleteImageFromMediaStore(Uri imageUri) {
        ContentResolver contentResolver = requireContext().getContentResolver();
        int rowsDeleted = contentResolver.delete(imageUri, null, null);  // Delete the image

        if (rowsDeleted > 0) {
            return true;
        } else {
            return false;
        }
    }

}
