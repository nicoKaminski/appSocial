package com.murek.appsocial.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.murek.appsocial.R;
import com.murek.appsocial.model.Post;
import com.murek.appsocial.model.User;
import com.murek.appsocial.providers.PostProvider;
import com.murek.appsocial.view.PostDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tvTitulo.setText(post.getTitulo());
        holder.tvDescripcion.setText(post.getDescripcion());
        Log.d("PostAdapter", "Título: " + post.getImagen());
        if (post.getImagen() != null) {
            if (post.getImagen().size() > 0) {
                Picasso.get()
                        .load(post.getImagen().get(0))
                        .into(holder.ivImage1);
                holder.ivImage1.setVisibility(View.VISIBLE);
            }
            if (post.getImagen().size() > 1) {
                Picasso.get()
                        .load(post.getImagen().get(1)) // Cargar la segunda imagen
                        .into(holder.ivImage2);
                holder.ivImage2.setVisibility(View.VISIBLE);
            }
            if (post.getImagen().size() > 2) {
                Picasso.get()
                        .load(post.getImagen().get(2)) // Cargar la tercera imagen
                        .into(holder.ivImage3);
                holder.ivImage3.setVisibility(View.VISIBLE);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            PostProvider postProvider = new PostProvider();

//            LiveData<Post> postDetailLiveData = postProvider.getPostDetail(post.getId());
            int postId = post.getIdPost();
            LiveData<Post> postDetailLiveData = postProvider.getPostDetail(String.valueOf(postId));
            postDetailLiveData.observe((LifecycleOwner) context, postDetail -> {
                if (postDetail != null) {
                    Log.d("Postadapter", postDetail.getIdPost() + postDetail.getTitulo());
                    Intent intent = new Intent(context, PostDetailActivity.class);

                    // Datos del Post
                    Log.d("Postadapter", postDetail.getIdPost() + postDetail.getTitulo());
                    intent.putExtra("titulo", postDetail.getTitulo());
                    intent.putExtra("descripcion", postDetail.getDescripcion());
                    intent.putExtra("categoria", postDetail.getCategoria());
                    intent.putExtra("duracion", postDetail.getDuracion());
                    intent.putExtra("presupuesto", postDetail.getPresupuesto());

                    // Datos del Usuario
                    User user = postDetail.getUser();

                    if (user != null) {
                        Log.d("Postadapter", user.getUserName());
                        intent.putExtra("username", user.getUserName());
                        intent.putExtra("email", user.getUserEmail());
                        intent.putExtra("fotoperfil", user.getUserFotoPerfil());
                    } else {
                        Log.d("Postadapter", "null");
                    }
                    // Lista de imágenes
                    ArrayList<String> imageUrls = new ArrayList<>(postDetail.getImagen());
                    intent.putStringArrayListExtra("imagenes", imageUrls);

                    context.startActivity(intent);
                } else {
                    Log.e("PostDetail", "No se pudo obtener el detalle del post.");
                }
            });
        });
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion;
        ImageView ivImage1, ivImage2, ivImage3;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            ivImage1 = itemView.findViewById(R.id.ivImage1);
            ivImage2 = itemView.findViewById(R.id.ivImage2);
            ivImage3 = itemView.findViewById(R.id.ivImage3);
        }
    }

}
