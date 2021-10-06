package com.example.myrecipes.ui;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.myrecipes.MainActivity;
import com.example.myrecipes.R;
import com.example.myrecipes.SplashScreenActivity;
import com.example.myrecipes.adapters.Category_RvAdapter;
import com.example.myrecipes.dto.Category;
import com.example.myrecipes.dto.Singleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    static FirebaseFirestore db;
    static Singleton singleton;

    private RecyclerView recyclerView;
    private Category_RvAdapter homeRvAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_fragment, container, false);
        this.setHasOptionsMenu(true);

        db = FirebaseFirestore.getInstance();
        singleton = Singleton.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_home);
        swipeRefreshLayout = view.findViewById(R.id.home_swipeRefreshLayout);

        homeRvAdapter = new Category_RvAdapter(requireActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        recyclerView.setAdapter(homeRvAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        return view;
    }

    private void refresh() {
        SplashScreenActivity.init();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        ((SearchView) menu.findItem(R.id.toolbar_menu_item_search)
//                .getActionView()).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filter(newText);
//                return false;
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //switch (item.getItemId()) {
        //    case R.id.toolbar_menu_item_search:
        //        SearchView searchView = (SearchView) item.getActionView();
        //        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        //            @Override
        //            public boolean onQueryTextSubmit(String query) {
        //                return false;
        //            }
//
        //            @Override
        //            public boolean onQueryTextChange(String newText) {
        //                filter(newText);
        //                return false;
        //            }
        //        });
        //}
        return super.onOptionsItemSelected(item);
    }

//    private void filter(String text) {
//        ArrayList<Category> filteredList = new ArrayList<>();
//
//        for (Category category : singleton.getCategories()) {
//            if (category.getTitle().toLowerCase().contains(text.toLowerCase())) {
//                filteredList.add(category);
//            }
//        }
//
//        if (filteredList.isEmpty()) {
//            System.out.println("No data found...");
//        } else {
//            homeRvAdapter.filterList(filteredList);
//        }
//    }
}