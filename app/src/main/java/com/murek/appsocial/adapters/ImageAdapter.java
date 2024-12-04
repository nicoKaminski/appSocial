package com.murek.appsocial.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.murek.appsocial.R;
import com.murek.appsocial.view.PostActivity;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<String> imagenesUrl;
    private PostActivity postActivity;

    public ImageAdapter(List<String> imagenesUrl, PostActivity postActivity) {
        this.imagenesUrl = imagenesUrl;
        this.postActivity = postActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = imagenesUrl.get(position);

        try {
            URL url = new URL(imageUrl);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
            holder.ivImage1.setImageBitmap(bitmap);
            holder.ivImage2.setImageBitmap(bitmap);
            holder.ivImage3.setImageBitmap(bitmap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Cambia la propiedad visibility a visible
        holder.ivImage1.setVisibility(View.VISIBLE);
        holder.ivImage2.setVisibility(View.VISIBLE);
        holder.ivImage3.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return imagenesUrl.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage1;
        public ImageView ivImage2;
        public ImageView ivImage3;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage1 = itemView.findViewById(R.id.ivImage1);
            ivImage2 = itemView.findViewById(R.id.ivImage2);
            ivImage3 = itemView.findViewById(R.id.ivImage3);
        }
    }

}
