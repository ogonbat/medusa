package org.medusa.iandt.crypto;
import org.junit.Assert;
import org.medusa.crypto.cert.MedusaCSR;
import org.medusa.crypto.cert.MedusaCaRSA;
import org.medusa.crypto.cert.MedusaCertRSA;
import org.medusa.crypto.key.MedusaKeyPairRSA;

public class CryptoTest {
    public void generate_ca() {
        MedusaKeyPairRSA keyPairCA = new MedusaKeyPairRSA();
        keyPairCA.build();
        MedusaCaRSA caCert = new MedusaCaRSA();
        caCert.setSubject("Node CA", "Default");
        caCert.setKeyPair(keyPairCA);
        caCert.build();
        Assert.assertEquals(true, caCert.verify(keyPairCA));
    }

    public void generate_csr() {
        MedusaKeyPairRSA keyPair = new MedusaKeyPairRSA();
        keyPair.build();
        MedusaCSR csrCert = new MedusaCSR();
        csrCert.setKeyPair(keyPair);
        csrCert.setSubject("Node CSR", "Default");
        csrCert.build("RSA");
    }

    public void generate_cert() {
        // generate CA
        MedusaKeyPairRSA keyPairCA = new MedusaKeyPairRSA();
        keyPairCA.build();
        MedusaCaRSA caCert = new MedusaCaRSA();
        caCert.setSubject("Node CA", "Default");
        caCert.setKeyPair(keyPairCA);
        caCert.build();

        //generate CSR
        MedusaKeyPairRSA keyPairCsr = new MedusaKeyPairRSA();
        keyPairCsr.build();
        MedusaCSR csrCert = new MedusaCSR();
        csrCert.setKeyPair(keyPairCsr);
        csrCert.setSubject("Node CSR", "Default");
        csrCert.build("RSA");

        //generate Cert
        MedusaKeyPairRSA keyPairCert = new MedusaKeyPairRSA();
        keyPairCert.build();
        MedusaCertRSA cert = new MedusaCertRSA();
        cert.setIssuer(caCert.getSubject());
        cert.setAuthorityKeyPair(keyPairCA);
        cert.buildFromCSR(csrCert);
        Assert.assertEquals(true, caCert.verifyChain(cert));
    }
}
