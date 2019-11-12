package com.example.bbsigner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bbsigner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NovaAssinaturaActivity extends AppCompatActivity {

    private Button mbtnCriarAssinatura;
    private EditText medtNomeOutro, medtDescricao;
    private Spinner mspinnerNomeAtendente;
    private String[] nomesAtendentes;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ArrayAdapter<String> adapterAtendenteSpinner;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_assinatura);

        progressBar = findViewById(R.id.progressBar2);
        mbtnCriarAssinatura = findViewById(R.id.btnCriarAssinatura);
        medtNomeOutro = findViewById(R.id.edtNomeOutro);
        medtDescricao = findViewById(R.id.edtDescricao);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("usuarios");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                String s = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    s += snapshot.child("nome").getValue().toString() + ";";
                }
                nomesAtendentes = s.split(";");
                adapterAtendenteSpinner = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, nomesAtendentes);
                progressBar.setVisibility(View.INVISIBLE);
                adapterAtendenteSpinner.setDropDownViewResource(R.layout.custom_item_spinner_layout);
                mspinnerNomeAtendente = findViewById(R.id.spinnerNomeAtendente);
                mspinnerNomeAtendente.setAdapter(adapterAtendenteSpinner);
                mspinnerNomeAtendente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                        ((TextView) adapterView.getChildAt(0)).setTextSize(25);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //nomesAtendentes = new String[]{"Thiago Costa", "Mariana Stor", "Mariana Muller", "Luciana Bahia", "Cláudio Vinícius", "Bernardo Brandão"};
        //medtDescricao.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        //medtDescricao.setImeOptions(EditorInfo.IME_ACTION_DONE);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.ballooning);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
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
