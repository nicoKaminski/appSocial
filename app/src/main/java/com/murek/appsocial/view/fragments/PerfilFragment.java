package com.murek.appsocial.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.murek.appsocial.R;

public class PerfilFragment extends Fragment {

    public PerfilFragment(){}

    public static PerfilFragment newInstance(String p1, String p2) {
        return new PerfilFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
