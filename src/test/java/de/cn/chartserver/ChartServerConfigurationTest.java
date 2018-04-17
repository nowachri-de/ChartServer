package de.cn.chartserver;

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

import de.cn.chartserver.resource.ResourceFileHandler;
import de.cn.chartserver.util.TestUtil;

/**
 * Test some of the configuration arguments that can be passed to the server
 */
public class ChartServerConfigurationTest {
    final static String TEST_URL_HTTPS="https://localhost:8686/example";
    final static String TEST_RESOURCE = "html/samples/example1.html";
    
	ChartServer server;
	@Before
	public void startServer() throws ParseException, IOException{
		String []args = {"-p","8686","-wsp","8687","-nws", "1"};
        server = ChartServer.createNewInstance(args);
        server.start();
	}
	
	@After
	public void stopServer(){
		server.stop();
	}
	
    /**
     * Test that server configuration worked as expected.
     * 
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void argumentTest() throws IOException, ParseException {
    	
        CloseableHttpClient client = TestUtil.newHttpClientInstance();
        HttpGet httpGet = new HttpGet(TEST_URL_HTTPS);
        httpGet.setHeader("Accept", "text/html");
     
        HttpResponse response = client.execute(httpGet);
        Assert.assertTrue(response.getStatusLine().getStatusCode() == 200);
        Assert.assertEquals(ResourceFileHandler.getResourceAsString(TEST_RESOURCE), ResourceFileHandler.inputStreamToString(response.getEntity().getContent(), StandardCharsets.UTF_8));
        response.getEntity().getContent().close();
    }
}
