/*
 * Copyright 2020 The Medusa Authors - Andrea Mucci
 */
package org.medusa.crypto.cert;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.openssl.MiscPEMGenerator;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemWriter;
import org.medusa.crypto.key.interfaces.IMedusaKeyPair;
import org.medusa.node.interfaces.MedusaCSRFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class MedusaCSR implements MedusaCSRFactory {
    protected PKCS10CertificationRequest objectCert;
    protected X500Name subject;
    protected PublicKey publicKey;
    protected PrivateKey privateKey;

    @Override
    public void setSubject(String commonName, String organizationName) {
        subject = new X500NameBuilder()
                .addRDN(BCStyle.CN, commonName)
                .addRDN(BCStyle.O, organizationName)
                .build();
    }
    public void setSubject(String commonName, String organizationName, String countryName) {
        subject = new X500NameBuilder()
                .addRDN(BCStyle.CN, commonName)
                .addRDN(BCStyle.O, organizationName)
                .addRDN(BCStyle.C, countryName)
                .build();
    }
    public void setSubject(String commonName, String organizationName, String countryName, String email) {
        subject = new X500NameBuilder()
                .addRDN(BCStyle.CN, commonName)
                .addRDN(BCStyle.O, organizationName)
                .addRDN(BCStyle.C, countryName)
                .addRDN(BCStyle.EmailAddress, email)
                .build();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
    public X500Name getSubject() {
        return objectCert.getSubject();
    }
    @Override
    public String getPem() {
        try{
            StringWriter string = new StringWriter();
            PemWriter pemWriter = new PemWriter(string);

            PemObjectGenerator objGen = new MiscPEMGenerator(objectCert);
            pemWriter.writeObject(objGen);
            pemWriter.close();
            return string.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void load(String filename) {
        try{
            Reader fileReader = new FileReader(filename);
            PEMParser reader = new PEMParser(fileReader);
            Object parsedObj = reader.readObject();
            if (parsedObj instanceof PKCS10CertificationRequest) {
                objectCert = (PKCS10CertificationRequest) parsedObj;
                extractPublicKeyFromCert();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void build(String algorithm) {
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                subject,
                publicKey
        );
        try {
            String algorithmCert = String.format("SHA256with%s", algorithm);
            JcaContentSignerBuilder csrBuilder = new JcaContentSignerBuilder(algorithmCert);
            ContentSigner csrContentSigner = csrBuilder.build(privateKey);
            objectCert = p10Builder.build(csrContentSigner);
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }
    }

    public void setKeyPair(IMedusaKeyPair keypair){
        publicKey = keypair.getPublicKey();
        privateKey = keypair.getPrivateKey();
    }

    private void extractPublicKeyFromCert(){
        try{
            RSAKeyParameters params = (RSAKeyParameters) PublicKeyFactory.createKey(objectCert.getSubjectPublicKeyInfo());
            RSAPublicKeySpec specs = new RSAPublicKeySpec(params.getModulus(), params.getExponent());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicKey = kf.generatePublic(specs);
        } catch (IOException | NoSuchAlgorithmException |InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
}
