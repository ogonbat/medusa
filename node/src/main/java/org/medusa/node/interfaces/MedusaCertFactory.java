package org.medusa.node.interfaces;

public interface MedusaCertFactory {
    void setSubject(String commonName, String organizationName);
    void setIssuer(String commonName, String organizationName);
    void load(String filename);
}
