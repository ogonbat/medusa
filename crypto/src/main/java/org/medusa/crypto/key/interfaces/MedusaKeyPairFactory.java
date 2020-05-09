/*
 * Copyright 2020 The Medusa Authors - Andrea Mucci
 */
package org.medusa.core.interfaces;

public interface MedusaKeyPairFactory {
    String getCryptAlgorithm();
    void load(String privateFilePath, String publicFilePath);
    void build();
    String getPrivateKeyPem();
    String getPublicKeyPem();
}
