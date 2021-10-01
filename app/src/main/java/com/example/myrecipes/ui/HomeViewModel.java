package com.example.myrecipes.ui;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myrecipes.MainActivity;
import com.example.myrecipes.dto.Category;
import com.example.myrecipes.dto.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class HomeViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot>, OnFailureListener {

    FirebaseFirestore db;
    MutableLiveData<ArrayList<Category>> userLiveData;
    ArrayList<Category> categories;

    public HomeViewModel() {
        db = FirebaseFirestore.getInstance();
        userLiveData = new MutableLiveData<>();
        categories = new ArrayList<>();
        init();
    }

    public MutableLiveData<ArrayList<Category>> getUserLiveData() {
        return userLiveData;
    }

    public void init() {
        populateList();
        userLiveData.setValue(categories);
    }

    public void populateList() {
        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .collection("categories")
                .get()
                .addOnCompleteListener(this::onComplete);

//        Category soup = new Category("Soup");
//        Category hamburger = new Category("Hamburger");
//        Category vegetable = new Category("Vegetable");
//
//        categories = new ArrayList<>();
//        categories.add(soup);
//        categories.add(hamburger);
//        categories.add(vegetable);

    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                categories.add(new Category(document.getId()));
            }
        } else {
            System.out.println(task.getException().getStackTrace());
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        System.out.println(e.getStackTrace());
    }
}