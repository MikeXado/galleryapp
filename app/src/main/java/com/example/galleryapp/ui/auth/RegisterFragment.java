package com.example.galleryapp.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.galleryapp.R;
import com.example.galleryapp.databinding.FragmentRegisterBinding;
import com.example.galleryapp.feature_auth.AuthService;
import com.example.galleryapp.feature_images.ImageDto;
import com.example.galleryapp.feature_images.ImagesService;

import java.util.List;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    private EditText usernameInput, phoneInput, passwordInput;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Initialize views

        usernameInput = root.findViewById(R.id.username);
        phoneInput = root.findViewById(R.id.phone);
        passwordInput = root.findViewById(R.id.password);
        Button registerButton = root.findViewById(R.id.register_button);
        Button signInButton = root.findViewById(R.id.sign_in_button); // Reference to Sign In button

        // Set login button click listener
        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password) ) {
                ImagesService imagesService = new ImagesService();
                List<ImageDto> images = imagesService.getPhotosFromGallery(requireActivity());
                int imagesSize = images.size();
                AuthService authService = new AuthService(requireContext());
                boolean isRegisterSucessfull = authService.registerUser(username, password, phone, 30);
                if (isRegisterSucessfull) {
                    NavController navController = NavHostFragment.findNavController(RegisterFragment.this);
                    navController.navigate(R.id.action_registerFragment_to_loginFragment);
                } else {
                    Toast.makeText(getContext(), "Invalid login credentials", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        signInButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(RegisterFragment.this);
            navController.navigate(R.id.action_registerFragment_to_loginFragment); // Change to your actual action ID for navigation
        });

        return root;
    }

}
