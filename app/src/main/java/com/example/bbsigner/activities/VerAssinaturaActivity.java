package com.example.bbsigner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bbsigner.R;

public class VerAssinaturaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_assinatura);

        String nomeImagem = getIntent().getStringExtra("nomeImagem");
    }
}
