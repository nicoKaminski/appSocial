package com.murek.appsocial.model;

import android.os.Bundle;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


@ParseClassName("Post")
public class Post extends ParseObject {
//    private int idPost;
//    private String titulo;
//    private String descripcion;
//    private int duracion;
//    private String categoria;
//    private double presupuesto;
//    private List<String> imagen;
//    private User user;

    public Post() {
    }


    public String getIdPost() {
        return getObjectId();
    }

    public String getTituloPost() {
        return getString("titulo");
    }

    public void setTituloPost(String titulo) {
        put("titulo", titulo);
    }

    public String getDescripcionPost() {
        return getString("descripcion");
    }

    public void setDescripcionPost(String descripcion) {
        put("descripcion", descripcion);
    }

    public int getDuracionPost() {
        return getInt("duracion");
    }

    public void setDuracionPost(int duracion) {
        put("duracion", duracion);
    }

    public String getCategoriaPost() {
        return getString("categoria");
    }

    public void setCategoriaPost(String categoria) {
        put("categoria", categoria);
    }

    public double getPresupuestoPost() {
        return getDouble("presupuesto");
    }

    public void setPresupuestoPost(double presupuesto) {
        put("presupuesto", presupuesto);
    }

    public List<String> getImagenPost() {
        return getList("imagenes");
    }

    public void setImagenPost(List<String> imagen) {
        put("imagenes", imagen);
    }

    public User getUser() {
        return (User)getParseObject("user");
    }

    public void setUser(User user) {
        put("user", user);
    }

    // Método para exportar los datos del post como un Bundle
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("titulo", getTituloPost());
        bundle.putString("descripcion", getDescripcionPost());
        bundle.putString("categoria", getCategoriaPost());
        bundle.putInt("duracion", getDuracionPost());
        bundle.putDouble("presupuesto", getPresupuestoPost());

        // Datos del Usuario
        User user = getUser();
        if (user != null) {
            bundle.putString("username", user.getUserName());
            bundle.putString("email", user.getUserEmail());
            bundle.putString("foto_perfil", user.getString("foto_perfil"));
        }

        // Lista de imágenes
        bundle.putStringArrayList("imagenes", new ArrayList<>(getImagenPost()));
        return bundle;
    }

}


