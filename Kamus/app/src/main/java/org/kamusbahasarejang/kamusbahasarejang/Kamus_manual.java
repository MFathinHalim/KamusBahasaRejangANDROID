package org.kamusbahasarejang.kamusbahasarejang;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Kamus_manual extends AppCompatActivity {
    private Button button;
    private AdView mAdView;
    private SlidrInterface slidr;

    private RecyclerView recyclerView;
    DatabaseAcces myDB;
    ArrayList<String> id,rjg;
    customAdapter customadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamus_manual);
        mAdView = findViewById(R.id.adView);
        recyclerView = findViewById(R.id.recyclerView);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.a);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        slidr= Slidr.attach(this);

        button = findViewById(R.id.kaganga_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToManual();
            }
        });

        id = new ArrayList<>();
        rjg = new ArrayList<>();


        StoreDataInArrays();
        customadapter = new customAdapter(Kamus_manual.this,id,rjg);
        recyclerView.setAdapter(customadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Kamus_manual.this));

    }

    public void GoToManual(){
        Intent intent = new Intent(this, Kamus_manual_kaganga.class);
        startActivity(intent);
    }

    void StoreDataInArrays(){
        myDB = DatabaseAcces.getInstance(getApplicationContext());
        myDB.open();
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "Error!",Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                id.add(cursor.getString(1));
                rjg.add(cursor.getString(0));
            }
        }
    }

}