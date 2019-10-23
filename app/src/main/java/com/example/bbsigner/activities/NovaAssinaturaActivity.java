package com.example.bbsigner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bbsigner.R;

public class NovaAssinaturaActivity extends AppCompatActivity {

    private Button mbtnCriarAssinatura;
    private EditText medtNomeAtendente, medtNomeOutro, medtDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_assinatura);

        mbtnCriarAssinatura = findViewById(R.id.btnCriarAssinatura);
        medtNomeAtendente = findViewById(R.id.edtNomeAtendente);
        medtNomeOutro = findViewById(R.id.edtNomeOutro);
        medtDescricao = findViewById(R.id.edtDescricao);

        mbtnCriarAssinatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!medtNomeAtendente.getText().toString().isEmpty() &&
                        !medtNomeOutro.getText().toString().isEmpty() &&
                        !medtDescricao.getText().toString().isEmpty()) {

                    Intent irDesenharAssinatura = new Intent(getApplicationContext(),AssinarActivity.class);
                    irDesenharAssinatura.putExtra("atendente", medtNomeAtendente.getText().toString());
                    irDesenharAssinatura.putExtra("outro", medtNomeAtendente.getText().toString());
                    irDesenharAssinatura.putExtra("descricao", medtNomeAtendente.getText().toString());
                    startActivity(irDesenharAssinatura);

                }else {
                    Toast.makeText(getApplicationContext(), "Há campos vazios!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
