package com.murek.appsocial.adapters;

import static com.murek.appsocial.util.ImageUtils.getRealPathFromURI;

import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.murek.appsocial.R;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder> {

    private final List<String> imageUrls;

    public ImageSliderAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ImageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Log.d("ImageSliderAdapter", "Cargando imagen: " + imageUrl);

        Uri uri = Uri.parse(imageUrl);
        String realPath = getRealPathFromURI(holder.imageView.getContext(), uri);

        if (realPath != null) {
            Glide.with(holder.imageView.getContext())
                    .load(realPath)
                    .centerCrop()
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.upload_img)
                            .error(R.drawable.ic_close))
                    .into(holder.imageView);
        } else {
            // Fallback a la URI original si no se puede obtener el path real
            Glide.with(holder.imageView.getContext())
                    .load(uri)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.upload_img)
                            .error(R.drawable.ic_close))
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull ImageView itemView) {
            super(itemView);
            this.imageView = itemView;
        }
    }
}