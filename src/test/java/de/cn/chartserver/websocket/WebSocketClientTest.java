package de.cn.chartserver.websocket;

import java.net.URI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.cn.chartserver.ChartServer;
import de.cn.chartserver.ChartServerConfiguration;


public class WebSocketClientTest {
    final String WEBSOCKET_URI = "wss://localhost:8789";
	ChartServer chartServer;
	@Before
	public void setupServer(){
		chartServer = ChartServer.createNewInstance(ChartServerConfiguration.createConfiguration(8787,8789));	
	}
	
	@After
	public void stopServer(){
		chartServer.stop();
	}
	
	/**
	 * Test if secure websocket connection and communication works 
	 */
	@Test
	public void webSocketServerTest() throws Exception {	
		WebSocketTestClient testClient = new WebSocketTestClient(new URI(WEBSOCKET_URI));
		testClient.setupSSL();
		
		//connectBlocking will return true if everything is O.K
		Assert.assertTrue(testClient.connectBlocking());
		testClient.send("Unit Test");
		testClient.close();
	}
	
	/**
     * Test if communication between multiple clients via WebSocketServer works 
     */
    @Test
    public void multipleClientTest() throws Exception {
        WebSocketTestClient testClient1 = new WebSocketTestClient(new URI(WEBSOCKET_URI));
        testClient1.setupSSL();
        
        WebSocketTestClient testClient2 = new WebSocketTestClient(new URI(WEBSOCKET_URI));
        testClient2.setupSSL();
        
        WebSocketTestClient testClient3 = new WebSocketTestClient(new URI("wss://localhost:8789"));
        testClient3.setupSSL();
        
        //connectBlocking will return true if everything is O.K
        Assert.assertTrue(testClient1.connectBlocking());
        Assert.assertTrue(testClient2.connectBlocking());
        Assert.assertTrue(testClient3.connectBlocking());
        
        testClient1.send("Unit Test");
        testClient2.send("Unit Test");
        testClient3.send("Unit Test");
        
        //Wait for a while
        Thread.sleep(1000);
        
        Assert.assertEquals(2,testClient1.getCallCount());
        Assert.assertEquals(2,testClient2.getCallCount());
        Assert.assertEquals(2,testClient3.getCallCount());
    }
}