package com.example.galleryapp.feature_images;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.galleryapp.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<ImageDto> imageList;

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageDto imageDto = imageList.get(position);
        holder.bind(imageDto);
    }

    @Override
    public int getItemCount() {
        return imageList != null ? imageList.size() : 0;
    }

    public void setImages(List<ImageDto> images) {
        this.imageList = images;
        notifyDataSetChanged(); // Notify that data has changed
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_image);
        }

        public void bind(ImageDto imageDto) {
            Glide.with(imageView.getContext()).load(imageDto.getUrl()).into(imageView);

            imageView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("imageUri", imageDto.getUrl());
                Navigation.findNavController(v).navigate(R.id.innerPhotoFragment, bundle);
            });


        }
    }
}