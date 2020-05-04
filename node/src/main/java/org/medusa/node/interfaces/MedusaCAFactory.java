package org.medusa.node.interfaces;

public interface MedusaCAFactory {
    void setSubject(String commonName, String organizationName);
    String getPem();
    void load(String filename);
    void build();
    String getCryptAlgorithm();
}
