package com.murek.appsocial.providers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.murek.appsocial.model.Post;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostProvider {

    public LiveData<String> addPost(Post post) {
        MutableLiveData<String> result = new MutableLiveData<>();
        ParseObject postObject = new ParseObject("Post");

        postObject.put("titulo", post.getTitulo());
        postObject.put("descripcion", post.getDescripcion());
//        postObject.put("idUser", post.getIdUser());
        postObject.put("duracion", post.getDuracion());
        postObject.put("categoria", post.getCategoria());
        postObject.put("presupuesto", post.getPresupuesto());
//        postObject.put("imagenes", post.getImagenes());
        postObject.put("user", ParseUser.getCurrentUser());
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
        //falta terminar
        return posts;
    }

}
