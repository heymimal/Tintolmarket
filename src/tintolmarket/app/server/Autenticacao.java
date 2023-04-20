package tintolmarket.app.server;

import java.security.SecureRandom;

public class Autenticacao {

    public double generateNonce(){
        SecureRandom sr = new SecureRandom();
        return sr.nextDouble();
    }
}
