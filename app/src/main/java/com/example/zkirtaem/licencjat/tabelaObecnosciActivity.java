package com.example.zkirtaem.licencjat;

import java.io.*;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.*;
import android.os.*;
import android.support.v4.*;

import java.util.ArrayList;


public class tabelaObecnosciActivity extends AppCompatActivity {


    MyDBDataSource datasource;
    Button export_button;

    ListView odbyteZajeciaChooseList;

    private ArrayAdapter<String> adapter;

    String[][] dane;

    ArrayList<String> zajarr;
    int zajeciaid=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabela_obecnosci);

        export_button=(Button)findViewById(R.id.export_button);


        datasource = new MyDBDataSource(this);
        datasource.open();

        setTitle("Obecności");

        Intent intent = getIntent();
        zajeciaid= intent.getIntExtra("zajecia_id", 3);

        dane=datasource.getOdbyteZajeciaTable(zajeciaid);


        TableLayout tabela_obecnosci_tablayout=createTableLayout(dane);
        final RelativeLayout tabela_obecnosci_layout=(RelativeLayout)findViewById(R.id.tabela_obecnosci_relative);
        tabela_obecnosci_layout.addView(tabela_obecnosci_tablayout);




        TextView zajeciaNameText=(TextView)findViewById(R.id.zajeciaNameText);
        TextView zapisaniStudenciText=(TextView)findViewById(R.id.zapisaniStudenciText);
        TextView OdbyteZajeciaText=(TextView)findViewById(R.id.odbyteZajeciaText);

        zajeciaNameText.setText(datasource.getZajeciaName(zajeciaid));
        zapisaniStudenciText.setText("" + (dane.length - 1) + " zapisanych studentów");
        OdbyteZajeciaText.setText("" + (dane[0].length - 1) + " odbytych zajęć");


        export_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //TODO jakieś info o tym że faktycznie wyeksportowało + obsługa błędów
                saveTable();
            }
        });
    }

    private void saveTable() {

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //TODO: NAPRAW UPRAWNIENIA ANDROIDA 6
        /*
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (!(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)))
                    */
        if (permissionCheck == -1) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
            //}
        }

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v("ZAPIS","Permission is granted");
        }


        File workingDirectory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "tabele_obecnosci");

        if (!workingDirectory.mkdirs()) {
            Log.i("label","nie zrobilo folderu");
        }

        String filename = dane[0][0]+".csv";
        String string = "";


        boolean przecinkuj=false;
        for (int i = 0; i < dane.length; i++) {
            for (int j= 0; j < dane[0].length; j++) {
                if (przecinkuj) string=string+",";
                string=string+'"'+dane[i][j]+'"';
                przecinkuj=true;
            }
            przecinkuj=false;
            string=string+"\n";
        }

//zapis
        boolean succeed=true;
        FileOutputStream outputStream;
        File outFile = new File(workingDirectory, filename);
/*
        String state = Environment.getExternalStorageState();
        if (!(Environment.MEDIA_MOUNTED.equals(state))) {
            Snackbar.make(findViewById(R.id.tabelaobecnosciLayout), "Nie można zapisywać w pamięci " +
                    "zewnętrznej", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
         }
*/

        permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        Log.i("bla",""+permissionCheck);


        try {
//            outFile.getParentFile().mkdirs();
            outputStream = new FileOutputStream(outFile);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            succeed=false;
            e.printStackTrace();
            Snackbar.make(findViewById(R.id.tabelaobecnosciLayout), //"Plik "+
//                    dane[0][0]+
//                    ".csv nie mógł zostać utworzony! ("+
                            e.toString()+"", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        if (succeed)
        Snackbar.make(findViewById(R.id.tabelaobecnosciLayout), "Plik "+
                dane[0][0]+
                ".csv został utworzony poprawnie.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private TableLayout createTableLayout(String[][] dane) {
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.BLACK);

        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;

        for (int i = 0; i < dane.length; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundColor(Color.BLACK);

            for (int j= 0; j < dane[0].length; j++) {
                TextView textView = new TextView(this);
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView, tableRowParams);
                textView.setPadding(6, 1, 6, 1);
                textView.setText(dane[i][j]);
                if (i>0 && j > 0)
                {
                    if (dane[i][j] == "1") textView.setText("✓");
                    if (dane[i][j] == "0") textView.setText(" ");
                }
            }
            tableLayout.addView(tableRow, tableLayoutParams);
        }

        return tableLayout;
    }
}
