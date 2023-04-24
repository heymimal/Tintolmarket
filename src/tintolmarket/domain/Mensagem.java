package tintolmarket.domain;

import java.io.Serializable;

public class Mensagem implements Serializable {

    private String from;
    private String to;
    private byte[] message;

    public Mensagem(String from,String to, byte[] message){
        this.to = to;
        this.from = from;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public byte[] getMessage() {
        return message;
    }
}
