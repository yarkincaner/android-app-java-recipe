package com.example.myrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myrecipes.dto.Category;
import com.example.myrecipes.dto.Recipe;
import com.example.myrecipes.dto.Singleton;
import com.example.myrecipes.ui.dialogs.DialogAddCategoryFragment;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    static FirebaseUser currentUser;
    static FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;

    static Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        singleton = Singleton.getInstance();

        init();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        //ActionBar ab = getSupportActionBar();
        //ab.setDisplayShowTitleEnabled(false);

        BottomNavigationView navigationView = findViewById(R.id.main_bottom_nav_bar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.bottom_nav_bar_home, R.id.bottom_nav_bar_random
        ).build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public static void init() {
        singleton.clearArray();
        Task<QuerySnapshot> taskGetCategories = db.collection("users").document(currentUser.getUid())
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

            Task<QuerySnapshot> taskGetRecipes = db.collection("users").document(currentUser.getUid())
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_item_logout:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addCategory(View view) {
        System.out.println("Add Category");

        DialogFragment addCategoryFragment = new DialogAddCategoryFragment();
        addCategoryFragment.show(getSupportFragmentManager(), "add_category");
    }

    public void addRecipe(View view) {
        System.out.println("MainActivity Add Recipe");
    }
}