package com.example.bbsigner.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bbsigner.R;
import com.example.bbsigner.classes.AdapterRecycleView;
import com.example.bbsigner.classes.AssinaturaDados;

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_assinaturas);

        adapterRecycleView = new AdapterRecycleView(getApplicationContext(), dados, this, this);
        dados = load(dados);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterRecycleView);

        TextView nAss = findViewById(R.id.txtNumeroAss);
        nAss.setText(nAss.getText().toString() + dados.size());
        mprocurarInput = findViewById(R.id.procurarInput);

        dados.add(new AssinaturaDados("teste","testest", "tsspdofmsdpmap aspd mapsdmfpasmd", "20190202_202020"));

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
                Toast.makeText(getApplicationContext(), "Saved to " + DIRECTORY + "dao.txt", Toast.LENGTH_LONG).show();
            } else Toast.makeText(getApplicationContext(), "Deu ruim", Toast.LENGTH_LONG).show();
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

    @Override
    public void onNoteClick(int position) {
        AssinaturaDados dado = dados.get(position);
        Intent intent = new Intent(getApplicationContext(), VerAssinaturaActivity.class);
        intent.putExtra("nomeImagem", dado.getAssinaturadata());
        startActivity(intent);
    }

    @Override
    public void onLongNoteClick(int position) {
        dados.remove(position);
        adapterRecycleView.notifyItemRemoved(position);
        adapterRecycleView.notifyItemRangeChanged(position,dados.size());
        save();
        Toast.makeText(getApplicationContext(), "Dado removido.", Toast.LENGTH_LONG).show();
    }
}
