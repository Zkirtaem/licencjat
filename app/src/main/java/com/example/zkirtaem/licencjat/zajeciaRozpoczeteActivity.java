package com.example.zkirtaem.licencjat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;




public class zajeciaRozpoczeteActivity extends AppCompatActivity {

    ListView zajeciaChooseList;
    private ArrayAdapter<String> adapter;

    ArrayList<String>zajarr;
    ArrayList<String>stuarr;

    MyDBDataSource datasource;



    NfcAdapter nfc;

    Tag mytag;


    PendingIntent nfcPendingIntent;
    IntentFilter tagIntentFilter;
    Intent nfcIntent;


    RelativeLayout zajeciaRozpoczeteLayout;

    RelativeLayout addStudentInListLayout;
    Button cancelButton;
    Button confirmButton;
    EditText imieinazwiskoField;
    EditText indeksField;

    boolean waitingForNfc=false;

    int zajeciaid=-1;



    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2)+"X", new BigInteger(1, data));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zajecia_rozpoczete);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datasource = new MyDBDataSource(this);
        datasource.open();

        setTitle("Zajęcia");

        zajeciaChooseList=(ListView)findViewById(R.id.zajeciaChooseList);

        zajeciaRozpoczeteLayout=(RelativeLayout)findViewById(R.id.zajeciaRozpoczeteLayout);


        zajarr = datasource.getZajeciaArray();

        adapter = new ArrayAdapter<String>(this, R.layout.row, zajarr);

        zajeciaChooseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                zajeciaid = datasource.addOdbyteZajecia(datasource.findZajecia(zajarr.get(position)));
                waitingForNfc = true;
                setContentView(R.layout.content_zajecia_wyborstudentow);
            }
        });



        nfc=NfcAdapter.getDefaultAdapter(this);

        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        nfcPendingIntent =
                PendingIntent.getActivity(this, 0, nfcIntent, 0);
        IntentFilter tagIntentFilter =
                new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);



        zajeciaChooseList.setAdapter(adapter);

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfc.disableForegroundDispatch(this);
        Log.i("|MOJE LOGI|", "onPause:zajeciarozpoczete coś zrobił");
    }


    @Override
    public void onNewIntent(Intent intent) {
        handleIntent(intent);
        Log.i("|MOJE LOGI|", "onNewIntent:zajeciarozpoczete coś zrobił");
    }



    private void handleIntent(Intent intent) {
        if (waitingForNfc) {
            mytag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (datasource.findStudent(bin2hex(mytag.getId())) != -1) {

                if (datasource.findObecnosc(zajeciaid, datasource.findStudent(bin2hex(mytag.getId()))) == -1) {

                    datasource.addObecnosc(zajeciaid, bin2hex(mytag.getId()).toString());
                    TextView studentlist = (TextView) findViewById(R.id.studentlist);
                    studentlist.setText(datasource.getObecnosciList(zajeciaid));
                }
                else
                {
                    Snackbar.make(findViewById(R.id.wyborstudentowlayout), "Student jest już zapisany na liście!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
            else
            {

                addStudentInListLayout=(RelativeLayout)findViewById(R.id.addStudentInListLayout);

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        addStudentInListLayout.setVisibility(View.VISIBLE);
                                        imieinazwiskoField=(EditText)findViewById(R.id.imieinazwiskoField);
                                        indeksField=(EditText)findViewById(R.id.indeksField);
                                        imieinazwiskoField.setText("");
                                        indeksField.setText("");

                                        cancelButton = (Button) findViewById(R.id.cancelButton);

                                        cancelButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                                addStudentInListLayout = (RelativeLayout) findViewById(R.id.addStudentInListLayout);
                                                addStudentInListLayout.setVisibility(View.GONE);
                                            }
                                        });


                                        confirmButton = (Button) findViewById(R.id.confirmButton);
                                        confirmButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                imieinazwiskoField=(EditText)findViewById(R.id.imieinazwiskoField);
                                                indeksField=(EditText)findViewById(R.id.indeksField);

                                                if (!(    ((TextView)findViewById(R.id.imieinazwiskoField)).getText().toString().length() < 8 ||
                                                        ((TextView)findViewById(R.id.indeksField)).getText().toString().length() < 4)) {
                                                    addStudentInListLayout = (RelativeLayout) findViewById(R.id.addStudentInListLayout);
                                                    addStudentInListLayout.setVisibility(View.GONE);

                                                    String imieinazwisko = ((TextView) findViewById(R.id.imieinazwiskoField)).getText().toString();
                                                    int indeks = Integer.parseInt(((TextView) findViewById(R.id.indeksField)).getText().toString());

                                                    String rfid = bin2hex(mytag.getId());
                                                    datasource.addStudent(imieinazwisko, rfid, indeks);

                                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                                                    Snackbar.make(findViewById(R.id.wyborstudentowlayout), "Dane wprowadzone do systemu. Przyłóż kartę ponownie aby zapisać się na obecną listę.", Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                }
                                                else
                                                {
                                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                                    Snackbar.make(findViewById(R.id.wyborstudentowlayout), "Wypełnij pola poprawnymi danymi.", Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                }
                                            }
                                        });


                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:

                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(findViewById(R.id.wyborstudentowlayout).getContext());
                        builder.setMessage("Ta legitymacja nie jest zarejestrowana. Czy chcesz dodać nowego studenta i przypisać mu tę kartę?").setPositiveButton("Tak", dialogClickListener)
                                .setNegativeButton("Nie", dialogClickListener).show();
            }

        }
    }






}
