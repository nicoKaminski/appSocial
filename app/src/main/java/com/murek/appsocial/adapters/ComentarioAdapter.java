package com.murek.appsocial.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.murek.appsocial.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ViewHolder> {

    private List<ParseObject> comentarios;

    public ComentarioAdapter(List<ParseObject> comentarios) {
        this.comentarios = comentarios;
    }

    public void setComentarios(List<ParseObject> nuevosComentarios) {
        this.comentarios = nuevosComentarios;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseObject comentario = comentarios.get(position);
        holder.bind(comentario);
    }

    @Override
    public int getItemCount() {
        return comentarios != null ? comentarios.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtComentario;
        private TextView txtUsuario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtComentario = itemView.findViewById(R.id.txtComentario);
            txtUsuario = itemView.findViewById(R.id.txtUsuario);
        }

        public void bind(ParseObject comentario) {
            txtComentario.setText(comentario.getString("texto"));
            ParseUser usuario = comentario.getParseUser("user");
            if (usuario != null) {
                txtUsuario.setText(usuario.getUsername());
            }
        }
    }
}
