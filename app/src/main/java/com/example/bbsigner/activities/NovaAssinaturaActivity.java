package com.example.bbsigner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
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
        medtDescricao.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        medtDescricao.setImeOptions(EditorInfo.IME_ACTION_DONE);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.ballooning);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        medtNomeAtendente.startAnimation(animation);
        medtNomeOutro.startAnimation(animation);
        medtDescricao.startAnimation(animation);
        mbtnCriarAssinatura.startAnimation(animation);

        mbtnCriarAssinatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!medtNomeAtendente.getText().toString().isEmpty() &&
                        !medtNomeOutro.getText().toString().isEmpty() &&
                        !medtDescricao.getText().toString().isEmpty()) {
                    Intent irDesenharAssinatura = new Intent(getApplicationContext(), AssinarActivity.class);
                    irDesenharAssinatura.putExtra("atendente", medtNomeAtendente.getText().toString());
                    irDesenharAssinatura.putExtra("outro", medtNomeOutro.getText().toString());
                    irDesenharAssinatura.putExtra("descricao", medtDescricao.getText().toString());
                    startActivity(irDesenharAssinatura);
                } else {
                    Toast.makeText(getApplicationContext(), "Há campos vazios!", Toast.LENGTH_LONG).show();
                }
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
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }

}
