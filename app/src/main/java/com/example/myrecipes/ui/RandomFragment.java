package com.example.myrecipes.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myrecipes.R;
import com.example.myrecipes.adapters.Random_RvAdapter;
import com.example.myrecipes.dto.Category;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RandomFragment extends Fragment {

    FirebaseFirestore db;
    ArrayList<Category> categories;

    private RecyclerView recyclerView;
    private Random_RvAdapter randomRvAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.random_fragment, container, false);
        this.setHasOptionsMenu(true);

        db = FirebaseFirestore.getInstance();
        categories = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.random_fragment_rv);

        getCategories();
        randomRvAdapter = new Random_RvAdapter(requireActivity(), categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(randomRvAdapter);

        return view;
    }

    private void getCategories() {
        Task<QuerySnapshot> taskGetCategories = db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .collection("categories").get();

        while (!taskGetCategories.isComplete()) {
            System.out.println(taskGetCategories.isComplete());
        }

        QuerySnapshot querySnapshot = taskGetCategories.getResult();
        if (!querySnapshot.isEmpty()) {
            List<DocumentSnapshot> documents = querySnapshot.getDocuments();

            for (DocumentSnapshot document : documents) {
                categories.add(new Category(document.getId()));
            }
        } else {
            System.out.println("User doesn't have any categories");
        }
    }

}