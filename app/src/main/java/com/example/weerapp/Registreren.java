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

public class Registreren extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText nieuwEmailadres;
    private EditText nieuwWachtwoord;
    private Button registreerbtn;
    private static final String TAG = "Registreren";

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
        setContentView(R.layout.activity_registreren);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        nieuwEmailadres = findViewById(R.id.emailadresregisteren);
        nieuwWachtwoord = findViewById(R.id.wachtwoordregistreren);
        registreerbtn = findViewById(R.id.registreerbtn);
    }

    public void updateUI(FirebaseUser currentUser) {
        Toast.makeText(Registreren.this, "Gebruiker geregistreerd met e-mailadres " +
                currentUser.getEmail(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Registreren.this, StadInvoeren.class);
        startActivity(intent);
    }

    public void registreren(View view) {
        String emailadres = nieuwEmailadres.getText().toString();
        String wachtwoord = nieuwWachtwoord.getText().toString();

        if ((!TextUtils.isEmpty(emailadres)) || (!TextUtils.isEmpty(wachtwoord))) {
            mAuth.createUserWithEmailAndPassword(emailadres, wachtwoord)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(Registreren.this,
                                        "Voer een geldig e-mailadres en wachtwoord in",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}
