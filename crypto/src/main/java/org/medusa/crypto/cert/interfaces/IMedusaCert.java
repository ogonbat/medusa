package org.medusa.crypto.cert.interfaces;

import org.medusa.crypto.key.interfaces.IMedusaKeyPair;

public interface IMedusaCert {
    void setKeyPair(IMedusaKeyPair keyPair);
}
