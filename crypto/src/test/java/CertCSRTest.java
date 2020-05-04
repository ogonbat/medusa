import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.medusa.crypto.cert.MedusaCSR;
import org.medusa.crypto.key.MedusaKeyPairRSA;

import java.util.logging.Logger;

@RunWith(JUnit4.class)
public class CertCSRTest {
    public static final Logger logger = Logger.getLogger("Cert Test Class");
    @Test
    public void test_generate_csr() {
        MedusaKeyPairRSA keyPair = new MedusaKeyPairRSA();
        keyPair.build();
        MedusaCSR csrCert = new MedusaCSR();
        csrCert.setKeyPair(keyPair);
        csrCert.setSubject("Node CSR", "Default");
        csrCert.build("RSA");
    }
}
