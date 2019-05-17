package com.example.weerapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class StadInvoeren extends AppCompatActivity {

    private static final String TAG = "StadInvoeren";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stad_invoeren);

    }

    public void uitloggen(View view) {
        Toast.makeText(StadInvoeren.this, "Gebruiker met emailadres " +
                        FirebaseAuth.getInstance().getCurrentUser().getEmail() + " is uitgelogd"
                ,
                Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(StadInvoeren.this, MainActivity.class);
        startActivity(intent);
    }

    public void controleren(View view) {
        // logica
    }

    public void wachtwoordreset(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String emailAddress = auth.getCurrentUser().getEmail();
        auth.useAppLanguage();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email verstuurd");
                            Toast.makeText(StadInvoeren.this,
                                    "Wachtwoordreset e-mail gestuurd naar "
                                    + emailAddress, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
