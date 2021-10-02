package com.example.myrecipes.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myrecipes.dto.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecipesViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    FirebaseFirestore db;
    String categoryTitle;

    MutableLiveData<ArrayList<Recipe>> userLiveData;
    ArrayList<Recipe> recipes;

    public RecipesViewModel() {
        userLiveData = new MutableLiveData<>();
        categoryTitle = null;
        recipes = new ArrayList<>();
    }

    public MutableLiveData<ArrayList<Recipe>> getUserLiveData() {
        return userLiveData;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
        init();
    }

    public void init() {
        db = FirebaseFirestore.getInstance();
        populateList();
        userLiveData.setValue(recipes);
    }

    public void populateList() {

        Task<QuerySnapshot> taskGetRecipes = db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .collection("categories").document(categoryTitle)
                .collection("recipes")
                .get();

        while (!taskGetRecipes.isComplete()) {
            System.out.println(taskGetRecipes.isComplete());
        }

        QuerySnapshot querySnapshot = taskGetRecipes.getResult();

        if (!querySnapshot.isEmpty()) {
            List<DocumentSnapshot> documents = querySnapshot.getDocuments();

            for (DocumentSnapshot document : documents) {
                String title = document.getString("title");
                String description = document.getString("description");
                String recipe = document.getString("recipe");
                String imagePath = document.getString("imagePath");
                recipes.add(new Recipe(title, description, recipe, imagePath));
            }
        }
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                String title = document.getString("title");
                String description = document.getString("description");
                String recipe = document.getString("recipe");
                String imagePath = document.getString("imagePath");
                recipes.add(new Recipe(title, description, recipe, imagePath));
            }
        } else {
            System.out.println(task.getException().getStackTrace());
        }
    }
}