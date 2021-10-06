package com.example.myrecipes.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipes.R;
import com.example.myrecipes.dto.Category;
import com.example.myrecipes.dto.Singleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Category_RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity context;
    static FirebaseFirestore db;
    static FirebaseStorage storage;
    static Singleton singleton;

    class Category_RvAdapter_ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageButton imageButton;

        public Category_RvAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.category_title);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            imageButton = itemView.findViewById(R.id.category_button_delete);
        }

//        @Override
//        public void onClick(View v) {
//            System.out.println(getAdapterPosition());
//            System.out.println(categories.get(getAdapterPosition()).getTitle());
//            Navigation.createNavigateOnClickListener(R.id.action_bottom_nav_bar_home_to_recipesFragment).onClick(v);
//        }
    }

    public Category_RvAdapter(Activity context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        singleton = Singleton.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.category, parent, false);

        return new Category_RvAdapter_ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Category category = singleton.getCategories().get(position);
        Category_RvAdapter_ViewHolder viewHolder = (Category_RvAdapter_ViewHolder) holder;
        viewHolder.title.setText(category.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(category.getTitle());
                Bundle bundle = new Bundle();
                bundle.putString("categoryTitle", category.getTitle());
                Navigation.findNavController(v).navigate(R.id.action_bottom_nav_bar_home_to_recipesFragment, bundle);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (viewHolder.imageButton.getVisibility() == View.GONE) {
                    viewHolder.imageButton.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.imageButton.setVisibility(View.GONE);
                }
                return true;
            }
        });

        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(FirebaseAuth.getInstance().getUid())
                        .collection("categories").document(viewHolder.title.getText().toString())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("Document: " + viewHolder.title.getText().toString() + " deleted");
                                deleteRecipeImage(viewHolder);
                                singleton.deleteCategory(viewHolder.title.getText().toString());
                                notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println(e.getStackTrace());
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return singleton.getCategories().size();
    }

    private void deleteRecipeImage(Category_RvAdapter_ViewHolder viewHolder) {
        StorageReference storageReference = storage.getReference();
        for (int i = 0; i < singleton.getCategory(viewHolder.title.getText().toString()).getRecipes().size(); i++) {
            String imagePath = singleton.getCategory(viewHolder.title.getText().toString()).getRecipes().get(i).getImagePath();
            storageReference.child(imagePath).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("Image from: " + imagePath + " has been deleted");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e.getStackTrace());
                        }
                    });
        }
    }

//    public void filterList(ArrayList<Category> filterList) {
//        categories = filterList;
//        notifyDataSetChanged();
//    }
}
