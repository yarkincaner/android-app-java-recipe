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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class HomeViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

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
        Task<QuerySnapshot> taskGetCategories = db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .collection("categories")
                .get();

        while (!taskGetCategories.isComplete()) {
            System.out.println(taskGetCategories.isComplete());
        }

        QuerySnapshot querySnapshot = taskGetCategories.getResult();
        if (!querySnapshot.isEmpty()) {
            List<DocumentSnapshot> documents = querySnapshot.getDocuments();

            for (DocumentSnapshot document : documents) {
                categories.add(new Category(document.getId()));
            }
        }
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (DocumentSnapshot document : task.getResult()) {
                categories.add(new Category(document.getId()));
            }
        }
    }
}