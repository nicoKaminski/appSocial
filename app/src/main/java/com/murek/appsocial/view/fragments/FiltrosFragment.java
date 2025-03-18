package com.murek.appsocial.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.murek.appsocial.R;
import com.murek.appsocial.adapters.PostAdapter;
import com.murek.appsocial.databinding.FragmentFiltrosBinding;
import com.murek.appsocial.viewModel.PostViewModel;

import java.util.ArrayList;

public class FiltrosFragment extends Fragment {

    private Spinner spCategoriaFiltro;
    private RecyclerView recyclerViewFiltros;
    private PostAdapter postAdapter;
    private PostViewModel postViewModel;
    private String categoriaSeleccionada = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filtros, container, false);

        spCategoriaFiltro = view.findViewById(R.id.spCategoriaFiltro);
        recyclerViewFiltros = view.findViewById(R.id.recyclerViewFiltros);

        setupCategorySpinner();
        setupRecyclerView();

        return view;
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.categorias));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoriaFiltro.setAdapter(adapter);

        spCategoriaFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                cargarPostsFiltrados();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoriaSeleccionada = null;
            }
        });
    }

    private void setupRecyclerView() {
        recyclerViewFiltros.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerViewFiltros.setAdapter(postAdapter);
    }

    private void cargarPostsFiltrados() {
        if (postViewModel == null) {
            postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        }
        postViewModel.getPostsByCategory(categoriaSeleccionada).observe(getViewLifecycleOwner(), posts -> {
            if (posts != null) {
                postAdapter.updatePosts(posts);
            }
        });
    }
}
