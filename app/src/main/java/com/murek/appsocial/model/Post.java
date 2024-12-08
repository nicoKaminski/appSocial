package com.murek.appsocial.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;


public class Post {
    private int idPost;
    private String titulo;
    private String descripcion;
    private int duracion;
    private String categoria;
    private double presupuesto;
    private List<String> imagen;
    private User user;

    public Post() {
    }

    //constructor 1
    public Post(String titulo, String descripcion, int duracion, String categoria, double presupuesto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.categoria = categoria;
        this.presupuesto = presupuesto;
    }

    //constructor 2
    public Post(String titulo, String descripcion, int duracion, String categoria, double presupuesto,  List<String> imagen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.presupuesto = presupuesto;
        this.categoria = categoria;
        this.imagen = imagen;
    }

    // CON USER
    public Post(User user, String titulo, String descripcion, int duracion, String categoria, double presupuesto,  List<String> imagen) {
        this.user = user;
        this.imagen = imagen;
        this.presupuesto = presupuesto;
        this.categoria = categoria;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.titulo = titulo;
    }


    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public List<String> getImagen() {
        return imagen;
    }

    public void setImagen(List<String> imagen) {
        this.imagen = imagen;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
