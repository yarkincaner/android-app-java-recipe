package com.example.myrecipes.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myrecipes.MainActivity;
import com.example.myrecipes.R;
import com.example.myrecipes.dto.Recipe;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseStorage storage;

    ImageView recipeImage;
    Drawable defaultImageDrawable;
    EditText editText_title;
    EditText editText_description;
    EditText editText_recipe;
    Button button_save;
    Button button_cancel;

    Bundle bundle;

    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");
                        recipeImage.setImageBitmap(bitmap);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        recipeImage = findViewById(R.id.fragment_add_recipe_imageViewImage);
        defaultImageDrawable = recipeImage.getDrawable();
        editText_title = findViewById(R.id.fragment_add_recipe_editTextTitle);
        editText_description = findViewById(R.id.fragment_add_recipe_editTextDescription);
        editText_recipe = findViewById(R.id.fragment_add_recipe_editTextRecipe);
        button_save = findViewById(R.id.fragment_add_recipe_buttonSave);
        button_cancel = findViewById(R.id.fragment_add_recipe_buttonCancel);

        Intent intent = getIntent();
        bundle = intent.getExtras();
    }

    public void addRecipeImage(View view) {
        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraResultLauncher.launch(openCamera);
    }

    public void recipeCancel(View view) {
        Intent intent = new Intent(AddRecipeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void recipeSave(View view) {

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        String uuid = FirebaseAuth.getInstance().getUid();
        UUID iuid = UUID.randomUUID(); //Image Unique ID
        StorageReference storageReference = storage.getReference();
        StorageReference imageReference = storageReference.child("images/" + uuid + "/" + iuid + ".jpeg");

        recipeImage.setDrawingCacheEnabled(true);
        recipeImage.buildDrawingCache();

        if (defaultImageDrawable == recipeImage.getDrawable()) {
            Toast.makeText(AddRecipeActivity.this, R.string.addRecipeActivity_toast_imageNotDetected, Toast.LENGTH_LONG)
                    .show();
        } else {
            Bitmap bitmap = ((BitmapDrawable) recipeImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            String title = editText_title.getText().toString();
            String description = editText_description.getText().toString();
            String recipeText = editText_recipe.getText().toString();

            Recipe recipe = new Recipe(title, description, recipeText, imageReference.getPath());

            UploadTask uploadTask = imageReference.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    db.collection("users").document(uuid)
                            .collection("categories").document(bundle.getString("categoryTitle"))
                            .collection("recipes").document(title)
                            .set(recipe);
                    Toast.makeText(AddRecipeActivity.this, R.string.addRecipeActivity_toast_saved, Toast.LENGTH_LONG);
                    startActivity(new Intent(AddRecipeActivity.this, MainActivity.class));
                }
            });
        }
    }
}