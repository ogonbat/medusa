import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.medusa.crypto.cert.MedusaCSR;
import org.medusa.crypto.cert.MedusaCaRSA;
import org.medusa.crypto.cert.MedusaCertRSA;
import org.medusa.crypto.key.MedusaKeyPairRSA;

import java.util.logging.Logger;

@RunWith(JUnit4.class)
public class CertIntegrationTest {
    public static final Logger logger = Logger.getLogger("Cert Integration Test Class");
    @Test
    public void integration_test(){
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
