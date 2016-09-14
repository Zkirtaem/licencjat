package com.example.zkirtaem.licencjat;

/**
 * Created by zkirtaem on 01.11.15.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyDBDataSource {

    // Database fields
    private SQLiteDatabase database;
    private mySQLiteHelper dbHelper;

    private String[] studenciColumns = { "id",
            "imie_i_nazwisko", "rfid", "indeks" };

    private String[] zajeciaColumns = { "id",
            "nazwa" };

    private String[] odbytezajeciaColumns = { "id",
            "zajecia_id", "dataigodzina" };

    private String[] obecnosciColumns = { "id",
            "odbyte_zajecia_id", "student_id", "dataigodzina" };




    public MyDBDataSource(Context context) {
        dbHelper = new mySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public String getStudent() {
        Cursor kursor = database.query("studenci",
                studenciColumns, null, null, null, null, null);
        String imie="";
        while (kursor.moveToNext()) {
            String id=kursor.getString(0);
            String imieinazwisko=kursor.getString(1);
            String rfid=kursor.getString(2);
            int indeks=kursor.getInt(3);
            imie=""+imie+imieinazwisko+"/"+rfid+"/"+Integer.toString(indeks)+"\n";
        }
        kursor.close();

        return imie;
    }


    public MyDBDataSource() {
        super();
    }

    public String getObecnosciList(int zajeciaid) {

        String[] cols= { Integer.toString(zajeciaid) };
        Cursor kursor = database.query("obecnosci",
                obecnosciColumns, "odbyte_zajecia_id=?", cols, null, null, "dataigodzina ASC", null);
        String imie="\n"+"\n";
        while (kursor.moveToNext()) {
            String id=kursor.getString(1);
            String nazwa=findStudent(kursor.getInt(2));
            imie=""+imie+nazwa+"\n";
        }
        kursor.close();

        return imie;
    }

    public void addObecnosc(int zajeciaid, String studentrfid) {
        int studentid=findStudent(studentrfid);
        ContentValues values = new ContentValues();
        values.put("odbyte_zajecia_id", zajeciaid);
        values.put("student_id", studentid);
        long insertId = database.insert("obecnosci", null,
                values);
    }


    public int addOdbyteZajecia(int zajeciaid) {
        ContentValues values = new ContentValues();
        values.put("zajecia_id", zajeciaid);

        long insertId = database.insert("odbyte_zajecia", null,
                values);

        return (int)insertId;
    }

    public void addZajecia(String nazwa) {
        ContentValues values = new ContentValues();
        values.put("nazwa", nazwa);
        long insertId = database.insert("zajecia", null,
                values);
    }


    public int findZajecia(String nazwa) {
        String[] cols= { nazwa };
        Cursor kursor = database.query("zajecia",
                zajeciaColumns, "nazwa=?", cols, null, null, "ID ASC", null);

        while (kursor.moveToNext()) {
            return kursor.getInt(0);
        }
        return -1;
    }


    public ArrayList<String> getStudenciArray() {
        ArrayList<String> listazwrotna;
        listazwrotna=new ArrayList<String>();

        Cursor kursor = database.query("studenci",
                studenciColumns, null, null, null, null, "ID ASC", null);

        while (kursor.moveToNext()) {
            listazwrotna.add(kursor.getString(1));
        }
        return listazwrotna;
    }





    public ArrayList<String> getOdbyteZajeciaArray() {
        ArrayList<String> listazwrotna;
        listazwrotna=new ArrayList<String>();

        Cursor kursor = database.query("odbyte_zajecia",
                odbytezajeciaColumns, null, null, null, null, "dataigodzina DESC", null);

        while (kursor.moveToNext()) {
            listazwrotna.add( ""+findZajecia(kursor.getInt(1))+", "+kursor.getString(2) );
        }
        return listazwrotna;
    }

    //TA FUNKCJA JEST POWIĄZANA Z GETODBYTEZAJECIAARRAY, ZWROT Z BAZY MUSI BYĆ W TEJ SAMEJ KOLEJNOŚCI
    public int findOdbyteZajecia(int arrpos) {
        ArrayList<String> listazwrotna;
        listazwrotna=new ArrayList<String>();

        Cursor kursor = database.query("odbyte_zajecia",
                odbytezajeciaColumns, null, null, null, null, "dataigodzina DESC", null);

        kursor.moveToPosition(arrpos);
        return kursor.getInt(0);
    }


    public String getOdbyteZajeciaDate(int id) {
        String[] cols= { Integer.toString(id) };
        Cursor kursor = database.query("odbyte_zajecia",
                odbytezajeciaColumns, "id=?", cols, null, null, "ID ASC", null);

        while (kursor.moveToNext()) {
            return kursor.getString(2);
        }
        return "-1";
    }


    public String[][] getOdbyteZajeciaTable(int zajeciaid) {


        Vector studenciVector=new Vector();
        Vector odbyteZajeciaVector=new Vector();

        Cursor kursor_studenci = database.query("studenci",
                studenciColumns, null, null, null, null, "ID ASC", null);
        while (kursor_studenci.moveToNext())
        {
            if (czyStudentBylNaZajeciach(kursor_studenci.getInt(0), zajeciaid))
                studenciVector.add(kursor_studenci.getInt(0));
        }

        String[] odbyte_zajecia_cols= { Integer.toString(zajeciaid) };
        Cursor kursor_odbyte_zajecia = database.query("odbyte_zajecia",
                odbytezajeciaColumns, "zajecia_id=?", odbyte_zajecia_cols, null, null, "ID ASC", null);
        while (kursor_odbyte_zajecia.moveToNext())
        {
                odbyteZajeciaVector.add(kursor_odbyte_zajecia.getInt(0));
        }

        String output[][]=new String[studenciVector.size()+1][odbyteZajeciaVector.size()+1];

        output[0][0]=findZajecia(zajeciaid);
        for (int i=0; i<studenciVector.size(); i++) {
            output[i+1][0]=findStudent((int)studenciVector.get(i));
            for (int j=0; j<odbyteZajeciaVector.size(); j++) {
                output[0][j+1]=getOdbyteZajeciaDate((int)odbyteZajeciaVector.get(j));
                if (findObecnosc((int)odbyteZajeciaVector.get(j), (int)studenciVector.get(i)) == 1)
                    output[i+1][j+1]="1";
                else output[i+1][j+1]="0";
            }
        }


        return output;
    }

    public boolean czyStudentBylNaZajeciach(int studentid, int zajeciaid)
    {

        String[] cols= { Integer.toString(zajeciaid), Integer.toString(studentid) };
        Cursor kursor = database.rawQuery("SELECT o.id from obecnosci o join odbyte_zajecia ob on (o.odbyte_zajecia_id=ob.id) where ob.zajecia_id=? AND o.student_id=?", cols);
        while (kursor.moveToNext()) {
           return true;
        }
        kursor.close();
        return false;
    }

    public int getOdbyteZajeciaCount(int zajeciaid) {
        String[] cols= { Integer.toString(zajeciaid) };
        Cursor kursor = database.query("odbyte_zajecia",
                odbytezajeciaColumns, "zajecia_id=?", cols, null, null, "ID ASC", null);

        int count=0;
        while (kursor.moveToNext()) {
            count++;
        }
        return count;
    }


    public String getZajeciaName(int id) {
        String[] cols= { Integer.toString(id) };
        Cursor kursor = database.query("zajecia",
                zajeciaColumns, "id=?", cols, null, null, "ID ASC", null);

        while (kursor.moveToNext()) {
            return kursor.getString(1);
        }
        return "-1";
    }


    public int getZajeciaIdFromPos(int pos) {
        ArrayList<String> listazwrotna;
        listazwrotna=new ArrayList<String>();

        Cursor kursor = database.query("zajecia",
                zajeciaColumns, null, null, null, null, "ID ASC", null);


        kursor.moveToPosition(pos);
        return kursor.getInt(0);
    }

    public ArrayList<String> getZajeciaArray() {
        ArrayList<String> listazwrotna;
        listazwrotna=new ArrayList<String>();

        Cursor kursor = database.query("zajecia",
                zajeciaColumns, null, null, null, null, "ID ASC", null);

        while (kursor.moveToNext()) {
            listazwrotna.add(kursor.getString(1));
        }
        return listazwrotna;
    }

    public String getOdbyteList() {
        Cursor kursor = database.query("odbyte_zajecia",
                odbytezajeciaColumns, null, null, null, null, null);
        String imie="ILOSC OBIEKTOW: "+kursor.getCount()+"\n";
        while (kursor.moveToNext()) {
            String nazwa=kursor.getString(1);
            String data=kursor.getString(2);
            imie=""+imie+nazwa+", "+data+"\n";
        }
        kursor.close();

        return imie;
    }

    public String getZajeciaList() {
        Cursor kursor = database.query("zajecia",
                zajeciaColumns, null, null, null, null, null);
        String imie="ILOSC OBIEKTOW: "+kursor.getCount()+"\n";
        while (kursor.moveToNext()) {
            String id=kursor.getString(0);
            String nazwa=kursor.getString(1);
            imie=""+imie+nazwa+"\n";
        }
        kursor.close();

        return imie;
    }


    public void addStudent(String imieinazwisko, String rfid, int indeks) {
//        database.delete("studenci",null, null);
        ContentValues values = new ContentValues();
        values.put("imie_i_nazwisko", imieinazwisko);
        values.put("rfid", rfid);
        values.put("indeks", indeks);
        long insertId = database.insert("studenci", null,
                values);
    }

    public int findObecnosc(int zajecia_id, int id) {
        String[] cols= { Integer.toString(zajecia_id), Integer.toString(id) };
        Cursor kursor = database.query("obecnosci",
                obecnosciColumns, "odbyte_zajecia_id=? AND student_id=?", cols, null, null, "ID ASC", null);
        while (kursor.moveToNext()) {
            return 1;
        }
        return -1;
    }


    public String findStudent(int id) {
        String[] cols= { Integer.toString(id) };
        Cursor kursor = database.query("studenci",
                studenciColumns, "id=?", cols, null, null, "ID ASC", null);

        while (kursor.moveToNext()) {
            return kursor.getString(1);
        }
        return "-1";
    }

    public int findStudent(String rfid) {
        String[] cols= { rfid };
        Cursor kursor = database.query("studenci",
                studenciColumns, "rfid=?", cols, null, null, "ID ASC", null);

        while (kursor.moveToNext()) {
            return kursor.getInt(0);
        }
        return -1;
    }


    public String findZajecia(int id) {
        String[] cols= { Integer.toString(id) };
        Cursor kursor = database.query("zajecia",
                zajeciaColumns, "id=?", cols, null, null, "ID ASC", null);

        while (kursor.moveToNext()) {
            return kursor.getString(1);
        }
        return "-1";
    }








    public String getStudentsList() {
        Cursor kursor = database.query("studenci",
                studenciColumns, null, null, null, null, null);
        String imie="ILOSC OBIEKTOW: "+kursor.getCount()+"\n";
        while (kursor.moveToNext()) {
            String id=kursor.getString(0);
            String imieinazwisko=kursor.getString(1);
            String rfid=kursor.getString(2);
            int indeks=kursor.getInt(3);
            imie=""+imie+imieinazwisko+"/"+rfid+"/"+Integer.toString(indeks)+"\n";
        }
        kursor.close();

        return imie;
    }


    public void eraseDB() {
        database.delete("studenci", null, null);
        database.delete("zajecia", null, null);
        database.delete("odbyte_zajecia", null, null);
        database.delete("obecnosci", null, null);
    }
}

