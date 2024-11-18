package com.murek.appsocial.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.murek.appsocial.databinding.*;

import com.google.android.material.navigation.NavigationBarView;
import com.murek.appsocial.R;
import com.murek.appsocial.view.fragments.ChatsFragment;
import com.murek.appsocial.view.fragments.FiltrosFragment;
import com.murek.appsocial.view.fragments.HomeFragment;
import com.murek.appsocial.view.fragments.PerfilFragment;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
//    final int HOME = R.id.navItemHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navItemHome) {
                    openFragment(HomeFragment.newInstance("", ""));
                } else if (item.getItemId() == R.id.navItemFiltros) {
                    openFragment(new FiltrosFragment());
                } else if (item.getItemId() == R.id.navItemCharts) {
                    openFragment(new ChatsFragment());
                } else if (item.getItemId() == R.id.navItemPerfil) {
                    openFragment(new PerfilFragment());
                }
                return true;
            }
        });
        openFragment(HomeFragment.newInstance("", ""));
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}
