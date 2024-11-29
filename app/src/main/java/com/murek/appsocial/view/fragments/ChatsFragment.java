package com.murek.appsocial.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.murek.appsocial.R;
import com.murek.appsocial.databinding.FragmentChatsBinding;


public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;

    public ChatsFragment() {}

    public static ChatsFragment newInstance(String p1, String p2) {
        return new ChatsFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }
}
