package com.example.myrecipes.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myrecipes.R;
import com.example.myrecipes.adapters.Recipes_RvAdapter;
import com.example.myrecipes.dto.Recipe;
import com.example.myrecipes.dto.Singleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipesFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Recipes_RvAdapter recipeRvAdapter;

    FloatingActionButton fab;
    Bundle bundle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recipes_fragment, container, false);
        this.setHasOptionsMenu(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_recipe);
        fab = view.findViewById(R.id.recipes_fab);
        bundle = getArguments();

        recipeRvAdapter = new Recipes_RvAdapter(requireActivity(), bundle.getString("categoryTitle"));
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        recyclerView.setAdapter(recipeRvAdapter);

        fab.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recipes_fab:
                navigateToAddRecipe();
        }
    }

    private void navigateToAddRecipe() {
        System.out.println("RecipesFragment Add Recipe");
        System.out.println(bundle.getString("categoryTitle"));

        Intent intent = new Intent(getContext(), AddRecipeActivity.class);
        intent.putExtra("categoryTitle", bundle.getString("categoryTitle"));
        startActivity(intent);
    }
}