package tintolmarket.app.security;

import tintolmarket.domain.blockchain.BlockTintol;
import tintolmarket.domain.blockchain.Transaction;
import tintolmarket.domain.Tipo;

import java.util.Arrays;
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

public class ServerSecurity {

    private String password;

    private String keystore;
    private String passKeyStore;

    private static  String users;
    private static  String wines;
    private static  String wallet;
    private static  String messages;

    private final File certificadosFolder = new File("certificados");

    private final File macFolder = new File("Macs");
    private final File msgMac = new File(macFolder,"msgMac");
    private final File wineMac = new File(macFolder,"wineMac");
    private final File walletMac = new File(macFolder,"walletMac");

    public ServerSecurity(String password, String keystore, String passKeyStore, String wines, String wallet, String messages){
        this.password = password;
        this.keystore = keystore;
        this.passKeyStore = passKeyStore;
        this.wines = wines;
        this.wallet = wallet;
        this.messages = messages;
        certificadosFolder.mkdir();
        macFolder.mkdir();
    }

    public boolean[] serverAutenticate(ObjectOutputStream outStream, ObjectInputStream inStream, String user) throws NoSuchPaddingException, NoSuchAlgorithmException {
        //String crttemp = auth.decryptUsers(user,null);
        boolean[] booleanArray = new boolean[2];
        String crttemp = decryptUsers(user,null);
        try{
            boolean found = !crttemp.isBlank();
            double nonce = generateNonce();
            outStream.writeObject(nonce);

            outStream.writeObject(found);
            Cipher c = Cipher.getInstance("RSA");
            double nonce_client;
            byte[] nonce_encoded;
            java.security.cert.Certificate cert;

            nonce_client = (double) inStream.readObject();
            nonce_encoded = (byte[])inStream.readObject();
            if(!found){
                cert = (java.security.cert.Certificate)inStream.readObject();
            } else {
                cert = getCertificate(crttemp);
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

                File file = new File(certificadosFolder,filename);

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
            File file = new File(certificadosFolder,crttemp);
            FileInputStream fis = new FileInputStream(file);
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
    }

    public boolean verificaBlockChain(BlockTintol b, byte[] previous_hash, byte[] currentHash){
        boolean hashCompare;
        try {
            hashCompare = compareHash(previous_hash,currentHash);
            System.out.println("Valor do hashCompare = "+ hashCompare);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] signature = b.getSignature();
        boolean s = verifyBlockSignature(signature,b);
        System.out.println("Valor do verifyBlockSignature = "+ s);

        return s && hashCompare;
    }

    private boolean compareHash(byte[] previousHash, byte[] currentHash) throws NoSuchAlgorithmException {
        //MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return MessageDigest.isEqual(previousHash,currentHash);
    }

    private boolean verifyBlockSignature(byte[] signature, BlockTintol b) {
        try{
            PublicKey publicKey = getServerPublicKey();
            Signature s = Signature.getInstance("MD5withRSA");

            s.initVerify(publicKey);

            updateSignature(b, s);

            return s.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getServerSignature(BlockTintol bloco) {
        try{
            Key myprivatekey = getServerPrivateKey();
            Signature s = Signature.getInstance("MD5withRSA");
            s.initSign((PrivateKey) myprivatekey);

            updateSignature(bloco, s);

            return s.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateSignature(BlockTintol bloco, Signature s) throws SignatureException {
        s.update(bloco.getPreviousHash());
        s.update((byte) bloco.getIndex());
        s.update((byte) bloco.getN_trx());
        for(Transaction t : bloco.getTransactions()){
            s.update(t.getNome_vinho().getBytes());
            s.update((byte) t.getnEntidades());
            s.update((byte)t.getValor());
            s.update(t.getUser().getBytes());
            s.update(t.getTipo().toString().getBytes());
            s.update(t.getSignature());
        }
    }

    private PrivateKey getServerPrivateKey(){
        try{
            FileInputStream kfile = new FileInputStream(keystore);  //keystore
            KeyStore kstore = KeyStore.getInstance("PKCS12");
            kstore.load(kfile, passKeyStore.toCharArray());
            return(PrivateKey) kstore.getKey("myServer",passKeyStore.toCharArray());
        } catch (UnrecoverableKeyException | CertificateException | KeyStoreException | IOException |
                 NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private SecretKey sx(){
        try{
            FileInputStream kfile = new FileInputStream(keystore);  //keystore
            KeyStore kstore = KeyStore.getInstance("PKCS12");
            kstore.load(kfile, passKeyStore.toCharArray());
            return (SecretKey) kstore.getKey("secKey",passKeyStore.toCharArray());
        } catch (UnrecoverableKeyException | CertificateException | KeyStoreException | IOException |
                 NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    private byte[] readFile(String filepath) throws IOException {
		File f = new File(filepath);
		FileInputStream fis = new FileInputStream(filepath);
        byte[] data = null;
        if(f.length()>0){
            data = new byte[(int) f.length()];
        }
		fis.close();
		return data;
	}

    /**
     * Verifica a integridade de um ficheiro
     *
     * @return true se o ficheiro nao foi alterado
     * @requires {@code 0 <= contentType <= 2}
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public boolean fileIntegrity(Macs mac) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        File macFile = null;
        String filepath = null;
        switch(mac) {
            case MESSAGES:
                macFile = this.msgMac;
                filepath = this.messages;
                break;
            case WINES:
                macFile = this.wineMac;
                filepath = this.wines;
                break;
            case WALLET:
                macFile = this.walletMac;
                filepath = this.wallet;
                break;
        }
        macFile.createNewFile();
        /*if(!macFile.exists()) {
            macFile.createNewFile();
        }*/
        byte[] prevMac = parseMacFile(macFile);
        byte[] newMac = computeMac(filepath);
		if(prevMac == null) {
			rewriteMacFile(newMac, macFile);
			return true;
		} else {
			return Arrays.equals(prevMac,newMac);
		}
	}

    private byte[] computeMac(String filepath) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Mac mac = Mac.getInstance("HmacSHA1");
        Key key = sx();
        mac.init(key);
		byte[] buf = readFile(filepath);
        mac.update(buf);
        return mac.doFinal();
    }

    /**
     * Obtem um array de strings correspondentes aos MACs dos ficheiros do server a partir do ficheiro onde estao guardados
     * 
     * @return array de strings correspondentes aos MACs dos ficheiros do server
     * @throws IOException
     */
    private byte[] parseMacFile(File macFile) throws IOException {
        if(macFile.length() == 0) {
            return null;
        }
        FileInputStream fis = new FileInputStream(macFile);
        byte[] data = new byte[(int) macFile.length()];
        fis.read(data);
        fis.close();
        return data;
    }

    /**
     * Atualiza um dos ficheiros que contem um MAC.
     * 
     * @param macs o conteudo a escrever
     * @param macFile o ficheiro a reescrever
     * @throws IOException
     */
    private void rewriteMacFile(byte[] macs, File macFile) throws IOException {
        macFile.delete();
        macFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(macFile);
        fos.write(macs);
        fos.close();
    }

    //?
    public void updateMacFile(Macs mc) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
        File macFile = fileSelector(mc);
        macFile.delete();
        macFile.createNewFile();
        String filepath = getFilePath(mc);
        byte[] mac = computeMac(filepath);
        FileOutputStream fos = new FileOutputStream(macFile);
        fos.write(mac);
        fos.close();
    }

    private String getFilePath(Macs mc) {
        switch (mc) {
            case MESSAGES:
                return this.messages;
            case WINES:
                return this.wines;
            case WALLET:
                return this.wallet;
        }
        return null;
    }

    private File fileSelector(Macs mc) {
        switch (mc) {
            case MESSAGES:
                return this.msgMac;
            case WINES:
                return this.wineMac;
            case WALLET:
                return this.walletMac;
        }
        return null;
    }
    private PublicKey getServerPublicKey(){
        try{
            FileInputStream kfile = new FileInputStream(keystore);  //keystore
            KeyStore kstore = KeyStore.getInstance("PKCS12");
            kstore.load(kfile, passKeyStore.toCharArray());           //password para aceder à keystore
            // GETS PUBLIC KEY FROM CERTIFICATE

            Certificate cert = kstore.getCertificate("myServer");
            return cert.getPublicKey();
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }
}

