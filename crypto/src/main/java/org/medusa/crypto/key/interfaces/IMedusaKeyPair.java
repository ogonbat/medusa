package org.medusa.crypto.key.interfaces;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface IMedusaKeyPair {
    PrivateKey getPrivateKey();
    PublicKey getPublicKey();
    void setPublicKey(PublicKey publicKey);
    void setPrivateKey(PrivateKey privateKey);
}
