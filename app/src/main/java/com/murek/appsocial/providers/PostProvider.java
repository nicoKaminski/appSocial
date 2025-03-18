package com.murek.appsocial.providers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.murek.appsocial.model.Post;
import com.murek.appsocial.model.User;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class PostProvider {

    // Agregar post
    public LiveData<String> addPost(Post post) {
        MutableLiveData<String> result = new MutableLiveData<>();
        post.put("titulo", post.getTituloPost());
        post.put("descripcion", post.getDescripcionPost());
        post.put("duracion", post.getDuracionPost());
        post.put("categoria", post.getCategoriaPost());
        post.put("presupuesto", post.getPresupuestoPost());
        ParseUser currentUser = ParseUser.getCurrentUser();
        post.put("user", currentUser);

//        // Establecer permisos de ACL
//        ParseACL acl = new ParseACL();
//        acl.setPublicReadAccess(true); // Permitir lectura a todos
//        //acl.setWriteAccess(currentUser, true); // Solo el usuario que crea el post puede escribir (modificar)
//        ParseACL.setDefaultACL(acl, true); //ver si esto va!
//        post.setACL(acl); // Establecer ACL en el post

        // Guardar el post
        post.saveInBackground(e -> {
            if (e == null) {
                ParseRelation<ParseObject> relation = post.getRelation("images");
                for (String url : post.getImagenPost()) {
                    ParseObject imageObject = new ParseObject("Image");
                    imageObject.put("url", url);
                    imageObject.saveInBackground(imgSaveError -> {
                        if (imgSaveError == null) {
                            relation.add(imageObject);
                            post.saveInBackground(saveError -> {
                                if (saveError == null) {
                                    result.setValue("Post publicado");
                                } else {
                                    result.setValue("Error al guardar la relación con las imágenes: " + saveError.getMessage());
                                }
                            });
                        } else {
                            result.setValue("Error al guardar la imagen: " + imgSaveError.getMessage());
                        }
                    });
                }
            } else {
                result.setValue("Error al guardar el post: " + e.getMessage());
            }
        });
        return result;
    }

    // Obtener posts del usuario
    public LiveData<List<Post>> getPostsByCurrentUser(int page) {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            result.setValue(new ArrayList<>());
            return result;
        }
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user", currentUser);
        query.include("user");
        query.orderByDescending("createdAt"); // Ordenar por fecha de creación
        query.setLimit(5); // Límite de posts por página
        query.setSkip(page * 5); // Saltar posts según la página
        query.findInBackground((posts, e) -> {
            if (e == null) {
                result.setValue(posts);
            } else {
                result.setValue(new ArrayList<>());
                Log.e("ParseError", "Error al recuperar los posts del usuario: ", e);
            }
        });
        return result;
    }

    // Obtener todos los posts
    public LiveData<List<Post>> getAllPosts(int page) {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include("user"); // Incluye información del usuario
        query.orderByDescending("createdAt");
        query.setLimit(5); // Límite de 5 posts por página
        query.setSkip(page * 5); // Desplaza según la página (5 posts por página)
        query.findInBackground((posts, e) -> {
            if (e == null) {
                List<Post> postList = new ArrayList<>();
                for (ParseObject postObject : posts) {
                    Log.d("PostObject", "ID: " + postObject.getObjectId() + ", Title: " + postObject.getString("titulo"));

                    Post post = ParseObject.create(Post.class);
                    post.setObjectId(postObject.getObjectId());
                    post.setTituloPost(postObject.getString("titulo"));
                    post.setDescripcionPost(postObject.getString("descripcion"));
                    post.setDuracionPost(postObject.getInt("duracion"));
                    post.setCategoriaPost(postObject.getString("categoria"));
                    post.setPresupuestoPost(postObject.getDouble("presupuesto"));

                    // Obtener imágenes
                    ParseRelation<ParseObject> relation = postObject.getRelation("images");
                    try {
                        List<ParseObject> images = relation.getQuery().find();
                        List<String> imageUrls = new ArrayList<>();
                        for (ParseObject imageObject : images) {
                            imageUrls.add(imageObject.getString("url"));
                        }
                        post.setImagenPost(imageUrls);
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }

                    // Mapeo del usuario
                    ParseUser parseUser = postObject.getParseUser("user");
                    if (parseUser != null) {
                        try {
                            parseUser.fetchIfNeeded();
                            User user = ParseObject.createWithoutData(User.class, parseUser.getObjectId());
                            user.setUserName(parseUser.getUsername());
                            user.setUserEmail(parseUser.getEmail());
                            user.setUserFotoPerfil(parseUser.getString("foto_perfil"));
                            user.setRedSocial(parseUser.getString("red_social"));

                            post.setUser(user); // Asignar el usuario convertido al post
                        } catch (ParseException parseException) {
                            Log.e("FetchUserError", "Error al obtener el usuario: ", parseException);
                        }
                    } else {
                        Log.d("UserPointer", "User pointer es null");
                    }
                    postList.add(post);
                }
                result.setValue(postList);
            } else {
                result.setValue(new ArrayList<>());
                Log.e("ParseError", "Error al recuperar todos los posts: ", e);
            }
        });
        return result;
    }

    // Eliminar post
    public LiveData<String> deletePost(String postId) {
        MutableLiveData<String> result = new MutableLiveData<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.getInBackground(postId, (post, e) -> {
            if (e == null) {
                post.deleteInBackground(e1 -> {
                    if (e1 == null) {
                        Log.d("PostDelete", "Post eliminado con éxito.");
                        result.postValue("Post eliminado correctamente");
                    } else {
                        Log.e("PostDelete", "Error al eliminar el post: ", e1);
                        result.postValue("Error al eliminar el post: " + e1.getMessage());
                    }
                });
            } else {
                Log.e("PostDelete", "Error al encontrar el post: ", e);
                result.postValue("Error al encontrar el post: " + e.getMessage());
            }
        });
        return result;
    }

    // Obtener post por ID
    public LiveData<Post> getPostDetail(String postId) {
        MutableLiveData<Post> result = new MutableLiveData<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include("user");
        query.include("images");
        query.getInBackground(postId, (post, e) -> {
            if (e == null) {
                // Manejar imágenes
                ParseRelation<ParseObject> relation = post.getRelation("images");
                try {
                    List<ParseObject> images = relation.getQuery().find();
                    List<String> imageUrls = new ArrayList<>();
                    for (ParseObject imageObject : images) {
                        imageUrls.add(imageObject.getString("url"));
                    }
                    post.setImagenPost(imageUrls);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                // Manejar usuario
                ParseObject userObject = post.getParseObject("user");
                if (userObject != null) try {
                    userObject.fetchIfNeeded();
                    User user = new User();
                    user.setUsername(userObject.getString("username"));
                    user.setEmail(userObject.getString("email"));
                    user.setUserFotoPerfil(userObject.getString("foto_perfil"));
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

    // Obtener posts por categoría
    public LiveData<List<Post>> getPostsByCategory(String categoria) {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include("user");
        query.orderByDescending("createdAt");
        if (categoria != null && !categoria.isEmpty()) {
            query.whereEqualTo("categoria", categoria);
        }
        query.findInBackground((posts, e) -> {
            if (e == null) {
                result.setValue(posts);
            } else {
                result.setValue(new ArrayList<>());
                Log.e("ParseError", "Error al recuperar los posts por categoría: ", e);
            }
        });
        return result;
    }


    public interface CommentsCallback {
        void onSuccess(List<ParseObject> comments);
        void onFailure(Exception e);
    }

    public void fetchComments(String postId, CommentsCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comentario");
        query.whereEqualTo("post", ParseObject.createWithoutData("Post", postId));
        query.include("user"); // Incluye los datos del usuario en la consulta
        query.findInBackground((comentarios, e) -> {
            if (e == null) {
                callback.onSuccess(comentarios);
            } else {
                callback.onFailure(e);
            }
        });
    }

    public void saveComment(String postId, String commentText, ParseUser currentUser, SaveCallback callback) {
        ParseObject post = ParseObject.createWithoutData("Post", postId);
        ParseObject comentario = ParseObject.create("Comentario");
        comentario.put("texto", commentText);
        comentario.put("post", post);
        comentario.put("user", currentUser);
        comentario.saveInBackground(callback);
    }

}
