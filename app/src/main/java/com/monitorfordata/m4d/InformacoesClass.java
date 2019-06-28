package com.monitorfordata.m4d;

class InformacoesClass {

    //region [ VARIAVEIS ]
    private final String nome;
    private final String informacao;
    private int cor;
    //endregion

    //region [ CONSTRUTOR ]
    InformacoesClass(String nome, String informacao, int cor) {
        this.nome = nome;
        this.informacao = informacao;
        this.cor = cor;
    }
    //endregion

    //region [ GETS E SETS ]
    public String getNome() {
        return nome;
    }

    public String getInformacao() {
        return informacao;
    }

    public int getCor() {
        return cor;
    }
    //endregion
}