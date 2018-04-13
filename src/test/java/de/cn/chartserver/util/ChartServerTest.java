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
    ChartServer server;
    
    @Before
    public void setup(){
        server = ChartServer.createNewInctance();
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
        HttpGet httpGet = new HttpGet("https://localhost:" + server.getListeningPort() + "/test");
        httpGet.setHeader("Accept", "text/html");
     
        HttpResponse response = client.execute(httpGet);
        Assert.assertTrue(response.getStatusLine().getStatusCode() == 200);
        Assert.assertEquals("<html><body>This is a ChartServer</body></html>", ResourceFileHandler.inputStreamToString(response.getEntity().getContent(), StandardCharsets.UTF_8));
        response.getEntity().getContent().close();
    }
    
    @Test
    public void requestExample1PageTest() throws IOException {

        server.addRoute("/example1", Example1Handler.class);
        server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        
        CloseableHttpClient client = TestUtil.newHttpClientInstance();
        HttpGet httpGet = new HttpGet("https://localhost:" + server.getListeningPort() + "/example1");
        httpGet.setHeader("Accept", "text/html");
     
        HttpResponse response = client.execute(httpGet);
        Assert.assertTrue(response.getStatusLine().getStatusCode() == 200);
        Assert.assertEquals(ResourceFileHandler.inputStreamToString(response.getEntity().getContent(),StandardCharsets.UTF_8),ResourceFileHandler.getResourceAsString("html/samples/example1.html"));
        response.getEntity().getContent().close();
    }
    
}
