/*
 * Copyright 2020 The Medusa Authors - Andrea Mucci
 */
import org.medusa.crypto.cert.MedusaCertRSA;
import org.medusa.crypto.key.MedusaKeyPairRSA;
import org.medusa.node.interfaces.MedusaCertFactory;
import org.medusa.node.interfaces.MedusaKeyPairFactory;

module org.medusa.crypto {
    provides MedusaCertFactory
            with MedusaCertRSA;
    provides MedusaKeyPairFactory
            with MedusaKeyPairRSA;
    requires org.medusa.node;
    requires org.bouncycastle.provider;
    requires org.bouncycastle.pkix;
}