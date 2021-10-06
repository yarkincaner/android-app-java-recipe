package com.example.myrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser fbUser;

    EditText editText_email;
    EditText editText_password;
    Button button_logIn;
    Button button_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editText_email = findViewById(R.id.login_editTextTextEmailAddress);
        editText_password = findViewById(R.id.login_editTextTextPassword);

        button_logIn = findViewById(R.id.login_buttonLogin);

        button_signUp = findViewById(R.id.login_buttonSignUp);
    }

    public void login(View view) {
        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Successfully logged in!");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
    }

    public void signUp(View view) {
        Intent toSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(toSignUp);
    }
}