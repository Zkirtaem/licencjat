package com.example.zkirtaem.licencjat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class settingsActivity extends AppCompatActivity {

    private MyDBDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        datasource = new MyDBDataSource(this);
        datasource.open();

        setTitle("Ustawienia");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        final Button eraseDBButton=(Button)findViewById(R.id.eraseDBButton);

        eraseDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                datasource.eraseDB();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(eraseDBButton.getContext());
                builder.setMessage("Wszystkie listy obecności, zajęcia i informacje o studentach zostaną usunięte. Czy na pewno chcesz to zrobić?").setPositiveButton("Tak", dialogClickListener)
                        .setNegativeButton("Nie", dialogClickListener).show();
            }
        });


    }
}
