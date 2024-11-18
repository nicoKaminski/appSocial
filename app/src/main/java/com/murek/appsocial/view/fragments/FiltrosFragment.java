package com.murek.appsocial.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.murek.appsocial.R;

public class FiltrosFragment extends Fragment {

    public FiltrosFragment() {}

    public static FiltrosFragment newInstance(String p1, String p2) {
        return new FiltrosFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filtros, container, false);
    }
}
