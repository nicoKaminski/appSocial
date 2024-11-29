package com.murek.appsocial.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.murek.appsocial.databinding.FragmentHomeBinding;
import com.murek.appsocial.providers.AuthProvider;
import com.murek.appsocial.view.MainActivity;
import com.murek.appsocial.view.PostActivity;
import com.murek.appsocial.viewModel.AuthViewModel;
import com.murek.appsocial.viewModel.PostViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AuthProvider authProvider;
    private PostViewModel postViewModel;
    private AuthViewModel authViewModel;

    public HomeFragment() {}

    public static HomeFragment newInstance(String p1, String p2) {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onLogout() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    //No lo se, VER!
    public void setupMenu() {
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout();
            }
        });
    }
}
