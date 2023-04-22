package tintolmarket.app.testesBlockChain;

import tintolmarket.domain.Tipo;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String nome_vinho;

    private int nEntidades;

    private int valor;

    private String user;

    private Tipo tipo;

    private byte[] signature;

    public String getNome_vinho() {
        return nome_vinho;
    }

    public int getnEntidades() {
        return nEntidades;
    }

    public int getValor() {
        return valor;
    }

    public String getUser() {
        return user;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public byte[] getSignature() {
        return signature;
    }

    public Transaction(String nome_vinho, int nEntidades, int valor, String user, Tipo tipo) {
        this.nome_vinho = nome_vinho;
        this.nEntidades = nEntidades;
        this.valor = valor;
        this.user = user;
        this.tipo = tipo;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}

