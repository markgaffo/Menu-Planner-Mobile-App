package com.example.caproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.editTextTextEmailAddress);
        EditText etPassword = findViewById(R.id.editTextTextPassword);
        Button bSignUp = findViewById(R.id.button);
        Button bSingIn = findViewById(R.id.button2);
        tvUser = findViewById(R.id.textView);

        mAuth = FirebaseAuth.getInstance();

        String email, password;
        //adding user to the firebase
        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                //creating new user
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "User created: "+user.getEmail(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        bSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String email, password;
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                    //checking user account for login
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //if user is in database
                            if(task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                                LoginActivity.this.startActivity(mainIntent);
                                LoginActivity.this.finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}