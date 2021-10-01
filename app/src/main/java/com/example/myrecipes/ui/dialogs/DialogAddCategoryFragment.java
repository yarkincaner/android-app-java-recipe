package com.example.myrecipes.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myrecipes.R;
import com.example.myrecipes.dto.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class DialogAddCategoryFragment extends DialogFragment {

    FirebaseFirestore db;

    View myView;
    EditText categoryName;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        myView = inflater.inflate(R.layout.fragment_dialog_add_category, null);
        db = FirebaseFirestore.getInstance();

        builder.setView(myView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        System.out.println("Add category");
                        categoryName = myView.findViewById(R.id.dialog_add_category_editTextName);
                        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        db.collection("users").document(uuid)
                                .collection("categories")
                                .document(categoryName.getText().toString())
                                .set(new Category(categoryName.getText().toString()));

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Cancel add category");
                    }
                });
        return builder.create();
    }
}