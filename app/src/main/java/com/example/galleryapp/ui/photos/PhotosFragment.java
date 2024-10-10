package com.example.galleryapp.ui.photos;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.databinding.FragmentPhotosBinding;
import com.example.galleryapp.feature_images.ImagesGroupAdapter;
import com.example.galleryapp.feature_images.ImagesService;

import java.util.ArrayList;

public class PhotosFragment extends Fragment {

    private FragmentPhotosBinding binding;

    @SuppressLint("NotifyDataSetChanged")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PhotosViewModel photosViewModel = new ViewModelProvider(this).get(PhotosViewModel.class);
        binding = FragmentPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SharedPreferences sharedPrefs = requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false);

        NavController navController = NavHostFragment.findNavController(PhotosFragment.this);
        if(!isLoggedIn) {
            navController.navigate(R.id.loginFragment);
        }


        Button logOutButton = root.findViewById(R.id.logout_button);

        logOutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            navController.navigate(R.id.loginFragment);
        });


        if (ImagesService.getInstance().isStoragePermissionGranted(requireActivity())) {
            photosViewModel.loadPhotos(requireActivity());
        } else {
            ImagesService.getInstance().requestStoragePermission(requireActivity());
        }
        final TextView textView = binding.textPhotos;
        photosViewModel.getTitle().observe(getViewLifecycleOwner(), textView::setText);


        RecyclerView recyclerView = binding.recyclerViewPhotos;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final ImagesGroupAdapter photosAdapter = new ImagesGroupAdapter(new ArrayList<>());
        recyclerView.setAdapter(photosAdapter);

        photosViewModel.getPhotoItems().observe(getViewLifecycleOwner(), photosAdapter::updateDate);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

