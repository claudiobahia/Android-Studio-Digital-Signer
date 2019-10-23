package com.example.bbsigner.classes;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class AssinaturaDadosDAO {

    private Scanner scanner;
    private Formatter formatter;
    private String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";

    public AssinaturaDadosDAO() {
    }

    public ArrayList<AssinaturaDados> readFileCliente(ArrayList<AssinaturaDados> clientes) {
        if (isFile()) {
            scanner = openFileToRead(scanner);
        }
        if (scanner != null) {
            addDBToDadosArrayList(clientes, scanner);
        } else {
            formatter = createFileToWrite();
            scanner = openFileToRead(scanner);
            addDBToDadosArrayList(clientes, scanner);
        }
        close(scanner);
        return clientes;
    }


    public void writeDAOtxt(ArrayList<AssinaturaDados> dados) {
        formatter = createFileToWrite();
        for (AssinaturaDados dado : dados) {
            formatter.format("%s;%s;%s;%s\n", dado.getAtendente(), dado.getOutro(), dado.getDescricao(), dado.getAssinaturadir());
        }
        close(formatter);
    }

    private void addDBToDadosArrayList(ArrayList<AssinaturaDados> dados, Scanner scanner) {
        String linha;
        String[] stringVet;
        while (scanner.hasNext()) {
            linha = scanner.nextLine();
            stringVet = linha.split(";");
            AssinaturaDados dado = new AssinaturaDados(stringVet[0], stringVet[1], stringVet[2], stringVet[3]);
            dados.add(dado);
        }
    }

    private Formatter createFileToWrite() {
        Formatter formatter = null;
        try {
            formatter = new Formatter(new File(DIRECTORY + "dao.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return formatter;
    }

    private Scanner openFileToRead(Scanner scanner) {
        if (isFile()) {
            try {
                scanner = new Scanner(new File(DIRECTORY + "dao.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else createFileToWrite();
        return scanner;
    }

    private boolean isFile() {
        try {
            new Scanner(new File(DIRECTORY + "dao.txt"));
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    private void close(Formatter formatter) {
        try {
            formatter.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void close(Scanner scanner) {
        try {
            scanner.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
