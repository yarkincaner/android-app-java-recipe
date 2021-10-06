package com.example.myrecipes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.widget.TextView;

import com.example.myrecipes.dto.Category;
import com.example.myrecipes.dto.Recipe;
import com.example.myrecipes.dto.Singleton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    static FirebaseAuth mAuth;
    static FirebaseUser fbUser;
    static FirebaseFirestore db;
    static Singleton singleton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        singleton = Singleton.getInstance();
        textView = findViewById(R.id.splashscreen_textView_title);

        new Handler().postDelayed(() -> {
            if (fbUser != null) {
                init();
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    public static void init() {
        singleton.clearArray();
        Task<QuerySnapshot> taskGetCategories = db.collection("users").document(fbUser.getUid())
                .collection("categories")
                .get();

        while (!taskGetCategories.isComplete()) {
            System.out.println(taskGetCategories.isComplete());
        }

        QuerySnapshot querySnapshotCategory = taskGetCategories.getResult();
        if (!querySnapshotCategory.isEmpty()) {
            List<DocumentSnapshot> documentsCategory = querySnapshotCategory.getDocuments();
            getRecipes(documentsCategory);
        }
    }

    private static void getRecipes(List<DocumentSnapshot> documentsCategory) {
        for (DocumentSnapshot documentCategory : documentsCategory) {
            singleton.addCategory(new Category(documentCategory.getId()));

            Task<QuerySnapshot> taskGetRecipes = db.collection("users").document(fbUser.getUid())
                    .collection("categories").document(documentCategory.getId())
                    .collection("recipes")
                    .get();

            while (!taskGetRecipes.isComplete()) {
                System.out.println(taskGetRecipes.isComplete());
            }

            QuerySnapshot querySnapshotRecipe = taskGetRecipes.getResult();
            if (!querySnapshotRecipe.isEmpty()) {
                Category category;
                if ((category = (singleton.getCategory(documentCategory.getId()))) != null) {
                    List<DocumentSnapshot> documentsRecipe = querySnapshotRecipe.getDocuments();

                    for (DocumentSnapshot documentRecipe : documentsRecipe) {
                        String title = documentRecipe.getString("title");
                        String description = documentRecipe.getString("description");
                        String recipeRecipe = documentRecipe.getString("recipe");
                        String imagePath = documentRecipe.getString("imagePath");
                        category.addRecipe(new Recipe(title, description, recipeRecipe, imagePath));
                    }
                }
            }
        }
    }
}