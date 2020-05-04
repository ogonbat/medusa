import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.medusa.crypto.cert.MedusaCaRSA;
import org.medusa.crypto.key.MedusaKeyPairRSA;

import java.util.logging.Logger;

@RunWith(JUnit4.class)
public class CertCATest {
    public static final Logger logger = Logger.getLogger("Cert CA Test Class");
    @Test
    public void generate_ca() {
        MedusaKeyPairRSA keyPairCA = new MedusaKeyPairRSA();
        keyPairCA.build();
        MedusaCaRSA caCert = new MedusaCaRSA();
        caCert.setSubject("Node CA", "Default");
        caCert.setKeyPair(keyPairCA);
        caCert.build();
        Assert.assertEquals(true, caCert.verify(keyPairCA));
    }


}
