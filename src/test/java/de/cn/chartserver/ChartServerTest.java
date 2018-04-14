package de.cn.chartserver;

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
import de.cn.chartserver.ChartServerConfiguration;
import de.cn.chartserver.handler.Example1Handler;
import de.cn.chartserver.util.ResourceFileHandler;
import de.cn.chartserver.util.TestUtil;
import fi.iki.elonen.NanoHTTPD;

public class ChartServerTest {
    ChartServer server;
    
    @Before
    public void setup(){
        server = ChartServer.createNewInstance(ChartServerConfiguration.createConfiguration(8787));
    }
    
    @After
    public void tearDown(){
        server.stop();
    }

    
    //@Test
    public void requestExample1PageTest() throws IOException {
        server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        
        CloseableHttpClient client = TestUtil.newHttpClientInstance();
        HttpGet httpGet = new HttpGet("https://localhost:" + server.getListeningPort() + "/example");
        httpGet.setHeader("Accept", "text/html");
     
        HttpResponse response = client.execute(httpGet);
        Assert.assertTrue(response.getStatusLine().getStatusCode() == 200);
        Assert.assertEquals(ResourceFileHandler.inputStreamToString(response.getEntity().getContent(),StandardCharsets.UTF_8),ResourceFileHandler.getResourceAsString("html/samples/example1.html"));
        response.getEntity().getContent().close();
    }
    
}