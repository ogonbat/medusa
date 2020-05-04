/*
 * Copyright 2020 The Medusa Authors - Andrea Mucci
 */
package org.medusa.crypto.key.interfaces;

import org.medusa.node.interfaces.MedusaKeyPairFactory;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface IMedusaKeyPair extends MedusaKeyPairFactory {
    PrivateKey getPrivateKey();
    PublicKey getPublicKey();
    void setPublicKey(PublicKey publicKey);
    void setPrivateKey(PrivateKey privateKey);
}
