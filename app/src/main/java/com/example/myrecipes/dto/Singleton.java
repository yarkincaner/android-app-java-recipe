package com.example.myrecipes.dto;

import java.util.ArrayList;

public class Singleton {

    private static Singleton instance = null;
    private ArrayList<Category> categories;

    public Singleton() {
        categories = new ArrayList<>();
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }

        return instance;
    }

    public Category getCategory(String title) {
        for (Category category : categories) {
            if (category.getTitle().equals(title)) {
                return category;
            }
        }

        return null;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void deleteCategory(String title) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getTitle().equals(title)) {
                categories.remove(i);
            }
        }
    }

    public void clearArray() {
        categories.clear();
    }

}
