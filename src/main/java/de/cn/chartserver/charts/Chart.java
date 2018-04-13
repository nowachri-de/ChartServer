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


/**
 * Base class for chart classes implementing concrete charts
 *
 */
public abstract class Chart {
    private ChartServerConfiguration configuration;
    private SSLContext sslContext = null;
    private CloseableHttpClient client = null;

    /**
     * No arg constructor
     */
    public Chart() {
        configuration = ChartServerConfiguration.createConfiguration();
        createClosableHttpClient();
    }

    /**
     * Constructor expecting port at which server is listening as parameter
     * 
     * @param port Port at which server is listening
     */
    public Chart(int port) {
        configuration = ChartServerConfiguration.createConfiguration(port);
        createClosableHttpClient();
    }

    /**
     * Constructor expecting port at which server is listening as parameter as well of the servers
     * hostname
     * 
     * @param host Hostname of server
     * @param port Port at which server is listening
     */
    public Chart(String host, int port) {
        configuration = ChartServerConfiguration.createConfiguration(host, port);
        createClosableHttpClient();
    }

    /**
     * Creates an instance of a ClosableHttpClient which expects every certificate.
     * Not very secure, but this code is definitelly not tuned for security.
     */
    protected void createClosableHttpClient() {
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();
            client = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            Logger.error(e);
        }
    }

    /**
     * Request the server to display the chart corresponding to the given path.
     * The data to be displayed is path of the JSON message beeing send to the 
     * server with the post request.
     * 
     * @param path Path to call
     * @param objectAsJson JSON message which is send to the server via a POST request
     * @return Content of server response as string
     */
    protected String send(String path, Object obj) {

        if (path == null || path.isEmpty()) {
            throw new RuntimeException("Path must be specified but was " + path);
        }
        if (obj == null) {
            throw new RuntimeException("No object specified");
        }

        String uri = this.getBaseURL()+path;
        
        Logger.debug("Send request to " + uri);
        
        CloseableHttpClient client = getClient();
        HttpPost httpPost = new HttpPost(this.getBaseURL()+path);
        try {

            httpPost.setEntity(new StringEntity(obj.toString()));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept", "text/html");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = client.execute(httpPost);
            String result = ResourceFileHandler.inputStreamToString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            response.getEntity().getContent().close();
            return result;
        } catch (UnsupportedEncodingException e) {
            Logger.error(e);
        } catch (ClientProtocolException e) {
            Logger.error(e);
        } catch (IOException e) {
            Logger.error(e);
        }

        return "An error occured when calling the server";
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
