import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.medusa.crypto.cert.MedusaCaRSA;
import org.medusa.crypto.key.MedusaKeyPairRSA;

@RunWith(JUnit4.class)
public class CertCATest {
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
