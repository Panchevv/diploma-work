package com.diploma.panchev.nrfcloud.adapter.mqtt.configuration;

import lombok.Getter;
import org.bouncycastle.asn1.ASN1Sequence;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.util.UUID;

public class MqttConfiguration {
    private final byte[] certificate;
    private final byte[] privateKey;
    private final char[] pwdArray = UUID.randomUUID().toString().toCharArray();

    @Getter
    private final String clientId;
    @Getter
    private final String prefix;
    @Getter
    private final String host;

    public MqttConfiguration(String host, String clientId, String prefix, byte[] certificate, byte[] privateKey) {
        this.certificate = certificate;
        this.privateKey = privateKey;
        this.clientId = clientId;
        this.prefix = prefix;
        this.host = host;
    }

    public PrivateKey getPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        org.bouncycastle.asn1.pkcs.RSAPrivateKey asn1PrivKey = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(
                ASN1Sequence.fromByteArray(privateKey)
        );
        RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(
                asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent()
        );
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(rsaPrivKeySpec);
    }

    public Certificate getCertificate() throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return cf.generateCertificate(new ByteArrayInputStream(certificate));
    }

    public KeyStore getKeyStore() throws Exception {
        Certificate cert = this.getCertificate();
        PrivateKey rsaPrivateKey = this.getPrivateKey();
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, pwdArray);
        ks.setEntry("mqtt-key", new KeyStore.PrivateKeyEntry(rsaPrivateKey, new Certificate[]{cert}),
                new KeyStore.PasswordProtection(pwdArray));
        return ks;
    }

    public SSLContext getSslContext() throws Exception {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(this.getKeyStore(), pwdArray);
        KeyManager[] kms = kmf.getKeyManagers();

        SSLContext sslContext = null;
        sslContext = SSLContext.getInstance("TLS");

        TrustManager[] tms = new TrustManager[] { new InsecureTrustManager() };
        sslContext.init(kms, tms, new SecureRandom());
        return sslContext;
    }

    class InsecureTrustManager implements X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // BAD: Does not verify the certificate chain, allowing any certificate.
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }
    }
}
