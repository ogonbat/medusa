/*
 * Copyright 2020 The Medusa Authors - Andrea Mucci
 */
module medusa.crypto {
    exports org.medusa.crypto.key;
    exports org.medusa.crypto.cert;
    requires org.bouncycastle.provider;
    requires org.bouncycastle.pkix;
}