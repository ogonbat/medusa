/*
 * Copyright 2020 The Medusa Authors - Andrea Mucci
 */
package org.medusa.crypto.cert.interfaces;

public interface MedusaCSRFactory {
    void setSubject(String commonName, String organizationName);
    String getPem();
    void load(String filename);
    void build(String algorithm);
}
