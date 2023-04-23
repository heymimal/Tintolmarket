package tintolmarket.domain.blockchain;

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

    public String toString(){
        if(this.tipo == Tipo.SELL){
            return "Foram colocadas "+this.nEntidades + " unidades do vinho " + this.nome_vinho +
                    " pelo utilizador "+ this.user +" ao preco de "+this.valor +".";
                    //a identificação
            //do vinho, o número de unidades criadas, o valor de cada unidade, e a identificação do seu dono
        } else {
            return "O utilizador " + this.user +" comprou " + this.nEntidades +" do vinho " + this.nome_vinho +
                    ", tendo custado cada unidade "+ this.valor +".";
        }
    }
}

