/*
 * Copyright 2020 The Medusa Authors - Andrea Mucci
 */
package org.medusa.crypto.key.interfaces;

public interface MedusaKeyPairFactory {
    String getCryptAlgorithm();
    void load(String privateFilePath, String publicFilePath);
    void build();
    String getPrivateKeyPem();
    String getPublicKeyPem();
}
