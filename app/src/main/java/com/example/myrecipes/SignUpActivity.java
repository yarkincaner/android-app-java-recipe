package com.example.myrecipes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrecipes.dto.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SignUpActivity extends AppCompatActivity {

    EditText editText_email;
    EditText editText_password;
    EditText editText_confirmPassword;
    TextView textView_Error;
    Button button_SignUp;

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        editText_email = findViewById(R.id.signUp_editTextTextEmailAddress);
        editText_password = findViewById(R.id.signUp_editTextPassword);
        editText_confirmPassword = findViewById(R.id.signUp_editTextPassword2);
        textView_Error = findViewById(R.id.signUp_textViewError);
        textView_Error.setVisibility(View.GONE);
        button_SignUp = findViewById(R.id.signUp_button_signUp);

        button_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText_email.getText().toString();
                String password = editText_password.getText().toString();
                String confirmPassword = editText_confirmPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, R.string.signup_error_empty, Toast.LENGTH_LONG)
                            .show();
                } else {
                    if (editText_password.getText().toString().equals(editText_confirmPassword.getText().toString())) {
                        signUp();
                    } else {
                        textView_Error.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void signUp() {
        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Authentication Complete");

                            Toast toast = Toast.makeText(getApplicationContext(), "Process Successfull!", Toast.LENGTH_SHORT);
                            toast.show();

                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            startActivity(intent);
                        } else {
                            System.out.println(task.getException().getMessage());
                            Toast toast = Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }
}