package com.example.myrecipes.ui;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myrecipes.R;
import com.example.myrecipes.dto.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecipeFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseStorage storage;

    ImageView repcipeImage;
    TextView title;
    TextView description;
    TextView recipeOfTheDish;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recipe_fragment, container, false);
        this.setHasOptionsMenu(true);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        repcipeImage = view.findViewById(R.id.recipe_image);
        title = view.findViewById(R.id.recipe_title);
        description = view.findViewById(R.id.recipes_description);
        recipeOfTheDish = view.findViewById(R.id.recipe_recipe);

        getData();

        return view;
    }

    private void getData() {
        Task<DocumentSnapshot> getDocument = db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .collection("categories").document(getArguments().getString("categoryTitle"))
                .collection("recipes").document(getArguments().getString("recipeTitle")).get();

        while (!getDocument.isComplete()) {
            System.out.println(getDocument.isComplete());
        }

        DocumentSnapshot documentSnapshot = getDocument.getResult();
        if (documentSnapshot.exists()) {
            title.setText(documentSnapshot.getString("title"));
            description.setText(documentSnapshot.getString("description"));
            recipeOfTheDish.setText(documentSnapshot.getString("recipe"));
            getImage(documentSnapshot.getString("imagePath"));
        }
    }

    private void getImage(String path) {
        StorageReference storageReference = storage.getReference();
        StorageReference imageReference = storageReference.child(path);

        final long ONE_MEGABYTE = 1024 * 1024;
        imageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                repcipeImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getStackTrace());
            }
        });
    }

}