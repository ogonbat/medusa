package org.medusa.crypto.cert;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.medusa.node.interfaces.MedusaCAFactory;
import org.medusa.node.interfaces.MedusaCertFactory;
import org.medusa.node.interfaces.MedusaKeyPairFactory;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MedusaCaRSA extends MedusaCertBase implements MedusaCAFactory {
    @Override
    public void build() {
        // get start and end date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.YEAR, deltaYears);
        Date endDate = calendar.getTime();

        // get serial number
        BigInteger serialNumber = getSerialNumber();
        // get formatted algorithm
        String algorithmCert = String.format("SHA256with%s", getCryptAlgorithm());
        try{
            ContentSigner rootCertContentSigner = new JcaContentSignerBuilder(algorithmCert)
                    .build(privateKey);
            X509v3CertificateBuilder rootCertBuilder = new JcaX509v3CertificateBuilder(
                    subject, serialNumber, startDate, endDate, subject, publicKey);
            JcaX509ExtensionUtils rootCertExtUtils = new JcaX509ExtensionUtils();
            rootCertBuilder.addExtension(Extension.basicConstraints,
                    true, new BasicConstraints(true));
            rootCertBuilder.addExtension(Extension.subjectKeyIdentifier,
                    false, rootCertExtUtils.createSubjectKeyIdentifier(publicKey));
            X509CertificateHolder rootCertHolder = rootCertBuilder.build(rootCertContentSigner);
            objectCert = new JcaX509CertificateConverter().getCertificate(rootCertHolder);
        } catch (OperatorCreationException | NoSuchAlgorithmException | CertIOException | CertificateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCryptAlgorithm() {
        return "RSA";
    }

    public Boolean verifyChain(MedusaKeyPairFactory keypair, MedusaCert cert) {
        PublicKey caPublicKey = objectCert.getPublicKey();
        try{
            if(!verify(caPublicKey)) {
                return false;
            }
            X509CertSelector selector = new X509CertSelector();
            selector.setCertificate(cert.getInstance());
            Set<TrustAnchor> setTrustAnchor = new HashSet<TrustAnchor>();
            setTrustAnchor.add(new TrustAnchor(objectCert, null));
            PKIXBuilderParameters pkixBuilderParameters = new PKIXBuilderParameters(setTrustAnchor, selector);
            pkixBuilderParameters.setRevocationEnabled(false);
            CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");
            PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) builder.build(pkixBuilderParameters);
            return true;
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | CertPathBuilderException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
