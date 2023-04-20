package tintolmarket.app.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class Cifra_Cliente {

    private final String keystore;
    private final String passKeyStore;
    private final String username;
    private final String truststore;

    public Cifra_Cliente(String keystore, String passKeyStore, String username, String truststore){
        this.keystore = keystore;
        this.passKeyStore = passKeyStore;
        this.username = username;
        this.truststore = truststore;
    }
    
    public boolean autenticaCliente(ObjectInputStream in, ObjectOutputStream out){
        try {
            Double nonce = (Double) in.readObject();
            Boolean x = (Boolean) in.readObject();
            System.out.println(x);
            FileInputStream kfile = new FileInputStream(keystore);  //keystore
            KeyStore kstore = KeyStore.getInstance("PKCS12");
            kstore.load(kfile, passKeyStore.toCharArray());           //password para aceder à keystore
            Key myprivatekey = kstore.getKey(username,passKeyStore.toCharArray());
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE,myprivatekey);
            byte []nonceEncoded = c.doFinal(nonce.toString().getBytes());
            out.writeObject(nonce);
            out.writeObject(nonceEncoded);
            if(!x) {
                KeyStore tStore = KeyStore.getInstance("PKCS12");
                FileInputStream tfile = new FileInputStream(truststore);
                tStore.load(tfile, "trustpass".toCharArray());
                java.security.cert.Certificate cert = tStore.getCertificate(username);
                out.writeObject(cert);
            }
            Boolean isconnected = (boolean)in.readObject();
            System.out.println(isconnected);
            return isconnected;
        } catch (UnrecoverableKeyException | NoSuchPaddingException | IllegalBlockSizeException | CertificateException |
                 KeyStoreException | IOException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
