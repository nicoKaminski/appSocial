package com.murek.appsocial.model;

import java.util.List;

public class Post {
    private int idPost;
    private String titulo;
    private String descripcion;
    private int duracion;
    private String categoria;
    private double presupuesto;
    private List<String> imagenes;

    //constructor 1
    public Post(String titulo, String descripcion, int duracion, String categoria, double presupuesto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.categoria = categoria;
        this.presupuesto = presupuesto;
    }

    //constructor 2
    public Post(String titulo, String descripcion, int duracion, String categoria, double presupuesto,  List<String> imagenes) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.presupuesto = presupuesto;
        this.categoria = categoria;
        this.imagenes = imagenes;
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

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

}
