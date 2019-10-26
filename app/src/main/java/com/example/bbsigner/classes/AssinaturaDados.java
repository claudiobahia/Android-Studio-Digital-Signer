package com.example.bbsigner.classes;

public class AssinaturaDados {
    private String atendente;
    private String outro;
    private String descricao;
    private String assinaturadata;

    public AssinaturaDados(String atendente, String outro, String descricao, String assinaturadata) {
        this.atendente = atendente;
        this.outro = outro;
        this.descricao = descricao;
        this.assinaturadata = assinaturadata;
    }

    public String getAtendente() {
        return atendente;
    }

    public String getOutro() {
        return outro;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getAssinaturadata() {
        return assinaturadata;
    }

    @Override
    public String toString() {
        return getAtendente() + "\n" +
                getOutro() + "\n" +
                getDescricao() + "\n" +
                getAssinaturadata();
    }
}
