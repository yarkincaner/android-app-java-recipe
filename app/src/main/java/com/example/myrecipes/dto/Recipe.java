package com.example.myrecipes.dto;

import android.widget.ImageView;

public class Recipe {

    private String title;
    private String description;
    private String recipe;
    private String imagePath;

    public Recipe(String title, String description, String recipe, String imagePath) {
        this.title = title;
        this.description = description;
        this.recipe = recipe;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
