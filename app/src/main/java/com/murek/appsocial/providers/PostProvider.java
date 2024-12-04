package com.murek.appsocial.providers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.murek.appsocial.model.Post;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostProvider {

    public LiveData<String> addPost(Post post) {
        MutableLiveData<String> result = new MutableLiveData<>();
        ParseObject postObject = new ParseObject("Post");

        postObject.put("user", ParseUser.getCurrentUser());
        postObject.put("titulo", post.getTitulo());
        postObject.put("descripcion", post.getDescripcion());
//        postObject.put("idUser", post.getIdUser());
        postObject.put("duracion", post.getDuracion());
        postObject.put("categoria", post.getCategoria());
        postObject.put("presupuesto", post.getPresupuesto());
//        postObject.put("imagenes", post.getImagenes());

        postObject.saveInBackground(imgError -> {
            if (imgError == null) {
                ParseRelation<ParseObject> relation = postObject.getRelation("imagenes");
                for (String url : post.getImagenes()) {
                    ParseObject imgObject = new ParseObject("Imagen");
                    imgObject.put("url", url);
                    imgObject.saveInBackground(imgSaveError -> {
                        if (imgSaveError == null) {
                            relation.add(imgObject);
                            postObject.saveInBackground(saveError -> {
                                if (saveError == null) {
                                    result.setValue("Post publicado");
                                } else {
                                    result.setValue("Error al guardar la relacion "+saveError.getMessage());
                                }
                            });
                        } else {
                            result.setValue("Error al guardar la imagen "+imgSaveError.getMessage());
                        }
                    });
                }
            } else {
                result.setValue("Error al guardar el post "+imgError.getMessage());
            }
        });
        return result;
    }

    public LiveData<List<Post>> getPostByCurrentUser() {
        MutableLiveData<List<Post>> posts = new MutableLiveData<>();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            posts.setValue(new ArrayList<>());
            return posts;
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("user", currentUser);
        query.include("user");
        query.findInBackground((postsList, error) -> {
            if (error == null) {
                List<Post> postList = new ArrayList<>();
                for (ParseObject postObject : postsList) {
                    Post post = new Post(
                            postObject.getString("titulo"),
                            postObject.getString("descripcion"),
                            postObject.getInt("duracion"),
                            postObject.getString("categoria"),
                            postObject.getDouble("presupuesto")
                    );

                    ParseRelation<ParseObject> imagenesRelation = postObject.getRelation("imagenes");
                    try{
                        List<ParseObject> imagenes = imagenesRelation.getQuery().find();
                        List<String> imagenesUrl = new ArrayList<>();
                        for (ParseObject imagen : imagenes) {
                            imagenesUrl.add(imagen.getString("url"));
                        }
                        post.setImagenes(imagenesUrl);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    postList.add(post);
                }
                posts.setValue(postList);
            } else {
                posts.setValue(new ArrayList<>());
                Log.e("PostProvider", "Error al obtener los posts: " + error.getMessage());
            }
        });
        return posts;
    }

    public LiveData<List<Post>> getAllPosts() {
        MutableLiveData<List<Post>> posts = new MutableLiveData<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include("user");
        query.findInBackground((postsList, error) -> {
            if (error == null) {
                List<Post> postList = new ArrayList<>();
                for (ParseObject postObject : postsList) {
                    Post post = new Post(
                            postObject.getString("titulo"),
                            postObject.getString("descripcion"),
                            postObject.getInt("duracion"),
                            postObject.getString("categoria"),
                            postObject.getDouble("presupuesto")
                    );
                    ParseRelation<ParseObject> imagenesRelation = postObject.getRelation("imagenes");
                    try{
                        List<ParseObject> imagenes = imagenesRelation.getQuery().find();
                        List<String> imagenesUrl = new ArrayList<>();
                        for (ParseObject immagenObject : imagenes) {
                            imagenesUrl.add(immagenObject.getString("url"));
                        }
                        post.setImagenes(imagenesUrl);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    postList.add(post);
                }
                posts.setValue(postList);
            } else {
                posts.setValue(new ArrayList<>());
                Log.e("PostProvider", "Error al obtener los posts: " + error.getMessage());
            }
        });
        return posts;
    }

}
