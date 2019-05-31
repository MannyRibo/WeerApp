package com.example.weerapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weerapp.R;
import com.example.weerapp.model.WeerObject;

public class KorteBroek extends AppCompatActivity {

    private TextView omschrijving;
    String naamStad;
    double temperatuur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korte_broek);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        omschrijving = findViewById(R.id.omschrijvingKorteBroek);

        // intent ontvangen van de vorige activity
        final WeerObject weerObject = getIntent().getParcelableExtra(StadInvoeren.WEEROBJECT);

        // attributen van het weerobject binden aan de lokale variabelen
        temperatuur = weerObject.getMain().getTemp();
        naamStad = weerObject.getNaamStad();

        omschrijving.setText(getString(R.string.weervoorkortebroek) + "\n" +
                getString(R.string.hetis) + " " + temperatuur + getString(R.string.gradenin) + " " + naamStad);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
