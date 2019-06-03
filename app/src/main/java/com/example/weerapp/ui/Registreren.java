package com.example.weerapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weerapp.R;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.afsluiten_main)
            new AlertDialog.Builder(this)
                    .setTitle("Afsluiten")
                    .setMessage("Weet je zeker dat je wilt afsluiten?")

                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    })

                    .setNegativeButton("Nee", null)
                    .show();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Controleren of gebruiker is ingelogd (niet null) en UI updaten
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registreren);

        mAuth = FirebaseAuth.getInstance();

        nieuwEmailadres = findViewById(R.id.emailadresregisteren);
        nieuwWachtwoord = findViewById(R.id.wachtwoordregistreren);
        registreerbtn = findViewById(R.id.registreerbtn);
    }

    /**
     * Deze methode zorgt ervoor dat er naar het volgende scherm wordt genavigeerd
     * als er een gebruiker succesvol is ingelogd
     * @param currentUser is de gebruiker die momenteel is ingelogd
     */
    public void updateUI(FirebaseUser currentUser) {
        Toast.makeText(Registreren.this, "Gebruiker geregistreerd met e-mailadres " +
                currentUser.getEmail(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Registreren.this, StadInvoeren.class);
        startActivity(intent);
    }

    /**
     * Deze methode zorgt ervoor dat een gebruiker op basis van
     * emailadres en wachtwoord wordt geregistreerd
     * @param view is de view (button) waar op geklikt wordt om deze methode aan te roepen
     */
    public void registreren(View view) {
        String emailadres = nieuwEmailadres.getText().toString();
        String wachtwoord = nieuwWachtwoord.getText().toString();

        // als emailadres en wachtwoord niet zijn ingevuld melding geven
        if ((TextUtils.isEmpty(emailadres)) || (TextUtils.isEmpty(wachtwoord))) {
            // If sign in fails, display a message to the user.
            Toast.makeText(Registreren.this,
                    "Voer een geldig e-mailadres en wachtwoord in",
                    Toast.LENGTH_LONG).show(); }
                    else {
            mAuth.createUserWithEmailAndPassword(emailadres, wachtwoord)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // inloggen gelukt. UI updaten.
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }
                        }
                    });
        }
    }
}