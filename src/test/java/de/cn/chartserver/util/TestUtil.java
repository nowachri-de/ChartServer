package de.cn.chartserver.util;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.pmw.tinylog.Logger;

/**
 * Class providing utility methods used by unit tests
 */
public class TestUtil {
    
    /**
     * Creates an ClosableHttpClient which accept any kind of certificate.
     * 
     * @return
     */
    public static CloseableHttpClient newHttpClientInstance() {
        SSLContext sslContext = null;
        CloseableHttpClient client = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();
            client = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            Logger.error(e);
        }
		return client;        
    }
}
