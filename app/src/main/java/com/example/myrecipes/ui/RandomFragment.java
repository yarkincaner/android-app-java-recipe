package com.example.myrecipes.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myrecipes.R;

public class RandomFragment extends Fragment {

    private RandomViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(RandomFragment.this).get(RandomViewModel.class);
        View root = inflater.inflate(R.layout.random_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_random);

        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });

        return root;
    }

}