package org.medusa.crypto.key;

public class MedusaKeyPairRSA extends MedusaKeyPair{
    @Override
    public String getCryptAlgorithm() {
        return "RSA";
    }
}
