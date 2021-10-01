package com.example.myrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myrecipes.dto.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser fbUser;

    EditText editText_name;
    EditText editText_surname;
    EditText editText_username;
    Button button_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

        editText_name = findViewById(R.id.register_editTextName);
        editText_surname = findViewById(R.id.register_editTextSurname);
        editText_username = findViewById(R.id.register_editTextUsername);
        button_continue = findViewById(R.id.register_buttonSave);

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText_name.getText().toString();
                String surname = editText_surname.getText().toString();
                String username = editText_username.getText().toString();

                db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    boolean isAuthenticated = true;

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (username.equals(document.get("userName"))) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Username has already been taken!", Toast.LENGTH_LONG);
                                    toast.show();
                                    isAuthenticated = false;
                                    break;
                                }
                            }

                            if (isAuthenticated) {
                                User user = new User(username, fbUser.getEmail(), name, surname);
                                db.collection("users").document(fbUser.getUid()).set(user);

                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
    }
}