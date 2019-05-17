package com.example.weerapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText nieuwEmailadres;
    private EditText nieuwWachtwoord;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        nieuwEmailadres = findViewById(R.id.emailadres);
        nieuwWachtwoord = findViewById(R.id.wachtwoord);

    }

    public void updateUI(FirebaseUser currentUser) {
        Toast.makeText(MainActivity.this, "Gebruiker ingelogd met e-mailadres " +
                currentUser.getEmail(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, StadInvoeren.class);
        startActivity(intent);
    }

    public void inloggen(View view) {
        String emailadres = nieuwEmailadres.getText().toString();
        String wachtwoord = nieuwWachtwoord.getText().toString();

        if ((!TextUtils.isEmpty(emailadres)) || (!TextUtils.isEmpty(wachtwoord))) {
            mAuth.signInWithEmailAndPassword(emailadres, wachtwoord)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "E-mailadres of wachtwoord is incorrect",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        else {
            Toast.makeText(MainActivity.this, "Voer een e-mailadres en wachtwoord in", Toast.LENGTH_LONG).show();
        }

    }

    public void naarRegistreren(View view) {
        Intent intent = new Intent(MainActivity.this, Registreren.class);
        startActivity(intent);
    }

}