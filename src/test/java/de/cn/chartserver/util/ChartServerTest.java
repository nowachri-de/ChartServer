package de.cn.chartserver.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

public class ChartServerTest {
    protected final int SERVER_PORT = 8080;
    
    ChartServer server;
    
    @Before
    public void setup(){
        server = ChartServer.createNewInctance(SERVER_PORT);
    }
    
    @After
    public void tearDown(){
        server.stop();
    }

    @Test
    public void simpleChartServerTest() throws IOException {

        server.addRoute("/test", TestHandler.class);
        server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        
        CloseableHttpClient client = TestUtil.newHttpClientInstance();
        HttpGet httpGet = new HttpGet("https://localhost:" + SERVER_PORT + "/test");
        httpGet.setHeader("Accept", "text/html");
     
        HttpResponse response = client.execute(httpGet);
        Assert.assertTrue(response.getStatusLine().getStatusCode() == 200);
        Assert.assertEquals("<html><body>This is a ChartServer</body></html>", ResourceFileHandler.inputStreamToString(response.getEntity().getContent(), StandardCharsets.UTF_8));      
    }
    
    @Test
    public void requestExample1PageTest() throws IOException {

        server.addRoute("/example1", Example1Handler.class);
        server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        
        CloseableHttpClient client = TestUtil.newHttpClientInstance();
        HttpGet httpGet = new HttpGet("https://localhost:" + SERVER_PORT + "/example1");
        httpGet.setHeader("Accept", "text/html");
     
        HttpResponse response = client.execute(httpGet);
        Assert.assertTrue(response.getStatusLine().getStatusCode() == 200);
        Assert.assertEquals(ResourceFileHandler.inputStreamToString(response.getEntity().getContent(),StandardCharsets.UTF_8),ResourceFileHandler.getResourceAsString("html/samples/example1.html"));
        System.out.println();
    }
    
}
