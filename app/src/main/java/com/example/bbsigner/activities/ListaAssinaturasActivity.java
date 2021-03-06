package com.example.bbsigner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bbsigner.R;
import com.example.bbsigner.classes.AdapterRecycleView;
import com.example.bbsigner.classes.AssinaturaDados;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.lang.String.format;

public class ListaAssinaturasActivity extends AppCompatActivity implements AdapterRecycleView.OnNoteListener, AdapterRecycleView.OnLongNoteListener {

    private String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
    private ArrayList<AssinaturaDados> dados = new ArrayList();
    private RecyclerView recyclerView;
    private EditText mprocurarInput;
    private AdapterRecycleView adapterRecycleView;
    private TextView nAss;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ProgressBar progressBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_assinaturas);

        progressBar = findViewById(R.id.progressBar);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("assinaturas");

        //dados = load(dados);
        dados = loadFirebase();

        adapterRecycleView = new AdapterRecycleView(getApplicationContext(), dados, this, this);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterRecycleView);

        nAss = findViewById(R.id.txtNumeroAss);
        mprocurarInput = findViewById(R.id.procurarInput);

        mprocurarInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapterRecycleView.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    protected void ajustarNAss() {
        nAss.setText(String.format("%s%d", getString(R.string.nAss), dados.size()));
    }

    public ArrayList<AssinaturaDados> load(ArrayList<AssinaturaDados> dados) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(DIRECTORY + "dao.txt");
        } catch (FileNotFoundException e) {
            try {
                new FileOutputStream(new File(DIRECTORY + "dao.txt"));
                fileInputStream = new FileInputStream(DIRECTORY + "dao.txt");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            String[] strings;
            while ((linha = bufferedReader.readLine()) != null) {
                strings = linha.split(";");
                AssinaturaDados dado = new AssinaturaDados(strings[0], strings[1], strings[2], strings[3]);
                dados.add(dado);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dados;
    }

    public void save() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(DIRECTORY, "dao.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String string = "";
            for (AssinaturaDados dado : dados) {
                string += format("%s;%s;%s;%s\n", dado.getAtendente(), dado.getOutro(), dado.getDescricao(), dado.getAssinaturadata());
            }
            if (fileOutputStream != null) {
                fileOutputStream.write(string.getBytes());
            } else
                Toast.makeText(getApplicationContext(), "Erro no salvamento, chame o suporte. 128", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onNoteClickIntent(AssinaturaDados dado) {
        Intent intent = new Intent(getApplicationContext(), VerAssinaturaActivity.class);
        intent.putExtra("nomeImagem", dado.getAssinaturadata());
        startActivity(intent);
    }

    @Override
    public void onNoteClick(int position) {
        String procurando = mprocurarInput.getText().toString();
        if (!procurando.isEmpty()) {
            if (position != -1) {
                ArrayList<AssinaturaDados> novoArr = adapterRecycleView.novoArray();
                AssinaturaDados dado = novoArr.get(position);
                onNoteClickIntent(dado);
            }
        } else {
            if (position != -1) {
                AssinaturaDados dado = dados.get(position);
                onNoteClickIntent(dado);
            }
        }
    }

    @Override
    public void onLongNoteClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja excluir o item?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String procurando = mprocurarInput.getText().toString();
                        AssinaturaDados dado1;
                        int posicaoreal;
                        if (!procurando.isEmpty()) {
                            ArrayList<AssinaturaDados> novoArr = adapterRecycleView.novoArray();
                            AssinaturaDados dado = novoArr.get(position);
                            dado1 = dado;
                            posicaoreal = dados.indexOf(dado);
                            dados.remove(dado);
                        } else {
                            posicaoreal = position;
                            dado1 = dados.get(posicaoreal);
                            dados.remove(position);
                        }
                        adapterRecycleView.notifyItemRemoved(posicaoreal);
                        adapterRecycleView.notifyItemRangeChanged(posicaoreal, dados.size());
                        mDatabaseReference.child(dado1.getAssinaturadata()).removeValue();
                        mprocurarInput.setText("");
                        ajustarNAss();
                        //save();
                        Toast.makeText(getApplicationContext(), "Dado removido.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private ArrayList<AssinaturaDados> loadFirebase() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                dados.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AssinaturaDados dado = new AssinaturaDados(postSnapshot.child("assinaturadata").getValue().toString(),
                            postSnapshot.child("outro").getValue().toString(),
                            postSnapshot.child("descricao").getValue().toString(),
                            postSnapshot.child("assinaturadata").getValue().toString());
                    dados.add(dado);
                    adapterRecycleView.notifyItemInserted(dados.indexOf(dado));
                    adapterRecycleView.notifyItemRangeChanged(dados.indexOf(dado), dados.size());
                }

                ajustarNAss();

                if (dados.size() == 0) {
                    Toast.makeText(getApplicationContext(), "EMPTY FIREBASE DATABASE!", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return dados;
    }
}
