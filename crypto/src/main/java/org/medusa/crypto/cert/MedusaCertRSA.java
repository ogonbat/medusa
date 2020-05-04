/*
 * Copyright 2020 The Medusa Authors - Andrea Mucci
 */
package org.medusa.crypto.cert;

import org.medusa.node.interfaces.MedusaCertFactory;

public class MedusaCertRSA extends MedusaCert implements MedusaCertFactory {
    @Override
    public String getCryptAlgorithm() {
        return "RSA";
    }
}
