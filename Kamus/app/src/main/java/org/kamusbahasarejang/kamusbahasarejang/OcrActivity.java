package org.kamusbahasarejang.kamusbahasarejang;


import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;
import java.util.List;

public class OcrActivity extends AppCompatActivity {
    protected static final int RESULT_SPEECH=1;

    private static final String[] prediksi_indo = new String[]{
            /*keracok*/"baju", "pakaian"/*keracok*/, "terang", "gelap", "cahaya", "senja", "kamar", "beranda",                 "halaman",              "uang", "ini", "itu", "kamu", "anda",/*uku*/"aku", "saya"/*uku*/, "mau",              "makan",               "nasi","padi", "bunga bangkai", "bunga rafflesia", "nikah",                  "siapa","nama",               "jangan",                      "tabrak", "darat", /*datea*/"darat", "daratan"/*datea*/, "gunung", "danau", "hutan", /*bioa*//*unen*/"sungai"/*unen*/, "air"/*bioa*/, "sawah", "harimau",           "ular",                  "jeruk", "nangka", "pepaya", "durian", "sayuran", "kangkung",      "bayam",    "kopi",                       "api", "merah", "putih", "hitam", "hijau","sedikit", "banyak", "cucu", "bayi", "pergi", "lema", "desa", "kampung", "marah", "pesuruh", "nanti", "sekarang", "turun", "tinggal",            "ayah",            "ibu", "anak","cicit","moyang",              "kakak laki-laki",             "kakak perempuan", "ada",    "sedia", "depan","sendiri","sarung", "besok", "malam",              "pagi",                        "tadi",         "kotoran", "berak", "arti","telur","anjing",/*saro*/"sengsara", "susah"/*saro*/,       "tahu",           "tahun","bulan", "hari", "malas", "sabut kelapa", "kelapa", "kepala", "badan", "kaki", "tangan", "jari", "jempol",    "telunjuk", "jari tengah", "jari manis", "kelingking", "induk jari kaki", "pusar", "pelacur", "mata",                "hidung",             "gigi", "telinga","pungung","dada", "leher",   "kuduk","pantat",   "penis",  "lutut",      "kuku", "rambut",  "lidah", "testis", "paha",  "ketiak",     "siku", "bahu", "bibir", "selangkang", "perut", "kening", "datang", "naik","intim", "bawah", "atas", "dapur", /*tun*/"orang", "kaum"/*tun*/, "satu", "dua", "tiga", "empat", "lima", "enam", "tujuh", "delapan", "sepuluh"
    };


    boolean rejang=false;
    private SQLiteDatabase db = null;

    private Button btnSpeak;
    private TextView label;
    private MultiAutoCompleteTextView value;


    private SlidrInterface slidr;

    public Button carikata;
    public Button carikaganga;

