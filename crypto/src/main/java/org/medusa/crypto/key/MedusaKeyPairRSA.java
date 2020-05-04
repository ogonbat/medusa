/*
 * Copyright 2020 The Medusa Authors - Andrea Mucci
 */
package org.medusa.crypto.key;

public class MedusaKeyPairRSA extends MedusaKeyPair{
    @Override
    public String getCryptAlgorithm() {
        return "RSA";
    }
}
