package com.example.myrecipes.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myrecipes.R;
import com.example.myrecipes.adapters.Random_RvAdapter;
import com.example.myrecipes.dto.Category;
import com.example.myrecipes.dto.Recipe;
import com.example.myrecipes.dto.Singleton;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomFragment extends Fragment implements View.OnClickListener {

    FirebaseFirestore db;
    static Singleton singleton;
    List<DocumentSnapshot> documents;

    private RecyclerView recyclerView;
    private Random_RvAdapter randomRvAdapter;
    Button buttonGetRecipe;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.random_fragment, container, false);
        this.setHasOptionsMenu(true);

        db = FirebaseFirestore.getInstance();
        singleton = Singleton.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.random_fragment_rv);

        randomRvAdapter = new Random_RvAdapter(requireActivity());

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(requireActivity());
        recyclerView.setLayoutManager(flexboxLayoutManager);
        recyclerView.setAdapter(randomRvAdapter);

        buttonGetRecipe = view.findViewById(R.id.random_fragment_button);
        buttonGetRecipe.setOnClickListener(this::onClick);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.random_fragment_button:
                getActiveRecipes(v);
        }
    }

    private void getActiveRecipes(View v) {
        HashMap<Recipe, Category> activeRecipes = new HashMap<>();

        for (int i = 0; i < singleton.getCategories().size(); i++) {
            if (singleton.getCategories().get(i).isActive()) {
                for (Recipe recipe : singleton.getCategories().get(i).getRecipes()) {
                    activeRecipes.put(recipe, singleton.getCategories().get(i));
                }
            }
        }
        selectRandomRecipe(v, activeRecipes);
    }

    private void selectRandomRecipe(View v, HashMap<Recipe, Category> activeRecipes) {
        Random random = new Random();
        List<Recipe> keys = new ArrayList<>(activeRecipes.keySet());
        int randomIndex = random.nextInt(keys.size());
        navigateToRecipeFragment(v, activeRecipes.get(keys.get(randomIndex)).getTitle(),
                keys.get(randomIndex).getTitle());
    }

    private void navigateToRecipeFragment(View v, String category, String recipe) {
        Bundle bundle = new Bundle();
        bundle.putString("categoryTitle", category);
        bundle.putString("recipeTitle", recipe);
        Navigation.findNavController(v).navigate(R.id.action_bottom_nav_bar_random_to_recipeFragment, bundle);
    }
}