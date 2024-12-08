package com.murek.appsocial.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.murek.appsocial.model.Post;
import com.murek.appsocial.providers.PostProvider;

import java.util.List;

public class PostViewModel extends ViewModel {
    private MutableLiveData<String> postSuccess = new MutableLiveData<>();
    private PostProvider postProvider;
    private LiveData<List<Post>> postList;


    public PostViewModel() {
        postList = new MutableLiveData<>();
        postProvider = new PostProvider();
    }

    public LiveData<String> getPostSuccess() {
        return postSuccess;
    }

    public void publicarPost(Post post) {
        postProvider.addPost(post).observeForever(result -> {
            if ("Post publicado".equals(result)) {
                postSuccess.setValue(String.valueOf(true));
            } else {
                postSuccess.setValue(String.valueOf(false));
            }
        });
    }

    public LiveData<List<Post>> getPostList() {
        postList = postProvider.getAllPosts();
        Log.d("PostViewModel", "getPostList called");
        return postList;
    }


    /* CODIGO VIEJO......
    public void uploadPost(Post post) {
        postProvider.addPost(post).observeForever(result -> {
            if ("Post publicado".equals(result)) {
                postSuccess.setValue(true);
            } else {
                postSuccess.setValue(false);
            }
        });
    }
    */

}

