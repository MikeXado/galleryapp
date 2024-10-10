package com.example.galleryapp.ui.photos;

import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.galleryapp.R;
import com.example.galleryapp.databinding.FragmentInnerPhotoBinding;
import com.example.galleryapp.databinding.FragmentPhotosBinding;

import java.nio.Buffer;

public class InnerPhotoFragment extends Fragment {
    private FragmentInnerPhotoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInnerPhotoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView imageView = root.findViewById(R.id.full_image_view);

        if(getArguments() != null) {
            Uri imageUri = requireArguments().getParcelable("imageUri", Uri.class);

            if(imageUri != null) {
                Glide.with(this).load(imageUri).into(imageView);
            }
        }

        ImageView backButton = root.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(InnerPhotoFragment.this);
            navController.navigateUp();
        });

        return root;
    }
}
