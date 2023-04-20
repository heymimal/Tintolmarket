package tintolmarket.app.server;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class Autenticar {

    private String password;

    public Autenticar(String password){
        this.password = password;
    }

    public double generateNonce(){
        SecureRandom sr = new SecureRandom();
        return sr.nextDouble();
    }

    private byte[] generateSalt(){
        byte[] salt = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);
        return salt;
    }

    public String decryptUsers(String user, String newLineToAdd){
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


        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IOException | IllegalBlockSizeException |
                 InvalidKeySpecException | BadPaddingException | InvalidAlgorithmParameterException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        /*
        Cipher c = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");

			FileInputStream fis = new FileInputStream("users.txt"); //users.txt
			// Ficheiro Vazio !-
			byte[] readSalt = new byte[16];
			fis.read(readSalt);
			int lenght = fis.read();
			System.out.println("lenght in decrypt: " + lenght);
			byte[] params2 = new byte[lenght];
			fis.read(params2);
			byte[] encryptedBytes = fis.readAllBytes();
			fis.close();

			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), readSalt, 20); // pass, salt, iterations
			SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
			SecretKey key = kf.generateSecret(keySpec);

			AlgorithmParameters p = AlgorithmParameters.getInstance("PBEWithHmacSHA256AndAES_128");
			p.init(params2);
			c.init(Cipher.DECRYPT_MODE, key, p);
			byte[] decryptedBytes = c.doFinal(encryptedBytes);




	        String fileContent = new String(decryptedBytes);

	        String certpth = "";
	        // Split the string into lines and iterate over the lines
	        String[] lines = fileContent.split("\n");
	        for (String line : lines) {
	        	String[] split = line.split(":");
				if(split[0].equals(user)) {
					System.out.println("test");
					certpth = split[1];
					break;
				}
	        }

	        encryptU(decryptedBytes);
         */

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


}

