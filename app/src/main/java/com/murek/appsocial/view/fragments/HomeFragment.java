package com.murek.appsocial.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.murek.appsocial.R;
import com.murek.appsocial.adapters.PostAdapter;
import com.murek.appsocial.databinding.FragmentHomeBinding;
import com.murek.appsocial.model.Post;
import com.murek.appsocial.view.HomeActivity;
import com.murek.appsocial.view.MainActivity;
import com.murek.appsocial.view.PostActivity;
import com.murek.appsocial.viewModel.AuthViewModel;
import com.murek.appsocial.viewModel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PostViewModel postViewModel; // ViewModel para los posts
    private AuthViewModel authViewModel; // ViewModel para autenticación

    private List<Post> postList = new ArrayList<>();
    private PostAdapter adapter;
    private int currentPage = 0; // Página actual
    private boolean isLoading = false; // Para evitar cargas múltiples

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    /** codigo viejo....

     public HomeFragment() {
     }

     public static HomeFragment newInstance(String p1, String p2) {
     return new HomeFragment();
     }

     public static HomeFragment newInstance() {
     return new HomeFragment();
     }

     @Override
     public void onResume() {
     super.onResume();
     cargarPosts(); // Llama al método para recargar los posts
     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     binding = FragmentHomeBinding.inflate(inflater, container, false);
     return binding.getRoot();
     }

     @Override
     public void onViewCreated(View view, Bundle savedInstanceState) {
     super.onViewCreated(view, savedInstanceState);

     // Inicializa el ViewModel
     postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

     postList = new ArrayList<>();
     adapter = new PostAdapter(postList);
     binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
     binding.recyclerView.setAdapter(adapter);

     cargarPosts();

     // Listener para cargar más posts al hacer scroll
     binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
     @Override
     public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
     super.onScrolled(recyclerView, dx, dy);

     // Comprueba si el usuario ha llegado al final
     LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
     if (layoutManager != null) {
     int totalItemCount = layoutManager.getItemCount();
     int visibleItemCount = layoutManager.getChildCount();
     int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

     // Si estamos en el final de la lista
     if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
     cargarPosts(); // Cargar más posts
     }
     }
     }
     });


     binding.btnAdd.setOnClickListener(v -> {
     Intent intent = new Intent(getContext(), PostActivity.class);
     startActivity(intent);
     });
     }

     private void cargarPosts() {
     if (isLoading) return; // Evita solicitudes múltiples mientras se está cargando
     isLoading = true; // Marca como cargando
     postViewModel.getPostList(currentPage).observe(getViewLifecycleOwner(), posts -> {
     if (posts != null && !posts.isEmpty()) {
     postList.addAll(posts); // Añade nuevos posts a la lista existente
     adapter.notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
     currentPage++; // Incrementa la página después de cargar los posts
     }
     isLoading = false; // Permite nuevas solicitudes
     });
     }

     public void onLogout() {
     Intent intent = new Intent(getContext(), MainActivity.class);
     startActivity(intent);
     }

     public void setupMenu() {
     binding.btnAdd.setOnClickListener(v -> onLogout());
     }

     */

    /** PROBANDO....
    @Override
    public void onResume() {
        super.onResume();
        cargarPosts(); // Llama al método para recargar los posts
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar barra de herramientas
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.tools);

        binding.btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PostActivity.class);
            startActivity(intent);
        });

        // Configurar RecyclerView
        adapter = new PostAdapter(postList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        // Inicializar la carga de posts
        cargarPosts();

        // Configurar menú
        setupMenu();
    }

    private void cargarPosts() {
        if (isLoading) return;
        isLoading = true;
        ((HomeActivity) requireActivity()).showProgressBar(); // Muestra barra de progreso
        postViewModel.getPostList(currentPage).observe(getViewLifecycleOwner(), posts -> {
            if (posts != null && !posts.isEmpty()) {
                postList.addAll(posts);
                adapter.notifyDataSetChanged();
                currentPage++;
            }else {
                Toast.makeText(getContext(), "No hay más publicaciones disponibles.", Toast.LENGTH_SHORT).show();
            }
            // Oculta la barra de progreso y permite nuevas solicitudes
            isLoading = false;
            ((HomeActivity) requireActivity()).hideProgressBar();
        });
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.itemLogout) {
                    onLogout();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void onLogout() {
        authViewModel.logOut().observe(getViewLifecycleOwner(), logoutResult -> {
            if (logoutResult != null && logoutResult) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Error al cerrar sesión. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar barra de herramientas
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.tools);

        binding.btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PostActivity.class);
            startActivity(intent);
        });

        // Configurar RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postViewModel.getPostList(currentPage).observe(getViewLifecycleOwner(), posts -> {
            if (posts != null && !posts.isEmpty()) {
                Log.d("HomeFragment", "Número de posts: " + posts.size());
                PostAdapter adapter = new PostAdapter(posts);
                binding.recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ((HomeActivity) requireActivity()).hideProgressBar();
            } else {
                Log.d("HomeFragment", "No hay posts disponibles.");
                ((HomeActivity) requireActivity()).hideProgressBar();
            }
        });
        setupMenu();
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.itemLogout) {
                    onLogout();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void onLogout() {
        authViewModel.logOut().observe(getViewLifecycleOwner(), logoutResult -> {
            if (logoutResult != null && logoutResult) {

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {

                Toast.makeText(getContext(), "Error al cerrar sesión. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Evitar fugas de memoria
    }
}

