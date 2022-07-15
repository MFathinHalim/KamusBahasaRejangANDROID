package org.kamusbahasarejang.kamusbahasarejang;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccesInggris {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccesInggris instance;
    Cursor c = null;
    //main function
    private DatabaseAccesInggris(Context context){
        this.openHelper=new DatabaseOpenHelperInggris(context);
    }
    //get instance
    public static DatabaseAccesInggris getInstance(Context context){
        if (instance==null){
            instance=new DatabaseAccesInggris(context);
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
    //Indonesia Database
    public String getIngris(String Indo){
        //database query
        c=db.rawQuery("select Rejang from Table1 where Inggris = '"+Indo+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();

        //add list
        List<String> al = new ArrayList<>();
        String h = "";
        String Rejang = Indo;
        //change rejang database
        while(c.moveToNext()){
            Rejang = c.getString(0);

        }
        //Add Rejang to list
        al.add(Rejang);
        h = al.toString().replace("[", "").replace("]", "");
        return h;
    }
    //Rejang Database
    public String getRejang(String Indo){
        //yes query again
        c=db.rawQuery("select Inggris from Table1 where Rejang = '"+Indo+"'",new String[]{});
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
