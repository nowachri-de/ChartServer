package de.cn.chartserver.charts;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.pmw.tinylog.Logger;

import de.cn.chartserver.ChartServerConfiguration;
import de.cn.chartserver.util.ResourceFileHandler;

public abstract class Chart {
    private ChartServerConfiguration configuration;
    private SSLContext sslContext = null;
    private CloseableHttpClient client = null;

    public Chart() {
        configuration = ChartServerConfiguration.createConfiguration();
        createClosableHttpClient();
    }

    public Chart(int port) {
        configuration = ChartServerConfiguration.createConfiguration(port);
        createClosableHttpClient();
    }

    public Chart(String host, int port) {
        configuration = ChartServerConfiguration.createConfiguration(host, port);
        createClosableHttpClient();
    }

    protected void createClosableHttpClient() {
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();
            client = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            Logger.error(e);
        }
    }

    protected String send(String path, String objectAsJson) {

        if (path == null || path.isEmpty()) {
            throw new RuntimeException("Path must be specified but was " + path);
        }
        if (objectAsJson == null) {
            throw new RuntimeException("No object specified");
        }

        String uri = this.getBaseURL()+path;
        
        Logger.info("Send request to " + uri);
        
        CloseableHttpClient client = getClient();
        HttpPost httpPost = new HttpPost("https://localhost:" + 8080 + "/test");
        try {

            httpPost.setEntity(new StringEntity(objectAsJson));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept", "text/html");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = client.execute(httpPost);
            return ResourceFileHandler.inputStreamToString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            Logger.error(e);
        } catch (ClientProtocolException e) {
            Logger.error(e);
        } catch (IOException e) {
            Logger.error(e);
        }

        return "An error occured";

    }

    protected CloseableHttpClient getClient() {
        return client;
    }

    /**
     * @return protocol://host:port
     */
    protected String getBaseURL() {

        return (new StringBuilder()).append(configuration.getProtocol())
                                    .append("://")
                                    .append(configuration.getHost())
                                    .append(":")
                                    .append(configuration.getPort())
                                    .toString();
    }
}
