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
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class PostProvider {

    /** CODIGO VIEJO sin ParseObject.....
    public LiveData<String> addPost(Post post) {
        MutableLiveData<String> result = new MutableLiveData<>();
        ParseObject postObject = new ParseObject("Post");
        postObject.put("user", ParseUser.getCurrentUser());
        postObject.put("titulo", post.getTituloPost());
        postObject.put("descripcion", post.getDescripcionPost());
        postObject.put("duracion", post.getDuracionPost());
        postObject.put("categoria", post.getCategoriaPost());
        postObject.put("presupuesto", post.getPresupuestoPost());

        postObject.saveInBackground(imgError -> {
            if (imgError == null) {
                ParseRelation<ParseObject> relation = postObject.getRelation("images");
                for (String url : post.getImagenPost()) {
                    ParseObject imgObject = new ParseObject("Image");
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
     */

    public LiveData<String> addPost(Post post) {
        MutableLiveData<String> result = new MutableLiveData<>();

        if (post.getImagenPost() == null || post.getImagenPost().isEmpty()) {
            result.setValue("Error: El post debe tener al menos una imagen.");
            return result;
        }

        ParseObject postObject = new ParseObject("Post");

        postObject.put("titulo", post.getTituloPost());
        postObject.put("descripcion", post.getDescripcionPost());
        postObject.put("duracion", post.getDuracionPost());
        postObject.put("categoria", post.getCategoriaPost());
        postObject.put("presupuesto", post.getPresupuestoPost());
        postObject.put("user", ParseUser.getCurrentUser()); // Relación con el usuario
        postObject.saveInBackground(e -> {
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

    /** CODIGO VIEJO sin ParseObject.....
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

                    ParseRelation<ParseObject> imagenRelation = postObject.getRelation("images");
                    try{
                        List<ParseObject> imagenes = imagenRelation.getQuery().find();
                        List<String> imagenUrl = new ArrayList<>();
                        for (ParseObject imagen : imagenes) {
                            imagenUrl.add(imagen.getString("url"));
                        }
                        post.setImagenPost(imagenUrl);
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

    public LiveData<List<Post>> getAllPosts(int page) {
        MutableLiveData<List<Post>> posts = new MutableLiveData<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include("user");
        query.orderByDescending("createdAt");
        query.setLimit(5);
        query.setSkip(page *5);
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
                        post.setImagenPost(imagenUrl);
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
    } */

    public LiveData<List<Post>> getPostsByCurrentUser() {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            result.setValue(new ArrayList<>());
            return result;
        }

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user", currentUser);
        query.include("user");
        query.findInBackground((posts, e) -> {
            if (e == null) {
                result.setValue(posts);
            } else {
                result.setValue(new ArrayList<>());
                Log.e("ParseError", "Error al recuperar los posts: ", e);
            }
        });
        return result;
    }

    /**
    public LiveData<List<Post>> getAllPosts(int page) {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();
        ParseQuery<Post> query = ParseQuery.getQuery("Post");
        query.include("user");
        query.orderByDescending("createdAt");
        query.setLimit(5);
        query.setSkip(page *5);
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
    }*/

    public LiveData<List<Post>> getAllPosts(int page) {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include("user"); // Incluye información del usuario
        query.orderByDescending("createdAt");
        query.setLimit(5); // Límite de 5 posts por página
        query.setSkip(page * 5); // Desplaza según la página (5 posts por página)

        query.findInBackground((posts, e) -> {
            if (e == null) {
                List<Post> postList = new ArrayList<>();
                for (ParseObject postObject : posts) {
                    try {
                        // Crear un objeto Post de ParseObject
                        Post post = (Post) postObject;  // Usar el casteo a Post

                        // Verificar si los valores son nulos y asignar valores predeterminados si es necesario
                        String titulo = post.getTituloPost();
                        String descripcion = post.getDescripcionPost();
                        int duracion = post.getDuracionPost();
                        String categoria = post.getCategoriaPost();
                        double presupuesto = post.getPresupuestoPost();

                        // Si alguno de estos campos es nulo, asignar un valor predeterminado
                        if (titulo == null) titulo = "Título no disponible";
                        if (descripcion == null) descripcion = "Descripción no disponible";
                        if (categoria == null) categoria = "Categoría no disponible";

                        // Asignar los valores a los campos de post
                        post.setTituloPost(titulo);
                        post.setDescripcionPost(descripcion);
                        post.setDuracionPost(duracion);
                        post.setCategoriaPost(categoria);
                        post.setPresupuestoPost(presupuesto);

                        // Cargar imágenes
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

                        postList.add(post);
                    } catch (Exception ex) {
                        Log.e("ParseError", "Error al procesar el post: ", ex);
                    }
                }
                result.setValue(postList);
            } else {
                result.setValue(new ArrayList<>());
                Log.e("ParseError", "Error al recuperar todos los posts: ", e);
            }
        });

        return result;
    }



    public void removePost(String postId) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.getInBackground(postId, (post, e) -> {
            if (e == null) {
                post.deleteInBackground(e1 -> {
                    if (e1 == null) {
                        Log.d("PostDelete", "Post eliminado con éxito.");
                    } else {
                        Log.e("PostDelete", "Error al eliminar el post: ", e1);
                    }
                });
            } else {
                Log.e("PostDelete", "Error al encontrar el post: ", e);
            }
        });
    }

    public LiveData<Post> getPostDetail(String postId) {
        MutableLiveData<Post> result = new MutableLiveData<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
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
                    post.setImagenPost(imageUrls);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                ParseObject userObject = post.getParseObject("user");
                if (userObject != null) try {
                    userObject.fetchIfNeeded();
                    User user = new User();
                    user.setUserName(userObject.getString("username"));
                    user.setUserEmail(userObject.getString("email"));
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

        ParseObject comentario = new ParseObject("Comentario");
        comentario.put("texto", commentText);
        comentario.put("post", post);
        comentario.put("user", currentUser);

        comentario.saveInBackground(callback);
    }

}
