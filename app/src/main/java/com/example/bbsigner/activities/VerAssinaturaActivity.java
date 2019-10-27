package com.example.bbsigner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import com.example.bbsigner.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class VerAssinaturaActivity extends AppCompatActivity {

    private String nomeImagem;
    private String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_assinatura);

        nomeImagem = getIntent().getStringExtra("nomeImagem");
        imageView = findViewById(R.id.imgViewAssitatura);

        try {
            File f = new File(DIRECTORY, nomeImagem+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
