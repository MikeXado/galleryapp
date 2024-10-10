package com.example.galleryapp.feature_images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.galleryapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImagesGroupAdapter extends RecyclerView.Adapter<ImagesGroupAdapter.ImageGroupViewHolder> {

    private final List<ImageGroup> imageGroupList;

    public ImagesGroupAdapter(List<ImageGroup> imageGroupList) {
        this.imageGroupList = imageGroupList;
    }

    @NonNull
    @Override
    public ImageGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photos, parent, false);
        return new ImageGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageGroupViewHolder holder, int position) {
        ImageGroup imageGroup = imageGroupList.get(position);
        holder.bind(imageGroup);
    }

    @Override
    public int getItemCount() {
        return imageGroupList.size();
    }

    public void updateDate(List<ImageGroup> newImageGroups) {
        imageGroupList.clear();
        imageGroupList.addAll(newImageGroups);
        notifyDataSetChanged();
    }

    static class ImageGroupViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTextView;
        private final RecyclerView imagesRecyclerView;
        private final ImageAdapter imageAdapter;

        public ImageGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.photo_date);
            imagesRecyclerView = itemView.findViewById(R.id.photo_images);
            imagesRecyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 4)); // Use GridLayoutManager for a grid layout
            imageAdapter = new ImageAdapter(); // Create a new ImageAdapter instance
            imagesRecyclerView.setAdapter(imageAdapter); // Set the adapter to the RecyclerView
        }

        public void bind(ImageGroup imageGroup) {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());

            try {
                // Parse the date string to a Date object
                Date date = inputDateFormat.parse(imageGroup.getKey());
                // Format the Date object to the desired format
                if (date != null) {
                    dateTextView.setText(displayDateFormat.format(date));
                } else {
                    dateTextView.setText(""); // Handle null date if needed
                }
            } catch (ParseException e) {
                e.printStackTrace(); // Handle parse exception
                dateTextView.setText(""); // Fallback to empty if parsing fails
            }

            // Set images in the child adapter
            imageAdapter.setImages(imageGroup.getImages());
        }
    }
}