package com.murek.appsocial.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import com.murek.appsocial.R;
import com.murek.appsocial.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private ActivityResultLauncher<Intent> galleryLauncher;

    public PerfilFragment(){}

    public static PerfilFragment newInstance(String p1, String p2) {
        return new PerfilFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }


}
