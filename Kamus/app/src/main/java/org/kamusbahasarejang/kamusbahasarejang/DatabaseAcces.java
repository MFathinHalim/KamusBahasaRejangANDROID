package org.kamusbahasarejang.kamusbahasarejang;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAcces {
    public SQLiteOpenHelper openHelper;
    public SQLiteDatabase db;
    private static DatabaseAcces instance;
    Cursor c = null;
    //main function
    private DatabaseAcces(Context context){
        this.openHelper=new DatabaseOpenHelper(context);
    }
    //get instance
    public static DatabaseAcces getInstance(Context context){
        if (instance==null){
            instance=new DatabaseAcces(context);
        }
        return instance;
    }
    public void open(){
        this.db=openHelper.getWritableDatabase();
    }//open Funtion
    //close funtion
    public void close(){
        if(db==null){
            this.db.close();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM Table1";
        SQLiteDatabase dbopen = openHelper.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;

    }

    //Indonesia Database
    public String getIndo(String Indo){
        //database query
        c=db.rawQuery("select Rejang from Table1 where Indo = '"+Indo+"'",new String[]{});
        //List
        StringBuffer buffer = new StringBuffer();
        List<String> al = new ArrayList<>();
        String h = "";
        //change Indonesian dabase
        while(c.moveToNext()){
            String Rejang = c.getString(0);
            //add Rejang To List
            al.add(Rejang);
            h = al.toString().replace("[", "").replace("]", "");

        }
        return h;
    }
    //Rejang Database
    public String getRejang(String Indo){
        //yes query again
        c=db.rawQuery("select Indo from Table1 where Rejang = '"+Indo+"'",new String[]{});
        //List
        StringBuffer buffer = new StringBuffer();
        List<String> al = new ArrayList<>();
        String h = "";
        //change Indonesian dabase
        while(c.moveToNext()){
            String Rejang = c.getString(0);
            //add Rejang To List
            al.add(Rejang);
            h = al.toString().replace("[", "").replace("]", "");

        }
        return h;
    }
}
