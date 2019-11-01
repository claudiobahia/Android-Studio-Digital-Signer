package com.example.bbsigner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bbsigner.R;

public class NovaAssinaturaActivity extends AppCompatActivity {

    private Button mbtnCriarAssinatura;
    private EditText medtNomeOutro, medtDescricao;
    private Spinner mspinnerNomeAtendente;
    private String[] nomesAtendentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_assinatura);

        nomesAtendentes = new String[]{"Thiago Costa", "Mariana Stor",
                "Mariana Muller", "Luciana Bahia", "Cláudio Vinícius"};
        ArrayAdapter<String> adapterAtendenteSpinner = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, nomesAtendentes);
        adapterAtendenteSpinner.setDropDownViewResource(R.layout.custom_item_spinner_layout);
        mspinnerNomeAtendente = findViewById(R.id.spinnerNomeAtendente);
        mspinnerNomeAtendente.setAdapter(adapterAtendenteSpinner);

        mbtnCriarAssinatura = findViewById(R.id.btnCriarAssinatura);
        medtNomeOutro = findViewById(R.id.edtNomeOutro);
        medtDescricao = findViewById(R.id.edtDescricao);
        medtDescricao.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        medtDescricao.setImeOptions(EditorInfo.IME_ACTION_DONE);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.ballooning);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        mspinnerNomeAtendente.startAnimation(animation);
        medtNomeOutro.startAnimation(animation);
        medtDescricao.startAnimation(animation);
        mbtnCriarAssinatura.startAnimation(animation);

        mbtnCriarAssinatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mspinnerNomeAtendente.getSelectedItem().toString().isEmpty() &&
                        !medtNomeOutro.getText().toString().isEmpty() &&
                        !medtDescricao.getText().toString().isEmpty()) {
                    Intent irDesenharAssinatura = new Intent(getApplicationContext(), AssinarActivity.class);
                    irDesenharAssinatura.putExtra("atendente", mspinnerNomeAtendente.getSelectedItem().toString());
                    irDesenharAssinatura.putExtra("outro", medtNomeOutro.getText().toString());
                    irDesenharAssinatura.putExtra("descricao", medtDescricao.getText().toString());
                    startActivity(irDesenharAssinatura);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Há campos vazios!", Toast.LENGTH_LONG).show();
                }
            }
        });

        mspinnerNomeAtendente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView)adapterView.getChildAt(0)).setTextSize(25);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }
}
