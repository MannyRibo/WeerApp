package com.example.weerapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weerapp.R;
import com.example.weerapp.model.Main;
import com.example.weerapp.model.WeerObject;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.afsluiten_main) {
            afsluiten();
        }
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
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        nieuwEmailadres = findViewById(R.id.emailadres);
        nieuwWachtwoord = findViewById(R.id.wachtwoord);

        // titel niet weergeven
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    /**
     * Deze methode zorgt ervoor dat er naar het volgende scherm wordt genavigeerd
     * als er een gebruiker succesvol is ingelogd
     * @param currentUser is de gebruiker die momenteel is ingelogd
     */
    public void updateUI(FirebaseUser currentUser) {
        Toast.makeText(MainActivity.this, "Gebruiker ingelogd met e-mailadres " +
                currentUser.getEmail(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, StadInvoeren.class);
        startActivity(intent);
    }


    /**
     * Deze methode zorgt ervoor dat een gebruiker op basis van
     * emailadres en wachtwoord wordt ingelogd
     * @param view is de view (button) waar op geklikt wordt om deze methode aan te roepen
     */
    public void inloggen(View view) {
        String emailadres = nieuwEmailadres.getText().toString();
        String wachtwoord = nieuwWachtwoord.getText().toString();

        // als emailadres en wachtwoord zijn ingevuld proberen in te loggen
        if ((!TextUtils.isEmpty(emailadres)) || (!TextUtils.isEmpty(wachtwoord))) {
            mAuth.signInWithEmailAndPassword(emailadres, wachtwoord)

                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // inloggen gelukt, update UI met informatie van de gebruiker
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // Als inloggen mislukt melding geven aan de gebruiker
                                Toast.makeText(MainActivity.this, "E-mailadres of wachtwoord is incorrect",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            // als emailadres of wachtwoord niet is ingevuld melding geven dat deze nodig zijn
            Toast.makeText(MainActivity.this,
                    "Voer een e-mailadres en wachtwoord in", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Deze methode zorgt ervoor dat er naar het hoofdscherm wordt genavigeerd
     * @param view is de view (button) waar op geklikt wordt om deze methode aan te roepen
     */
    public void naarRegistreren(View view) {
        Intent intent = new Intent(MainActivity.this, Registreren.class);
        startActivity(intent);
    }

    /**
     * Deze methode zorgt ervoor dat de applicatie wordt afgesloten
     * als de gebruiker op afsluiten klikt
     */
    public void afsluiten() {
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
    }
}