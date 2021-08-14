package com.vritti.crm.ssl;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Taken from: http://janis.peisenieks.lv/en/76/english-making-an-ssl-connection-via-android/
 *
 */
public class CustomSSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public CustomSSLSocketFactory(KeyStore truststore)
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new CustomX509TrustManager();

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    public CustomSSLSocketFactory(SSLContext context)
            throws KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, UnrecoverableKeyException {
        super(null);
        sslContext = context;
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
                               boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port,
                autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
    public static class CustomX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs,
                                       String authType) throws CertificateException {

            // Here you can verify the servers certificate. (e.g. against one which is stored on mobile device)

            // InputStream inStream = null;
            // try {
            // inStream = MeaApplication.loadCertAsInputStream();
            // CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // X509Certificate ca = (X509Certificate)
            // cf.generateCertificate(inStream);
            // inStream.close();
            //
            // for (X509Certificate cert : certs) {
            // // Verifing by public key
            // cert.verify(ca.getPublicKey());
            // }
            // } catch (Exception e) {
            // throw new IllegalArgumentException("Untrusted Certificate!");
            // } finally {
            // try {
            // inStream.close();
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            // }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    }

}