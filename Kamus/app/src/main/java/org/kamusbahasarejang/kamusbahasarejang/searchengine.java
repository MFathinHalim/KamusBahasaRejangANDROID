package org.kamusbahasarejang.kamusbahasarejang;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;


public class searchengine extends AppCompatActivity {
    private SlidrInterface slidr;
    private EditText search;
    private Button button;
    public String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_engine);
        slidr= Slidr.attach(this);
        search = findViewById(R.id.search);
        button = findViewById(R.id.search_btn);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.a);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                text = search.getText().toString();
                gotToB();

                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = search.getText().toString();
                gotToB();

            }
        });
    }

    public void gotToB(){
        Intent intent = new Intent(this, browser.class);
        Bundle bundle = new Bundle();
        bundle.putString("search", text);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
