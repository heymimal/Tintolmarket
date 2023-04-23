package tintolmarket.app.security;

import tintolmarket.domain.Tipo;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class ClientSecurity {

    private final String keystore;
    private final String passKeyStore;
    private final String username;
    private final String truststore;

    public ClientSecurity(String keystore, String passKeyStore, String username, String truststore){
        this.keystore = keystore;
        this.passKeyStore = passKeyStore;
        this.username = username;
        this.truststore = truststore;
    }

    public byte[] cifraMensagemParaUtilizador(String utilizador, String mensagem){
        try{
            // Chave publica do utilizador para quem a mensagem vai ser enviada
            Key publickey = getCertificate(utilizador).getPublicKey();

            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE,publickey);

            return c.doFinal(mensagem.getBytes());
        } catch (NoSuchAlgorithmException |
                 NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }

    public String decifraMensagemDoUtilizador(byte[] mensagem){
        try{
            Key myprivatekey = getPrivateKey();
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE,myprivatekey);

            byte[] decryptedMessage = c.doFinal(mensagem);

            return new String(decryptedMessage);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }
    
    public boolean autenticaCliente(ObjectInputStream in, ObjectOutputStream out){
        try {
            Double nonce = (Double) in.readObject();
            Boolean flag = (Boolean) in.readObject();

            // gets privatekey from kstore
            Key myprivatekey = getPrivateKey();

            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE,myprivatekey);
            byte []nonceEncoded = c.doFinal(nonce.toString().getBytes());

            out.writeObject(nonce);
            out.writeObject(nonceEncoded);

            if(!flag) {
                out.writeObject(getCertificate(username));
            }

            return (boolean)in.readObject();

        } catch (NoSuchPaddingException | IllegalBlockSizeException |
                 IOException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] transaction(String winename, int winevalue, int winequantity, String username, Tipo tipo){
        try{
            PrivateKey myprivateKey = getPrivateKey();
            Signature s = Signature.getInstance("MD5withRSA");
            s.initSign(myprivateKey);
            s.update(winename.getBytes());
            s.update((byte) winevalue);
            s.update((byte) winequantity);
            s.update(username.getBytes());
            s.update(tipo.toString().getBytes());
            return s.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    private PrivateKey getPrivateKey() {
        try{
            FileInputStream kfile = new FileInputStream(keystore);  //keystore
            KeyStore kstore = KeyStore.getInstance("PKCS12");
            kstore.load(kfile, passKeyStore.toCharArray());           //password para aceder à keystore
            Key myprivatekey = kstore.getKey(username,passKeyStore.toCharArray());
            return (PrivateKey)myprivatekey;
        } catch (UnrecoverableKeyException | CertificateException | IOException | NoSuchAlgorithmException |
                 KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private Certificate getCertificate(String nome){
        try{
            KeyStore tStore = KeyStore.getInstance("PKCS12");
            FileInputStream tfile = new FileInputStream(truststore);
            tStore.load(tfile, "trustpass".toCharArray());
            return tStore.getCertificate(nome);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
