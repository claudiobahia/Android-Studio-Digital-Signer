package com.example.bbsigner.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Adapter;

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

public class ListaAssinaturasActivity extends AppCompatActivity {

    private String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
    private ArrayList<AssinaturaDados> dados = new ArrayList();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_assinaturas);
        dados = load(dados);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterRecycleView(this, dados));
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

}
