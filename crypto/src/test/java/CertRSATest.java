import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.medusa.crypto.cert.MedusaCertRSA;
import org.medusa.crypto.key.MedusaKeyPairRSA;

@RunWith(JUnit4.class)
public class CertRSATest {
    @Test
    public void generate_certificate(){
        // generate cert Keypair
        MedusaKeyPairRSA certKeyPair = new MedusaKeyPairRSA();
        certKeyPair.build();
        // generate authority key pair
        MedusaKeyPairRSA caKeyPair = new MedusaKeyPairRSA();
        caKeyPair.build();
        // generate the generic certificate
        MedusaCertRSA cert = new MedusaCertRSA();
        cert.setAuthorityKeyPair(caKeyPair);
        cert.setKeyPair(certKeyPair);
        cert.setIssuer("Node CA", "Default");
        cert.setSubject("Node Test", "Default");
        cert.build();
        Assert.assertEquals(true, cert.verify(caKeyPair));
    }
}
