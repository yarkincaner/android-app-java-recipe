package com.example.myrecipes.dto;

import java.util.ArrayList;

public class Category {
    private String title;
    private boolean isActive;
    private ArrayList<Recipe> recipes;

    public Category(String title) {
        this.title = title;
        isActive = false;
        recipes = new ArrayList<>();
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Recipe getRecipe(String title) {
        for (Recipe recipe : recipes) {
            if (recipe.getTitle().equals(title)) {
                return recipe;
            }
        }

        return null;
    }

    public void deleteRecipe(String title) {
        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i).getTitle().equals(title)) {
                recipes.remove(i);
                break;
            }
        }
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }
}
