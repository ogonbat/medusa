/*
 * Copyright 2020 The Medusa Authors - Andrea Mucci
 */
package org.medusa.crypto.cert.interfaces;

public interface MedusaCertFactory {
    void setSubject(String commonName, String organizationName);
    void setIssuer(String commonName, String organizationName);
    String getPem();
    void load(String filename);
    void build();
    String getCryptAlgorithm();
}
