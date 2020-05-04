package org.medusa.crypto.cert;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;

import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.medusa.crypto.key.MedusaKeyPair;
import org.medusa.crypto.key.interfaces.IMedusaKeyPair;
import org.medusa.node.interfaces.MedusaCertFactory;
import org.medusa.node.interfaces.MedusaKeyPairFactory;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

public abstract class MedusaCertBase {
    protected X509Certificate objectCert;
    protected X500Name subject;
    protected X500Name issuer;
    protected PublicKey publicKey;
    protected PrivateKey privateKey;
    protected PublicKey authorityPublicKey;
    protected PrivateKey authorityPrivateKey;
    protected int deltaYears = 10;

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
    public void setSubject(X500Name subject) {
        this.subject = subject;
    }

    public X500Name getSubject() {
        return X500Name.getInstance(objectCert.getSubjectX500Principal().getEncoded());
    }

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
    public void setIssuer(X500Name issuer) {
        this.issuer = issuer;
    }

    public X500Name getIssuer() {
        return X500Name.getInstance(objectCert.getIssuerX500Principal().getEncoded());
    }

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
    public X509Certificate getInstance() {
        return objectCert;
    }

    public Boolean verify(MedusaKeyPair keyPair) {
        return verify(keyPair.getPublicKey());
    }

    public Boolean verify(PublicKey key) {
        try{
            objectCert.verify(key);
            return true;
        } catch (CertificateException e) {
            return false;
        } catch (NoSuchAlgorithmException e) {
            return false;
        } catch (InvalidKeyException e) {
            return false;
        } catch (SignatureException e) {
            return false;
        } catch (NoSuchProviderException e) {
            return false;
        }
    }
    public void build() {
        // get start and end date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.YEAR, deltaYears);
        Date endDate = calendar.getTime();

        // get serial number
        BigInteger serialNumber = getSerialNumber();
        // get formatted algorithm
        String algorithmCert = String.format("SHA256with%s", getCryptAlgorithm());
        try{
            X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                    issuer,
                    serialNumber,
                    startDate,
                    endDate,
                    subject,
                    publicKey
            );
            // set is a CA to false
            certBuilder.addExtension(Extension.basicConstraints,
                    true,
                    new BasicConstraints(false));

            JcaX509ExtensionUtils certExtUtils = new JcaX509ExtensionUtils();
            // set the identifier of the authority CA
            certBuilder.addExtension(Extension.authorityKeyIdentifier,
                    false,
                    certExtUtils.createAuthorityKeyIdentifier(authorityPublicKey));
            // set the identifier of the certificate owner
            certBuilder.addExtension(Extension.subjectKeyIdentifier,
                    false,
                    certExtUtils.createSubjectKeyIdentifier(publicKey));
            // Add intended key usage extension if needed
            certBuilder.addExtension(Extension.keyUsage,
                    false,
                    new KeyUsage(KeyUsage.keyEncipherment));
            // set the Content Signer
            ContentSigner certContentSigner = new JcaContentSignerBuilder(algorithmCert)
                    .build(authorityPrivateKey);
            X509CertificateHolder issuedCertHolder = certBuilder.build(certContentSigner);
            // generate the certificate
            objectCert = new JcaX509CertificateConverter().getCertificate(issuedCertHolder);
        } catch (NoSuchAlgorithmException | CertIOException | OperatorCreationException | CertificateException e) {
            e.printStackTrace();
        }
    }

    public String getPem() {
        try{
            StringWriter string = new StringWriter();
            PemWriter pemWriter = new PemWriter(string);
            pemWriter.writeObject(new PemObject("CERTIFICATE", objectCert.getEncoded()));
            pemWriter.close();
            return string.toString();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCryptAlgorithm() {
        return null;
    }

    public void setKeyPair(IMedusaKeyPair keyPair) {
        // get public key and private key
        publicKey = keyPair.getPublicKey();
        privateKey = keyPair.getPrivateKey();
    }

    public void setAuthorityKeyPair(IMedusaKeyPair authorityKeyPair) {
        authorityPublicKey = authorityKeyPair.getPublicKey();
        authorityPrivateKey = authorityKeyPair.getPrivateKey();
    }

    protected final BigInteger getSerialNumber() {
        return new BigInteger(Long.toString(new SecureRandom().nextLong()));
    }
}
