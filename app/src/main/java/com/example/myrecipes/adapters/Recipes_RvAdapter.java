package com.example.myrecipes.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipes.R;
import com.example.myrecipes.dto.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Recipes_RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    ArrayList<Recipe> recipes;
    String categoryTitle;

    class Recipe_RvAdapter_ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView description;

        public Recipe_RvAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipes_image);
            title = itemView.findViewById(R.id.recipes_title);
            description = itemView.findViewById(R.id.recipes_description);
        }
    }

    public Recipes_RvAdapter(Activity context, ArrayList<Recipe> recipes, String categoryTitle) {
        this.context = context;
        this.recipes = recipes;
        this.categoryTitle = categoryTitle;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.recipes_cardview, parent, false);
        return new Recipe_RvAdapter_ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        Recipe recipe = recipes.get(position);
        Recipe_RvAdapter_ViewHolder viewHolder = (Recipe_RvAdapter_ViewHolder) holder;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageReference = storageReference.child(recipe.getImagePath());
        final long ONE_MEGABYTE = 1024 * 1024;
        imageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewHolder.imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getStackTrace());
            }
        });

        viewHolder.title.setText(recipe.getTitle());
        viewHolder.description.setText(recipe.getDescription());

        Bundle bundle = new Bundle();
        bundle.putString("categoryTitle", categoryTitle);
        bundle.putString("recipeTitle", recipe.getTitle());
        holder.itemView.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_recipesFragment_to_recipeFragment, bundle)
        );
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
