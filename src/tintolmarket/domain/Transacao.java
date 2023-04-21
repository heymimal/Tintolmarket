package tintolmarket.domain;

import java.io.Serializable;
import java.security.PrivateKey;

public class Transacao implements Serializable {

    private String nome_vinho;

    private int nEntidades;

    private int valor;

    private String user;

    private Tipo tipo;

    private byte[] signature;

    public Transacao(String nome_vinho, int nEntidades, int valor, String user, Tipo tipo) {
        this.nome_vinho = nome_vinho;
        this.nEntidades = nEntidades;
        this.valor = valor;
        this.user = user;
        this.tipo = tipo;
    }
}
