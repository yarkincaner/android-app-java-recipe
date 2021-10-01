package com.example.myrecipes.dto;

import java.util.ArrayList;

public class Category {
    private String title;
    private ArrayList<Recipe> recipes;

    public Category(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }
}
