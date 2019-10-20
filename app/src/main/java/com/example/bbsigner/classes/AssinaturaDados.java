package com.example.bbsigner.classes;

import android.media.Image;

public class AssinaturaDados {
    private String atendente;
    private String outro;
    private String descricao;
    private Image assinatura;

    public AssinaturaDados(String atendente, String outro, String descricao, Image assinatura) {
        this.atendente = atendente;
        this.outro = outro;
        this.descricao = descricao;
        this.assinatura = assinatura;
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

    public Image getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(Image assinatura) {
        this.assinatura = assinatura;
    }
}
