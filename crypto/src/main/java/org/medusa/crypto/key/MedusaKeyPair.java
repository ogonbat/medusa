package org.medusa.crypto.key;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.medusa.crypto.key.interfaces.IMedusaKeyPair;
import org.medusa.node.interfaces.MedusaKeyPairFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public abstract class MedusaKeyPair implements IMedusaKeyPair {
    protected PrivateKey privateKey;
    protected PublicKey publicKey;
    protected int size = 2048;

    @Override
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public void setPublicKey(PublicKey publickey) {
        this.publicKey = publickey;
    }

    @Override
    public void setPrivateKey(PrivateKey privatekey) {
        this.privateKey = privatekey;
    }

    @Override
    public String getCryptAlgorithm() {
        return null;
    }

    @Override
    public void load(String privateFilePath, String publicFilePath) {
        try{
            String algorithm = getCryptAlgorithm();
            KeyFactory factory = KeyFactory.getInstance(algorithm);

            // read the private key
            PemReader reader = new PemReader(new FileReader(privateFilePath));
            PemObject pemObject = reader.readPemObject();
            byte[] pemContent = pemObject.getContent();
            reader.close();
            PKCS8EncodedKeySpec encodedKey = new PKCS8EncodedKeySpec(pemContent);
            privateKey = factory.generatePrivate(encodedKey);

            // read public key
            PemReader readerPub = new PemReader(new FileReader(publicFilePath));
            PemObject pemObjectPub = readerPub.readPemObject();
            byte[] pemPubContent = pemObjectPub.getContent();
            readerPub.close();
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pemPubContent);
            publicKey = factory.generatePublic(publicKeySpec);
        } catch (FileNotFoundException | NoSuchAlgorithmException  | InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void build() {
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(getCryptAlgorithm());
            if(size > 0) {
                keyGen.initialize(size);
            }
            KeyPair kp = keyGen.generateKeyPair();
            privateKey = kp.getPrivate();
            publicKey = kp.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getPrivateKeyPem() {
        String encoded = getEncoded(privateKey);
        return String.format("-----BEGIN PRIVATE KEY-----\n%s\n-----END PRIVATE KEY-----", encoded);
    }

    @Override
    public String getPublicKeyPem() {
        String encoded = getEncoded(publicKey);
        return String.format("-----BEGIN PUBLIC KEY-----\n%s\n-----END PUBLIC KEY-----", encoded);
    }

    protected String getEncoded(Key key){
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        return encodedKey.replaceAll("(.{64})", "$1\n");
    }
}
