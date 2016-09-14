package com.example.zkirtaem.licencjat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;


public class addZajeciaActivity extends AppCompatActivity {

    Button zajeciaAddButton;
    EditText zajeciaTextField;

    private MyDBDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datasource = new MyDBDataSource(this);
        datasource.open();

        setContentView(R.layout.activity_add_zajecia);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Nowe zajęcia");

        zajeciaAddButton=(Button)findViewById(R.id.zajeciaAddButton);
        zajeciaTextField=(EditText)findViewById(R.id.zajeciaTextField);

        zajeciaAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (zajeciaTextField.getText().toString().length() < 4)
                {
                    Snackbar.make(view, "Aby przejść dalej, wypełnij pola poprawnymi danymi", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    datasource.addZajecia(zajeciaTextField.getText().toString());
                    Intent i = new Intent(getApplicationContext(), firstActivity.class);
                    startActivity(i);
                }
            }
        });
    }

}
