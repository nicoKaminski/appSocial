package com.murek.appsocial.providers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.murek.appsocial.model.Post;
import com.murek.appsocial.model.User;
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
//        postObject.put("user", post.getUser());  // PROBAR CON EL TEMA DE MANDAR USER
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
                for (String url : post.getImagen()) {
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

                    ParseRelation<ParseObject> imagenRelation = postObject.getRelation("imagenes");
                    try{
                        List<ParseObject> imagenes = imagenRelation.getQuery().find();
                        List<String> imagenUrl = new ArrayList<>();
                        for (ParseObject imagen : imagenes) {
                            imagenUrl.add(imagen.getString("url"));
                        }
                        post.setImagen(imagenUrl);
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
                    ParseRelation<ParseObject> imagenRelation = postObject.getRelation("imagenes");
                    try{
                        List<ParseObject> imagenes = imagenRelation.getQuery().find();
                        List<String> imagenUrl = new ArrayList<>();
                        for (ParseObject immagenObject : imagenes) {
                            imagenUrl.add(immagenObject.getString("url"));
                        }
                        post.setImagen(imagenUrl);
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

    // Probar si funciona....
    public LiveData<Post> getPostDetail(String postId) {
        MutableLiveData<Post> result = new MutableLiveData<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include("user");
        query.include("images");

        query.getInBackground(postId, (postObject, e) -> {
            if (e == null) {
                Post post = new Post();

                // Mapear los campos del ParseObject a la clase Post
                post.setIdPost(Integer.parseInt(postObject.getObjectId()));
                post.setTitulo(postObject.getString("title"));
                post.setDescripcion(postObject.getString("descripcion"));

                // Obtener las imágenes relacionadas
                ParseRelation<ParseObject> relation = postObject.getRelation("images");
                try {
                    List<ParseObject> images = relation.getQuery().find();
                    List<String> imageUrls = new ArrayList<>();
                    for (ParseObject imageObject : images) {
                        imageUrls.add(imageObject.getString("url"));
                    }
                    post.setImagen(imageUrls);
                } catch (ParseException parseException) {
                    Log.e("PostProvider", "Error fetching images", parseException);
                }

                // Obtener el usuario relacionado
                ParseObject userObject = postObject.getParseObject("user");
                if (userObject != null) try {
                    userObject.fetchIfNeeded();
                    User user = new User();
                    user.setUserName(userObject.getString("username"));
                    user.setUserEmail(userObject.getString("email"));
                    user.setUserFotoPerfil(userObject.getString("fotoperfil"));

                    post.setUser(user);
                } catch (ParseException userFetchException) {
                    Log.e("PostProvider", "Error fetching user", userFetchException);
                } else {
                    Log.w("PostDetail", "El usuario asociado al post es nulo.");
                }

                result.setValue(post);
            } else {
                Log.e("ParseError", "Error al obtener el post: ", e);
                result.setValue(null);
            }
        });

        return result;
    }

    //esta es la de la profe:
    /*
    public LiveData<Post> getPostDetail(String postId) {
        MutableLiveData<Post> result = new MutableLiveData<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        // Incluir la relación con el usuario y las imágenes
        query.include("user");
        query.include("images");

        query.getInBackground(postId, (post, e) -> {
            if (e == null) {

                ParseRelation<ParseObject> relation = post.getRelation("images");
                try {
                    List<ParseObject> images = relation.getQuery().find();
                    List<String> imageUrls = new ArrayList<>();
                    for (ParseObject imageObject : images) {
                        imageUrls.add(imageObject.getString("url"));
                    }
                    post.setImagenes(imageUrls);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }


                ParseObject userObject = post.getParseObject("user");
                if (userObject != null) try {
                    userObject.fetchIfNeeded();
                    User user = new User();
                    user.setUsername(userObject.getString("username"));
                    user.setEmail(userObject.getString("email"));
                    user.setFotoperfil(userObject.getString("fotoperfil"));

                    post.setUser(user);
                } catch (ParseException userFetchException) {
                    userFetchException.printStackTrace();
                }
                else {
                    Log.w("PostDetail", "El usuario asociado al post es nulo.");
                }

                result.setValue(post);
            } else {
                Log.e("ParseError", "Error al obtener el post: ", e);
                result.setValue(null);
            }
        });

        return result;
    }
     */

}
