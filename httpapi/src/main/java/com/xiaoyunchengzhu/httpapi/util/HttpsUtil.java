package com.xiaoyunchengzhu.httpapi.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by zhangshiyu on 2016/4/27.
 */
public class HttpsUtil {


    public static SSLSocketFactory getSSLSocketFactory(InputStream inputStream,String sslProtocol,String algorithm,String keyStoreType,String keyStorePassword,String trustKeyStorePassword)
    {
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance(sslProtocol);


        KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);

        KeyStore ks = KeyStore.getInstance(keyStoreType);
        KeyStore tks = KeyStore.getInstance(keyStoreType);

        ks.load(inputStream,keyStorePassword.toCharArray());
        tks.load(inputStream, trustKeyStorePassword.toCharArray());

        kmf.init(ks, keyStorePassword.toCharArray());
        tmf.init(tks);

        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ctx.getSocketFactory();
    }
}
