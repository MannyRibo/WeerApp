package com.example.weerapp.ui;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import maes.tech.intentanim.CustomIntent;

import com.example.weerapp.R;
import com.example.weerapp.api.OnGetWeerObjectCallback;
import com.example.weerapp.api.WeerObjectenRepository;
import com.example.weerapp.database.WeerObjectRoomDatabase;
import com.example.weerapp.model.WeerObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StadInvoeren extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = "StadInvoeren";
    private static final int TEMPERATUUR_VOOR_KORTE_BROEK = 18;
    public static final String WEEROBJECT= "weerobject";
    EditText editTextStad;
    String tekstStad;
    LocationManager locationManager;
    LocationListener locationListener;
    Double latitude;
    Double longitude;
    private WeerObjectenRepository weerObjectenRepository;
    private WeerViewModel mWeerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stad_invoeren);

        mWeerViewModel = ViewModelProviders.of(this).get(WeerViewModel.class);

        editTextStad = findViewById(R.id.editTextStad);
        tekstStad = editTextStad.getText().toString();

        weerObjectenRepository = WeerObjectenRepository.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.uitloggen:
                uitloggen();
                return true;

            case R.id.wachtwoordreset:
                wachtwoordResetten();
                return true;

            case R.id.afsluiten:
                afsluiten();
                return true;

            case R.id.geschiedenis:
                naarGeschiedenis();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Deze methode controleert of er toestemming is om GPS te gebruiken
     * @param view is de view (button) waar op geklikt wordt om deze methode aan te roepen
     */
    public void checkPermissies(final View view) {

        if (ContextCompat.checkSelfPermission(StadInvoeren.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Toestemming is geweigerd
            // Alertdialog weergeven om GPS toestemming te vragen
            if (ActivityCompat.shouldShowRequestPermissionRationale(StadInvoeren.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Toestemming nodig")
                        .setMessage("Toestemming is nodig om GPS-functie te kunnen gebruiken")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(StadInvoeren.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }

                        })
                        .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();

            } else {
                // vragen om toestemming
                ActivityCompat.requestPermissions(StadInvoeren.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else
        // toestemming is gegeven
        {
            locatieBepalen();
        }
    }


    /**
     * Deze methode geeft een alertdialog weer om aan te geven dat GPS moet worden ingeschakeld
     */
    public void showAlertGeenGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Het lijkt erop dat GPS is uitgeschakeld, wil je GPS nu inschakelen?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }

                })
                .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Deze methode zorgt ervoor dat er een locatie wordt opgehaald
     * De lengte- en breedtegraad van deze locatie wordt vervolgens opgeslagen in twee variabelen
     */
    public void locatieBepalen() {
        if (ContextCompat.checkSelfPermission(StadInvoeren.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // als GPS is uitgeschakeld vragen deze aan te zetten
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showAlertGeenGPS();
            }

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        // stoppen met GPS gebruiken
                        locationManager.removeUpdates(locationListener);

                        stadBepalen();
                    } else {
                        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                            Toast.makeText(StadInvoeren.this, "Geen locatie gevonden", Toast.LENGTH_LONG).show();
                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // stoppen met GPS gebruiken
                    locationManager.removeUpdates(locationListener);
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                    // stoppen met GPS gebruiken
                    locationManager.removeUpdates(locationListener);
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

    /**
     * Deze methode zorgt ervoor dat een gebruiker wordt uitgelogd
     */
    public void uitloggen() {
        Toast.makeText(StadInvoeren.this, "Gebruiker met emailadres " +
                        FirebaseAuth.getInstance().getCurrentUser().getEmail() + " is uitgelogd"
                ,
                Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(StadInvoeren.this, MainActivity.class);
        startActivity(intent);
        CustomIntent.customType(StadInvoeren.this, "left-to-right");
    }

    /**
     * Deze methode zorgt ervoor dat er een reset e-mail wordt gestuurd naar de gebruiker
     */
    public void wachtwoordResetten() {
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

    /**
     * Deze methode zorgt ervoor dat de applicatie wordt afgesloten
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

    /**
     * Deze methode zorgt ervoor dat er wordt genavigeerd naar de geschiedenis activity
     */
    public void naarGeschiedenis() {
        Intent intent = new Intent(StadInvoeren.this, WeerGeschiedenis.class);
        startActivity(intent);
        CustomIntent.customType(StadInvoeren.this, "left-to-right");
    }

    /**
     * Deze methode levert op basis van de eerder opgehaalde lengte- en breedtegraad
     * een stadnaam en vult deze in in de editText op het scherm
     */
    public void stadBepalen() {

        try {
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);

            if (addresses.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Geen adres gevonden, voer stad handmatig in", Toast.LENGTH_LONG).show();
            } else {
                if (addresses.size() > 0) {
                    editTextStad.setText(addresses.get(0).getLocality());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deze methode kijkt of de GPS functie is gebruikt of dat er handmatig een stadnaam is ingevuld
     * en roept vervolgens de juiste methode aan die de juiste API call uitvoert
     * @param view is de view (button) waar op geklikt wordt om deze methode aan te roepen
     */
    public void temperatuurOphalen(View view) {

        tekstStad = editTextStad.getText().toString();

        if((!TextUtils.isEmpty(tekstStad)))
            resultaatOpBasisVanStadNaam(tekstStad);
            else
        Toast.makeText(StadInvoeren.this,
                "Voer een stad in of gebruik GPS om locatie te bepalen", Toast.LENGTH_LONG).show();

        /*if (latitude == null && longitude == null && (!TextUtils.isEmpty(tekstStad))) {
            resultaatOpBasisVanStadNaam(tekstStad);

        } else if (latitude != null && longitude != null) {
            resultaatOpBasisVanCoordinaten(latitude, longitude);

        } else {
            Toast.makeText(StadInvoeren.this,
                    "Voer een stad in of gebruik GPS om locatie te bepalen", Toast.LENGTH_LONG).show();
        }*/

    }

    /**
     * Deze methode voert een API call uit op basis van stadnaam en krijgt een weerobject terug
     * Vervolgens wordt de huidige datum toegevoegd, de stadnaam in de juiste format gezet en de
     * temperatuur wordt afgerond op 1 decimaal
     * Uiteindelijk wordt op basis van de temperatuur naar de ene of andere activity genavigeerd
     * @param stadNaam is de naam van de stad die is ingevoerd door de gebruiker
     */
    public void resultaatOpBasisVanStadNaam(String stadNaam) {

        weerObjectenRepository.getWeerObjectfromStadNaam(stadNaam, new OnGetWeerObjectCallback() {
            @Override
            public void onSuccess(WeerObject weerObject) {

                datumToevoegen(weerObject);
                naamStadNaarHoofdletter(weerObject);
                temperatuurAfronden(weerObject);

                mWeerViewModel.insert(weerObject);

                if (weerObject.getMain().getTemp() >= TEMPERATUUR_VOOR_KORTE_BROEK) {
                    naarSchermKorteBroek(weerObject);
                } else {
                    naarSchermGeenKorteBroek(weerObject);
                }
            }

            @Override
            public void onError() {
                Toast.makeText(StadInvoeren.this,
                        "Controleer of de naam van de stad juist is", Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Deze methode voert een API call uit op basis van lengte- en breedtegraad en krijgt een weerobject terug
     * Vervolgens wordt de huidige datum toegevoegd, de stadnaam in de juiste format gezet en de
     * temperatuur wordt afgerond op 1 decimaal
     * Uiteindelijk wordt op basis van de temperatuur naar de ene of andere activity genavigeerd
     * @param latitude is de breedtegraad die door de GPS is geleverd
     * @param longitude is de lengtegraad die door de GPS is geleverd
     */
    public void resultaatOpBasisVanCoordinaten(Double latitude, Double longitude) {
        weerObjectenRepository.getWeerObjectfromCoordinaten(latitude, longitude, new OnGetWeerObjectCallback() {
            @Override
            public void onSuccess(WeerObject weerObject) {

                datumToevoegen(weerObject);
                naamStadNaarHoofdletter(weerObject);
                temperatuurAfronden(weerObject);

                mWeerViewModel.insert(weerObject);

                if (weerObject.getMain().getTemp() >= TEMPERATUUR_VOOR_KORTE_BROEK) {
                    naarSchermKorteBroek(weerObject);
                } else {
                    naarSchermGeenKorteBroek(weerObject);
                }
            }

            @Override
            public void onError() {
                Toast.makeText(StadInvoeren.this,
                        "Er ging iets niet goed bij het ophalen van het weer", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Deze methode voegt de huidige datum toe aan het weerobject
     * @param weerObject is het weerobject verkregen van de API call
     */
    public void datumToevoegen(WeerObject weerObject) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String datum = formatter.format(date);

        weerObject.setDatum(datum);
    }

    /**
     * Deze methode zorgt ervoor dat de stadnaam in de juiste format wordt opgeslagen in het weerobject
     * voor een goede weergave in de volgende activity en de recyclerview
     * @param weerObject is het weerobject verkregen van de API call
     */
    public void naamStadNaarHoofdletter(WeerObject weerObject) {
        tekstStad = editTextStad.getText().toString();

        tekstStad.trim();
        String[] arr = tekstStad.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        String naamStadHoofdletter = sb.toString().trim();

        weerObject.setNaamStad(naamStadHoofdletter);
    }

    /**
     * Deze methode zorgt ervoor dat de temperatuur opgeslagen in het weerobject wordt afgerond naar
     * 1 decimaal voor een nettere weergave
     * @param weerObject is het weerobject verkregen van de API call
     */
    public void temperatuurAfronden(WeerObject weerObject) {
        /*DecimalFormat decimalFormat = new DecimalFormat("#.#");
        weerObject.getMain().setTemp(Double.parseDouble(decimalFormat.format(weerObject.getMain().getTemp())));*/

        double tweeDecimaalTemperatuur = weerObject.getMain().getTemp();

        double eenDecimaalTemperatuur = Math.round(tweeDecimaalTemperatuur * 10) / 10.0;

        weerObject.getMain().setTemp(eenDecimaalTemperatuur);

    }

    /**
     * Deze methode zorgt ervoor dat de korte broek activity wordt gestart
     * @param weerObject is het weerobject waar de temperatuur wordt uitgehaald in de volgende
     *                   activity
     */
    public void naarSchermKorteBroek(WeerObject weerObject) {
        Intent intent = new Intent(StadInvoeren.this, KorteBroek.class);
        intent.putExtra(WEEROBJECT, weerObject);
        startActivity(intent);
        CustomIntent.customType(StadInvoeren.this, "left-to-right");
    }

    /**
     * Deze methode zorgt ervoor dat de geen korte broek activity wordt gestart
     * @param weerObject is het weerobject waar de temperatuur wordt uitgehaald in de volgende
     *                   activity
     */
    public void naarSchermGeenKorteBroek(WeerObject weerObject) {
        Intent intent = new Intent(StadInvoeren.this, GeenKorteBroek.class);
        intent.putExtra(WEEROBJECT, weerObject);
        startActivity(intent);
        CustomIntent.customType(StadInvoeren.this, "left-to-right");
    }

}