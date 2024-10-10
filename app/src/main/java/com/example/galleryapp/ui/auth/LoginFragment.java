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
import com.example.galleryapp.databinding.FragmentLoginBinding;
import com.example.galleryapp.feature_auth.AuthService;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private EditText usernameInput, passwordInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Initialize views
        usernameInput = root.findViewById(R.id.username);
        passwordInput = root.findViewById(R.id.password);
        Button loginButton = root.findViewById(R.id.login_button);
        Button signUpButton = root.findViewById(R.id.sign_up_button);
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                AuthService authService = new AuthService(requireContext());
                if (authService.loginUser(username, password)) {
                    authService.saveUserPreferences(username, requireActivity());
                    NavController navController = NavHostFragment.findNavController(LoginFragment.this);
                    navController.navigate(R.id.action_loginFragment_to_navigation_photos);
                } else {
                    Toast.makeText(getContext(), "Invalid login credentials", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        signUpButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(LoginFragment.this);
            navController.navigate(R.id.action_loginFragment_to_registerFragment); // Change to your actual action ID for navigation
        });

        return root;
    }

    // Mock authentication function
    private boolean authenticateUser(String username, String password) {
        // In a real application, check with the SQLite database or other auth services
        return username.equals("user") && password.equals("password");
    }
}