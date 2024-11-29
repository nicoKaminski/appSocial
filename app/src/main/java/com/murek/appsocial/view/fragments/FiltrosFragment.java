package com.murek.appsocial.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import com.murek.appsocial.R;
import com.murek.appsocial.databinding.FragmentFiltrosBinding;

public class FiltrosFragment extends Fragment {

    private FragmentFiltrosBinding binding;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    public FiltrosFragment() {}

    public static FiltrosFragment newInstance(String p1, String p2) {
        return new FiltrosFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filtros, container, false);
    }
}
