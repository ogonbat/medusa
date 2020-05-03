package org.medusa.crypto.cert;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.util.io.pem.PemReader;
import org.medusa.crypto.cert.interfaces.IMedusaCert;
import org.medusa.crypto.key.interfaces.IMedusaKeyPair;
import org.medusa.node.interfaces.MedusaCertFactory;

import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class MedusaCertBase implements MedusaCertFactory, IMedusaCert {
    protected X509Certificate objectCert;
    protected X500Name subject;
    protected X500Name issuer;
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

    @Override
    public void setIssuer(String commonName, String organizationName) {
        issuer = new X500NameBuilder()
                .addRDN(BCStyle.CN, commonName)
                .addRDN(BCStyle.O, organizationName)
                .build();
    }
    public void setIssuer(String commonName, String organizationName, String countryName) {
        issuer = new X500NameBuilder()
                .addRDN(BCStyle.CN, commonName)
                .addRDN(BCStyle.O, organizationName)
                .addRDN(BCStyle.C, countryName)
                .build();
    }
    public void setIssuer(String commonName, String organizationName, String countryName, String email) {
        issuer = new X500NameBuilder()
                .addRDN(BCStyle.CN, commonName)
                .addRDN(BCStyle.O, organizationName)
                .addRDN(BCStyle.C, countryName)
                .addRDN(BCStyle.EmailAddress, email)
                .build();
    }

    @Override
    public void load(String filename) {
        try{
            Reader fileReader = new FileReader(filename);
            PemReader reader = new PemReader(fileReader);
            byte[] requestBytes = reader.readPemObject().getContent();
            CertificateFactory factory = CertificateFactory.getInstance("x.509");
            ByteArrayInputStream in = new ByteArrayInputStream(requestBytes);
            objectCert = (X509Certificate) factory.generateCertificate(in);
            issuer = X500Name.getInstance(objectCert.getIssuerX500Principal().getEncoded());
            subject = X500Name.getInstance(objectCert.getSubjectX500Principal().getEncoded());
            publicKey = objectCert.getPublicKey();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setKeyPair(IMedusaKeyPair keyPair) {
        // get public key and private key

    }
}
