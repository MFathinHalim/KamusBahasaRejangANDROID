package org.kamusbahasarejang.kamusbahasarejang;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.IOException;


public class ocr extends AppCompatActivity {
    Button button_capture, button_copy;
    TextView textView_data;
    private  static final int REQUEST_CAMERA_CODE = 100;
    Bitmap bitmap;
    Button hasil;
    TextView data;
    private SlidrInterface slidr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_activity);

        button_capture = findViewById(R.id.buttonCapture);
        button_copy = findViewById(R.id.buttoncopy);
        textView_data = findViewById(R.id.text_data);
        data = findViewById(R.id.hasiltext);
        hasil = findViewById(R.id.buttonke);
        slidr= Slidr.attach(this);

        if(ContextCompat.checkSelfPermission(ocr.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ocr.this, new String[]{

                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ocr.this);

            }
        });

        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String scanned_text = data.getText().toString();
                copyToClipBoard(scanned_text);
            }
        });

        hasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasilVoid();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getTextFromImage(Bitmap bitmap){
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if(!recognizer.isOperational()){
            Toast.makeText(ocr.this, "Error!!", Toast.LENGTH_SHORT).show();
        }
        else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0; i<textBlockSparseArray.size();i++){
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            data.setText(stringBuilder.toString());
            button_capture.setText("Retake");
            button_copy.setVisibility(View.VISIBLE);


        }
    }

    public void hasilVoid(){
        Intent intent = new Intent(this, OcrActivity.class);
        Bundle bundle = new Bundle();
        String hasil = data.getText().toString().toLowerCase();
        bundle.putString("keyname", hasil);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void copyToClipBoard(String text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied data",text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(ocr.this,"Copied To CLipboard!", Toast.LENGTH_SHORT).show();
    }

}
