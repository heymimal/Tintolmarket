package tintolmarket.app.security;

import tintolmarket.domain.Tipo;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

public class Cifra_Server {

    private String password;

    public Cifra_Server(String password){
        this.password = password;
    }

    public boolean[] serverAutenticate(ObjectOutputStream outStream, ObjectInputStream inStream, String user) throws NoSuchPaddingException, NoSuchAlgorithmException {
        //String crttemp = auth.decryptUsers(user,null);
        boolean[] booleanArray = new boolean[2];
        String crttemp = decryptUsers(user,null);
        try{
            boolean found = false;
            if(!crttemp.isBlank()) {
                found = true;
            }
            double nonce = generateNonce();
            outStream.writeObject(nonce);

            outStream.writeObject(found);
            Cipher c = Cipher.getInstance("RSA");
            double nonce_client;
            byte[] nonce_encoded;
            java.security.cert.Certificate cert;
            //String certpth = user+"cert.pub";
            if(!found){
                nonce_client = (double) inStream.readObject();
                nonce_encoded = (byte[])inStream.readObject();
                cert = (java.security.cert.Certificate)inStream.readObject();

            } else {
                nonce_client = (double) inStream.readObject();
                nonce_encoded = (byte[])inStream.readObject();

                FileInputStream fis = new FileInputStream(crttemp);
                CertificateFactory cf = CertificateFactory.getInstance("X509");
                cert = cf.generateCertificate(fis);
            }
            boolean equalNonce = nonce==nonce_client;
            Key publickeyuser = cert.getPublicKey();

            c.init(Cipher.DECRYPT_MODE, publickeyuser);

            byte[] nonce_decoded = c.doFinal(nonce_encoded);

            double decryptedN = Double.parseDouble(new String(nonce_decoded));
            boolean decode = nonce == decryptedN;

            boolean checkAuth = decode && equalNonce;
            System.out.println(checkAuth);
            booleanArray[0]=checkAuth;
            booleanArray[1]=found;
            if(checkAuth && !found){
                String filename = user+"serverCert.cer";

                File file = new File(filename);

                FileOutputStream fis = new FileOutputStream(file);
                fis.write(cert.getEncoded());
                fis.close();
                decryptUsers(user, user+":"+filename+"\n");
            }

            return booleanArray;

        }catch (IOException | ClassNotFoundException | CertificateException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }


    public String decryptUsers(String user, String newLineToAdd){
            String fileContent = decryptUsersAux();
            String certpth = "";
            if(newLineToAdd!=null){
                fileContent += newLineToAdd;
                certpth = newLineToAdd.split(":")[1];
            }else{
                String[] lines = fileContent.split("\n");
                for (String line : lines) {
                    String[] split = line.split(":");
                    if(split[0].equals(user)) {
                        System.out.println("test");
                        certpth = split[1];
                        break;
                    }
                }
            }
            encryptUsers(fileContent.getBytes());
            return certpth;
        }

    public void encryptUsers(byte[] decryptedbytes){
        try {

            System.out.println("Hello");

            Cipher c = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");

            //
            byte[] salt = generateSalt();

            //

            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 20); // pass, salt, iterations
            SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
            SecretKey key = kf.generateSecret(keySpec);

            //

            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = null;
            boolean isEmpty = false;
            if(decryptedbytes == null) {
                isEmpty = true;
            } else {
                encryptedBytes = c.doFinal(decryptedbytes);
            }
            byte[] params = c.getParameters().getEncoded();
            writeEncryption(salt,params,encryptedBytes,isEmpty);

        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeySpecException |
                 NoSuchPaddingException | BadPaddingException | IOException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }


    }

    public List<String> getAllUsers(){
        String allLines = decryptUsersAux();
        List<String> allUsers = new ArrayList<>();
        String[] splitLines = allLines.split("\n");
        for(String line:splitLines){
            String s = line.split(":")[0];
            allUsers.add(line.split(":")[0]);
            System.out.println("Info:" + s);
        }
        encryptUsers(allLines.getBytes());
        return allUsers;
    }

    private void writeEncryption(byte[] salt, byte[]params, byte[] encryptedBytes, boolean isEmpty) throws IOException {
        FileOutputStream fos = new FileOutputStream("users.txt");
        //fos.write(encryptedBytes.length);
        fos.write(salt);
        System.out.println(params.length);
        fos.write(params.length);
        fos.write(params);
        System.out.println("Params lenght : " + params.length);
        if(!isEmpty) {
            //fos.write(encryptedBytes.length);
            fos.write(encryptedBytes);
        }

        fos.close();
    }

    private String decryptUsersAux(){
        try {
            Cipher c = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");


            // get from file

            FileInputStream fis = new FileInputStream("users.txt"); //users.txt


            byte[] salt = new byte[16];
            fis.read(salt);
            int length = fis.read();
            System.out.println("lenght in decrypt: " + length);
            byte[] params = new byte[length];
            fis.read(params);
            //int size = fis.read();
            byte[] encryptedBytes = fis.readAllBytes(); // might change due to java 8
            fis.close();

            //

            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 20); // pass, salt, iterations
            SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
            SecretKey key = kf.generateSecret(keySpec);

            AlgorithmParameters p = AlgorithmParameters.getInstance("PBEWithHmacSHA256AndAES_128");
            p.init(params);
            c.init(Cipher.DECRYPT_MODE, key, p);
            byte[] decryptedBytes = c.doFinal(encryptedBytes);


            String fileContent = new String(decryptedBytes);
            return  fileContent;
        } catch (InvalidAlgorithmParameterException | BadPaddingException | NoSuchAlgorithmException |
                 NoSuchPaddingException | IllegalBlockSizeException | IOException | InvalidKeySpecException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private double generateNonce(){
        SecureRandom sr = new SecureRandom();
        return sr.nextDouble();
    }

    private byte[] generateSalt(){
        byte[] salt = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);
        return salt;
    }

    private Certificate getCertificate(String crttemp){
        try{
            FileInputStream fis = new FileInputStream(crttemp);
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            return cf.generateCertificate(fis);
        } catch (FileNotFoundException | CertificateException e) {
            throw new RuntimeException(e);
        }

    }


    public boolean verificaAssinatura(String winename, int value, int quantity, byte[] signature, String user, Tipo t) {
        try{
           String certpath = user+"serverCert.cer";
           Certificate c = getCertificate(certpath);
           PublicKey pk = c.getPublicKey( );
           Signature s = Signature.getInstance("MD5withRSA");
           s.initVerify(pk);
           s.update(winename.getBytes());
           s.update((byte)value);
           s.update((byte)quantity);
           s.update(user.getBytes());
           s.update(t.toString().getBytes());
           return s.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }


        /*
        Certificate c = … //obtém um certificado de alguma forma (ex., de um ficheiro)
PublicKey pk = c.getPublicKey( );
Signature s = Signature.getInstance("MD5withRSA");
s.initVerify(pk);
s.update(data.getBytes( ));
if (s.verify(signature))
System.out.println("Message is valid");
else
System.out.println("Message was corrupted");
fis.close();

         */
    }
}

