package com.example.zkirtaem.licencjat;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class listObecnosciActivity extends AppCompatActivity {

    MyDBDataSource datasource;

    ListView odbyteZajeciaChooseList;
    private ArrayAdapter<String> adapter;

    ArrayList<String> zajarr;
    int odbytezajeciaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_obecnosci);

        datasource = new MyDBDataSource(this);
        datasource.open();

        setTitle("Obecności");

        odbyteZajeciaChooseList=(ListView)findViewById(R.id.obecnoscilist);

        zajarr = datasource.getZajeciaArray();


        adapter = new ArrayAdapter<String>(this, R.layout.row, zajarr);

        odbyteZajeciaChooseList.setAdapter(adapter);

        odbyteZajeciaChooseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int zajeciaid = datasource.getZajeciaIdFromPos(position);
//                Snackbar.make(view, "POS: " + position + ", ID:" + id + ", MYID: " + zajeciaid, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), tabelaObecnosciActivity.class);
                i.putExtra("zajecia_id", zajeciaid);
                startActivity(i);
            }
        });
    }
}
