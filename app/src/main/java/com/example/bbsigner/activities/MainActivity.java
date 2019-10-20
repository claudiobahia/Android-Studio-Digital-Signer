package com.example.bbsigner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bbsigner.R;

public class MainActivity extends AppCompatActivity {

    private Button mbtnNovaAssinatura;
    private Button mbtnIrListarAssunaturas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mbtnNovaAssinatura = findViewById(R.id.btnNovaAssinatura);
        mbtnIrListarAssunaturas = findViewById(R.id.btnIrListaAssinaturas);

        mbtnNovaAssinatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NovaAssinaturaActivity.class));
            }
        });

        mbtnIrListarAssunaturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ListaAssinaturasActivity.class));
            }
        });
    }
}
