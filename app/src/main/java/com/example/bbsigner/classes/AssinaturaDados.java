package com.example.bbsigner.classes;

import android.media.Image;

public class AssinaturaDados {
    private String atendente;
    private String outro;
    private String descricao;
    private String assinaturadir;

    public AssinaturaDados(String atendente, String outro, String descricao, String assinaturadir) {
        this.atendente = atendente;
        this.outro = outro;
        this.descricao = descricao;
        this.assinaturadir = assinaturadir;
    }

    public String getAtendente() {
        return atendente;
    }

    public void setAtendente(String atendente) {
        this.atendente = atendente;
    }

    public String getOutro() {
        return outro;
    }

    public void setOutro(String outro) {
        this.outro = outro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAssinaturadir() {
        return assinaturadir;
    }

    public void setAssinaturadir(String assinaturadir) {
        this.assinaturadir = assinaturadir;
    }
}
