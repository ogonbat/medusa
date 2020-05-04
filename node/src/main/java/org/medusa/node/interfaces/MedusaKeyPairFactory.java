package org.medusa.node.interfaces;

public interface MedusaKeyPairFactory {
    String getCryptAlgorithm();
    void load(String privateFilePath, String publicFilePath);
    void build();
    String getPrivateKeyPem();
    String getPublicKeyPem();
}
