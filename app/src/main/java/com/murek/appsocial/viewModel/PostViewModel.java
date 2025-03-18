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
        postProvider.addPost(post)
                .observeForever(result -> {
                    postSuccess.setValue(result);
                });
    }

    public LiveData<List<Post>> getPostList(int page) {
        postList = postProvider.getAllPosts(page);
        Log.d("PostViewModel", "getPostList called");
        return postList;
    }

    public LiveData<List<Post>> getUserPostList(int page) {
        return postProvider.getPostsByCurrentUser(page);
    }

    public LiveData<List<Post>> getPostsByCategory(String categoria) {
        return postProvider.getPostsByCategory(categoria);
    }

}

