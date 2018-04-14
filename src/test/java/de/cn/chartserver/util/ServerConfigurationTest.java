package de.cn.chartserver.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.cli.ParseException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.cn.chartserver.ChartServer;
import de.cn.chartserver.handler.Example1Handler;
import de.cn.chartserver.handler.TestHandler;
import fi.iki.elonen.NanoHTTPD;

public class ServerConfigurationTest {
  
    @Test
    public void argumentTest() throws IOException, ParseException {
    	String []args = {"-p","8686","-wsp","8687","-nws", "40"};
        ChartServer server = ChartServer.createNewInstance(args);
        server.start();
        
        CloseableHttpClient client = TestUtil.newHttpClientInstance();
        HttpGet httpGet = new HttpGet("https://localhost:8686/test");
        httpGet.setHeader("Accept", "text/html");
     
        HttpResponse response = client.execute(httpGet);
        Assert.assertTrue(response.getStatusLine().getStatusCode() == 200);
        Assert.assertEquals("<html><body>This is a ChartServer</body></html>", ResourceFileHandler.inputStreamToString(response.getEntity().getContent(), StandardCharsets.UTF_8));
        response.getEntity().getContent().close();
    }
  
    
}