    public boolean translateMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ocr);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.a);

        slidr= Slidr.attach(this);

        value = (MultiAutoCompleteTextView) findViewById(R.id.value2);

        label = (TextView) findViewById(R.id.label2);

        TextView rlabel = (TextView) findViewById(R.id.rlabel2);

        ArrayAdapter<String> adapter_indo= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, prediksi_indo);

        String hasil = getIntent().getStringExtra("keyname");
        value.setText(hasil);
        String name = Data(value.getText().toString());
        label.setText(name);
        rlabel.setText(kagangaFitur(name));


        btnSpeak = findViewById(R.id.button_kembali);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        carikata= findViewById(R.id.button_mode_trans);
        carikata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = label.getText().toString().toLowerCase();
                translateMode = true;

            }
        });


        carikaganga= findViewById(R.id.button_mode_kaganga);
        carikaganga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = label.getText().toString().toLowerCase();
                translateMode = false;
                String kaganga = kagangaFitur(name);
                rlabel.setText(kaganga.toString());
            }
        });

        value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                value.setAdapter(adapter_indo);
                label.setText(value.getText());
                rlabel.setText("  " + label.getText());
                String name = label.getText().toString().toLowerCase();
                String input = value.getText().toString().trim();

                if(translateMode==true) {
                    label.setVisibility(View.VISIBLE);
                    name = Data(name);
                }
                else{
                    label.setVisibility(View.INVISIBLE);
                }


                //================================================
                label.setText(name.toString());
                String kaganga = kagangaFitur(name);
                rlabel.setText(kaganga.toString());


                if (input.isEmpty()) {

                    label.setText("Arti Muncul Disini");
                    rlabel.setText("kgf");
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {
            }
        });




    }
    public String Data(String name){
        if(!name.isEmpty()) {
            //database
            DatabaseAcces databaseAcces = DatabaseAcces.getInstance(getApplicationContext());
            databaseAcces.open();

            Cursor c=databaseAcces.db.rawQuery("select * from Table1 where Indo = '"+name+"'",new String[]{});
            //List
            StringBuffer buffer = new StringBuffer();
            List<String> al = new ArrayList<>();
            String h = "";
            //change Indonesian dabase
            if(c.moveToNext()){
                name = name.replace(c.getString(0),c.getString(1));


            }
            //name = name.replace("baju","keracok");
        }else{
            name = "Arti Muncul Disini";
        }

        return name;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.IndoKeJang:
                rejang=false;
                setTitle("Indonesia ke Rejang");
                return true;
            case R.id.JangKeIndo:
                rejang=true;
                setTitle("Rejang ke Indonesia");
                return true;



        }
        return super.onOptionsItemSelected(item);
    }
    public void back(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestcode, int resultcode, @Nullable Intent data){
        super.onActivityResult(requestcode, resultcode, data);
        switch (requestcode){
            case RESULT_SPEECH:
                if (resultcode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    value.setText(text.get(0));
                }
                break;
        }
    }

    public String kagangaFitur(String name){
        String nae = name;
        nae = nae.replaceAll("nd", "Dx");

        nae = nae.replaceAll("mb", "Bx");
        nae = nae.replaceAll("nj", "Jx");
        nae = nae.replaceAll("ngg", "Gx");
        nae = nae.replaceAll("ngk", "Qx");
        nae = nae.replaceAll("nc", "Cx");
        nae = nae.replaceAll("nt", "Tx");
        nae = nae.replaceAll("mp", "Px");
        nae = nae.replaceAll("gh", "qx");
        nae = nae.replaceAll("ng", "F");
        nae = nae.replaceAll("n", "N");
        nae = nae.replaceAll("eak", "K");
        nae = nae.replaceAll("k", "kx");
        nae = nae.replaceAll("g", "gx");
        nae = nae.replaceAll("t", "tx");
        nae = nae.replaceAll("d", "dx");
        nae = nae.replaceAll("p", "px");
        nae = nae.replaceAll("b", "bx");
        nae = nae.replaceAll("m", "M");
        nae = nae.replaceAll("c", "cx");
        nae = nae.replaceAll("j", "jx");
        nae = nae.replaceAll("s", "sx");
        nae = nae.replaceAll("r", "rx");
        nae = nae.replaceAll("l", "lx");
        nae = nae.replaceAll("y", "yx");
        nae = nae.replaceAll("w", "wx");
        nae = nae.replaceAll("h", "hx");
        nae = nae.replaceAll("i", "ia");
        nae = nae.replaceAll("u", "ua");
        nae = nae.replaceAll("e", "ea");
        nae = nae.replaceAll("o", "oa");
        nae = nae.replaceAll("Ma", "m");
        nae = nae.replaceAll("Mia", "im");
        nae = nae.replaceAll("Mua", "um");
        nae = nae.replaceAll("Mea", "em");
        nae = nae.replaceAll("Moa", "om");
        nae = nae.replaceAll("mF", "Fm");
        nae = nae.replaceAll("imF", "Fim");
        nae = nae.replaceAll("umF", "Fum");
        nae = nae.replaceAll("emF", "Fem");
        nae = nae.replaceAll("omF", "Fom");
        nae = nae.replaceAll("MK", "mK");
        nae = nae.replaceAll("mN", "Nm");
        nae = nae.replaceAll("imN", "Nmi");
        nae = nae.replaceAll("umN", "Nmu");
        nae = nae.replaceAll("emN", "Nme");
        nae = nae.replaceAll("omN", "Nmo");
        nae = nae.replaceAll("mM", "Mm");
        nae = nae.replaceAll("miM", "Mim");
        nae = nae.replaceAll("muM", "Mum");
        nae = nae.replaceAll("meM", "Mem");
        nae = nae.replaceAll("moM", "Mom");
        nae = nae.replaceAll("Na", "n");
        nae = nae.replaceAll("Ni", "in");
        nae = nae.replaceAll("Nu", "un");
        nae = nae.replaceAll("Ne", "en");
        nae = nae.replaceAll("No", "on");
        nae = nae.replaceAll("va", "v");
        nae = nae.replaceAll("vi", "iv");
        nae = nae.replaceAll("vu", "uv");
        nae = nae.replaceAll("ve", "ev");
        nae = nae.replaceAll("vo", "ov");
        nae = nae.replaceAll("Fa", "f");
        nae = nae.replaceAll("Fia", "if");
        nae = nae.replaceAll("Fua", "uf");
        nae = nae.replaceAll("Fea", "ef");
        nae = nae.replaceAll("Foa", "of");
        nae = nae.replaceAll("FK", "fK");
        nae = nae.replaceAll("fN", "Nf");
        nae = nae.replaceAll("ifN", "Nif");
        nae = nae.replaceAll("ufN", "Nuf");
        nae = nae.replaceAll("efN", "Nef");
        nae = nae.replaceAll("ofN", "Nof");
        nae = nae.replaceAll("fM", "Mf");
        nae = nae.replaceAll("ifM", "Mif");
        nae = nae.replaceAll("ufM", "Muf");
        nae = nae.replaceAll("efM", "Mef");
        nae = nae.replaceAll("ofM", "Mof");
        nae = nae.replaceAll("txa", "t");
        nae = nae.replaceAll("txia", "it");
        nae = nae.replaceAll("txua", "ut");
        nae = nae.replaceAll("txea", "et");
        nae = nae.replaceAll("txoa", "ot");
        nae = nae.replaceAll("tF", "Ft");
        nae = nae.replaceAll("itF", "Fit");
        nae = nae.replaceAll("utF", "Fut");
        nae = nae.replaceAll("etF", "Fet");
        nae = nae.replaceAll("otF", "Fot");
        nae = nae.replaceAll("txK", "tK");
        nae = nae.replaceAll("tN", "Nt");
        nae = nae.replaceAll("tiN", "Nit");
        nae = nae.replaceAll("tuN", "Nut");
        nae = nae.replaceAll("teN", "Net");
        nae = nae.replaceAll("toN", "Not");
        nae = nae.replaceAll("tM", "Mt");
        nae = nae.replaceAll("itM", "Mit");
        nae = nae.replaceAll("utM", "Mut");
        nae = nae.replaceAll("etM", "Met");
        nae = nae.replaceAll("otM", "Mot");
        nae = nae.replaceAll("bxa", "b");
        nae = nae.replaceAll("bxia", "ib");
        nae = nae.replaceAll("bxua", "ub");
        nae = nae.replaceAll("bxea", "eb");
        nae = nae.replaceAll("bxoa", "ob");
        nae = nae.replaceAll("bF", "Fb");
        nae = nae.replaceAll("ibF", "Fib");
        nae = nae.replaceAll("ubF", "Fub");
        nae = nae.replaceAll("ebF", "Feb");
        nae = nae.replaceAll("obF", "Fob");
        nae = nae.replaceAll("bxK", "bK");
        nae = nae.replaceAll("bN", "Nb");
        nae = nae.replaceAll("ibN", "Nib");
        nae = nae.replaceAll("ubN", "Nub");
        nae = nae.replaceAll("ebN", "Neb");
        nae = nae.replaceAll("obN", "Nob");
        nae = nae.replaceAll("bM", "Mb");
        nae = nae.replaceAll("ibM", "Mib");
        nae = nae.replaceAll("ubM", "Mub");
        nae = nae.replaceAll("ebM", "Meb");
        nae = nae.replaceAll("obM", "Mob");
        nae = nae.replaceAll("cxa", "c");
        nae = nae.replaceAll("cxia", "ic");
        nae = nae.replaceAll("cxua", "uc");
        nae = nae.replaceAll("cxea", "ec");
        nae = nae.replaceAll("cxoa", "oc");
        nae = nae.replaceAll("cF", "Fc");
        nae = nae.replaceAll("icF", "Fic");
        nae = nae.replaceAll("ucF", "Fuc");
        nae = nae.replaceAll("ecF", "Fec");
        nae = nae.replaceAll("ocF", "Foc");
        nae = nae.replaceAll("cxK", "cK");
        nae = nae.replaceAll("cN", "Nc");
        nae = nae.replaceAll("icN", "Nic");
        nae = nae.replaceAll("ucN", "Nuc");
        nae = nae.replaceAll("ecN", "Nec");
        nae = nae.replaceAll("ocN", "Noc");
        nae = nae.replaceAll("cM", "Mc");
        nae = nae.replaceAll("icM", "Mic");
        nae = nae.replaceAll("ucM", "Muc");
        nae = nae.replaceAll("ecM", "Mec");
        nae = nae.replaceAll("ocM", "Moc");
        nae = nae.replaceAll("dxa", "d");
        nae = nae.replaceAll("dxia", "id");
        nae = nae.replaceAll("dxua", "ud");
        nae = nae.replaceAll("dxea", "ed");
        nae = nae.replaceAll("dxoa", "od");
        nae = nae.replaceAll("Fd", "dF");
        nae = nae.replaceAll("idF", "Fid");
        nae = nae.replaceAll("udF", "Fud");
        nae = nae.replaceAll("edF", "Fed");
        nae = nae.replaceAll("odF", "Fod");
        nae = nae.replaceAll("dxK", "dK");
        nae = nae.replaceAll("dN", "Nd");
        nae = nae.replaceAll("idN", "Nid");
        nae = nae.replaceAll("udN", "Nud");
        nae = nae.replaceAll("edN", "Ned");
        nae = nae.replaceAll("odN", "Nod");
        nae = nae.replaceAll("dM", "Md");
        nae = nae.replaceAll("idM", "Mid");
        nae = nae.replaceAll("udM", "Mud");
        nae = nae.replaceAll("edM", "Med");
        nae = nae.replaceAll("odM", "Mod");
        nae = nae.replaceAll("Bxa", "B");
        nae = nae.replaceAll("Bxia", "iB");
        nae = nae.replaceAll("Bxua", "uB");
        nae = nae.replaceAll("Bxea", "eB");
        nae = nae.replaceAll("Bxoa", "oB");
        nae = nae.replaceAll("BF", "FB");
        nae = nae.replaceAll("iBF", "FiB");
        nae = nae.replaceAll("uBF", "FuB");
        nae = nae.replaceAll("eBF", "FeB");
        nae = nae.replaceAll("oBF", "FoB");
        nae = nae.replaceAll("BxK", "BK");
        nae = nae.replaceAll("BN", "NB");
        nae = nae.replaceAll("iBN", "NiB");
        nae = nae.replaceAll("uBN", "NuB");
        nae = nae.replaceAll("eBN", "NeB");
        nae = nae.replaceAll("oBN", "NoB");
        nae = nae.replaceAll("BM", "MB");
        nae = nae.replaceAll("iBM", "MiB");
        nae = nae.replaceAll("uBM", "MuB");
        nae = nae.replaceAll("eBM", "MeB");
        nae = nae.replaceAll("oBM", "MoB");
        nae = nae.replaceAll("Jxa", "J");
        nae = nae.replaceAll("Jxia", "iJ");
        nae = nae.replaceAll("Jxua", "uJ");
        nae = nae.replaceAll("Jxea", "eJ");
        nae = nae.replaceAll("Jxoa", "oJ");
        nae = nae.replaceAll("JF", "FJ");
        nae = nae.replaceAll("iJF", "FiJ");
        nae = nae.replaceAll("uJF", "FuJ");
        nae = nae.replaceAll("eJF", "FeJ");
        nae = nae.replaceAll("oJF", "FoJ");
        nae = nae.replaceAll("JxK", "JK");
        nae = nae.replaceAll("JN", "NJ");
        nae = nae.replaceAll("iJN", "NiJ");
        nae = nae.replaceAll("uJN", "NuJ");
        nae = nae.replaceAll("eJN", "NeJ");
        nae = nae.replaceAll("oJN", "NoJ");
        nae = nae.replaceAll("JM", "MJ");
        nae = nae.replaceAll("iJM", "MiJ");
        nae = nae.replaceAll("uJM", "MuJ");
        nae = nae.replaceAll("eJM", "MeJ");
        nae = nae.replaceAll("oJM", "MoJ");
        nae = nae.replaceAll("Gxa", "G");
        nae = nae.replaceAll("Gxia", "iG");
        nae = nae.replaceAll("Gxua", "uG");
        nae = nae.replaceAll("Gxea", "eG");
        nae = nae.replaceAll("Gxoa", "oG");
        nae = nae.replaceAll("GF", "FG");
        nae = nae.replaceAll("iGF", "FiG");
        nae = nae.replaceAll("uGF", "FuG");
        nae = nae.replaceAll("eGF", "FeG");
        nae = nae.replaceAll("oGF", "FoG");
        nae = nae.replaceAll("GxK", "GK");
        nae = nae.replaceAll("GN", "NG");
        nae = nae.replaceAll("iGN", "NiG");
        nae = nae.replaceAll("uGN", "NuG");
        nae = nae.replaceAll("eGN", "NeG");
        nae = nae.replaceAll("oGN", "NoG");
        nae = nae.replaceAll("GM", "MG");
        nae = nae.replaceAll("iGM", "MiG");
        nae = nae.replaceAll("uGM", "MuG");
        nae = nae.replaceAll("eGM", "MeG");
        nae = nae.replaceAll("oGM", "MoG");
        nae = nae.replaceAll("Qxa", "Q");
        nae = nae.replaceAll("Qxia", "iQ");
        nae = nae.replaceAll("Qxua", "uQ");
        nae = nae.replaceAll("Qxea", "eQ");
        nae = nae.replaceAll("Qxoa", "oQ");
        nae = nae.replaceAll("QF", "FQ");
        nae = nae.replaceAll("iQF", "FiQ");
        nae = nae.replaceAll("uQF", "FuQ");
        nae = nae.replaceAll("eQF", "FeQ");
        nae = nae.replaceAll("oQF", "FoQ");
        nae = nae.replaceAll("QxK", "QK");
        nae = nae.replaceAll("QN", "NQ");
        nae = nae.replaceAll("iQN", "NiQ");
        nae = nae.replaceAll("uQN", "NuQ");
        nae = nae.replaceAll("eQN", "NeQ");
        nae = nae.replaceAll("oQN", "NoQ");
        nae = nae.replaceAll("QM", "MQ");
        nae = nae.replaceAll("iQM", "MiQ");
        nae = nae.replaceAll("uQM", "MuQ");
        nae = nae.replaceAll("eQM", "MeQ");
        nae = nae.replaceAll("oQM", "MoQ");
        nae = nae.replaceAll("Cxa", "C");
        nae = nae.replaceAll("Cxia", "iC");
        nae = nae.replaceAll("Cxua", "uC");
        nae = nae.replaceAll("Cxea", "eC");
        nae = nae.replaceAll("Cxoa", "oC");
        nae = nae.replaceAll("CF", "FC");
        nae = nae.replaceAll("iCF", "FiC");
        nae = nae.replaceAll("uCF", "FuC");
        nae = nae.replaceAll("eCF", "FeC");
        nae = nae.replaceAll("oCF", "FoC");
        nae = nae.replaceAll("CxK", "CK");
        nae = nae.replaceAll("CN", "NC");
        nae = nae.replaceAll("iCN", "NiC");
        nae = nae.replaceAll("uCN", "NuC");
        nae = nae.replaceAll("eCN", "NeC");
        nae = nae.replaceAll("oCN", "NoC");
        nae = nae.replaceAll("CM", "MC");
        nae = nae.replaceAll("iCM", "MiC");
        nae = nae.replaceAll("uCM", "MuC");
        nae = nae.replaceAll("eCM", "MeC");
        nae = nae.replaceAll("oCM", "MoC");
        nae = nae.replaceAll("Txa", "T");
        nae = nae.replaceAll("Txia", "iT");
        nae = nae.replaceAll("Txua", "uT");
        nae = nae.replaceAll("Txea", "eT");
        nae = nae.replaceAll("Txoa", "oT");
        nae = nae.replaceAll("TF", "FT");
        nae = nae.replaceAll("iTF", "FiT");
        nae = nae.replaceAll("uTF", "FuT");
        nae = nae.replaceAll("eTF", "FeT");
        nae = nae.replaceAll("oTF", "FoT");
        nae = nae.replaceAll("TxK", "TK");
        nae = nae.replaceAll("TN", "NT");
        nae = nae.replaceAll("iTN", "NiT");
        nae = nae.replaceAll("uTN", "NuT");
        nae = nae.replaceAll("eTN", "NeT");
        nae = nae.replaceAll("oTN", "NoT");
        nae = nae.replaceAll("TM", "MT");
        nae = nae.replaceAll("iTM", "MiT");
        nae = nae.replaceAll("uTM", "MuT");
        nae = nae.replaceAll("eTM", "MeT");
        nae = nae.replaceAll("oTM", "MoT");
        nae = nae.replaceAll("Pxa", "P");
        nae = nae.replaceAll("Pxia", "iP");
        nae = nae.replaceAll("Pxua", "uP");
        nae = nae.replaceAll("Pxea", "eP");
        nae = nae.replaceAll("Pxoa", "oP");
        nae = nae.replaceAll("PF", "FP");
        nae = nae.replaceAll("iPF", "FiP");
        nae = nae.replaceAll("uPF", "FuP");
        nae = nae.replaceAll("ePF", "FeP");
        nae = nae.replaceAll("oPF", "FoP");
        nae = nae.replaceAll("PxK", "PK");
        nae = nae.replaceAll("PN", "NP");
        nae = nae.replaceAll("iPN", "NiP");
        nae = nae.replaceAll("uPN", "NuP");
        nae = nae.replaceAll("ePN", "NeP");
        nae = nae.replaceAll("oPN", "NoP");
        nae = nae.replaceAll("PM", "MP");
        nae = nae.replaceAll("iPM", "MiP");
        nae = nae.replaceAll("uPM", "MuP");
        nae = nae.replaceAll("ePM", "MeP");
        nae = nae.replaceAll("oPM", "MoP");
        nae = nae.replaceAll("qxa", "q");
        nae = nae.replaceAll("qxia", "iq");
        nae = nae.replaceAll("qxua", "uq");
        nae = nae.replaceAll("qxea", "eq");
        nae = nae.replaceAll("qxoa", "oq");
        nae = nae.replaceAll("qF", "Fq");
        nae = nae.replaceAll("iqF", "Fiq");
        nae = nae.replaceAll("uqF", "Fuq");
        nae = nae.replaceAll("eqF", "Feq");
        nae = nae.replaceAll("oqF", "Foq");
        nae = nae.replaceAll("qxK", "qK");
        nae = nae.replaceAll("qN", "Nq");
        nae = nae.replaceAll("iqN", "Niq");
        nae = nae.replaceAll("uqN", "Nuq");
        nae = nae.replaceAll("eqN", "Neq");
        nae = nae.replaceAll("oqN", "Noq");
        nae = nae.replaceAll("qM", "Mq");
        nae = nae.replaceAll("iqM", "Miq");
        nae = nae.replaceAll("uqM", "Muq");
        nae = nae.replaceAll("eqM", "Meq");
        nae = nae.replaceAll("oqM", "Moq");
        nae = nae.replaceAll("Dxa", "D");
        nae = nae.replaceAll("Dxia", "iD");
        nae = nae.replaceAll("Dxua", "uD");
        nae = nae.replaceAll("Dxea", "eD");
        nae = nae.replaceAll("Dxoa", "oD");
        nae = nae.replaceAll("DF", "FD");
        nae = nae.replaceAll("iDF", "FiD");
        nae = nae.replaceAll("uDF", "FuD");
        nae = nae.replaceAll("eDF", "FeD");
        nae = nae.replaceAll("oDF", "FoD");
        nae = nae.replaceAll("DxK", "DK");
        nae = nae.replaceAll("DN", "ND");
        nae = nae.replaceAll("iDN", "NiD");
        nae = nae.replaceAll("uDN", "NuD");
        nae = nae.replaceAll("eDN", "NeD");
        nae = nae.replaceAll("oDN", "NoD");
        nae = nae.replaceAll("DM", "MD");
        nae = nae.replaceAll("iDM", "MiD");
        nae = nae.replaceAll("uDM", "MuD");
        nae = nae.replaceAll("eDM", "MeD");
        nae = nae.replaceAll("oDM", "MoD");
        nae = nae.replaceAll("mbxa", "B");
        nae = nae.replaceAll("mbxia", "iB");
        nae = nae.replaceAll("mbxua", "uB");
        nae = nae.replaceAll("mbxea", "eB");
        nae = nae.replaceAll("mbxoa", "oB");
        nae = nae.replaceAll("BxK", "BK");
        nae = nae.replaceAll("BM", "MB");
        nae = nae.replaceAll("iBM", "MiB");
        nae = nae.replaceAll("uBM", "MuB");
        nae = nae.replaceAll("eBM", "MeB");
        nae = nae.replaceAll("oBM", "MoB");
        nae = nae.replaceAll("Na", "n");
        nae = nae.replaceAll("Nia", "in");
        nae = nae.replaceAll("Nua", "un");
        nae = nae.replaceAll("Nea", "en");
        nae = nae.replaceAll("Noa", "on");
        nae = nae.replaceAll("nF", "Fn");
        nae = nae.replaceAll("inF", "Fin");
        nae = nae.replaceAll("unF", "Fun");
        nae = nae.replaceAll("enF", "Fen");
        nae = nae.replaceAll("onF", "Fon");
        nae = nae.replaceAll("nK", "nK");
        nae = nae.replaceAll("nN", "Nn");
        nae = nae.replaceAll("inN", "Nni");
        nae = nae.replaceAll("unN", "Nnu");
        nae = nae.replaceAll("enN", "Nne");
        nae = nae.replaceAll("onN", "Nno");
        nae = nae.replaceAll("nM", "Mn");
        nae = nae.replaceAll("inM", "Min");
        nae = nae.replaceAll("unM", "Mun");
        nae = nae.replaceAll("enM", "Men");
        nae = nae.replaceAll("onM", "Mon");
        nae = nae.replaceAll("pxa", "p");
        nae = nae.replaceAll("pxia", "ip");
        nae = nae.replaceAll("pxua", "up");
        nae = nae.replaceAll("pxea", "ep");
        nae = nae.replaceAll("pxoa", "op");
        nae = nae.replaceAll("pF", "Fp");
        nae = nae.replaceAll("ipF", "Fip");
        nae = nae.replaceAll("upF", "Fup");
        nae = nae.replaceAll("epF", "Fep");
        nae = nae.replaceAll("opF", "Fop");
        nae = nae.replaceAll("pxK", "pK");
        nae = nae.replaceAll("pN", "Np");
        nae = nae.replaceAll("ipN", "Nip");
        nae = nae.replaceAll("upN", "Nup");
        nae = nae.replaceAll("epN", "Nep");
        nae = nae.replaceAll("opN", "Nop");
        nae = nae.replaceAll("pM", "Mp");
        nae = nae.replaceAll("ipM", "Mip");
        nae = nae.replaceAll("upM", "Mup");
        nae = nae.replaceAll("epM", "Mep");
        nae = nae.replaceAll("opM", "Mop");
        nae = nae.replaceAll("jxa", "j");
        nae = nae.replaceAll("jxia", "ij");
        nae = nae.replaceAll("jxua", "uj");
        nae = nae.replaceAll("jxea", "ej");
        nae = nae.replaceAll("jxoa", "oj");
        nae = nae.replaceAll("jF", "Fj");
        nae = nae.replaceAll("ijF", "Fij");
        nae = nae.replaceAll("ujF", "Fuj");
        nae = nae.replaceAll("ejF", "Fej");
        nae = nae.replaceAll("ojF", "Foj");
        nae = nae.replaceAll("jxK", "jK");
        nae = nae.replaceAll("jN", "N");
        nae = nae.replaceAll("ijN", "Nij");
        nae = nae.replaceAll("ujN", "Nuj");
        nae = nae.replaceAll("ejN", "Nej");
        nae = nae.replaceAll("ojN", "Noj");
        nae = nae.replaceAll("jM", "Mj");
        nae = nae.replaceAll("ijM", "Mij");
        nae = nae.replaceAll("ujM", "Muj");
        nae = nae.replaceAll("ejM", "Mej");
        nae = nae.replaceAll("ojM", "Moj");
        nae = nae.replaceAll("nyxa", "v");
        nae = nae.replaceAll("nyxia", "iv");
        nae = nae.replaceAll("nyxua", "uv");
        nae = nae.replaceAll("nyxea", "ev");
        nae = nae.replaceAll("nyxoa", "ov");
        nae = nae.replaceAll("vF", "Fv");
        nae = nae.replaceAll("ivF", "Fiv");
        nae = nae.replaceAll("uvF", "Fuv");
        nae = nae.replaceAll("evF", "Fev");
        nae = nae.replaceAll("ovF", "Fov");
        nae = nae.replaceAll("vxK", "vK");
        nae = nae.replaceAll("vN", "Nv");
        nae = nae.replaceAll("ivN", "Niv");
        nae = nae.replaceAll("uvN", "Nuv");
        nae = nae.replaceAll("evN", "Nev");
        nae = nae.replaceAll("ovN", "Nov");
        nae = nae.replaceAll("vM", "M");
        nae = nae.replaceAll("ivM", "Miv");
        nae = nae.replaceAll("uvM", "Muv");
        nae = nae.replaceAll("evM", "Mev");
        nae = nae.replaceAll("ovM", "Mov");
        nae = nae.replaceAll("sxa", "s");
        nae = nae.replaceAll("sxia", "is");
        nae = nae.replaceAll("sxua", "us");
        nae = nae.replaceAll("sxea", "es");
        nae = nae.replaceAll("sxoa", "os");
        nae = nae.replaceAll("sF", "Fs");
        nae = nae.replaceAll("isF", "Fis");
        nae = nae.replaceAll("usF", "Fus");
        nae = nae.replaceAll("esF", "Fes");
        nae = nae.replaceAll("osF", "Fos");
        nae = nae.replaceAll("sxK", "sK");
        nae = nae.replaceAll("sN", "Ns");
        nae = nae.replaceAll("isN", "Nis");
        nae = nae.replaceAll("usN", "Nus");
        nae = nae.replaceAll("esN", "Nes");
        nae = nae.replaceAll("osN", "Nos");
        nae = nae.replaceAll("sM", "Ms");
        nae = nae.replaceAll("isM", "Mis");
        nae = nae.replaceAll("usM", "Mus");
        nae = nae.replaceAll("esM", "Mes");
        nae = nae.replaceAll("osM", "Mos");
        nae = nae.replaceAll("rxa", "r");
        nae = nae.replaceAll("rxia", "ir");
        nae = nae.replaceAll("rxua", "ur");
        nae = nae.replaceAll("rxea", "er");
        nae = nae.replaceAll("rxoa", "or");
        nae = nae.replaceAll("rF", "Fr");
        nae = nae.replaceAll("irF", "Fir");
        nae = nae.replaceAll("urF", "Fur");
        nae = nae.replaceAll("erF", "Fer");
        nae = nae.replaceAll("orF", "For");
        nae = nae.replaceAll("rxK", "rK");
        nae = nae.replaceAll("rN", "Nr");
        nae = nae.replaceAll("irN", "Nir");
        nae = nae.replaceAll("urN", "Nur");
        nae = nae.replaceAll("erN", "Ner");
        nae = nae.replaceAll("orN", "Nor");
        nae = nae.replaceAll("rM", "Mr");
        nae = nae.replaceAll("irM", "Mir");
        nae = nae.replaceAll("urM", "Mur");
        nae = nae.replaceAll("erM", "Mer");
        nae = nae.replaceAll("orM", "Mor");
        nae = nae.replaceAll("lxa", "l");
        nae = nae.replaceAll("lxia", "il");
        nae = nae.replaceAll("lxua", "ul");
        nae = nae.replaceAll("lxea", "el");
        nae = nae.replaceAll("lxoa", "ol");
        nae = nae.replaceAll("lF", "Fl");
        nae = nae.replaceAll("ilF", "Fil");
        nae = nae.replaceAll("ulF", "Ful");
        nae = nae.replaceAll("elF", "Fel");
        nae = nae.replaceAll("olF", "Fol");
        nae = nae.replaceAll("lxK", "lK");
        nae = nae.replaceAll("lN", "Nl");
        nae = nae.replaceAll("ilN", "Nil");
        nae = nae.replaceAll("ulN", "Nul");
        nae = nae.replaceAll("elN", "Nel");
        nae = nae.replaceAll("olN", "Nol");
        nae = nae.replaceAll("lM", "Ml");
        nae = nae.replaceAll("ilM", "Mil");
        nae = nae.replaceAll("ulM", "Mul");
        nae = nae.replaceAll("elM", "Mel");
        nae = nae.replaceAll("olM", "Mol");
        nae = nae.replaceAll("yxa", "y");
        nae = nae.replaceAll("yxia", "iy");
        nae = nae.replaceAll("yxua", "uy");
        nae = nae.replaceAll("yxea", "ey");
        nae = nae.replaceAll("yxoa", "oy");
        nae = nae.replaceAll("yF", "Fy");
        nae = nae.replaceAll("iyF", "Fiy");
        nae = nae.replaceAll("uyF", "Fuy");
        nae = nae.replaceAll("eyF", "Fey");
        nae = nae.replaceAll("oyF", "Foy");
        nae = nae.replaceAll("yxK", "yK");
        nae = nae.replaceAll("yN", "Ny");
        nae = nae.replaceAll("iyN", "Niy");
        nae = nae.replaceAll("uyN", "Nuy");
        nae = nae.replaceAll("eyN", "Ney");
        nae = nae.replaceAll("oyN", "Noy");
        nae = nae.replaceAll("yM", "My");
        nae = nae.replaceAll("iyM", "Miy");
        nae = nae.replaceAll("uyM", "Muy");
        nae = nae.replaceAll("eyM", "Mey");
        nae = nae.replaceAll("oyM", "Moy");
        nae = nae.replaceAll("wxa", "w");
        nae = nae.replaceAll("wxia", "iw");
        nae = nae.replaceAll("wxua", "uw");
        nae = nae.replaceAll("wxea", "ew");
        nae = nae.replaceAll("wxoa", "ow");
        nae = nae.replaceAll("wF", "Fw");
        nae = nae.replaceAll("iwF", "Fiw");
        nae = nae.replaceAll("uwF", "Fuw");
        nae = nae.replaceAll("ewF", "Few");
        nae = nae.replaceAll("owF", "Fow");
        nae = nae.replaceAll("wxK", "wK");
        nae = nae.replaceAll("wN", "Nw");
        nae = nae.replaceAll("iwN", "Niw");
        nae = nae.replaceAll("uwN", "Nuw");
        nae = nae.replaceAll("ewN", "New");
        nae = nae.replaceAll("owN", "Now");
        nae = nae.replaceAll("wM", "Mw");
        nae = nae.replaceAll("iwM", "Miw");
        nae = nae.replaceAll("uwM", "Muw");
        nae = nae.replaceAll("ewM", "Mew");
        nae = nae.replaceAll("owM", "Mow");
        nae = nae.replaceAll("hxa", "h");
        nae = nae.replaceAll("hxia", "ih");
        nae = nae.replaceAll("hxua", "uh");
        nae = nae.replaceAll("hxea", "eh");
        nae = nae.replaceAll("hxoa", "oh");
        nae = nae.replaceAll("hF", "Fh");
        nae = nae.replaceAll("ihF", "Fih");
        nae = nae.replaceAll("uhF", "Fuh");
        nae = nae.replaceAll("ehF", "Feh");
        nae = nae.replaceAll("ohF", "Foh");
        nae = nae.replaceAll("hxK", "hK");
        nae = nae.replaceAll("hN", "Nh");
        nae = nae.replaceAll("ihN", "Nih");
        nae = nae.replaceAll("uhN", "Nuh");
        nae = nae.replaceAll("ehN", "Neh");
        nae = nae.replaceAll("ohN", "Noh");
        nae = nae.replaceAll("hM", "Mh");
        nae = nae.replaceAll("ihM", "Mih");
        nae = nae.replaceAll("uhM", "Muh");
        nae = nae.replaceAll("ehM", "Meh");
        nae = nae.replaceAll("ohM", "Moh");
        nae = nae.replaceAll("kxa", "k");
        nae = nae.replaceAll("kxia", "ik");
        nae = nae.replaceAll("kxua", "uk");
        nae = nae.replaceAll("kxea", "ek");
        nae = nae.replaceAll("kxoa", "ok");
        nae = nae.replaceAll("kF", "Fk");
        nae = nae.replaceAll("ikF", "Fik");
        nae = nae.replaceAll("ukF", "Fuk");
        nae = nae.replaceAll("ekF", "Fek");
        nae = nae.replaceAll("okF", "Fok");
        nae = nae.replaceAll("kxK", "kK");
        nae = nae.replaceAll("kN", "Nk");
        nae = nae.replaceAll("ikN", "Nik");
        nae = nae.replaceAll("ukN", "Nuk");
        nae = nae.replaceAll("ekN", "Nek");
        nae = nae.replaceAll("okN", "Nok");
        nae = nae.replaceAll("kM", "Mk");
        nae = nae.replaceAll("ikM", "Mik");
        nae = nae.replaceAll("ukM", "Muk");
        nae = nae.replaceAll("ekM", "Mek");
        nae = nae.replaceAll("okM", "Mok");
        nae = nae.replaceAll("gxa", "g");
        nae = nae.replaceAll("gxia", "ig");
        nae = nae.replaceAll("gxua", "ug");
        nae = nae.replaceAll("gxea", "eg");
        nae = nae.replaceAll("gxoa", "og");
        nae = nae.replaceAll("gF", "Fg");
        nae = nae.replaceAll("igF", "Fig");
        nae = nae.replaceAll("ugF", "Fug");
        nae = nae.replaceAll("egF", "Feg");
        nae = nae.replaceAll("ogF", "Fog");
        nae = nae.replaceAll("gxK", "gK");
        nae = nae.replaceAll("gN", "N");
        nae = nae.replaceAll("igN", "Ngi");
        nae = nae.replaceAll("ugN", "Ngu");
        nae = nae.replaceAll("egN", "Nge");
        nae = nae.replaceAll("ogN", "Ngo");
        nae = nae.replaceAll("gM", "Mg");
        nae = nae.replaceAll("igM", "Mig");
        nae = nae.replaceAll("ugM", "Mug");
        nae = nae.replaceAll("egM", "Meg");
        nae = nae.replaceAll("ogM", "Mog");
        return nae;
    }

}
