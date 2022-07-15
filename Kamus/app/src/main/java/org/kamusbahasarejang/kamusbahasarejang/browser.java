package org.kamusbahasarejang.kamusbahasarejang;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;


public class browser extends AppCompatActivity {
    private WebView webView;
    private SlidrInterface slidr;

    private EditText text;

    private Button kembali;
    private Button kedepan;
    private Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.a);
        Bundle extras = getIntent().getExtras();
        String search = extras.getString("search");

        String hasil;
        if(search.contains("rejang") || search.contains("curup")){
            if(search.contains("toko") || search.contains("pasar")){
                hasil = "https://www.google.com/maps/search/"+search+"/@-3.4666575,102.5183312,15z/data=!3m1!4b1";
            }else{
                hasil = "https://duckduckgo.com/?q="+search+"&t=h_&ia=web";
            }return ;
        }else{
            if(search.contains("toko") || search.contains("pasar")){
                search = search + " di curup";
                hasil = "https://www.google.com/maps/search/"+search+"/@-3.4666575,102.5183312,15z/data=!3m1!4b1";
            }else{
                search = search + " di rejang";
                hasil = "https://duckduckgo.com/?q="+search+"&t=h_&ia=web";
            }
        }

        text = findViewById(R.id.browser_text);
        kembali = findViewById(R.id.kembaliBtn);
        kedepan = findViewById(R.id.kedepanBtn);
        home = findViewById(R.id.home);


        webView = (WebView) findViewById(R.id.webView);
        slidr= Slidr.attach(this);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl(hasil);

        text.setHint(hasil);
        text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if(text.getText().toString().contains("https://")){
                        String hasil = text.getText().toString();
                        webView.loadUrl(hasil);
                    }else{
                        String search = text.getText().toString() + " di rejang";
                        String hasil = "https://duckduckgo.com/?q="+search+"&t=h_&ia=web";
                    }
                    webView.loadUrl(hasil);
                    return true;
                }
                return false;
            }
        });


        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        kedepan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onForwardPressed();
            }
        });



    }
    @Override
    public void onBackPressed() {
        if(webView!= null && webView.canGoBack())
            webView.goBack();// if there is previous page open it
        else
            super.onBackPressed();//if there is no previous page, close app
    }
    public void onForwardPressed() {
        if(webView!= null && webView.canGoForward())
            webView.goForward();// if there is previous page open it
        else
            super.onBackPressed();//if there is no previous page, close app
    }

}
