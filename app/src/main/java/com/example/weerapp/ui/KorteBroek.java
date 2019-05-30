package com.example.weerapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        omschrijving = findViewById(R.id.omschrijvingKorteBroek);

        final WeerObject weerObject = getIntent().getParcelableExtra(StadInvoeren.WEEROBJECT);
        temperatuur = weerObject.getMain().getTemp();
        naamStad = weerObject.getNaamStad();

        omschrijving.setText("Het is weer voor een korte broek! \n" +
                "Het is " + temperatuur + "°C in " + naamStad);
    }
}
