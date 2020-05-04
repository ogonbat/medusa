import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.medusa.crypto.key.MedusaKeyPairRSA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@RunWith(JUnit4.class)
public class KeyPairRSATest {
    protected MedusaKeyPairRSA rsaKeyPair;

    public void generate_key() {
        rsaKeyPair = new MedusaKeyPairRSA();
        rsaKeyPair.build();
    }
    public void create_folder(String pathname) {
        File pathTest = new File(pathname);
        if(!pathTest.exists()) {
            // the path does not exist
            pathTest.mkdir();
        }
    }
    public void save_file(String content, String filepath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void generate_key_pair(){
        create_folder("./keys");
        generate_key();
        save_file(rsaKeyPair.getPrivateKeyPem(), "./keys/private.pem");
        save_file(rsaKeyPair.getPublicKeyPem(), "./keys/public.pem");
        MedusaKeyPairRSA importedKey = new MedusaKeyPairRSA();
        importedKey.load("./keys/private.pem", "./keys/public.pem");
        Assert.assertEquals(importedKey.getPrivateKeyPem(), rsaKeyPair.getPrivateKeyPem());
    }

    @After
    public void after_test(){
        try {
            Files.deleteIfExists(Paths.get("./keys/private.pem"));
            Files.deleteIfExists(Paths.get("./keys/public.pem"));
            Files.deleteIfExists(Paths.get("./keys"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
