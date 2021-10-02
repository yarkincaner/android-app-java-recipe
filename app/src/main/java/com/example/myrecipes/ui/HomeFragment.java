package com.example.myrecipes.ui;

import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.myrecipes.R;
import com.example.myrecipes.adapters.Category_RvAdapter;
import com.example.myrecipes.dto.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FirebaseFirestore db;
    ArrayList<Category> categories;

    private RecyclerView recyclerView;
    private Category_RvAdapter homeRvAdapter;
    private HomeViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_fragment, container, false);
        this.setHasOptionsMenu(true);

        db = FirebaseFirestore.getInstance();
        categories = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_home);
        swipeRefreshLayout = view.findViewById(R.id.home_swipeRefreshLayout);

        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), categoryListUpdateObserver);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        return view;
    }

    private Observer<ArrayList<Category>> categoryListUpdateObserver = new Observer<ArrayList<Category>>() {
        @Override
        public void onChanged(ArrayList<Category> categories) {
            if (categories.size() > 0) {
                homeRvAdapter = new Category_RvAdapter(requireActivity(), categories);
                recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                recyclerView.setAdapter(homeRvAdapter);
            }
        }
    };

    private void refresh() {
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), categoryListUpdateObserver);
        swipeRefreshLayout.setRefreshing(false);
    }
}