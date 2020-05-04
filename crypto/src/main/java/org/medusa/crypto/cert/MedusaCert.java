package org.medusa.crypto.cert;

import org.medusa.crypto.cert.interfaces.IMedusaCert;

public abstract class MedusaCert extends MedusaCertBase implements IMedusaCert {
    @Override
    public void buildFromCSR(MedusaCSR csrCert) {
        subject = csrCert.getSubject();
        publicKey = csrCert.getPublicKey();
        super.build();
    }
}
