package com.example.myrecipes.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myrecipes.R;
import com.example.myrecipes.dto.Recipe;

public class RecipeFragment extends Fragment {

    TextView title;
    TextView description;
    TextView recipeOfTheDish;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recipe_fragment, container, false);
        this.setHasOptionsMenu(true);

        title = view.findViewById(R.id.recipe_title);
        description = view.findViewById(R.id.recipes_description);
        recipeOfTheDish = view.findViewById(R.id.recipe_recipe);

        Recipe soup = new Recipe("Soup", "My favorite soup", "nibh cras pulvinar mattis nunc sed blandit libero volutpat sed", "");
        title.setText(soup.getTitle());
        description.setText(soup.getDescription());
        recipeOfTheDish.setText(soup.getRecipe());

        return view;
    }

}