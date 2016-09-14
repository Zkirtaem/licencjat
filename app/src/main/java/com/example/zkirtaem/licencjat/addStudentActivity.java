package com.example.zkirtaem.licencjat;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.*;
import android.nfc.tech.NfcA;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.math.BigInteger;
import java.util.Random;

public class addStudentActivity extends AppCompatActivity {


    Button step1_button;
    RelativeLayout step2layout;
    Button button_isClicked;
    Button backButton;

    String imieinazwisko;
    int indeks;
    String rfid;

    private MyDBDataSource datasource;


    NfcAdapter nfc;


    PendingIntent nfcPendingIntent;
    IntentFilter tagIntentFilter;
    Intent nfcIntent;

    boolean waitingForNfc=false;


    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2)+"X", new BigInteger(1, data));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        datasource = new MyDBDataSource(this);
        datasource.open();


        step1_button = (Button) findViewById(R.id.step1nextButton);
        backButton = (Button) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), firstActivity.class);
                startActivity(i);
            }
        });

        step1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imieinazwisko="";
                indeks=0;
                if (    ((TextView)findViewById(R.id.imieinazwiskoField)).getText().toString().length() < 8 ||
                        ((TextView)findViewById(R.id.indeksField)).getText().toString().length() < 4)
                {
                    Snackbar.make(view, "Aby przejść dalej, wypełnij pola poprawnymi danymi", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                    imieinazwisko = ((TextView) findViewById(R.id.imieinazwiskoField)).getText().toString();
                    indeks = Integer.parseInt(((TextView) findViewById(R.id.indeksField)).getText().toString());

                    setContentView(R.layout.activity_add_student_step2);
                    step2layout = (RelativeLayout) findViewById(R.id.step2layout);
                    waitingForNfc=true;
                }
            };
         });

        nfc=NfcAdapter.getDefaultAdapter(this);

        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        nfcPendingIntent =
                PendingIntent.getActivity(this, 0, nfcIntent, 0);
        IntentFilter tagIntentFilter =
                new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        Log.i("|MOJE LOGI|", "onCreate:dodajstudenta coś zrobił");
    }


    @Override
    protected void onResume() {
        super.onResume();


        Log.i("|MOJE LOGI|", "onResume:dodajstudenta coś zrobił");

            IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try {
                ndef.addDataType("*" +
                        "/*");

            } catch (IntentFilter.MalformedMimeTypeException e) {
                throw new RuntimeException("fail", e);
            }


            IntentFilter[] intentFiltersArray = new IntentFilter[]{ndef,};
            String[][] techLists = new String[][]{new String[]{NfcA.class.getName()}};

            nfc.enableForegroundDispatch(
                    this,
                    nfcPendingIntent,
                    intentFiltersArray,
                    techLists);
//        handleIntent(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfc.disableForegroundDispatch(this);
        Log.i("|MOJE LOGI|", "onPause:dodajstudenta coś zrobił");
    }


    @Override
    public void onNewIntent(Intent intent) {
        handleIntent(intent);
        Log.i("|MOJE LOGI|", "onNewIntent:dodajstudenta coś zrobił");
    }

    private void handleIntent(Intent intent) {
        if (waitingForNfc) {
            Tag mytag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (datasource.findStudent(bin2hex(mytag.getId())) == -1) {
                step2layout.setBackgroundColor(0x33FF33);
                step2layout.invalidate();

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                rfid = bin2hex(mytag.getId());
                datasource.addStudent(imieinazwisko, rfid, indeks);

                Intent i = new Intent(getApplicationContext(), firstActivity.class);
                startActivity(i);
            }
            else
            {
                Snackbar.make(findViewById(R.id.step2layout), "Ta karta jest już zarejestrowana!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
//        Log.i("ZNALAZLO ID", bin2hex(mytag.getId()));
        }
    }


}
